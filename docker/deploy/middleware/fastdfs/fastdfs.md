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
docker run -d --name leimingshop-storage -p 8888:8888 -v /data/leimingshop/fdfs/storage:/var/fdfs -e TRACKER_SERVER=tracker:22122  -e GROUP_NAME=group1   --link leimingshop-tracker:tracker harbor.shop7.leimingtech.com/leimingshop/leimingshop-fastdfs-storage:1.0
```


  leimingshop-storage:
    image: harbor.shop7.leimingtech.com/leimingshop/leimingshop-fastdfs-storage:1.0
    container_name: leimingshop-fastdfs-storage
    restart: on-failure:10
    ports:
      - 8888:8888
    environment:
      - TRACKER_SERVER=tracker:22122
      - GROUP_NAME=group1
    networks:
      - leimingshop
    volumes:
      - ./
    links:
      - leimingshop-tracker:tracker