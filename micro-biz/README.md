# micro-biz

### TODO
- 分布式事务
- MQ消息队列
- 支付宝支付
- 微信支付
- RPC
- IM


### 技术栈
- Spring Boot
- Jackson
- Mybatis：DAO框架
- Mybatis Plus：基于Mybatis实现单表的CURD操作
- jasypt：配置文件敏感配置项加密
- Swagger：API描述文档
- Jetcache：两级缓存
- log4j2+disruptor：异步高性能日志打印
- OSS：基于七牛云上传文件
- SMS：基于阿里云发送短信
- JWT：分布式会话
- mapstruct：对象转换，即非反射的参数值拷贝
- lombok：自动生成常用代码，减少机械代码的编写
- thymeleaf
- AOP
- Druid：数据库连接池，包括可视化界面监控SQL和应用
- Validator


### 功能特性
- 逻辑删除
- 乐观锁更新等
- 敏感配置项加密配置
- 简化简化单表CRUD操作
- Mybatis自动化识别Mapper接口扫描目录
- 角色、资源与权限动态管理，并且支持注解式验证权限与排除权限
- 详细链路追踪，可追踪请求在控制层、服务层、DAO层和其它自定义层之间的详细调用链，并且可追踪出每个切入点的输入/出参数
- DAO层可详细打印执行的SQL、耗时和执行结果集内容
- 支持七牛云OSS文件存储
- 支持阿里云发送短信
- 支持阿里JetCache二级缓存框架
- 使用log4j2+disruptor实现日志的高性能打印
- 支持JWT实现分布式会话
- 支持配置文件不打包进jar包中
- 使用JSW打包成（各主流系统：Aix、Linux、Windows、Macosx等）开箱即用的脚本与框架
- 使用assembly将JSW的结果打成压缩包，便于各环境传输
- 支持远程JMX监控
- 支持远程DEBUG调试
- Commons Logging自动桥接转为Log4j2
- 自动捕获并处理业务层和框架层的异常，并返回给前端做出友好提示
- DAO操作自动化填充created（创建时间）、creator（创建人）、updated（更新时间）、updater（更新人）字段
- 支持Mybatis分页插件
- 统一化API接口正常与异常的响应JSON结构
- 支持近期接口耗时分布统计，便于及时发现最近的接口调用平均耗时情况
- 支持自定义注解式参数验证器，如手机号码、枚举等数据类型


### 打包命令

```
mvn clean package appassembler:generate-daemons -Dmaven.test.skip=true
```

### Mapper层方法命名规范 —— DB视角CRUD操作
- selectXxx
- insertXxx
- updateXxx
- deleteXxx
- countXxx
- searchXxx

### Service层方法命名规范 —— 服务/业务视角操作
- getXxx
- addXxx
- editXxx
- removeXxx
- countXxx
- searchXxx
- queryXxx

### Controller层方法命名规范 —— 场景视角操作
- search
- login
- register
....

### MQ
#### RocketMQ
教程地址：https://guozh.net/rocketmqshiyongyizhiwindowdajianbushurocketmq/

**第一步**：下载解压
载地址：http://rocketmq.apache.org/release_notes/release-notes-4.4.0
注意：绝对路径不要有空格。

**第二步**：Start Name Server
```
$ nohup sh bin/mqnamesrv &
$ tail -f ~/logs/rocketmqlogs/namesrv.log
The Name Server boot success...
```

**第三步**：Start Broker
注意：
1.不要使用PowerShell客户端启动。
2.启动命令不要添加autoCreateTopicEnable=true参数（请先在管理端建立测试的Topic后再进行验证）
```
$ nohup sh bin/mqbroker -n localhost:9876 &
$ tail -f ~/logs/rocketmqlogs/broker.log 
The broker[%s, 172.30.30.233:10911] boot success...
```

**第四步**：启动管理端
```
java -jar rocketmq-console-ng-1.0.1.jar --server.port=12581 --rocketmq.config.namesrvAddr=127.0.0.1:9876
```

### SMS
1.最多可验证3次后过期
2.有效期为2分钟
3.判断用户可信度[风险识别]，不可信用户添加发短信前验证码校验

### SpringBoot性能优化项
- 优化注解@SpringBootApplication
- 将Servlet容器由Tomcat变成Undertow(Tomcat的吞吐量5000左右,Undertow的吞吐量8000左右)
- JVM调优
memory = heap + non-heap
non-heap = threads x stack + classes x 7/1000