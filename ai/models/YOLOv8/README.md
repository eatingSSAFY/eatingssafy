# YOLOv8 for eatingSSAFY

## Info
- train AI model: YOLOv8
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
1. customize dataset info
    - copy ["coco128.yaml"](./venv/Lib/site-packages/ultralytics/cfg/datasets/coco128.yaml) file
    - paste file in same directory
    - edit file name with "custom_dataset.yaml"
    - open ["custom_dataset.yaml"](./venv/Lib/site-packages/ultralytics/cfg/datasets/custom_dataset.yaml)
    - (if you need) change dataset directory in file
        ```
        path: ../datasets/coco128 # dataset root dir
        train: images/train2017 # train images (relative to 'path') 128 images
        val: images/train2017 # val images (relative to 'path') 128 images
        ```
    - change class info in file
        ```
        names:
            0: person
            1: lunchbox
        ```
    - save the file

2. generate dataset
    - save images in train image directory in ["custom_dataset.yaml"](./venv/Lib/site-packages/ultralytics/cfg/datasets/custom_dataset.yaml)
    - save labels in the path where the “images” folder name has been changed to the “labels” folder name in train image directory.

3. set base model
    - open file ["train.py"](./train.py)
    - select model in line 12
        ```
        model = YOLO('./yolov8x.pt')
        ```
    - save the file

4. customize epoch
    - open file ["train.py"](./train.py)
    - change epochs in line 15
        ```
        results = model.train(data='custom_dataset.yaml', epochs=100)
        ```
    - save the file

5. run train.py
    - in virtual environment
        ```bash
        python train.py
        ```

6. check the result
    - open directory ["./runs/detect/train"](./run/detect/train)
    - check model ["best.pt"](./run/detect/train/weights/best.pt) in ["weights"](./run/detect/train/weights) folder
    - (if model is used in Automatic Bounding Box Generator) copy the model or remember the directory to load it in Automatic Bounding Box Generator

## reference
- [Ultralytics](https://docs.ultralytics.com/ko/)
- [Ultralytics YOLOv8](https://github.com/ultralytics/ultralytics?tab=readme-ov-file)