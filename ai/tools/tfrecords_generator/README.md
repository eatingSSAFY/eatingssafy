# TFRecords Generator

## Installation
1. move to automatic_mask_generator folder
    ```bash
    cd ./ai/tools/tfrecords_generator
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
- format
    ```bash
    python ./generate_tfrecords.py -i {image_path} -l {label_path} -o {output_path}
    ```
- example code
    ```bash
    python ./generate_tfrecords.py -i "D:\preprocessed_data\2024-01-17" -l "D:\labeled_data\2024-01-17" -o "D:\tfrecords"
    ```