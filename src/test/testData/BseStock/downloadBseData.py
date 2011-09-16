#!/usr/bin/env python
#Copyright 2011 Nube Technologies
#Licensed under the Apache License, Version 2.0 (the "License");
#you may not use this file except in compliance with the License.
#You may obtain a copy of the License at
#http://www.apache.org/licenses/LICENSE-2.0
#Unless required by applicable law or agreed to in writing, software distributed
#under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
#CONDITIONS OF ANY KIND, either express or implied.
#See the License for the specific language governing permissions and limitations under the License.

import os
import sys
import urllib

def download(filePath,startDate,EndDate,outputPath):
	f = open(filePath, 'r')
	for line in f:
		url="http://www.bseindia.com/stockinfo/stockprc2_excel.aspx?scripcd="+line+"&FromDate="+startDate+"&ToDate="+EndDate+"&OldDMY=D"
   		print url
		try:
			webFile = urllib.urlopen(url)
			localFile = open(outputPath+"/"+line.strip(), 'w')
			localFile.write(webFile.read())
			webFile.close()
			localFile.close()
		except IOError:
			print 'Filename not found.'
	f.close()

if __name__ == '__main__':
	if len(sys.argv) == 5:
		try:
			download(sys.argv[1],sys.argv[2],sys.argv[3],sys.argv[4])
		except IOError:
			print 'Filename not found.'
	else:
		print 'usage: %s urls.txt 01/06/2011 05/06/2011 /outputPath' % os.path.basename(sys.argv[0])
