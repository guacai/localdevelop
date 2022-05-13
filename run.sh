#!/bin/bash
CURR=$(
  cd $(dirname $0)
  pwd
)
port=10000
name=localdevelop-0.0.1-SNAPSHOT.jar

start(){
  echo "start java"
  echo ${CURR}
  if [ ! -f "${name}" ];then
    echo "find no jar"
    exit 1
  fi
  if [ ! -d ${CURR}/log/ ];then
    mkdir -p ${CURR}/log
  fi
  OLD_IFS="$IFS" #保存旧的分隔符
  IFS="-"
  array=($SENTRY)
  IFS="$OLD_IFS" # 将IFS恢复成原来的
    nohup java -Xms512m -Xmx512m -jar ${name}  > localdevelop.log 2>&1 &
  echo "start ok"
}

stop(){
  echo "stop java"
   # 根据端口号去查询对应的PID
   pid=$(lsof -i tcp:${port})

   echo pid

   # 杀掉对应的进程 如果PID不存在,即该端口没有开启,则不执行
    if [ -n  "$pid" ]; then
       kill  -9  $pid;
    fi
  echo "stop ok"
}

restart(){
  echo "restart java"
    stop
    start
  echo "restart ok"
}

case "$1" in
start)
  start
  exit 0
  ;;
stop)
  stop
  exit 0
  ;;
restart)
  restart
  exit 0
  ;;
*)
echo "Usage: $0 {start|stop|restart}"
exit 1
esac
#2e07171813543ce253c7ecb22ec65fda