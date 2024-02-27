FROM python:3.11-slim-buster
RUN pip3 install paho-mqtt
RUN pip3 install requests
RUN pip3 install python-dotenv
COPY mqtt_server/mqtt_sub.py app.py
EXPOSE ${BROKER_PORT}
ENTRYPOINT ["python3", "-u", "app.py"]
