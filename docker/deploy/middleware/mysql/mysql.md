### mysql 镜像打包
##### 进入mysql当前目录
```shell script
docker build -d harbor.shop7.leimingtech.com/leimingshop/leimingshop-mysql:1.0 .
```
### mysql 镜像上传
```shell script
#登录docker仓库
docker login --username=xxx harbor.shop7.leimingtech.com
#push 镜像
docker psuh harbor.shop7.leimingtech.com/leimingshop/leimingshop-mysql:1.0
```
### mysql安装配置
##### 拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-mysql:1.0
```
##### 启动容器
```shell script
docker run -d --name leimingshop-mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -v /data/leimingshop/mysql/data:/var/lib/mysql  harbor.shop7.leimingtech.com/leimingshop/leimingshop-mysql:1.0
```