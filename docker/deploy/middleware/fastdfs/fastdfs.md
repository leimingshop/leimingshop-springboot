### fastdfs 安装配置
##### 拉取tracker镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-tracker:1.0
```
##### 启动容器
```shell script
docker run -d --name leimingshop-tracker -p 22122:22122 -v /data/leimingshop/fdfs/tracker:/var/fdfs  harbor.shop7.leimingtech.com/leimingshop/leimingshop-fastdfs-tracker:1.0
```
##### 拉取storage镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-fastdfs-storage:1.0
```
##### 启动容器
```shell script
#storage 使用host网络模式
docker run -d --name leimingshop-storage --net=host -v /data/leimingshop/fdfs/storage:/var/fdfs  -e TRACKER_SERVER=192.168.0.7:22122  -e GROUP_NAME=group1  harbor.shop7.leimingtech.com/leimingshop/leimingshop-fastdfs-storage:1.0
```