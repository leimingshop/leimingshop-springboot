### mongodb 安装配置
##### 拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-mongodb:1.0
```
##### 启动容器
```shell script
docker run -d --name leimingshop-mongodb -p 27017:27017 -v /data/leimingshop/mongodb:/data/db  harbor.shop7.leimingtech.com/leimingshop/leimingshop-mongodb:1.0
```


