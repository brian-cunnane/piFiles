#!/usr/bin/env python
#Author: Brian Cunnane
#Date: 01/Feb/2016
#Description: Script to read data on uart connection.
#		Data to be deliniated by ";"
import serial

ser = serial.Serial("/dev/ttyAMA0")
ser.baudrate = 9600
data = ""
row = 0
print("Starting")
while 1:
#	print("starting")
	try:
		state = ser.read(1)
#		if state.strip() != "0x0d":
#			data += state
#		else:
#			print(data)
#
#			data = ""
	#	print('z')
		x = state.encode('hex')
	#	x = ord(state)
		print(x)
	except:
		print("Error")
		pass
