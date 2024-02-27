# LunchBox Tracker
## Info
- tracking Lunch Box in refrigerator with customized YOLOv8
- need to YOLOv8 model which is trained in [ai/models/yolov8](./../../ai/models/yolov8)
- task environment
  - TBU

## Installation
### raspberry pi
1. install opencv
  ```
  pip install opencv-python
  ```
### GPU Server
1. move to LunchBoxTracker folder
    ```bash
    cd ./iot/LunchBoxTracker
    ```
2. set virtual environment python
    ```bash
    python -m venv venv
    ```
3. install PyTorch with GPU
  - check cuda version
  - install pytorch version with same cuda version, check [PyTorch site](https://pytorch.org/get-started/locally/)
  - with cuda 11.8 version
    ```
    pip3 install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118
    ```
4. install libraries with requirements.txt
    ```bash
    pip install -r requirements.txt
    ```

## How to use
### Raspberry Pi
1. copy raspberry_pi.py to Raspberry Pi with VNC or SSH
2. run raspberry_pi.py file
  ```
  python raspberry_pi.py -h {GPU Server ip address}
  ```
### GPU Server
0. check GPU Server IP Address
  - open cmd with windows+r
  - type ipconfig
    ```bash
    ipconfig
    ```
  - check IPv4 Address
1. set GPU Server IP Address
  - open file "main.py" with text eidtor
  - set IP Address which is GPU Server IP in line 32
    ```python
    server = ServerSocket('ip address', 8080, args.model, mqtt_client)
    ```
2. run LunchBox Tracker
  ```python
  python main.py --model {model_path} --host {ip address} --mqtt
  ```
  - model_path: trained YOLOv8 model directory
  - mqtt: option to use mqtt for sending result data