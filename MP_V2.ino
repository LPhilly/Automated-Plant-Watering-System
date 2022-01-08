//Add Libraries
#include <Arduino.h>
#include "DHT.h"

// Introduce Definitions
#define DHTPIN 3 
#define MOISTURE A0
#define MOSFET 2
#define BUZZ 5
#define LIGHT A6
# define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);


void setup() {
  Serial.begin(9600); // baud
  
  // ID PINS 
  pinMode (MOSFET, OUTPUT);
  pinMode(BUZZ, OUTPUT);

  // Initiate Temp Sensor
  dht.begin();

// Added redundancy 
  digitalWrite(MOSFET, LOW);  
  digitalWrite(BUZZ, LOW);  


}

// send moisture value to Java interface
 void sendMoistData() {
    const auto value =  analogRead( MOISTURE );
    const byte data[] = {0, 0, highByte(value), lowByte(value)};
    Serial.write(data, 4);
    Serial.println();}

// Too humid
  void error107() {
    const auto value =  107;
   const byte data[] = {0, 0, highByte(value), lowByte(value)};
   Serial.write(data, 4);
    Serial.println();}

// Too Cold
     void error108() {
    const auto value =  108;
   const byte data[] = {0, 0, highByte(value), lowByte(value)};
   Serial.write(data, 4);
    Serial.println();}

// Not enough light (assume daytime)
     void error109() {
    const auto value =  109;
   const byte data[] = {0, 0, highByte(value), lowByte(value)};
   Serial.write(data, 4);
    Serial.println();}

// Too warm
      void error110() {
    const auto value =  110;
   const byte data[] = {0, 0, highByte(value), lowByte(value)};
   Serial.write(data, 4);
    Serial.println();}

// Too arid
      void error111() {
    const auto value =  111;
   const byte data[] = {0, 0, highByte(value), lowByte(value)};
   Serial.write(data, 4);
    Serial.println();}

    void song() { 
      const auto recievedData = Serial.read();
if (recievedData == 250) { 
// Play Mario Undergound Theme

delay(1000);
// C2
  tone (BUZZ, 65.4); 
  delay(200); 
  noTone(BUZZ); 

  //C3
  tone(BUZZ, 130); 
  delay(200); 
  noTone(BUZZ);
  
  // A1 
  tone (BUZZ, 55); 
  delay(200); 
  noTone(BUZZ); 

//A2 
tone(BUZZ, 110); 
delay(200);
noTone(BUZZ); 

  // Bb1
  tone(BUZZ, 58);
  delay(200); 
  noTone(BUZZ);

  // Bb2 
  tone(BUZZ, 116); 
  delay(200);
  noTone(BUZZ);

delay(500); 

  // C4
  tone (BUZZ, 262); 
  delay(200); 
  noTone(BUZZ); 

  //C5
  tone(BUZZ, 523); 
  delay(200); 
  noTone(BUZZ);
  
  // A3
  tone (BUZZ, 220); 
  delay(200); 
  noTone(BUZZ); 

//A4
tone(BUZZ, 440); 
delay(200);
noTone(BUZZ); 

  // Bb3
  tone(BUZZ, 233);
  delay(200); 
  noTone(BUZZ);

  // Bb4
  tone(BUZZ, 466); 
  delay(200);
  noTone(BUZZ);}

else if (recievedData == 240){ 

    //Ode To Joy - Beethoven

// E4
    tone(BUZZ, 330); 
    delay(300) ;
    noTone(BUZZ);

    // E4
    tone(BUZZ, 330); 
    delay(300) ;
    noTone(BUZZ);

    //F4 
    tone(BUZZ, 350); 
    delay(300); 
    noTone(BUZZ); 

    //G4 
    
  tone(BUZZ, 392); 
    delay(250); 
    noTone(BUZZ); 

       //G4 
    
  tone(BUZZ, 392); 
    delay(300); 
    noTone(BUZZ); 

       //F4 
    tone(BUZZ, 350); 
    delay(300); 
    noTone(BUZZ); 

       // E4
    tone(BUZZ, 330); 
    delay(300) ;
    noTone(BUZZ);

    //D4 
    tone(BUZZ, 294); 
    delay(300); 
    noTone(BUZZ); 

    //C4
    tone(BUZZ, 261); 
    delay(300); 
    noTone(BUZZ);


      //C4
    tone(BUZZ, 261); 
    delay(300); 
    noTone(BUZZ);

    //D4 
    tone(BUZZ, 294); 
    delay(300); 
    noTone(BUZZ);

       // E4
    tone(BUZZ, 330); 
    delay(300) ;
    noTone(BUZZ);

      // E4
    tone(BUZZ, 330); 
    delay(500) ;
    noTone(BUZZ);

       //D4 
    tone(BUZZ, 294); 
    delay(200); 
    noTone(BUZZ);

//D4 
    tone(BUZZ, 294); 
    delay(500); 
    noTone(BUZZ);
  
  }  
  
    }

// Read moisture and decide upon action    
void automater () { 
const auto recievedData = Serial.read();
auto moist = analogRead(MOISTURE); 
if (moist > 660) 
  { digitalWrite(MOSFET, HIGH); } 
  
  else if ( recievedData == 255)
  {digitalWrite(MOSFET,HIGH); delay(2000);}

  else 
  {digitalWrite(MOSFET, LOW);}
}


void loop() {
  song();




// Measure vitals 
auto light = analogRead(LIGHT);
auto temperature =  1000; //dht.readTemperature();
auto humidity = dht.readHumidity();
auto recieve = Serial.read();


if ( light > 325) {
if (temperature >= 18 && temperature <= 29) { 
if (humidity >= 30 && humidity <= 60 ) {    

automater();
sendMoistData();

if (!Serial.available())
{delay(1000);
return;}


const auto RecievedData = Serial.read();
}

else if ( humidity < 30 ) 
{error111(); delay (1000);   
}

else if (humidity > 60) { 
  error107(); delay(1000);}}
else if ( temperature > 18 || temperature < 29) 
{ 
  if (temperature > 18) 
  {error110(); delay(1000);}
  
  else if (temperature < 29) 
  { error108(); delay(1000);} } 
}

 



else if ( light < 325) 
{ error109(); delay(1000); }











}
