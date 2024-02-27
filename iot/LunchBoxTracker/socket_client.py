import socket
import threading
import time
import base64
import cv2
import numpy as np

from datetime import datetime
from ultralytics import YOLO

from utils.const_config import *
from utils.message import *
# from yolo import *
from tracker import *

with open(CONST_PATH_LOG, 'w') as f:
    f.write("start\n")

def reset_box(y=0):
    return {"y_src": y, "y_dst": 0, "cnt":0, "status":0}

lunch_box_dict ={0: {"y_src": 0, "y_dst": 0, "cnt":0, "status":0}}

def done(key):
    print("init:", key)
    lunch_box_dict[key]['status'] = 2
    
    src = 3         # salad
    if lunch_box_dict[key]['y_src'] > CONST_INT_BOUNDARY_SANDWICH:
        src = 0     # outside
    elif lunch_box_dict[key]['y_src'] > CONST_INT_BOUNDARY_SALAD:
        src = 2     # SANDWICH

    dst = 3
    if lunch_box_dict[key]['y_dst'] > CONST_INT_BOUNDARY_SANDWICH:
        dst = 0            
    elif lunch_box_dict[key]['y_dst'] > CONST_INT_BOUNDARY_SALAD:
        dst = 2
    
    diff = dst - src
    
    print(f"done: {key}, {src} -> {dst}")

    return [int(diff/2), CONST_DICT_MENU[abs(diff)]]

class ServerSocket:

    def __init__(self, ip, port, model_path, mqtt_c):
        self.TCP_IP = ip
        self.TCP_PORT = port
        self.socketOpen()
        self.receiveThread = threading.Thread(target=self.receiveImages(model_path, mqtt_c))
        self.receiveThread.start()

    def socketClose(self):
        self.sock.close()
        print(u'Server socket [ TCP_IP: ' + self.TCP_IP + ', TCP_PORT: ' + str(self.TCP_PORT) + ' ] is close')

    def socketOpen(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.bind((self.TCP_IP, self.TCP_PORT))
        self.sock.listen(1)
        print(u'Server socket [ TCP_IP: ' + self.TCP_IP + ', TCP_PORT: ' + str(self.TCP_PORT) + ' ] is open')
        self.conn, self.addr = self.sock.accept()
        print(u'Server socket [ TCP_IP: ' + self.TCP_IP + ', TCP_PORT: ' + str(self.TCP_PORT) + ' ] is connected with client')

    def receiveImages(self, model_path, mqtt_c):
        # image = Image(model_path)
        model = YOLO(model_path)
        
        n = 0
        try:
            while True:
                msg = Message()
                length = self.recvall(self.conn, 64)
                length1 = length.decode('utf-8')
                stringData = self.recvall(self.conn, int(length1))
                stime = self.recvall(self.conn, 64)
                print('send time: ' + stime.decode('utf-8'))
                now = time.localtime()
                print('receive time: ' + datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S.%f'))
                data = np.frombuffer(base64.b64decode(stringData), np.uint8)
                image = cv2.imdecode(data, 1)
                results = model.track(image, stream=True, persist=True)
                # image.read_image(cv2.imdecode(data, 1))
                # image.get_boxes()
                print('predict time: ' + datetime.utcnow().strftime('%Y-%m-%d %H:%M:%S.%f'))
                box_list = []
                # Visualize the results on the frame
                for r in results:
                    res_plotted = r.plot()
                    boxes = r.boxes
                    
                    for box in boxes:
                        
                        if box.id:
                            box_id = int(box.id)
                            print(box_id)
                            box_list.append(box_id)
                            y_min = min(int(box.xyxy[0][1]), int(box.xyxy[0][3]))
                            if box_id not in lunch_box_dict.keys():
                                lunch_box_dict[box_id] = reset_box(int(y_min))
                            elif lunch_box_dict[box_id]['status'] == 2:
                                # continue
                                lunch_box_dict[box_id] = reset_box(int(y_min))
                            else:
                                lunch_box_dict[box_id]['y_dst'] = y_min
                                lunch_box_dict[box_id]['status'] = 1
                    
                    print("box_list:", box_list)
                for key in lunch_box_dict.keys():
                    if key == 0:
                        continue
                    if key in box_list:
                        continue
                    
                    if lunch_box_dict[key]['status'] == 2:
                        continue

                    lunch_box_dict[key]['cnt'] += 1

                    if lunch_box_dict[key]['cnt'] > CONST_INT_WAIT_COUNT:
                        res = done(key)
                        with open(CONST_PATH_LOG, 'a') as f:
                            f.writelines(f'{str(lunch_box_dict[key])}\n')
                        print("RESTUL:",res)
                        msg.add_salad(res[0]) if res[1] == CONST_STR_SALAD else msg.add_sandwich(res[0])

                # image.draw_boxes()
                cv2.imwrite(f"C:/Users/SSAFY/Desktop/woo/data/{datetime.now()}.jpg", res_plotted)
                cv2.imshow("image", res_plotted)
                cv2.waitKey(1)

                msg_res = msg.send(mqtt_c)
                print(lunch_box_dict)
                # if msg_res:
                #     sleep(0.1)

                # if image.boxes:
                #     n = 0
                #     print(image.boxes)
                # else:
                #     n += 1
                #     if n > CONST_INT_WAIT_COUNT:
                #         if box_list:
                #             for box in box_list:
                #                 res = box.done()
                #                 print(res)
                #                 msg.add_salad(res[0]) if res[1] == CONST_STR_SALAD else msg.add_sandwich(res[0])
                #             msg.send(mqtt_c)
                #             print(box_list[0].y_top_min)
                #             box_list = []
                #             n = 0
                #             break
                    
                #     continue

                # if box_list:    # tracking
                #     # box_list, msg = find_corresponding_lunchbox(box_list, image.boxes, msg)
                #     # 1개만 찾는다는 가정
                #     box_list[0].move(image.boxes[0])

                # else:           # 신규 추가
                #     for new_box in image.boxes:
                #         lb = LunchBox(new_box)
                #         box_list.append(lb)
                
        except Exception as e:
            print(e)
            self.socketClose()
            cv2.destroyAllWindows()
            self.socketOpen()
            self.receiveThread = threading.Thread(target=self.receiveImages)
            self.receiveThread.start()

    def recvall(self, sock, count):
        buf = b''
        while count:
            newbuf = sock.recv(count)
            if not newbuf: return None
            buf += newbuf
            count -= len(newbuf)
        return buf