#基础镜像
FROM openjdk:8-jdk
MAINTAINER whywhathow(whywhathow.fun@gmail.com)
#docker run -e PARAMS="--server.port 9090"
# JAVA_OPT: 设置堆大小, 随机函数加载,以及文件编码方式, 垃圾收集器
# GC 类型, UseG1GC,  UseParallelGC(default),  UseConcMarkSweepGC
#ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=utf8"  \
ENV \
# jvm 参数设置
JVM_GC="+UseConcMarkSweepGC"\
JVM_XMS="128m" \
JVM_XMX="256m"  \
# spring cloud, 等其他参数设置
SERVER_PORT="8080"\
SPRING_PROFILES_ACTIVE="prod"\
NACOS_ADDR="121.41.120.209"\
JAVA_RANDOM="-Djava.security.egd=file:/dev/./urandom"\
OTHER_PARAMS="--spring.cloud.nacos.config.namespace=prod --spring.cloud.nacos.config.file-extension=yml"\
# 设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
COPY ./*.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["/bin/sh","-c","java  ${JAVA_OPTS} -jar app.jar ${PARAMS}"]
# test shell
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
ENTRYPOINT ["/docker-entrypoint.sh"]