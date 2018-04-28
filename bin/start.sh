#!/bin/sh
APP_HOME=$(cd $(dirname $0)/..; pwd)
#
cd $APP_HOME
# loading dependency jar in lib directory
CLASSPATH=$APP_HOME
for file in $APP_HOME/lib/*.jar;
do
  CLASSPATH="$CLASSPATH":"$file"
done
#echo $CLASSPATH
main_class=com.yangx.springboot.App
cmd="java -classpath $CLASSPATH $main_class" 
 
$APP_HOME/bin/daemon.sh start  "$cmd"
