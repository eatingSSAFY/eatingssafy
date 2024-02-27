import torch
import torch.nn as nn
import torch.optim as optim
import torchvision
import torchvision.transforms as transforms
from torch.utils.data import DataLoader, Dataset
from torchvision.models import mobilenet_v2
from sklearn.metrics import accuracy_score, confusion_matrix
import os

# 경로 설정
CONST_PATH_MODEL = './training_result/mbn_v2_45_0.9856459330143541.pt' # Accuracy : 42.2%
CONST_PATH_TEST = './task02'

# 전처리 및 데이터 로딩
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


# GPU 사용 가능 여부 확인
device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')

# MobileNetV2 모델 불러오기
model = mobilenet_v2(pretrained=False)
model.classifier[1] = nn.Linear(model.last_channel, 4)
model.load_state_dict(torch.load(CONST_PATH_MODEL, map_location=device))
model = model.to(device)
model.eval()

# 데이터 로딩
test_dataset = CustomDataset(root_dir=CONST_PATH_TEST, transform=transform)
test_loader = DataLoader(test_dataset, batch_size=1, shuffle=False)


# 예측 및 정확도 계산
true_labels = []
predicted_labels = []

with torch.no_grad():
    for inputs, labels in test_loader:
        inputs, labels = inputs.to(device), labels.to(device)
        outputs = model(inputs)
        _, predicted = torch.max(outputs.data, 1)
        
        true_labels.append(labels.item())
        predicted_labels.append(predicted.item())

# 정확도 계산
accuracy = accuracy_score(true_labels, predicted_labels)
print(f'Accuracy: {accuracy * 100:.2f}%')

# Confusion Matrix 계산
conf_matrix = confusion_matrix(true_labels, predicted_labels)
print('\nConfusion Matrix:')
print(conf_matrix)
