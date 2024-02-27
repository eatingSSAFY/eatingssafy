## Server setting

1. Docker Setting
    1. Install docker/docker-compose
    2. Make `.env` file (It's written in [notion](https://www.notion.so/ondol1224/env-file-03e2b69608af490ca177a51bff633fd8?pvs=4))
    3. Set `.env` to read in docker: `docker-compose --env-file .env config`
    4. `docker-compose up -d`

## Arduino IDE setting(ESP8266)

1. File > Preference > Additional boards manager URLs  
    paste http://arduino.esp8266.com/stable/package_esp8266com_index.json 

2. If necessary, install CP210x driver to use Serial comm

3. Select NodeMCU 1.0 (ESP-12E) as board
