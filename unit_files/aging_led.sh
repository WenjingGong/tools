#!/bin/bash

/usr/lib/leeoalpha/sensors/led_off.sh
while [ 1 -lt 2 ]
do
	/usr/lib/leeoalpha/sensors/led_red.sh
	sleep 1
	/usr/lib/leeoalpha/sensors/led_off.sh
	/usr/lib/leeoalpha/sensors/led_green.sh
	sleep 1
	/usr/lib/leeoalpha/sensors/led_off.sh
	/usr/lib/leeoalpha/sensors/led_blue.sh
	sleep 1
	/usr/lib/leeoalpha/sensors/led_off.sh
	/usr/lib/leeoalpha/sensors/led_white.sh
	sleep 1
	/usr/lib/leeoalpha/sensors/led_off.sh

done

