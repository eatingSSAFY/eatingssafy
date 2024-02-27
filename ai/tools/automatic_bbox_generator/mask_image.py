import os
import cv2

from datetime import datetime

CONST_EXT_IMAGE = '.jpg'
CONST_EXT_LABEL = '.txt'

CONST_PATH_IMAGES = 'task02'
CONST_PATH_LABELS = 'task02'
CONST_PATH_OUTPUT = 'task02'

def print_log(msg):
    print(f'[{datetime.now}] {msg}')

def read_label(path):
    with open(path, 'r') as f:
        label = f.readlines()

    label = [x.split('\n')[0].split(' ') for x in label]
    return label

def mask_bbox(image, boxes):
    print_log(image.shape)
    h, w, c = image.shape
    for box in boxes:
        x1, y1, x2, y2 = [float(x) for x in box[1:]]
        pt1 = (int(x1*w-(x2*w/2)), int(y1*h-(y2*h/2)))
        pt2 = (int(x1*w+(x2*w/2)), int(y1*h+(y2*h/2)))
        image = cv2.rectangle(image, pt1, pt2, color=(0, 0, 0))
        image = cv2.putText(image, box[0], pt1, 1, 2, (0, 0, 0), 2)
    
    return image

def main():
    images_list = [x for x in os.listdir(CONST_PATH_IMAGES) if os.path.splitext(x)[-1] == CONST_EXT_IMAGE]
    labels_list = [x for x in os.listdir(CONST_PATH_IMAGES) if os.path.splitext(x)[-1] == CONST_EXT_LABEL]

    total_num = len(images_list)
    for i in range(total_num):
        image_name = images_list[i]
        file_name = os.path.basename(image_name).split(CONST_EXT_IMAGE)[0]
        label_name = file_name+CONST_EXT_LABEL
        bbox_name = file_name + '_bbox' + CONST_EXT_IMAGE

        if label_name not in labels_list:
            print(f'Can\'t find label: {label_name}...{i+1}/{total_num}')
            continue

        image = cv2.imread(os.path.join(CONST_PATH_IMAGES, image_name))
        # image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

        label = read_label(os.path.join(CONST_PATH_LABELS, label_name))
        
        image = mask_bbox(image, label)

        cv2.imwrite(os.path.join(CONST_PATH_OUTPUT, bbox_name), image)

if __name__ == '__main__':
    main()