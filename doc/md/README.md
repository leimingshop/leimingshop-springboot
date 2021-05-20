# 雷铭b2b2c多用户商城系统

## 项目介绍
雷铭电商7.0系统，是基于SpringBoot开发，运营端和商户端采用Element+Vue，买家使用采用Vue+Iview+nuxt服务端渲染。使用到的中间件有Redis、RabbitMQ、ElasticSearch、FastDFS、Mongodb等。主要功能包括有运营管理、商品管理、订单管理、售后管理、会员管理、财务管理、商户入驻、优惠券、满减活动、拼团活动、秒杀、限时抢购、虚拟商品、余额支付等。

QQ群：754193730

##演示地址
* 平台运营端 [http://b2b2c.leimingtech.com/admin](http://b2b2c.leimingtech.com/admin) 用户名/密码 admin/123456
* 商户端 [http://b2b2c.leimingtech.com/seller](http://b2b2c.leimingtech.com/seller) 用户名/密码 leiming/123456
* 用户端（H5使用手机浏览器）[http://b2b2c.leimingtech.com](http://b2b2c.leimingtech.com) 
* ![](../img/applet.jpg)
##技术体系
###基础框架
* Maven
* Spring 
* Mybatis-Plus
* Spring Security
### 前端框架
* VUE
* Element
* iview
* nuxt
* uniapp   
### 技术栈
* mysql
* fastdfs
* redis
* mongodb
* elasticsearch
* rabbitmq
* nginx
* docker
### 使用docker-compose一键安装部署
* 1.安装docker环境(centos)
```shell
 # step 1: 安装必要的一些系统工具
 sudo yum install -y yum-utils device-mapper-persistent-data lvm2
 # Step 2: 添加软件源信息
 sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
 # Step 3: 更新并安装 Docker-CE
 sudo yum makecache fast
 sudo yum -y install docker-ce
 # Step 4: 开启Docker服务
 sudo service docker start
```
* 2.安装docker-compose
```bash
#下载docker-compose
curl -L https://get.daocloud.io/docker/compose/releases/download/1.12.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose
#授权
chmod +x /usr/local/bin/docker-compose
#查看版本号，测试是否安装成功
docker-compose version    
```
* 3.上传docker-compose.yml到服务器,在docker-compose.yml 文件目录下启动docker-compose
```bash
docker-compose up -d 
```
* 4.服务启动成功后，配置hosts 
```
将部署服务器的ip地址指向：leimingshop
例如：192.168.1.108   leimingshop
```
* 5.访问服务
```
平台运营端： http://leimingshop/admin 用户名密码：admin/123456
商户端      http://leimingshop/seller 用户名密码： leiming/123456
用户端：    http://leimingshop

平台运营端接口地址： http://leimingshop/admin/doc.html
商户端接口地址：    http://leimingshop/seller/doc.html
用户端：          http://leimingshop/web/api/doc.html

```

### 项目结构
~~~
leimingshop-springboot
|--doc 
|--docker docker-compose 
|--leimingshop-admin 
    |--leimingshop-admin-api
    |--leimingshop-seller-api
|-- leimingshop-base
    |--leimingshop-commons
        |--leimingshop-commons-core
        |--leimingshop-commons-dynamic-datasource
        |--leimingshop-commons-lock
        |--leimingshop-commons-mybatis
        |--leimingshop-commons-tools
        |--leimingshop-logs
    |--leimingshop-elasticsearch
    |--leimingshop-mongodb
    |--leimingshop-mq
    |--leimingshop-nosql
|--leimingshop-execute
|--leimingshop-front
    |--leimingshop-auth
    |--leimingshop-web-api
|--leimingshop-frontend
|--leimingshop-monitor
|--leimingshop-mq-consumer
|--leimingshop-parment
|--leimingshop-service
    |--leimingshop-activity
    |--leimingshop-after-sale
    |--leimingshop-cart
    |--leimingshop-cms
    |--leimingshop-goods
    |--leimingshop-index-sync
    |--leimingshop-logistics
    |--leimingshop-member
    |--leimingshop-message
    |--leimingshop-operation
    |--leimingshop-order
    |--leimingshop-payment
    |--leimingshop-quartz
    |--leimingshop-settle
    |--leimingshop-static
    |--leimingshop-store
    |--leimingshop-sys
    |--leimingshop-third-party
    |--leimingshop-upload
    |--leimingshop-wechat
~~~
