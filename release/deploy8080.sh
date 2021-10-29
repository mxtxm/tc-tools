#! /bin/bash
echo "start deploy 8080 >>>"
sudo cp /var/lib/tomcat9/webapps/ROOT.war ~/ROOT.war.bak
sudo rm -r /var/lib/tomcat9/webapps/*
sudo cp ROOT-1.0.war /var/lib/tomcat9/webapps/ROOT.war
echo "<<<end deploy"

