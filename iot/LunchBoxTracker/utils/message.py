from utils.mqtt import publish
from utils.const_config import *
from time import sleep

CONST_PATH_LOG = './log.txt'

class Message():
    CONST_DICT_STRUCTURE = {'name': '', 'value': ''}
    def __init__(self):
        self.reset()
    
    def reset(self):
        self.salad = 0
        self.sandwich = 0
    
    def add_salad(self, value):
        self.salad += value
    
    def add_sandwich(self, value):
        self.sandwich += value
    
    def send(self, mqtt_c):
        msg = {'data': []}
        if self.salad:
            msg['data'].append({'name': CONST_STR_SALAD, 'value': str(self.salad)})

        if self.sandwich:
            msg['data'].append({'name': CONST_STR_SANDWICH, 'value': str(self.sandwich)})
        
        if msg['data']:
            msg = str(msg).replace('\'', '"')
            publish(mqtt_c, msg)
            with open(CONST_PATH_LOG, 'a') as f:
                f.writelines(msg+"\n")
            # sleep(0.01)
            return True

        print(f"message: {msg}")  
        self.reset()
        return False