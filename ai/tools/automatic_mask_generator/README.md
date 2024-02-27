# Automatic Mask Generator

## Info
- reference from [Segment Anything](https://github.com/facebookresearch/segment-anything)
- task environment
    - TBU

## Installation
1. move to automatic_mask_generator folder
    ```bash
    cd ./ai/tools/automatic_mask_generator
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
4. install python libraries
    ```powershell
    pip install --upgrade pip
    pip install -r requirements.txt
    ```

## How to use
- [download model: ViT-H SAM model](https://dl.fbaipublicfiles.com/segment_anything/sam_vit_h_4b8939.pth)
- format
    ```bash
    python ./main.py -i {image_path} -o {save_path} -m {model_path} -g
    ```
- example code
    ```bash
    python ./main.py -i "./workplace\data\preprocessed_data\2024-01-17\task01" -o "./workplace\data\masked_data\2024-01-17\task01" -m "./models/sam_vit_h_4b8939.pth" -g
    ```