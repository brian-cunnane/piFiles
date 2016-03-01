#!/usr/bin/env python
#Author: Brian Cunnane
#Date: 01/Feb/2016
#Description: Script to read data on uart connection.
#		Data is not deliniated
import serial

ser = serial.Serial("/dev/ttyAMA0")
ser.baudrate = 9600
data = ""
myList = []
print("Starting")
while 1:
	try:
		for index in range(0,7):
			state = ser.read(1)
			x = state.encode('hex')
			myList.append(x)
			if len(myList) == 7:			
				print myList
				myList = []		
	except IndexError:
		print("Error")
		pass
