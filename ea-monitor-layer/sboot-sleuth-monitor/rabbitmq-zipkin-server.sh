#!/usr/bin/env bash
if [ ! -f "zipkin.jar" ];then
    echo "文件不存在则下载"
    curl -sSL https://zipkin.io/quickstart.sh | bash -s
fi
echo "启动 zipkin server"
java -Xms800m -Xmx800m -XX:PermSize=256m -XX:MaxPermSize=512m -XX:MaxNewSize=512m \
-jar zipkin.jar \
--zipkin.collector.rabbitmq.addresses=192.168.36.39:5672 \
--zipkin.collector.rabbitmq.username=zipkin \
--zipkin.collector.rabbitmq.password=zipkin2018 \
--zipkin.collector.rabbitmq.virtual-host=/zipkin

