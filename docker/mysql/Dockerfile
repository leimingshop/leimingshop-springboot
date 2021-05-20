FROM mysql:5.7
MAINTAINER kuangweiguo <kuangweiguo@leimingtech.com>
COPY ./sql  /tmp/sql
COPY ./mysql.cnf  /etc/mysql/conf.d/mysql.cnf
RUN  ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime  \
      && echo 'Asia/Shanghai' >/etc/timezone
RUN mv /tmp/sql/*.sql /docker-entrypoint-initdb.d
RUN rm -rf /tmp/sql