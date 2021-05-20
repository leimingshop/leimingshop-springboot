FROM nginx:1.18
RUN mkdir -p /www/website
WORKDIR /www/website
COPY  ./admin/ /www/website/admin
COPY  ./seller/ /www/website/seller
RUN rm /etc/nginx/conf.d/default.conf
ADD default.conf /etc/nginx/conf.d/