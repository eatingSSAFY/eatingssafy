import os
import cv2

from datetime import datetime
from ultralytics import YOLO

from const_config import *

CONST_PATH_IMAGES = './test'
CONST_PATH_OUTPUT = './test/result'
CONST_PATH_MODEL = './yolov8x.pt'

def print_log(msg):
    print(f'[{datetime.now()}] {msg}')

def set_model():
    return YOLO(CONST_PATH_MODEL)

def mask_bbox(img, res):
    opt_save = False
    label = ''
    for cls in res.boxes.cls:
        if int(cls) > 0:
            opt_save = True
            break

    (h, w, c) = img.shape
    boxes = res.boxes
    num_boxes = len(boxes.cls)
    for i in range(num_boxes):
        box = boxes[i]
        # [x1, y1, x2, y2] = [int(x) for x in boxes.xyxy[i]]
        # pt1, pt2 = (x1, y1), (x2, y2)
        [xn, yn, wn, hn] = [float(x) for x in boxes.xywhn[i]]
        pt1 = (int(xn*w-(wn*w/2)), int(yn*h-(hn*h/2)))
        pt2 = (int(xn*w+(wn*w/2)), int(yn*h+(hn*h/2)))
        img = cv2.rectangle(img, pt1, pt2, color=CONST_LIST_COLORS[i])
        img = cv2.putText(img, res.names[int(box.cls)], org=pt1, fontFace=1, fontScale=2, color=CONST_LIST_COLORS[i], thickness=2)

        if opt_save:
            label += ' '.join(map(str, [int(box.cls), xn, yn, wn, hn]))
            label += '\n'
    print_log(f'success masking boxes: {num_boxes}')
    return img, label

def main():
    model = set_model()
    # model.train()
    image_list = [x for x in os.listdir(CONST_PATH_IMAGES) if os.path.splitext(x)[-1]==CONST_EXT_IMAGES]

    total_num = len(image_list)
    for i in range(total_num):
        if os.path.splitext(image_list[i])[-1] != CONST_EXT_IMAGES:
            print_log('wrong extension for masking image...{i+1}/{total_num}')
            continue
        image_name = image_list[i]
        masked_image_name = image_name.split(CONST_EXT_IMAGES)[0] + '_bbox' + CONST_EXT_IMAGES
        image = cv2.imread(os.path.join(CONST_PATH_IMAGES, image_name))
        
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

        result = model.predict(image)

        image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

        masked_image, label = mask_bbox(image, result[0])

        if label != '':
            cv2.imwrite(os.path.join(CONST_PATH_OUTPUT, image_name), image)
            # cv2.imwrite(os.path.join(CONST_PATH_OUTPUT, masked_image_name), masked_image)
            with open(os.path.join(CONST_PATH_OUTPUT, image_name.split(CONST_EXT_IMAGES)[0]+CONST_EXT_LABEL), 'w') as f:
                f.write(label)
                
        print_log(f'success saving masked image...{i+1}/{total_num}')

    
    print_log('done')

if __name__ == '__main__':
    main()