# spring cloud微服务
## eureka-server注册中心
客户端都向它去注册
## eureka-client-consumer服务消费者
综合使用REST+Ribbon、Freign组件对服务进行调用
## eureka-client-producer服务提供者
目前提供文件上传服务，接受表单提交文件或者文件进行BASE64加密之后上传，返回nginx代理的文件路径。
