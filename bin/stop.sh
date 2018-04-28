#!/bin/sh

APP_HOME=$(cd "$(dirname "$0")/.."; pwd)
$APP_HOME/bin/daemon.sh stop 
