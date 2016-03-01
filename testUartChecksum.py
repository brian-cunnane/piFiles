#!/usr/bin/env python
#Author: Brian Cunnane
#Date: 01/Feb/2016
#Description: Script to read data on uart connection.
#               Data is not deliniated
import serial

ser = serial.Serial("/dev/ttyAMA0")
ser.baudrate = 9600
ser.flushInput()
checksum = 0x00;
myList = []
print("Starting")
while 1:
        try:
                state = ser.read(1)
                myList.append(state)
                if len(myList) == 7:
                        for index in range(2,6):
                                checksum = checksum ^ ord(myList[index])
                        print "this is checksum %d" %checksum
                        if checksum == ord(myList[6]):
				for index in range(0,7):
					myList[index] = ord(myList[index])
				print myList
			else: print ("Checksum mismatch, discarding packet")
                        myList[:] = []
                        checksum = 0x00
        except (IndexError, serial.SerialException) as Error:
                print("Exception caught")
		print(Error)

