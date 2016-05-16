#!flask/bin/python

import numpy as np
import matplotlib.pyplot as plt
import psycopg2
import sys


con = None

try:

    con = psycopg2.connect(host='ec2-23-21-215-184.compute-1.amazonaws.com', database='d24hmpf2j1dcmb', user='yhgxfatitsbsux', port=5432, password='75lOcKaYggEsZilsEKONaeH6t7')
    cur = con.cursor()
    cur.execute('SELECT version()')
    ver = cur.fetchone()
    print ver
    cur.execute('SELECT * from tipousuario')
    ver = cur.fetchone()
    print ver

except psycopg2.DatabaseError, e:
    print 'Error %s' % e
    sys.exit(1)



finally:

    if con:
        con.close()
