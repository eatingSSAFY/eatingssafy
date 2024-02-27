import tensorflow as tf
import numpy as np
import argparse
import math
import os

from datetime import datetime

CONST_INT_GROUP = 1000
CONST_STR_ENCODING = 'utf-8'
CONST_EXT_LABEL = '.txt'
CONST_EXT_IMAGE = '.jpg'
CONST_EXT_TFRECORDS = '.tfrecords'

CONST_DICT_LABEL = {
   0: 'salad/sandwich'
}

def print_log(msg):
    print(f'[{datetime.now()}] {msg}')

def parsing_arguments():
    parser = argparse.ArgumentParser()

    parser.add_argument('-i', '--image_path', type=str, required=True,
                        help='')
    parser.add_argument('-l', '--label_path', type=str, required=True,
                        help='')
    parser.add_argument('-o', '--output_path', type=str, required=True,
                        help='')
    
    return parser.parse_args()

def read_label(path):
    with open(path, 'r') as f:
        label = f.readlines()

    label = [x.split('\n')[0].split(' ') for x in label]
    return label

# reference: https://www.tensorflow.org/tutorials/load_data/tfrecord#write_the_tfrecord_file
def _bytes_feature(value):
    '''Returns a bytes_list from a string / byte.'''
    if isinstance(value, type(tf.constant(0))):
        value = value.numpy() # BytesList won't unpack a string from an EagerTensor.
    return tf.train.Feature(bytes_list=tf.train.BytesList(value=[value]))

def _float_feature(value):
    '''Returns a float_list from a float / double.'''
    if type(value) != list:
        value = [value]
    return tf.train.Feature(float_list=tf.train.FloatList(value=value))

def _int64_feature(value):
    '''Returns an int64_list from a bool / enum / int / uint.'''
    if type(value) != list:
       value = [value]
    return tf.train.Feature(int64_list=tf.train.Int64List(value=value))

# Create a dictionary with features that may be relevant.
def image_example(image_string, labels, d, t):
    h, w, d = tf.io.decode_jpeg(image_string).shape
    # width, height, depth = image_shape
    xmins = []
    xmaxs = []
    ymins = []
    ymaxs = []
    classes = []

    for label in labels:
        [xn, yn, wn, hn] = [float(x) for x in label[1:]]
        (xmin, ymin) = (xn-(wn/2), yn-(hn/2))
        (xmax, ymax) = (xn+(wn/2), yn+(hn/2))
        xmins.append(xmin)
        xmaxs.append(xmax)
        ymins.append(ymin)
        ymaxs.append(ymax)
        classes.append(int(label[0]))

    feature = {
        'height': _int64_feature(h),
        'width': _int64_feature(w),
        'depth': _int64_feature(d),
        'image_raw': _bytes_feature(image_string),
        'xmin': _float_feature(xmins),
        'xmax': _float_feature(xmaxs),
        'ymin': _float_feature(ymins),
        'ymax': _float_feature(ymaxs),
        'label': _int64_feature(classes),
        'date': _bytes_feature(bytes(str(d), CONST_STR_ENCODING)),
        'time': _bytes_feature(bytes(t, CONST_STR_ENCODING))
    }

    return tf.train.Example(features=tf.train.Features(feature=feature))

def main(args):
    record_name = os.path.basename(args.label_path)

    file_list = [x for x in os.listdir(args.label_path) if os.path.splitext(x)[-1] == CONST_EXT_LABEL]
    file_index = -1
    num_files = len(file_list)
    num_records = math.ceil(num_files/CONST_INT_GROUP)
    for i in range(num_records):
        cnt = 0
        record_index = str(i+1).zfill(3)
        record_file = record_name+'_'+record_index+CONST_EXT_TFRECORDS
        record_path = os.path.join(args.output_path, record_file)
        with tf.io.TFRecordWriter(record_path) as writer:
            while cnt < CONST_INT_GROUP:
                file_index += 1
                if file_index >= num_files:
                    break
                label_name = file_list[file_index]
                file_name = label_name.split(CONST_EXT_LABEL)[0]
                image_name = file_name + CONST_EXT_IMAGE
                image_path = os.path.join(args.image_path, image_name)
                if os.path.exists(image_path) == False:
                    continue

                labels = read_label(os.path.join(args.label_path, label_name))
                image_string = open(image_path, 'rb').read()

                tf_example = image_example(image_string, labels, record_name, file_name)
                writer.write(tf_example.SerializeToString())
                cnt += 1
                print_log(f'done: {file_name}...{file_index+1}/{num_files}')
        print_log(f'success to save tfrecords...{i+1}/{num_records}')
    print_log('done')

if __name__ == '__main__':
   args = parsing_arguments()
   main(args)