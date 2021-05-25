#!/bin/sh
#SERVICE_NAME=leimingtech-monitor
## 添加日志目录
LOG_DIR=/app/logs/LMB03

## 创建日志目录
mkdir -p $LOG_DIR

JAVA_OPTS="-Xms256m -Xmx256m -Xmn250m -server -XX:+UseParNewGC  -XX:ParallelGCThreads=4 -XX:MaxTenuringThreshold=9 -XX:+DisableExplicitGC -XX:+ScavengeBeforeFullGC -XX:SoftRefLRUPolicyMSPerMB=0 -XX:+ExplicitGCInvokesConcurrent -XX:+HeapDumpOnOutOfMemoryError -XX:-OmitStackTraceInFastThrow -Duser.timezone=Asia/Shanghai -Dclient.encoding.override=UTF-8 -Dfile.encoding=UTF-8 -Djava.security.egd=file:/dev/./urandom"

nohup java $JAVA_OPTS -jar  /$SERVICE_NAME/$SERVICE_NAME".jar"
