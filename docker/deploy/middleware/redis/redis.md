### reds安装配置
##### 拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-redis:1.0
```
##### 启动容器
```shell script
docker run -d --name leimingshop-redis -p 6379:6379  -v /data/leimingshop/redis/redis.conf:/etc/redis/redis.conf harbor.shop7.leimingtech.com/leimingshop/leimingshop-redis:1.0 redis-server /etc/redis/redis.conf --port 6379 --requirepass 123456  --appendonly yes
```

