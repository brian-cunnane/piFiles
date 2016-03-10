#!/usr/bin/env python
#Author: Brian Cunnane
#Description: Test script to read values from database

import MySQLdb

try:
	db = MySQLdb.connect("localhost","Administrator","admin","BEEKEEPER")
	cursor = db.cursor()

	cursor.execute("USE BEEKEEPER;")
	cursor.execute("SELECT * FROM HIVE1;")

	print "\nTemperature\tHumidity\tWeight\t\tTime\t\tDate"
	print "**************************************************************************"

	for reading in cursor.fetchall():
		print str(reading[0])+"\t\t"+str(reading[1])+"\t\t"+str(reading[2])+"\t\t"+str(reading[3])+"\t"+str(reading[4])

	db.close()
except MySQLdb.Error as error:
	print error
