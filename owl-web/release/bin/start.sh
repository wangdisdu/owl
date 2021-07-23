#!/bin/sh

DIR=$(cd "$(dirname "$0")" && pwd)

if [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA="$JAVA_HOME/bin/java"
else
    echo "JAVA_HOME is not set and java could not be found."
    exit 1
fi

export OWL_HOME=`dirname "$DIR"`

cd $OWL_HOME

ProgramJar=${OWL_HOME}/lib/owl-web-*-exec.jar
LoggingConfig=${OWL_HOME}/conf/logback-spring.xml
ConfigLocation=classpath:/application.yml,file://${OWL_HOME}/conf/application.yml
LibDir=${OWL_HOME}/lib

OWL_JAVA_OPTS="-Xmx4096m -Xms4096m"

exec "$JAVA" -Dloader.path=${LibDir} -jar $OWL_JAVA_OPTS -Dspring.config.location=${ConfigLocation} -Dlogging.config=${LoggingConfig} ${ProgramJar} "$@" <&- &
