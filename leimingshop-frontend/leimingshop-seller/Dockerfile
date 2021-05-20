FROM nginx:1.18.0
#VOLUME 
ADD ["dist/", "/usr/share/nginx/html/seller"]

EXPOSE :80
ENTRYPOINT ["nginx","-g","daemon off;"]
