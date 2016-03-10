#!/usr/bin/env python
#Author: Brian Cunnane
#Description: Test script to insert values into database

import MySQLdb

now = time.strftime("%c")
print "connecting"
db = MySQLdb.connect("localhost","Administrator","admin","BEEKEEPER")
cursor = db.cursor()

try:
	print "Inserting data"
	cursor.execute("INSERT INTO HIVE1 (TEMPERATURE,HUMIDITY,WEIGHT,TIME,DATE) VALUES(24,90,150,CURTIME(),CURDATE())")
	db.commit()
	print "Data committed"

except MySQLdb.Error as dbie:
	print dbie
	db.rollback

