# 雷铭b2b2c多用户商城系统

## 项目介绍
雷铭电商7.0系统，是基于SpringBoot开发，运营端和商户端采用Element+Vue，买家使用采用Vue+Iview+nuxt服务端渲染。使用到的中间件有Redis、RabbitMQ、ElasticSearch、FastDFS、Mongodb等。主要功能包括有运营管理、商品管理、订单管理、售后管理、会员管理、财务管理、商户入驻、优惠券、满减活动、拼团活动、秒杀、限时抢购、虚拟商品、余额支付等。

QQ群：754193730
##演示地址
* 平台运营端 [http://b2b2c.leimingtech.com/admin](http://b2b2c.leimingtech.com/admin) 用户名/密码 admin/123456
* 商户端 [http://b2b2c.leimingtech.com/seller](http://b2b2c.leimingtech.com/seller) 用户名/密码 leiming/123456
* 用户端（H5使用手机浏览器）[http://b2b2c.leimingtech.com](http://b2b2c.leimingtech.com) 
* ![img.png](doc/image/applet.jpg)
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
### 一键安装
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
#下载docker-comose
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
leimingshop
|--doc
    |--db -- 数据库相关sql
    |--docker -- docker打包文件
    |--lib -- 项目所需jar包
|--leimingshop-admin 
    |--leimingshop-admin-api
    |--leimingshop-seller-api
|-- leimingshop-base
    |--leimingshop-commons
        |--leimingshop-commons-core -- 常用工具类
        |--leimingshop-commons-dynamic-datasource -- 动态数据源配置
        |--leimingshop-commons-lock -- 分布式锁配置
        |--leimingshop-commons-mybatis -- mybatis配置
        |--leimingshop-commons-tools -- 常用工具类
        |--leimingshop-logs -- 日志配置
    |--leimingshop-elasticsearch  -- elasticsearch模块
    |--leimingshop-mongodb -- mongodb模块
    |--leimingshop-mq -- RabbitMQ模块
    |--leimingshop-nosql -- 非关系型数据库模块
|--leimingshop-execute -- 定时任务执行器
|--leimingshop-front PC端相关接口
    |--leimingshop-auth -- 登录认证模块
    |--leimingshop-web-api -- PC端接口模块
|--leimingshop-monitor -- monitor监控中心
|--leimingshop-mq-consumer -- MQ消费者模块
|--leimingshop-parent -- 父工程，依赖管理
|--leimingshop-service -- 业务逻辑代码
    |--leimingshop-activity -- 活动模块
    |--leimingshop-after-sale -- 售后模块
    |--leimingshop-cart -- 购物车模块
    |--leimingshop-cms -- 圈子模块
    |--leimingshop-goods -- 商品模块
    |--leimingshop-index-sync -- 索引管理模块
    |--leimingshop-logistics -- 日志模块
    |--leimingshop-member -- 用户模块
    |--leimingshop-message -- 短信和邮箱模块
    |--leimingshop-operation -- 运营模块
    |--leimingshop-order -- 订单模块
    |--leimingshop-payment -- 支付模块
    |--leimingshop-quartz -- 定时任务模块
    |--leimingshop-settle -- 结算模块
    |--leimingshop-static -- 统计模块
    |--leimingshop-store -- 店铺模块
    |--leimingshop-sys -- 系统设置模块
    |--leimingshop-third-party -- 第三方相关模块
    |--leimingshop-upload -- 文件上传模块
    |--leimingshop-wechat -- 对接微信相关模块
~~~

### 页面展示 
#### App/H5/微信小程序展示
![img.png](doc/image/img.png)

![img_1.png](doc/image/img_1.png)
![img_2.png](doc/image/img_2.png)
![img_3.png](doc/image/img_3.png)
![img_4.png](doc/image/img_4.png)
![img_5.png](doc/image/img_5.png)
####  PC首页展示
![img.png](doc/image/img0.png)
#### 搜索页面
![img_1.png](doc/image/img_11.png)
#### 商品详情页
![img_2.png](doc/image/img_12.png)
#### 购物车结算页
![img_3.png](doc/image/img_13.png)
#### 店铺搜索页
![img_4.png](doc/image/img_14.png)
#### 平台运营端首页
![img_5.png](doc/image/img_15.png)
##### 商品管理
![img_6.png](doc/image/img_6.png)
#### 订单管理
![img_7.png](doc/image/img_7.png)

