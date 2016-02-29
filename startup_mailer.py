import subprocess
import smtplib
from email.mime.text import MIMEText
import datetime

def connect_type(word_list):
	"""This function takes a list of words, then, depending which key word, returns the corresponding internet connection type as a string"""
	if 'wlan0' in word_list or 'wlan1' in word_list:
		con_type = 'wifi'
	elif 'eth0' in word_list:
		con_type = 'ethernet'
	else:
		con_type = 'current'
	return con_type

#Account information
to='brian.cunnane@hotmail.com'
gmail_user = 'giantpizarro@gmail.com'
gmail_password = 'K5d;C2n?'
smtpserver = smtplib.SMTP('smtp.gmail.com',587)

smtpserver.ehlo()
smtpserver.starttls()
smtpserver.ehlo()
smtpserver.login(gmail_user, gmail_password)
today = datetime.date.today()

arg='ip route list'
p=subprocess.Popen(arg,shell=True,stdout=subprocess.PIPE)
data = p.communicate()  # Get data from 'p terminal'.

# Split IP text block into three, and divide the two containing IPs into words.
ip_lines = data[0].splitlines()
split_line_a = ip_lines[1].split()
#split_line_b = ip_lines[2].split()

# con_type variables for the message text. ex) 'ethernet', 'wifi', etc.
ip_type_a = connect_type(split_line_a)
#ip_type_b = connect_type(split_line_b)

"""Because the text 'src' is always followed by an ip address,
we can use the 'index' function to find 'src' and add one to
get the index position of our ip.
"""
ipaddr_a = split_line_a[split_line_a.index('src')+1]
#ipaddr_b = split_line_b[split_line_b.index('src')+1]

# Creates a sentence for each ip address.
my_ip_a = 'Your %s ip is %s' % (ip_type_a, ipaddr_a)
#my_ip_b = 'Your %s ip is %s' % (ip_type_b, ipaddr_b)

# Creates the text, subject, 'from', and 'to' of the message.
msg = MIMEText(my_ip_a + "\n")
msg['Subject'] = 'IPs For RaspberryPi on %s' % today.strftime('%b %d %Y')
msg['From'] = gmail_user
msg['To'] = to
# Sends the message
smtpserver.sendmail(gmail_user, [to], msg.as_string())
# Closes the smtp server.
smtpserver.quit()
