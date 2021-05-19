# Dockerfile for leimingtech-monitor
# 1. Copy leimingtech-monitor-${VERSION} to current directory
# 2. Build with: docker build -t leimingtech-monitor .
# 3. Run with: docker run -p 30304:30304 -e NACOS_URL="192.168.0.240:8848" -e SENTINEL_URL="192.168.0.240:8858" -d -v /app/logs:/app/logs --net=host --name leimingtech-monitor leimingtech-monitor

FROM openjdk:8-jre-alpine
MAINTAINER songwenhao <songwenhao@leimingtech.com>

ENV VERSION 0.0.1-SNAPSHOT
ENV SERVER_PORT 30101
# naocs地址信息
ENV NACOS_URL ""
# sentinel地址信息
ENV SENTINEL_URL ""

RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && set -xe \
    && apk --no-cache add ttf-dejavu fontconfig

ADD leimingtech-monitor-${VERSION}.zip /leimingtech-monitor/leimingtech-monitor-${VERSION}.zip

RUN unzip /leimingtech-monitor/leimingtech-monitor-${VERSION}.zip -d /leimingtech-monitor \
    && sh -c 'touch /leimingtech-monitor/leimingtech-monitor.jar' \
    && rm -rf /leimingtech-monitor/aleimingtech-monitor-${VERSION}.zip \
    && chmod +x /leimingtech-monitor/scripts/startup.sh \
    && echo "tail -f /dev/null" >> /leimingtech-monitor/scripts/startup.sh

EXPOSE $SERVER_PORT

CMD ["/leimingtech-monitor/scripts/startup.sh"]
