import argparse
import socket
import threading
import time
import base64
import numpy as np
import cv2
from datetime import datetime

# from ultralytics import YOLO
from utils.mqtt import *
from utils.message import *
from socket_client import *

def _parsing_arguments():
    parser = argparse.ArgumentParser()

    # parser.add_argument('-t', '--type', type=str, default='detection', choices=['detection', 'classification'],
    #                     help='')
    parser.add_argument('-m', '--model', type=str, default='./models/yolov8n_v4_best.pt',
                        help='')
    parser.add_argument('-h', '--host',
                        help='')
    parser.add_argument('--mqtt', action='store_true', default=False,
                        help='')
    return parser.parse_args()

def main(args):
    if args.mqtt:
        mqtt_client = connect_mqtt()
        mqtt_client.loop_start()
    
    # msg = Message()
    server = ServerSocket(args.host, 8080, args.model, mqtt_client)
    
    if args.mqtt:
        mqtt_client.loop_stop()

if __name__ == "__main__":
    args = _parsing_arguments()
    main(args)