import paho.mqtt.client as mqtt

from utils.mqtt_info import *
from utils.log import *

'''
reference: 
- https://developer-finn.tistory.com/1
- https://doa-oh.tistory.com/167
'''
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print_log('connected OK')
    else:
        print_log('Bad connection Returned code=', rc)


def on_disconnect(client, userdata, flags, rc=0):
    print_log(str(rc))


def on_publish(client, userdata, mid):
    print_log(f'In on_pub callback mid= {mid}')

def connect_mqtt():
    # 새로운 클라이언트 생성
    client = mqtt.Client(CONST_STR_PUBLISHER) # publisher name

    # 콜백 함수 설정 on_connect(브로커에 접속), on_disconnect(브로커에 접속중료), on_publish(메세지 발행)
    client.on_connect = on_connect
    client.on_disconnect = on_disconnect
    client.on_publish = on_publish
    
    # connect
    client.connect(CONST_STR_CONNECT_IP, CONST_INT_CONNECT_PORT)

    return client

def publish(client, message, topic=CONST_STR_TOPIC):
    res,_ = client.publish(topic, message)
    
    if res == mqtt.MQTT_ERR_SUCCESS:
        print_log(f'success to send message to Topic: {topic}')
    else:
        print_log(f'fail to send message')