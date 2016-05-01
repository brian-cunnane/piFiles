#!/usr/bin/env python
#Author: Brian Cunnane
#Date: 01/Feb/2016
#Description: Script to read data on uart connection.
#               Data is not deliniated
import serial
import MySQLdb
import smtplib
from email.mime.text import MIMEText
import datetime

ser = serial.Serial("/dev/ttyAMA0")
ser.baudrate = 9600
ser.flushInput()
checksum = 0x00;
weight = 0.0;
myList = []
emailSent = False
print("Starting")
while 1:
        try:
		db = MySQLdb.connect("localhost","Administrator","admin","BEEKEEPER")
		cursor = db.cursor()
                state = ser.read(1)
                myList.append(state)
                if len(myList) == 11:
                        print myList
                        for index in range(2,10):
                                checksum = checksum ^ ord(myList[index])
				print ("Checksum = %d") %checksum
                        print checksum
                        if checksum == ord(myList[10]):
				for index in range(0,11):
					myList[index] = ord(myList[index])
				print myList
				weight = (myList[4] << 24) + (myList[5] << 16) + (myList[6] << 8) + (myList[7])
				print weight
				weight = weight/1000.0
				cursor.execute("INSERT INTO HIVE1(TEMPERATURE,WEIGHT,HUMIDITY,TIME,DATE) VALUES(%s,%s,%s,CURTIME(),CURDATE())",(myList[3],weight,myList[8]))
				db.commit()
				if myList[9] > 50 or myList[9] < 0: #check Z axis value
                                        if emailSent == False:
                                                print("Warning; hive has fallen");
                                                emailWarning()
                                                emailSent = True
                                else: emailSent = False;
			else: print ("Checksum mismatch %d, discarding packet") %checksum
                        myList[:] = []
                        checksum = 0x00
        except (IndexError, serial.SerialException) as Error:
                print("Exception caught")
		print(Error)
	except MySQLdb.Error as DbError:
		db.rollback()
		print("Database Error")
		print(DbError)

        def emailWarning():
                "Function to create a warning email if hive has fallen"
                to='brian.cunnane@hotmail.com'
                gmail_user = 'giantpizarro@gmail.com'
                gmail_password = 'K5d;C2n?'
                smtpserver = smtplib.SMTP('smtp.gmail.com',587)

                smtpserver.ehlo() #identify self to extended smtp server
                smtpserver.starttls()#start encryption
                smtpserver.ehlo()#identify again as per documentation
                smtpserver.login(gmail_user, gmail_password)

                msg = MIMEText("Beekeeper has detected that a hive has fallen over \n")
                msg['Subject'] = "URGENT fallen hive detected"
                msg['From'] = gmail_user
                msg['To'] = to

                #send the message
                smtpserver.sendmail(gmail_user, [to], msg.as_string())
                #Close the smtp server
                smtpserver.quit()

                return;
            
