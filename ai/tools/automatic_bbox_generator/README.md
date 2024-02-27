# Automatic Bounding Box Generator

## Info
- auto labeling tools which label bounding box
- reference from [Ultralytics YOLOv8](https://github.com/ultralytics/ultralytics?tab=readme-ov-file)
- task environment
    - TBU

## Installation
1. move to automatic_bbox_generator folder
    ```bash
    cd ./ai/tools/automatic_bbox_generator
    ```
2. set virtual environment python
    ```bash
    python -m venv venv
    ```
3. activate virtual environment
    - in powershell
        ```powershell
        ./venv/Scripts/activate
        ```
    - in bash
        ```bash
        source ./venv/Script/activate
        ```
4. install PyTorch with GPU (or CPU)
    - update pip
        ```
        pip install --upgrade pip
        ```
    - install PyTorch with GPU
        - you need to check GPU and select install code in [PyTorch](https://pytorch.org/get-started/locally/)
        - in our case
            ```
            pip3 install torch torchvision torchaudio --index-url https://download.pytorch.org/whl/cu118
            ```
    - install libraries with requirements.txt
        ```
        pip install -r requirements.txt
        ```

## How to use
1. check AI model path
    - before run Automatic Bounding Box Generator, need AI model Object Detection which is YOLOv8
    - check [README.md](../../models/YOLOv8/README.md)

2. open [main.py](./main.py) with texteditor

3. change directory in lines 9~11
    - CONST_PATH_IMAGES : directory of source image data for labeling
    - CONST_PATH_OUTPUT : directory of label data
    - CONST_PATH_MODEL : model directory in "1. check AI model path"

4. run Automatic Bounding Box Generator
    ```
    python main.py
    ```

5. check result in CONST_PATH_OUTPUT
    - open directory: CONST_PATH_OUTPUT
    - check label data which extension is ".txt"
    - label data have classes greater than 0
    - each line means {class xn yn wn hn} in each label data
        - xn: ratio of bounding box center point x coordinate in image
        - xn: ratio of bounding box center point y coordinate in image
        - wn: ratio of bounding box width in image
        - wn: ratio of bounding box height in image

## reference
- [Ultralytics](https://docs.ultralytics.com/ko/)
- [Ultralytics YOLOv8](https://github.com/ultralytics/ultralytics?tab=readme-ov-file)