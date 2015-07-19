geowifiscanner
==============

Primitive example Android app (with a php / text file backend) to teach about data collection. The code sends data including 'lat/long/visible SSIDs/associated wifi strength' and saves it to a space-delimited text file with each line possessing the format:

lat lon accuracy att use string

As it stands, the app has no error catching. So, expect crashes if the device has wifi turned off (and similar states).

License clarification forthcoming.
