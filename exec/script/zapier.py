import requests
from urllib.request import urlopen 
import json

headers = {'Content-Type': 'application/json'}
timeout = 30

uri = '/api/batch/menu'
data = {
    'fileUrl': input_data['file'],
    'restaurant': ''
}


res = urlopen(data['fileUrl'])
fileName = res.headers.get_filename()

if 'balju' in fileName :
    uri = '/api/batch/stock'
elif '10ceung' in fileName :
    data['restaurant'] = '10층'
elif '20ceung' in fileName :
    data['restaurant'] = '20층'
else: 
    return

requests.post(
    'https://i10a204.p.ssafy.io' + uri,
    headers = headers,
    data = json.dumps(data)
)