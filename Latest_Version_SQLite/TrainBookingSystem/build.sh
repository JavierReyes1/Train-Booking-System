#!/bin/bash

#Build script for Train Booking System
#Compiles source code and packages into a jar

#Compile all java source code
echo "Compiling..." 
./compile.sh

#Create a temp folder and compy compiled classes into it
echo "Building jar.."
rm -rf tmp
mkdir -p tmp
cp -r out/* tmp/

#Extract the SQLite driver into the temp folder
cd tmp
jar xf ../lib/sqlite-jdbc-3.53.0.0.jar
cd ..

#Package everything into a single executable jar
jar cfm TrainBookingSystem.jar manifest.txt -C tmp .

#Remove the temp folder as it's not needed anymore
echo "Cleaning up..."
rm -rf tmp

echo "Done! Run with: java -jar TrainBookingSystem.jar"
