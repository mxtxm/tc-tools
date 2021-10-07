# Deployment


## Operating system
The services are multiplatform and can be installed on Unix, Linux and Windows based servers.
We recommend installing on Ubuntu/Debian Linux based servers. 
UTF8 EN and FA locales must be installed/enabled on the OS.

## Install
* Java JDK JVM (last version)
* Tomcat (last version)
* RabbitMQ (last version)
* MongoDB (last version)
* ElasticSearch (last version)
* PHP
* Backend:
    Backend is provided in just one file "ROOT.war", copy to: "/var/lib/tomcat9/webapps/ROOT.war", services are enabled/disabled
    and configs are set in two files: (config.properties and tune.properties).

## Config
* Tomcat:
    * /var/lib/tomcat9/conf/server.xml add: "<Context docBase="/opt/tc-tools/files/" path="/static" />"
    * /usr/share/tomcat9/bin/setenv.sh create and add: "JAVA_OPTS="-Xms2048m -Xmx4096m -XX:-OmitStackTraceInFastThrow"
* Backend:
    * /opt/tc-tools/config.properties (file is provided)
    * /opt/tc-tools/tune.properties (file is provided)
    * browse: "http://BASE_URL:8080/admin"
    * menu: "Advanced" > "System and services administrator" > "Startup" > "Factory reset"
    
## deploy on ubuntu (first time)

    sudo apt-get update
    sudo apt-get install tomcat9
    sudo service tomcat9 status

    wget -qO - https://www.mongodb.org/static/pgp/server-5.0.asc | sudo apt-key add -
    echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/5.0 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-5.0.list
    sudo apt-get update
    sudo apt-get install -y mongodb-org
    sudo systemctl start mongod
    sudo service mongod status

    sudo apt install curl gnupg
    curl -fsSL https://github.com/rabbitmq/signing-keys/releases/download/2.0/rabbitmq-release-signing-key.asc | sudo apt-key add -
    sudo apt install apt-transport-https
    sudo add-apt-repository ppa:rabbitmq/rabbitmq-erlang
    sudo apt update
    sudo apt install rabbitmq-server --fix-missing
    sudo service rabbitmq-server start
    sudo service rabbitmq-server status
    
    sudo apt-get install php


    cd /etc/netplan
    sudo vim 00-installer-config.yaml
    >> add dnses 185.51.200.2 and 178.22.122.100 
        nameservers:
        addresses:
            - 185.51.200.2
            - 178.22.122.100
            - 172.26.7.21
            - 172.26.7.22
    sudo netplan apply
    
    curl -fsSL https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
    echo "deb https://artifacts.elastic.co/packages/7.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-7.x.list
    sudo apt update
    sudo apt install elasticsearch
    sudo service elasticsearch start
    sudo service elasticsearch status
    
    cd /etc/netplan
    sudo vim 00-installer-config.yaml
    >> remove dnses 185.51.200.2 and 178.22.122.100 
        nameservers:
        addresses:
            - 172.26.7.21
            - 172.26.7.22
    sudo netplan apply
    
    sudo vim /usr/share/tomcat9/bin/setenv.sh
    >> add
        JAVA_OPTS="-Xms2048m -Xmx4096m -XX:-OmitStackTraceInFastThrow"
    
    sudo vim /var/lib/tomcat9/conf/server.xml
    >> add inside <host tag 
        <Context docBase="/opt/tc-tools/files/" path="/static" />
    
    sudo service tomcat9 restart
    
    mkdir /opt/tc-tools
    mkdir /opt/tc-tools/files
    
    cd ~
    vim deploy-backend.sh
    >> put this lines inside the file 
        #! /bin/bash
        echo "start deploy>>>"
        sudo service tomcat9 stop
        sudo rm -r /var/lib/tomcat9/webapps/*
        sudo cp ROOT-1.0.war /var/lib/tomcat9/webapps/ROOT.war
        sudo service tomcat9 start
        echo "<<<end deploy"

    >> upload backend file to server i.e: "ROOT-1.0.war"
    i.e: sudo scp /home/lynx/projects/tc-tools/target/ROOT-1.0.war ubuntu@172.26.8.122:~
    ./deploy-backend.sh
    
    >> browse http://172.26.8.122:8080/admin
    >> if everything is OK then admin dashboard will show up. ROOT access will be available for a short period of time to
    >> startup the system for first time.
    
## deploy on ubuntu (upgrades)

    >> upload backend file to server i.e: "ROOT-1.0.war"
    i.e: sudo scp /home/lynx/projects/tc-tools/target/ROOT-1.0.war ubuntu@172.26.8.122:~
    ./deploy-backend.sh

## log truncate
    vim ~/truncate-logs.sh
    >> add
    #! /bin/bash
    shopt -s globstar
    sudo truncate -s 0 /var/log/*.log
    sudo truncate -s 0 /var/log/**/*.log
    sudo find /var/log -type f -name '*.gz' -exec rm {} +
    sudo truncate -s 0 /opt/*.log
    sudo truncate -s 0 /var/**/*.log
    sudo find /opt -type f -name '*.gz' -exec rm {} +

    sudo crontab -e
    >> add
    0 3 * * 1 ~/truncate-logs.sh
