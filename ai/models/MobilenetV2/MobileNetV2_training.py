import torch
import torch.nn as nn
import torch.optim as optim
import torchvision
import torchvision.transforms as transforms
from torch.utils.data import DataLoader, Dataset
from torchvision.datasets import DatasetFolder
from torchvision.models import mobilenet_v2
from multiprocessing import freeze_support
from sklearn.model_selection import train_test_split
import os

# Hyper-Parameter 설정
CONST_INT_EPOCHS = 50
CONST_FLOAT_LR = 0.001

# 경로 설정
CONST_PATH_DATASET = './training_set'
CONST_PATH_OUTPUT = './training_result'

# 데이터 전처리 및 로딩
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize((0.485, 0.456, 0.406), (0.229, 0.224, 0.225))
])

# Custom dataset class
class CustomDataset(Dataset):
    def __init__(self, root_dir, transform=None):
        self.root_dir = root_dir
        self.transform = transform
        self.class_to_idx = {'1': 0, '2': 1, '3': 2, '4': 3}
        self.samples = self._make_dataset()
        self.y = [x[-1] for x in self.samples]

    def _make_dataset(self):
        samples = []
        for target_class in sorted(self.class_to_idx.keys()):
            class_index = self.class_to_idx[target_class]
            target_dir = os.path.join(self.root_dir, target_class)
            for root, _, fnames in sorted(os.walk(target_dir)):
                for fname in sorted(fnames):
                    path = os.path.join(root, fname)
                    item = (path, class_index)
                    samples.append(item)
        return samples

    def __len__(self):
        return len(self.samples)

    def __getitem__(self, idx):
        path, target = self.samples[idx]
        sample = torchvision.datasets.folder.default_loader(path)
        if self.transform is not None:
            sample = self.transform(sample)
        return sample, target

# 전이 학습 실행
if __name__ == '__main__':
    freeze_support()  # 추가된 부분
    # GPU 사용 가능 여부 확인
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

    # 데이터셋을 읽어온 후, 80%는 train, 20%는 val로 분리
    all_data = CustomDataset(root_dir=CONST_PATH_DATASET, transform=transform)
    ## 공정한 테스트를 위해 class 별로 분리하는 규칙 추가 (stratify=all_data.class_to_idx)
    train_data, val_data = train_test_split(all_data.samples, test_size=0.2, random_state=42, stratify=all_data.y)
        
    train_y = [x[-1] for x in train_data]
    val_y = [x[-1] for x in val_data]
    print("total: ", len(all_data))
    print(len(train_data), [train_y.count(i)/len(train_y) for i in set(train_y)])
    print(len(val_data), [val_y.count(i)/len(val_y) for i in set(val_y)])

    # 학습 데이터셋 로딩
    train_dataset = CustomDataset(root_dir=CONST_PATH_DATASET, transform=transform)
    train_dataset.samples = train_data

    # 검정 데이터셋 로딩
    val_dataset = CustomDataset(root_dir=CONST_PATH_DATASET, transform=transform)
    val_dataset.samples = val_data

    # 데이터로더 설정
    train_loader = DataLoader(train_dataset, batch_size=64, shuffle=True, num_workers=4)
    val_loader = DataLoader(val_dataset, batch_size=64, shuffle=False, num_workers=4)

    # MobileNetV2 모델 불러오기 (미리 학습된 가중치 포함)
    model = mobilenet_v2(pretrained=True)

    # MobileNetV2의 fully connected layer 변경
    model.classifier[1] = nn.Linear(model.last_channel, 4)  # 클래스가 4개인 경우 (1, 2, 3, 4)
    print(device)

    # 모델을 GPU로 이동
    model = model.to(device)

    # 손실 함수 및 최적화기 설정
    criterion = nn.CrossEntropyLoss()
    optimizer = optim.Adam(model.parameters(), lr=CONST_FLOAT_LR)
    best_model_accuracy = 0
    
    for epoch in range(CONST_INT_EPOCHS):
        model.train()
        running_loss = 0.0
        for inputs, labels in train_loader:
            inputs, labels = inputs.to(device), labels.to(device)

            optimizer.zero_grad()

            outputs = model(inputs)
            loss = criterion(outputs, labels)
            loss.backward()
            optimizer.step()

            running_loss += loss.item()

        epoch_loss = running_loss / len(train_loader)
        print(f'Epoch [{epoch+1}/{CONST_INT_EPOCHS}], Loss: {epoch_loss:.4f}')
        
        # 테스트 데이터에 대한 성능 평가
        model.eval()
        correct = 0
        total = 0

        # 모델 검정
        with torch.no_grad():
            for inputs, labels in val_loader:
                inputs, labels = inputs.to(device), labels.to(device)
                outputs = model(inputs)
                _, predicted = torch.max(outputs.data, 1)
                total += labels.size(0)
                correct += (predicted == labels).sum().item()

        accuracy = correct / total
        print(f'val Accuracy: {accuracy * 100:.2f}%')

        # 검정 데이터에 대한 모든 모델 저장
        torch.save(model.state_dict(), os.path.join(CONST_PATH_OUTPUT, f"mbn_v2_{epoch}_{accuracy}.pt"))

        # # 검정 데이터에 대한 성능이 향상되었을 경우 모델 저장
        # if accuracy > best_model_accuracy:
        #     torch.save(model.state_dict(), os.path.join(CONST_PATH_OUTPUT, f"mbn_v2_{epoch}_{accuracy}.pt"))
        #     best_model_accuracy = accuracy

    print('Training finished.')
    print('----------------------------')
