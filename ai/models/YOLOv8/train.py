from ultralytics import YOLO
import torch
# Create a new YOLO model from scratch
# model = YOLO('yolov8x.yaml')
if __name__ == '__main__':
        
    device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
    print ('Available devices ', torch.cuda.device_count())
    print ('Current cuda device ', torch.cuda.current_device())
    print(torch.cuda.get_device_name(device))
    # Load a pretrained YOLO model (recommended for training)
    model = YOLO('./yolov8x.pt')

    # Train the model using the 'coco128.yaml' dataset for 3 epochs
    results = model.train(data='custom_dataset.yaml', epochs=100)
    
    # Export the model to ONNX format
    success = model.export(format='onnx')