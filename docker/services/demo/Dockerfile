#基础镜像
FROM openjdk:8-jdk
MAINTAINER whywhathow(whywhathow.fun@gmail.com)
#docker run -e PARAMS="--server.port 9090"
# JAVA_OPT: 设置堆大小, 随机函数加载,以及文件编码方式, 垃圾收集器
# GC 类型, UseG1GC,  UseParallelGC(default),  UseConcMarkSweepGC
#ENV JAVA_OPTS="-Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -Dfile.encoding=utf8"  \
ENV \
JVM_GC="UseParallelGC"\
JVM_XMS="128m" \
JVM_XMX="256m"  \
#JAVA_OPTS="-Xms${JVM_XMS} -Xmx${JVM_XMX}-XX:+${JVM_GC} -Djava.security.egd=file:/dev/./urandom"\
JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom"\
PARAMS="--server.port=8080 --spring.profiles.active=prod "
# 设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone
COPY ./*.jar /app.jar
#EXPOSE 8080
#ENTRYPOINT ["/bin/sh","-c","java  ${JAVA_OPTS} -jar app.jar ${PARAMS}"]
# test shell
COPY docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]