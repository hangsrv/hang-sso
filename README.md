# 单点登录

分布式session：同一个网站，部署多台服务器，通过redis做统一会话存储

单点登录：存在A和B两个系统，不同系统间的会话认证

TGT：Ticket Granting Ticket

- TGT是为用户签发的Ticket，是验证用户登陆成功的唯一标识，封装了TGC以及用户信息

TGC：Ticket Granting Cookie

- TGC是这个session的唯一标识，可以认为是TGT的key

ST：Service Ticket（临时票据）

- 用户在访问某一服务时提供的ticket

# 测试

http://localhost:8080/sso/toLogin?redirectUri=http://localhost:8080/sso/toLogin