# Mobilenetv2 for eatingSSAFY
## Info
- train and test AI model : Mobilnetv2
- result model is used in Raspberry Pi with Real Time Classification
- task environment
  |device|spec|
  |:---:|:---|
  |CPU|11th Gen Intel(R) Core(TM) i7-11600H @ 2.90GHz   2.92 GHz|
  |RAM|32.0GB|
  |GPU|NVIDIA GeForce RTX 3050 Ti Laptop|
  |OS|Windows 10 Enterprise 64비트 운영 체제, x64 기반 프로세서|
  |Python 버전|3.9.13|

## Installation
1. move to Mobilenetv2 folder
    ```bash
    cd ./ai/models/MobilenetV2
    ```
2. set virtual environment python
    ```bash
    python -m venv venv
    ```
3. install libraries with requirements.txt
    ```bash
    pip install -r requirements.txt
    ```

## How to use
### train model
1. set data
  - make 4 folder each folder name is 1, 2, 3, 4
    - 1: only person
    - 2: person with lunch box (salad or sandwich)
    - 3: take out / restock salad lunch box
    - 4: take out / restock sandwich lunch box
2. change parameters
  - open file : [MobileNetV2_training.py](./MobileNetV2_training.py)
  - change parameters in lines 13 ~ 19
    - CONST_INT_EPOCHS: train epochs
    - CONST_FLOAT_LR: learning rate
    - CONST_PATH_DATASET: dataset directory
    - CONST_PATH_OUTPUT: saved model directory
3. run train model
  ```bash
  python MobileNetV2_training.py
  ```
4. check result
  - when finish traning model, log show result of accuracy and confusion matrix in kernal
  - check model in CONST_PATH_OUTPUT
### test model
1. set data
  - make 4 folder each folder name is 1, 2, 3, 4
    - 1: only person
    - 2: person with lunch box (salad or sandwich)
    - 3: take out / restock salad lunch box
    - 4: take out / restock sandwich lunch box
2. change parameters
  - open file : [MobileNetV2_test.py](./MobileNetV2_test.py)
  - change parameters in lines 11 ~ 13
    - CONST_PATH_MODEL: trained model directory
    - CONST_PATH_TEST: test dataset directory in {1. set data}
3. run test model
  ```bash
  python MobileNetV2_test.py
  ```
4. check result
  - when finish traning model, log show result of accuracy and confusion matrix in kernal
  - check model in CONST_PATH_OUTPUT
### classifier images
1. set data
  - set folder for images that be classified
2. change parameters
  - open file : [MobileNetV2_classifier.py](./MobileNetV2_classifier.py)
  - change parameters in lines 13 ~ 16
    - CONST_PATH_MODEL: trained model directory
    - CONST_PATH_IMAGES: dataset directory in {1. set data}
    - OUTPUT_PATH : result directory
3. run classifier
  ```bash
  python ./MobileNetV2_classifier.py
 ```
4. check result
  - open result directory : OUTPUT_PATH in [MobileNetV2_classifier.py](./MobileNetV2_classifier.py)
  - check folder each folder name is 1, 2, 3, 4
    - 1: only person
    - 2: person with lunch box (salad or sandwich)
    - 3: take out / restock salad lunch box
    - 4: take out / restock sandwich lunch box

## Reference
- [Real Time Inference on Raspberry Pi 4 (30 fps!)](https://pytorch.org/tutorials/intermediate/realtime_rpi.html)