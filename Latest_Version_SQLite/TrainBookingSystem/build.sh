#!/bin/bash

#Build script for Train Booking System
#Compiles source code, runs tests and packages into a jar

#Compile all java source code
echo "Compiling..." 
./compile.sh

#Compile the unit tests
echo "Compiling tests..."
javac -cp "lib/sqlite-jdbc-3.53.0.0.jar:lib/junit-platform-console-standalone-1.10.0.jar:out" \
-d out \
test/com/trainbooking/BookingServiceTest.java


#Compile the unit tests
echo "Running tests..."
java -jar lib/junit-platform-console-standalone-1.10.0.jar \
--classpath "out:lib/sqlite-jdbc-3.53.0.0.jar" \
--scan-classpath > test-results.txt 2>&1

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
