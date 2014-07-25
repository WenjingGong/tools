#! /bin/bash

i2cset -y 1 0x54 0x08 0x35

/usr/lib/leeoalpha/test/pirsetup

rm /usr/lib/leeoalpha/test/pir_test.txt
touch /usr/lib/leeoalpha/test/pir_test.txt

while [ 1 -lt 2 ]
do
	/usr/lib/leeoalpha/test/pirmain >> /usr/lib/leeoalpha/test/pir_test.txt
	sleep 0.04
done
