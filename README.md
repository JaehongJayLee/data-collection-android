# Data Collection for Android
Android application for collecting data from device

## all of permission-free data
- sensors (accelerometer, gyroscope, orientation, magnetic field, pressure, light, proximity)
- setting (volume, screen bright, accelerometer rotation, font scale)
- application list
- network traffic
- device information

## ONE instance for all sensor and setting values
All of sensor and setting values are captured at the same time to represent them as one row of database table.
It is helpful to merge various data sources into a feature vector for machine learning problem.


## etc.
- stored in SQLlite database
- able to configure sensing interval, duration and frequency

