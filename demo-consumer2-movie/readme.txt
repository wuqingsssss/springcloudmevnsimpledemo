1 sh mvn-clean mvn打包
2 sh docker-build  docker镜像创建，如果docker-compose 使用build 则不需要此步
3 sh mvn-docker-build 如果使用mvndocker镜像打包则不需要执行第3步
4 sh docker-compose up 当docker-cpmpose.yml 配置未images时需要执行2或者3来打包镜像，如果使用build则只需要执行1就可以运行up