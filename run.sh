#!/bin/bash
mkdir ./bin
javac -d bin -cp ./lib/jade.jar src/lab4/*.java
LOCALIP=`/sbin/ifconfig eth0 | grep 'inet addr:' | cut -d: -f2 | awk '{ print $1}'`
HOSTIP="172.30.0.196"
if [ "$1" == "main" ]; then 
    java -cp ./lib/jade.jar:./bin jade.Boot -gui -jade_domain_df_maxresult 10000000 -local-host $LOCALIP Coordinator-$LOCALIP:lab4.CoordinatorAgent
else
  if [ $# > 0 ]; then
    java -cp ./lib/jade.jar:./bin jade.Boot -container -container-name $LOCALIP -host $HOSTIP -local-host $LOCALIP -local-port $1 SubCoord-$LOCALIP:lab4.SubCoordinatorAgent
  else
    java -cp ./lib/jade.jar:./bin jade.Boot -container -container-name $LOCALIP -host $HOSTIP -local-host $LOCALIP SubCoord-$LOCALIP:lab4.SubCoordinatorAgent
  fi
fi
