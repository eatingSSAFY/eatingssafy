#include "EspMQTTClient.h"
#include "secrets.h"

#define TAG_TERM 420 // ms
#define REP_TERM 220 // ms
#define THRESHOLD 500 // cds sensor value


EspMQTTClient client(
  WIFI_SSID,    //wifi SSID
  WIFI_PASSWORD,      //wifi password
  MQTT_HOST,    // MQTT Broker server ip
  MQTT_USERNAME,   
  MQTT_PASSWORD,   
  CLIENT_NAME,     
  MQTT_PORT             
);

//송신용 tx()
char *tx_topic = "sensor/tag";
void tx(){
  String msg = "{\"added\":\"1\"}";
  client.publish(tx_topic, msg); //topic , msg
}

void tagCall() {
  tx();
}

void setup()
{
  Serial.begin(9600);
  client.enableDebuggingMessages(); 
  client.enableHTTPWebUpdater(); 
  client.enableOTA(); 
  client.enableLastWillMessage("TestClient/lastwill", "I am going offline");
}

void onConnectionEstablished(){
  //client.loop() 에 의해 호출되는 API
}

void loop()
{
  if(client.isConnected()){
    int readVal = analogRead(A0);
Serial.println(readVal);
    if(readVal < THRESHOLD) {
      unsigned long tagSt = millis();
      Serial.println(readVal);
      delay(10);
      //
      while(millis() - tagSt < TAG_TERM) {
        readVal = analogRead(readVal);
        if(readVal > THRESHOLD) {
          break;
        }
        Serial.println(readVal);
        delay(10);
      }
      // 중복 태그 검출 - threshold 이상으로 다시 올라가는 시점이
      // REP_TERM 미만인 경우 MQTT 송신하지 않음
      if(!(millis()-tagSt < REP_TERM)) 
        tagCall();
    }
    client.loop();
    delay(10);
  }
  else{
    client.loop();
    delay(1000);
  }
}
