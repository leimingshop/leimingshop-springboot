FROM openjdk:8-jre-alpine
MAINTAINER songwenhao <songwenhao@leimingtech.com>

ARG VERSION
ARG PROJECT_NAME
ENV SERVER_PORT 28080
ENV SERVICE_NAME  $PROJECT_NAME

RUN echo "http://mirrors.aliyun.com/alpine/v3.8/main" > /etc/apk/repositories \
    && echo "http://mirrors.aliyun.com/alpine/v3.8/community" >> /etc/apk/repositories \
    && apk update upgrade \
    && apk add --no-cache procps unzip curl bash tzdata \
    && ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone \
    && set -xe \
    && apk --no-cache add ttf-dejavu fontconfig

ADD $SERVICE_NAME-$VERSION.zip /$SERVICE_NAME/$SERVICE_NAME-$VERSION.zip

RUN unzip /$SERVICE_NAME/$SERVICE_NAME-$VERSION.zip -d /$SERVICE_NAME \
    && sh -c 'touch /$SERVICE_NAME/$SERVICE_NAME.jar' \
    && rm -rf /$SERVICE_NAME-$VERSION.zip \
    && chmod +x /$SERVICE_NAME/scripts/startup.sh \
    && echo "tail -f /dev/null" >> /$SERVICE_NAME/scripts/startup.sh

EXPOSE $SERVER_PORT

CMD ["sh","-c","/$SERVICE_NAME/scripts/startup.sh"]
