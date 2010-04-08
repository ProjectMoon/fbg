#!/bin/bash
#This is the Linux/UNIX installation script for the Factbook Generator program.
#It creates the config file, and copies files from the source directory to /usr/bin
#and other various places.

if [ "$(id -u)" != "0" ]; then
	echo "This script must be run as root!" 1>&2
	exit 1
fi

#Now that we know we're running as root, we can proceed.
#This script does the following:
#0. Does some preliminary checks (i.e. Java installed?)
#1. Copies the JAR and its libraries to /opt/FactbookGenerator
#2. Creates a config file in ~/FBG
#3. Creates a launcher script in /usr/bin

#---PRELIMINARY CHECKS---
#First, check Java
echo "Checking Java Version..."
java -version

if [ $? != "0" ]; then
	echo "You appear to be missing Java, or your PATH variable is messed up. Please fix this." 1>&2
	exit 2
fi

#---Copy all the files over---
echo "Copying files..."
PROGLOC=/opt/FactbookGenerator/
rm -rf $PROGLOC
mkdir -p $PROGLOC
cp FactbookGenerator.jar $PROGLOC
cp -r lib/ $PROGLOC
cp -r scripts/ $PROGLOC
cp license.txt $PROGLOC

#---Create the launcher script in /usr/bin
echo "Creating launch script."
LAUNCHER=/usr/bin/fbg
echo "#!/bin/bash" > $LAUNCHER
echo "#Launcher for Factbook Generator." >> $LAUNCHER
echo "java -jar ${PROGLOC}FactbookGenerator.jar \$*" >> $LAUNCHER
chmod 755 $LAUNCHER

echo "Installed!"
