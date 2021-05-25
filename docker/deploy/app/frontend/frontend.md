###部署admin,seller前端
#####拉取镜像
```shell script
  docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-frontend:1.0
```
#####运行容器
```shell script
  docker run -d --name leimingshop-frontend  -p 80:80  --add-host=web.api.com:127.0.0.1  --add-host=seller.api.com:127.0.0.1  --add-host=admin.api.com:127.0.0.1  --add-host=pc.node.com:127.0.0.1  --add-host=image.nginx.com:192.168.0.7  harbor.shop7.leimingtech.com/leimingshop/leimingshop-frontend:1.0
 ```

###部署pc前端
#####拉取镜像
```shell script
docker pull harbor.shop7.leimingtech.com/leimingshop/leimingshop-pc:1.0
```
#####运行容器
```shell script
docker run -d --name leimingshop-pc -p 8000:8000  harbor.shop7.leimingtech.com/leimingshop/leimingshop-pc:1.0
 ```

