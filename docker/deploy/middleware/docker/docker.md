###docker安装
##### 1、安装一些必要的系统工具：
```shell script
sudo yum install -y yum-utils device-mapper-persistent-data lvm2
```
##### 2、添加软件源信息：
```shell script
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
```
##### 3、更新 yum 缓存
```shell script
sudo yum makecache fast
```
##### 4、安装 Docker-CE：
```shell script
sudo yum -y install docker-ce
```
##### 5、启动 Docker 后台服务
```shell script
sudo systemctl start docker
```

### 2.安装docker-compose
```shell script
#下载docker-comose
curl -L https://get.daocloud.io/docker/compose/releases/download/1.12.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
#授权
chmod +x /usr/local/bin/docker-compose
#查看版本号，测试是否安装成功
docker-compose version    
```

