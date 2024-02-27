# Image preprocessor
## Info
- find images similar to a specific image
- calculate similarity between a specific image and other images
- task environment
  |device|spec|
  |:---:|:---|
  |CPU|11th Gen Intel(R) Core(TM) i7-11600H @ 2.90GHz   2.92 GHz|
  |RAM|32.0GB|
  |GPU|NVIDIA GeForce RTX 3050 Ti Laptop|
  |OS|Windows 10 Enterprise 64비트 운영 체제, x64 기반 프로세서|
  |Python 버전|3.9.13|

## Installation
1. move to automatic_bbox_generator folder
    ```bash
    cd ./ai/tools/image_preprocessor
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
1. customize parameter
  - open [histogram_image_preprocessor.py](./histogram_image_preprocessor.py) file with text eidtor
  - change parameter in lines 5 ~ 10
    - `CONST_FLOAT_PER`: similarity threshold
    - `CONST_IMAGE_PATH`: image folder directory
    - `CONST_OUTPUT_PATH`: directory of saved result
2. run Image preprocessor
  ```bash
  python histogram_image_preprocessor.py
  ```
3. check result
  - open file : CONST_OUTPUT_PATH in [histogram_image_preprocessor.py](./histogram_image_preprocessor.py)
  - file names in CONST_OUTPUT_PATH are similar in first image in CONST_IMAGE_PATH