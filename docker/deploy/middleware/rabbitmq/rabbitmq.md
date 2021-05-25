### rabbitmq安装配置
##### 拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-rabbitmq:1.0
```
##### 启动容器
```shell script
docker run -d --name leimingshop-rabbitmq -p 5672:5672 -p 15672:15672  -e RABBITMQ_DEFAULT_VHOST=leimingshop -e RABBITMQ_DEFAULT_USER=guest  -e RABBITMQ_DEFAULT_PASS=guest -v /data/leimingshop/rabbitmq/data:/var/lib/rabbitmq  -v /data/leimingshop/rabbitmq/log:/var/log/rabbitmq harbor.shop7.leimingtech.com/leimingshop/leimingshop-rabbitmq:1.0
```


                
           