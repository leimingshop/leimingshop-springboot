FROM node:12.19.0-alpine
MAINTAINER kuangweiguo
ENV NODE_ENV=production
ENV HOST 0.0.0.0
RUN mkdir -p /app
COPY  ./pc /app
WORKDIR /app
#此为cnpm淘宝镜像
#RUN npm config set registry https://registry.npm.taobao.org
RUN npm install
RUN npm install -global cross-env
EXPOSE 8000

CMD ["npm", "start"]