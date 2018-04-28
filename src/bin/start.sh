#!/bin/sh
APP_HOME=$(cd $(dirname $0)/..; pwd)
cd $APP_HOME
CLASSPATH=$APP_HOME
for file in $APP_HOME/lib/*.jar;
do
  CLASSPATH="$CLASSPATH":"$file"
done
main_class=com.ehl.tvc.TvcService2serviceApplication
appname=tvc_dir2webservice
cmd="java -classpath $CLASSPATH $main_class" 
 
$APP_HOME/bin/daemon.sh start ${appname} "$cmd"
