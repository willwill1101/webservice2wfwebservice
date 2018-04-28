#!/bin/sh

APP_HOME=$(cd "$(dirname "$0")/.."; pwd)
appname=tvc_dir2webservice
$APP_HOME/bin/daemon.sh stop ${appname}
