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
import glob
import sys
import time

def change(inputPath,outputPath):
	listing = os.listdir(inputPath)
	for infile in listing:
		print "current file is: " + infile
		readFile = open(inputPath+"/"+infile, 'r')
		writeFile = open(outputPath+"/"+infile.strip(), 'a')
		lineNum=0
		for line in readFile:
			outLine=""
			words=line.split(',')
			if words[0]!="Date":
				changedDate = time.strptime(words[0],"%d-%B-%Y")
				words[0] = time.strftime("%Y%m%d",changedDate)
				words[0] = infile[0:6]+words[0]
			count=0
			for word in words:
				if count==0:
					outLine += word
				else:
					outLine +=","+word
				count+=1
			if(lineNum!=0):
				writeFile.write(outLine)
			lineNum+=1

if __name__ == '__main__':
	if len(sys.argv) == 3:
		change(sys.argv[1],sys.argv[2])
	else:
		print 'usage: %s inputPath/ outputPath/' % os.path.basename(sys.argv[0])
