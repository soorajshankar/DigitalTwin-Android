# Android Digital Twin

##Project Description
This project is a sample digital twin which streams real time rotational sensor data to the mqtt cloud and the supporting web application will render the twin application on the web browser.

## Features
|                     |                    |
|---------------------|--------------------|
| MQTT                | :heavy_check_mark: |
| sensors             | Gyroscope          |

### Running the Sample App:

 * Open the this current directory in Android Studio.
 * Resolve dependancies by gradle sync.
 * Run the sample android application on a physical android device.
 * Provide a valid mqtt url (without http or tcp) and a client id to reconginze your device (this will identify this device to render the digital twin on the web application)
 * Press start button to connect with the mqtt broker.
 * Applicatio will start polling the mqtt data with the sensor data.