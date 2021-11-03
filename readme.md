项目docker 部署: 
-  构建maven项目
```
# 项目根路径下
mvn clean package -Dmaven.test.skip=true
```
- 移动jar包
```
# 项目根路径
cp target/boot-redis-demo-0.0.1-SNAPSHOT.jar docker/services/demo/ 
``` 
- 构建docker image
```
cd docker
docker-compose up -d demo
```

