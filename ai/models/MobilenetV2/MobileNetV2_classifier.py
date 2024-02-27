import torch
import torch.nn as nn
import torch.optim as optim
import torchvision
import torchvision.transforms as transforms
from torch.utils.data import DataLoader, Dataset
from torchvision.models import mobilenet_v2
from sklearn.metrics import accuracy_score, confusion_matrix
import os
import shutil
from PIL import Image

# 경로 설정
CONST_PATH_MODEL = './training_result/mbn_v2_10_0.9794941900205059.pt'
CONST_PATH_IMAGES = './mobilnetv2/mobilenetv2_test01'
OUTPUT_PATH = './output_folder2'  

# 전처리
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize((0.485, 0.456, 0.406), (0.229, 0.224, 0.225))
])

# MobileNetV2 모델 불러오기 및 가중치 로드
model = mobilenet_v2(pretrained=False)
model.classifier[1] = nn.Linear(model.last_channel, 4)
model.load_state_dict(torch.load(CONST_PATH_MODEL, map_location='cpu'))
model.eval()

# 클래스별 폴더 생성
for i in range(1, 5):
    class_folder = os.path.join(OUTPUT_PATH, str(i))
    os.makedirs(class_folder, exist_ok=True)

# 이미지 분류 및 폴더 이동
for filename in os.listdir(CONST_PATH_IMAGES):
    try:
        # 이미지 불러오기 및 전처리
        img_path = os.path.join(CONST_PATH_IMAGES, filename)
        img = Image.open(img_path).convert('RGB')
        img = transform(img).unsqueeze(0)

        # 모델 예측
        with torch.no_grad():
            output = model(img)
            _, predicted = torch.max(output, 1)
            predicted_label = predicted.item()

        # 이미지를 예측된 레이블에 해당하는 폴더로 이동
        output_folder = os.path.join(OUTPUT_PATH, str(predicted_label + 1))

        # shutil.move 는 이동
        shutil.copy(img_path, output_folder)

    except Exception as e:
        print(f"Error processing {filename}: {e}")

print("이미지 분류 및 폴더 이동이 완료되었습니다.")
