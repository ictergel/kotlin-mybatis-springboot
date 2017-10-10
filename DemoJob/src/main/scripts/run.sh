#!/bin/bash
binDir=$(cd "$(dirname "$0")"; pwd)
baseDir=$binDir/../
libDir=$baseDir/lib
confDir=$baseDir/config
appName=com.demo.nmc.NmcJobApplicationKt

CLASSPATH=$CLASSPATH:baseDir

for jarPath in $libDir/*.jar
    do
        CLASSPATH=$CLASSPATH:$jarPath
    done

export CLASSPATH

stop(){
    pid=`ps -ef|grep $appName|grep name=NMC|grep -v grep|awk '{print $2}'`

    if [ "$pid" = "" ];then
        echo "NMC service is not running!"
    else
        kill -9 $pid
        vpid=`ps -ef|grep $appName|grep name=NMC|grep -v grep|awk '{print $2}'`
        if [ "$vpid" = "" ];then
        echo "shutdown NMC service success! old_pid=$pid"
        else
            echo "shutdown NMC service failed!"
        fi
    fi

}

start(){
    nohup /usr/local/jdk1.8/bin/java -Dname="NMC" -Ddefault.client.encoding="UTF-8" -Dfile.encoding="UTF-8" -Duser.language="Zh" -Duser.region="CN" -Duser.timezone="GMT+08" $appName >/dev/null 2>&1 &

    vvpid=`ps -ef | grep $appName|grep name=NMC|grep -v grep|awk '{print $2}'`
    if [ "$vvpid" = "" ];then
        echo "startup NMC service failed!"
    else
        echo "startup NMC service success! new_pid=$vvpid"
    fi
}

case "$1" in
start)
    start
    ;;
stop)
    stop
    ;;
restart)
    stop
    start
    ;;
*)
    printf 'Usage: %s {start|stop|restart}\n'
    exit 1
    ;;
esac