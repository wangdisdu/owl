#!/bin/sh

DIR=$(cd "$(dirname "$0")" && pwd)

OWL_HOME=`dirname "$DIR"`

cd $OWL_HOME

PID_FILE="$OWL_HOME/owl.pid.lock"

if [ -f $PID_FILE ]; then
  TARGET_ID="$(cat $PID_FILE)"
  PID="$(ps -ef | grep $TARGET_ID | grep java | grep owl-web | awk '{print $2}')"
  if [ "x$PID" = "x" ]; then
    echo "can not find pid $TARGET_ID to stop"
  else
    echo "stopping $TARGET_ID"
    kill "$TARGET_ID"
    for i in {1..300}
    do
      pinfo=$(jps | grep "$TARGET_ID")
      if [ -n "${pinfo}" ]; then
        echo "waiting for stop ${pinfo}"
        sleep 2
      else
        break
      fi
    done
    echo "stopped $TARGET_ID"
  fi
else
  echo "can not find pid file $PID_FILE to stop"
fi
