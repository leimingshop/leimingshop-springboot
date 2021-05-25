###部署leimingshop-admin-api
#####拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-admin-api:1.0
```
#####运行容器
```shell script
#中间件容器和应用容器在同一服务器上，使用同一网络连接的方式
docker run -d --name leimingshop-admin-api --network springboot_leimingshop -p 28081:28081  --link leimingshop-redis:leimingshop-redis --link leimingshop-elasticsearch:leimingshop-elasticsearch --link leimingshop-rabbitmq:leimingshop-rabbitmq --link leimingshop-mysql:leimingshop-mysql  --link leimingshop-mongodb:leimingshop-mongodb --link leimingshop-fastdfs-tracker:leimingshop-fastdfs  -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-admin-api:1.0
#中间件容器和应用容器不在同一服务器是，使用添加host访问
docker run -d --name leimingshop-admin-api -p 28081:28081  --add-host=leimingshop-redis:192.168.0.7 --add-host=leimingshop-elasticsearch:192.168.0.7 --add-host=leimingshop-rabbitmq:192.168.0.7 --add-host=leimingshop-mysql:192.168.0.7 --add-host=leimingshop-mongodb:192.168.0.7 --add-host=leimingshop-fastdfs:192.168.0.7 -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-admin-api:1.0
```

###部署leimingshop-seller-api
#####拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-seller-api:1.0
```
#####运行容器
```shell script
#中间件容器和应用容器在同一服务器上，使用同一网络连接的方式
docker run -d --name leimingshop-seller-api --network springboot_leimingshop -p 28080:28080 --link leimingshop-redis:leimingshop-redis --link leimingshop-elasticsearch:leimingshop-elasticsearch --link leimingshop-rabbitmq:leimingshop-rabbitmq --link leimingshop-mysql:leimingshop-mysql  --link leimingshop-mongodb:leimingshop-mongodb --link leimingshop-fastdfs-tracker:leimingshop-fastdfs  -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-seller-api:1.0
#中间件容器和应用容器不在同一服务器是，使用添加host访问
docker run -d --name leimingshop-seller-api -p 28080:28080  --add-host=leimingshop-redis:192.168.0.7 --add-host=leimingshop-elasticsearch:192.168.0.7 --add-host=leimingshop-rabbitmq:192.168.0.7 --add-host=leimingshop-mysql:192.168.0.7 --add-host=leimingshop-mongodb:192.168.0.7 --add-host=leimingshop-fastdfs:192.168.0.7 -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-seller-api:1.0
```

###部署leimingshop-web-api
#####拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-web-api:1.0
```
#####运行容器
```shell script
#中间件容器和应用容器在同一服务器上，使用同一网络连接的方式
docker run -d --name leimingshop-web-api --network springboot_leimingshop -p 17070:17070 --link leimingshop-redis:leimingshop-redis --link leimingshop-elasticsearch:leimingshop-elasticsearch --link leimingshop-rabbitmq:leimingshop-rabbitmq --link leimingshop-mysql:leimingshop-mysql  --link leimingshop-mongodb:leimingshop-mongodb --link leimingshop-fastdfs-tracker:leimingshop-fastdfs  -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-web-api:1.0
#中间件容器和应用容器不在同一服务器是，使用添加host访问
docker run -d --name leimingshop-web-api -p 17070:17070  --add-host=leimingshop-redis:192.168.0.7 --add-host=leimingshop-elasticsearch:192.168.0.7 --add-host=leimingshop-rabbitmq:192.168.0.7 --add-host=leimingshop-mysql:192.168.0.7 --add-host=leimingshop-mongodb:192.168.0.7 --add-host=leimingshop-fastdfs:192.168.0.7 -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-web-api:1.0
```

###部署leimingshop-mq-consumer
#####拉取镜像
```shell script
  docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-mq-consumer:1.0
```
#####运行容器
```shell script
  #中间件容器和应用容器在同一服务器上，使用同一网络连接的方式
  docker run -d --name leimingshop-mq-consumer --network springboot_leimingshop -p 48080:48080 --link leimingshop-redis:leimingshop-redis --link leimingshop-elasticsearch:leimingshop-elasticsearch --link leimingshop-rabbitmq:leimingshop-rabbitmq --link leimingshop-mysql:leimingshop-mysql  --link leimingshop-mongodb:leimingshop-mongodb --link leimingshop-fastdfs-tracker:leimingshop-fastdfs  -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-mq-consumer:1.0
  #中间件容器和应用容器不在同一服务器是，使用添加host访问
  docker run -d --name leimingshop-mq-consumer -p 48080:48080 --add-host=leimingshop-redis:192.168.0.7 --add-host=leimingshop-elasticsearch:192.168.0.7 --add-host=leimingshop-rabbitmq:192.168.0.7 --add-host=leimingshop-mysql:192.168.0.7 --add-host=leimingshop-mongodb:192.168.0.7 --add-host=leimingshop-fastdfs:192.168.0.7 -v /data/leimingshop/leimingshop/logs:/app/logs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-mq-consumer:1.0
```
