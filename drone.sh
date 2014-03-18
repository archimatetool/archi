#!/bin/bash
# usage: $0 <debemail> <uploadtarget>
# e.g:
# ./drone.sh "magwas,archistyledhtml@frs.sourceforge.net:/home/pfs/project/archici"
set -x
date
mvnversion=$(grep version pom.xml |head -1|sed 's/.*<version>//;s/<.*//')
branchname=$(echo $DRONE_BRANCH-$DRONE_BUILD_NUMBER | sed 'sA/A-A')
version=$(echo $mvnversion |sed "s/qualifier/$branchname/")
mkdir ~/Downloads
(wget -q -O ~/Downloads/archi-extra.tar.gz.in http://magwas.rulez.org/archi-extra.tar.gz && mv ~/Downloads/archi-extra.tar.gz.in ~/Downloads/archi-extra.tar.gz)&
sudo apt-get update
sudo apt-get install libwebkitgtk-1.0-0 devscripts maven xvfb wine wkhtmltopdf
sudo Xvfb -nolock :99 -screen 0 1024x768x24+32&
export DISPLAY=:99
sleep 3s; xterm&#keep the display
mvn install
if [ $DRONE_BRANCH = "master" ]
then
     DEPLOYMENT=stable
else
     DEPLOYMENT=unstable
fi
dest=target/upload
kill `ps ax |grep xterm|grep -v grep|awk '{print $1}'`
ps ax |grep Xvfb|egrep -v "grep|sudo"|awk '{print $1}' |xargs sudo kill 
ps afx
date
mkdir -p target/upload
mv com.archimatetool.build/target/products/Archi/Archi-* target/upload
scp -r target/upload/* $1/$DEPLOYMENT
date
