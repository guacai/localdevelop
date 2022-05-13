#!/bin/bash
cat >bbb.sh <<'EOF' && chmod 777 bbb.sh
CURR=$(
  cd $(dirname $0)
  pwd
)
function errLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [ERR] [${ttime}] $1"
    exit 1
}

function infoLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [INFO] [${ttime}] $1"
}

function warnLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [WARN] [${ttime}] $1"
}

function processLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [PROCESS] [${ttime}] -> $1"
}

if [ $# -lt 5 ];then
     errLog 'the input param is not right'
     exit 1
fi
user=$1
pass=$2
host=$3
path=$4
cmd=$5

function stop() {
    svc_p=${path%/*}
    if [ ! -d ${svc_p} ];then
      errLog 'the svc is not exist!'
      exit 1
    fi
    path='${path%%.*}'
    cd ${path}
    sh run.sh stop
}
function refresh(){

  svc_p=${path%/*}
  if [ ! -d ${svc_p} ];then
      mkdir -p  ${svc_p}
  fi
  cd ${svc_p}
  dir_name=${path##*/}
  scp ${user}@${host}:${path} ./ >/dev/null
  if [ ! -f ${dir_name} ];then
     errLog 'transform error'
     exit 1
  fi
  if [  -d  ${svc_p}/${dir_name}/ ];then
      sh ${svc_p}/${dir_name}/run.sh stop
  fi
  ls
  tar -zxf ${dir_name}
  rm ${dir_name}
  dir_name=${dir_name%%.*}
  n=`ls ${dir_name}/*.jar |wc -l`
  if [ $n -eq 0 ];then
     errLog 'no jar is presented!'
     exit 1
  fi
  n=`ls ${dir_name}/run.sh |wc -l`
  if [ $n -eq 0 ];then
     errLog 'no script is presented!'
     exit 1
  fi
  if [ $? -eq  0 ];then
    cd  ${svc_p}/${dir_name}
    sh  run.sh start
  else
    errLog "download error,please check the url or the internet"
    return 1
  fi
}




case "$5" in
"restart")
    refresh
    ;;
"start")
    refresh
    ;;
"stop")
    stop
    ;;
*)
    errLog "输入节点必须为: {restart|start|stop}"
    exit 1
    ;;
esac
EOF
ssh $6  '/bin/bash -s' <bbb.sh $1 $2 $3  $4 $5 >/dev/null
if [ "$?" -eq '0' ];then
  echo 'success'
fi

#!/bin/bash
cat >aaa.sh <<'EOF' && chmod 777 aaa.sh
#!/bin/bash
CURR=$(
  cd $(dirname $0)
  pwd
)
echo $1,$2,$5,$#

function errLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [ERR] [${ttime}] $1"
    exit 1
}

function infoLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [INFO] [${ttime}] $1"
}

function warnLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [WARN] [${ttime}] $1"
}

function processLog() {
    local ttime=`date +%Y-%m-%d' '%H:%M:%S.%N | cut -b 1-23`
    echo " [PROCESS] [${ttime}] -> $1"
}

if [ $# -lt 5 ];then
     errLog 'the input param is not right'
     exit 1
fi
user=$1
pass=$2
host=$3
path=$4
cmd=$5


function refresh(){
  svc_p=${path%/*}
  if [ ! -d ${svc_p} ];then
      mkdir -p  ${svc_p}
  fi
  cd ${svc_p}
  #sshpass -p $pass
  scp ${user}@${host}:${path} ./

  dir_name=${path##*/}
  tar -zxf ${dir_name}
  rm ${dir_name}
  if [ $? -eq  0 ];then
     echo "成功"
  else
    errLog "download error,please check the url or the internet"
    return 1
  fi
}
if [ $cmd == 'refresh' ];then
   refresh
else
   errLog 'the cmd is not right!'
fi
EOF

ssh $6  '/bin/bash -s' <aaa.sh $1 $2 $3  $4 $5

if [ $# -lt 2 ];then
  echo "please input the ips"
  exit  1
fi


ips=$1
OLD_IFS="$IFS"
IFS=","
hostlist=($ips)
function test_ping()
{
  hostlist=$1
  for i in ${hostlist[@]}
    do
      res=`ssh $2 "ping -c2 $i" `
      #res=`eval  ping -c2  $i >/dev/null 2>&1`
      if [[ "$?" != "0" ]];then
        echo $i" is not connected"
      else
        echo $i " is connected"
      fi
    done
}

IFS="$OLD_IFS"

test_ping "$hostlist" $2