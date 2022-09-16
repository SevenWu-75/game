# 基于哪个镜像，可以是远程或者本地镜像 -> 镜像名称:tag
# 这个镜像是专门做出来的一个java基础镜像，除了减少部分体积外，还集成了javacv框架所需要的一些库
FROM watrix-java8-base:latest
# 设置作者
MAINTAINER ancy
# 设置镜像标签
LABEL game-service 1.0.0
# 在docker容器内执行命令
RUN mkdir /app
# 拷贝外部文件到容器
ADD game-service.jar /app/game-service.jar
# 修改docker容器的时间时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo 'Asia/Shanghai' >/etc/timezone
# 设置语言环境
ENV LANG=en_US.UTF-8 \
    LANGUAGE=en_US.UTF-8
# 声明需要暴漏的端口
EXPOSE 8083
# 指定工作目录
WORKDIR /app/
# 配置启动jar包命令
CMD ["sh","-c","start.sh"]