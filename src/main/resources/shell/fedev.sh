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