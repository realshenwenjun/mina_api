#!/bin/sh
source /etc/profile
SERVER_PORT=8080
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

PIDS=`ps -f | grep java | grep "$DIR" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR:  already started!"
    echo "PID: $PIDS"
    exit 1
fi

classes=$DIR/classes

LOGS_DIR=$DIR/logs
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi

STDOUT_FILE=$LOGS_DIR/apiout.log

LIB_DIR=$DIR/lib

LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`  
  
classpath=$classes:$LIB_JARS 

JAVA_OPTS=" -Xms128m -Xmx512m -server -Duser.timezone=Asia/Shanghai"
java $JAVA_OPTS -classpath $classpath com.mina.StartServer #>> "$STDOUT_FILE"  

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1 
    if [ -n "$SERVER_PORT" ]; then
        COUNT=`netstat -an | grep $SERVER_PORT | wc -l`
    else
    	COUNT=`ps -f | grep java | grep "$DIR" | awk '{print $2}' | wc -l`
    fi
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PIDS=`ps -f | grep java | grep "$DIR" | awk '{print $2}'`
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"

