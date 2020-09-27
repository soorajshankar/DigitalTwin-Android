# Android Digital Twin

## Project Description
This project is a sample digital twin which streams real time rotational sensor data to the mqtt cloud and the supporting web application will render the twin application on the web browser.

## Features
|                     |                    |
|---------------------|--------------------|
| MQTT                | :heavy_check_mark: |
| sensors             | Gyroscope          |

### Running the Sample App:

 * Open the this current directory in Android Studio.
 * Resolve dependencies by gradle sync.
 * Run the sample android application on a physical android device.
 * Provide a valid mqtt url (without http or tcp) and a client id to recognize your device (this will identify this device to render the digital twin on the web application)
> You may try free MQTT brokers like http://www.hivemq.com/demos/websocket-client/
 * Press start button to connect with the mqtt broker.
 * Application will start polling the mqtt data with the sensor data.
 
 
> To check the data on broker 
> subscribe to topic "digital_twin/android/#"
> will be replaced by the client id which you specify
