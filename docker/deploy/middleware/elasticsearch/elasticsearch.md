### elasticsearc 安装配置
##### 拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-elasticsearch:1.0
```
##### 启动容器
```shell script
docker run -d --name leimingshop-elasticsearch -p 9200:9200 -v /data/leimingshop/es01/data:/usr/share/elasticsearch/data  -v /data/leimingshop/es01/logs:/usr/share/elasticsearch/logs   -e node.name=master -e bootstrap.memory_lock=true  -e cluster.name=leimingshop -e ES_JAVA_OPTS="-Xms512m -Xmx512m" -e discovery.type="single-node"  harbor.shop7.leimingtech.com/leimingshop/leimingshop-elasticsearch:1.0
```

