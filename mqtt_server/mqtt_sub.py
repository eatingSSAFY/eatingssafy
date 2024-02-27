import paho.mqtt.client as mqtt
import json
import requests
import os
import threading
from dotenv import load_dotenv

class Subscriber(threading.Thread):
    def __init__(self, broker_host, broker_port, topic, backend_url):
        super().__init__()
        self.broker_host = broker_host
        self.broker_port = broker_port
        self.topic = topic
        self.backend_url = backend_url
        self.client = mqtt.Client()
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        
    # loop 동작
    def run(self):
        try:
            self.client.connect(host = self.broker_host, port = int(self.broker_port))
            self.client.loop_forever()
        except Exception as err:
            print('error: %s'%err)

    # request callback
    def sendRequest(self, message):
        json_obj = json.loads(message)
        header = {
            'Content-type':'application/json'
        }
        print(json_obj)
        try:
            requests.post(url = self.backend_url, data = json.dumps(json_obj), headers = header)
        except Exception as err:
            print('error in requests: %s'%err)

    # 브로커 접속 시도 결과 처리 callback
    def on_connect(self, client, userdata, falgs, rc):
        print("Connected with result code "+ str(rc))
        if rc==0:
            self.client.subscribe(self.topic)
        else:
            print('Fail connection : ', rc)

    # 메시지 수신 처리 callback
    def on_message(self, client, userdata, msg):
        try:
            message = msg.payload.decode('utf-8')
            self.sendRequest(message)
        except Exception as err:
            print('error in message: %s'%err)

# url 설정
load_dotenv()
backend_url = 'http://' + os.environ['BACKEND_HOST'] + ':' + os.environ['BACKEND_PORT']
broker_host = os.environ['BROKER_HOST']
broker_port = os.environ['BROKER_PORT']

# class 생성
subscriber_tag = Subscriber(broker_host, broker_port, "sensor/tag", backend_url+'/api/tag')
subscriber_cam1 = Subscriber(broker_host, broker_port, "sensor/cam1", backend_url+'/api/amount?cameraId=1')
subscriber_cam2 = Subscriber(broker_host, broker_port, "sensor/cam2", backend_url+'/api/amount?cameraId=2')

# thread start
subscriber_tag.start()
subscriber_cam1.start()
subscriber_cam2.start()