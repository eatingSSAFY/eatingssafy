from datetime import datetime

def print_log(msg):
    print(f'[{datetime.now()}] {str(msg)}')