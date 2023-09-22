#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/app/step2
cd $REPOSITORY

APPNAME=gradwork
JAR_NAME=$(ls $REPOSITORY/zip | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/zip/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH "
nohup java -jar -Dspring.config.location=classpath:/application.yml,/home/ubuntu/app/application-oauth.yml,/home/ubuntu/app/application-prod.yml,/home/ubuntu/app/application-aws.yml,/home/ubuntu/app/application-api.yml,/home/ubuntu/app/application-jwt.yml $JAR_PATH > $REPOSITORY/nohup.out 2>&1 &
