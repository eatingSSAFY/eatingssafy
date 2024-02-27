import numpy as np
import torch
import matplotlib.pyplot as plt
import cv2
import os
import argparse
import pickle

from sam import sam_model_registry, SamAutomaticMaskGenerator, SamPredictor

CONST_EXT_IMAGE = ".jpg"
CONST_EXT_MASK = ".pickle"

class Config():
    def __init__(self, args):
        self.sam_checkpoint = args.model # "./models/sam_vit_h_4b8939.pth"
        self.model_type = args.model_type # "vit_h"

        if args.gpu:
            self.device = "cuda"
        else:
            self.device = "cpu"

        self.input = args.input
        self.output = args.output

    def set_model(self):
        self.sam = sam_model_registry[self.model_type](checkpoint=self.sam_checkpoint)
        self.sam.to(device=self.device)

def parsing_argument():
    parser = argparse.ArgumentParser(
        prog="Automatic_Mask_Generator",
        description="Generate Segmentation Mask from Images using Segment Anything Model(SAM)"
    )

    parser.add_argument('-i', '--input', type=str, required=True,
                        help="path of the source image to upload")
    parser.add_argument('-o', '--output', type=str,
                        help="path of the mask data to save")
    parser.add_argument('-m', '--model', type=str, default="./models/sam_vit_h_4b8939.pth", 
                        help="path of the model to upload")
    parser.add_argument('-mt', '--model_type', type=str, choices=["vit_h", "vit_l", "vit_b"], default = "vit_h",
                        help="select model type from load model")
    parser.add_argument('-g', '--gpu', action='store_true', default=False,
                        help="use gpu (default: False)")
    
    return parser.parse_args()


def show_anns(anns):
    if len(anns) == 0:
        return
    sorted_anns = sorted(anns, key=(lambda x: x['area']), reverse=True)
    ax = plt.gca()
    ax.set_autoscale_on(False)

    img = np.ones((sorted_anns[0]['segmentation'].shape[0], sorted_anns[0]['segmentation'].shape[1], 4))
    img[:,:,3] = 0
    for ann in sorted_anns:
        m = ann['segmentation']
        color_mask = np.concatenate([np.random.random(3), [0.35]])
        img[m] = color_mask
    ax.imshow(img)

def generate_labels(image_path, output, generator):
    file_name = os.path.basename(image_path).split(CONST_EXT_IMAGE)[0]
    mask_path = os.path.join(output, file_name + CONST_EXT_MASK)

    if os.path.exists(mask_path):
        print("already done")
        return
    
    image = cv2.imread(image_path)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    masks = generator.generate(image)

    # save mask image
    plt.figure(figsize=(20,20))
    plt.imshow(image)
    show_anns(masks)
    plt.axis('off')
    plt.savefig(os.path.join(output, file_name+CONST_EXT_IMAGE))
    plt.close()


    # save mask data
    mask_dict = {file_name: masks}
    with open(mask_path, 'wb') as json_file:
        pickle.dump(mask_dict, json_file)

def main(i, o, generator):
    f_list = os.listdir(i)
    num = len(f_list)
    print("start to generate mask")
    for j in range(num):
        f = f_list[j]

        if os.path.splitext(f)[-1] != CONST_EXT_IMAGE:
            continue
        
        generate_labels(os.path.join(i, f), o, generator)

        print(f"success...({j+1}/{num})")
        

if __name__=="__main__":
    args = parsing_argument()
    config = Config(args)
    config.set_model()

    mask_generator = SamAutomaticMaskGenerator(config.sam)
    print("success load generator")
    main(args.input, args.output, mask_generator)



