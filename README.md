美容服务预约系统 - 后端
项目简介
这是一个基于 Spring Boot 3 + MyBatis-Plus 的美容服务预约系统后端项目，实现了用户管理、服务管理、订单管理等核心功能。

技术栈
Spring Boot 3.0.3
MyBatis-Plus 3.5.3.1
MySQL 8.0+
Maven
Lombok
FastJSON
项目结构
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器层
│   │   │   ├── entity/         # 实体类
│   │   │   ├── mapper/         # 数据访问层
│   │   │   ├── service/        # 服务层
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── db.sql                    # 数据库初始化脚本
└── pom.xml
功能模块
用户管理 (YonghuController)
用户登录
用户注册
获取用户信息
更新用户信息
重置密码
用户列表查询
用户删除
服务管理 (MeirongController)
服务列表查询（支持分页、搜索、排序）
服务详情查询
新增服务
更新服务
删除服务
订单管理 (MeirongOrderController)
订单列表查询（支持分页、状态筛选）
订单详情查询
创建订单
更新订单
删除订单
数据库设计
用户表 (yonghu)
id: 主键
username: 用户名
password: 密码
yonghu_name: 用户姓名
yonghu_phone: 手机号
yonghu_id_number: 身份证号
yonghu_photo: 照片
sex_types: 性别(1:男,2:女)
yonghu_email: 邮箱
new_money: 余额
create_time: 创建时间
服务表 (meirong_service)
id: 主键
service_name: 服务名称
service_category: 服务分类(1:面部护理,2:身体护理,3:美甲美睫,4:其他服务)
service_old_money: 服务原价
service_new_money: 服务现价
service_duration: 服务时长(分钟)
service_photo: 服务照片
service_process: 服务流程
service_products: 使用产品
service_notes: 注意事项
service_content: 服务简介
service_clicknum: 点击次数
service_rating: 服务评分
shangxia_types: 上下架状态(1:上架,2:下架)
create_time: 创建时间
订单表 (meirong_order)
id: 主键
order_uuid_number: 订单号
service_id: 服务项目
staff_id: 服务师
yonghu_id: 用户
order_true_price: 实付价格
order_status: 订单状态(1:待确认,2:已确认,3:服务中,4:已完成,5:已取消)
shijianduan: 预约时间段
buy_time: 预约日期
insert_time: 订单创建时间
create_time: 创建时间
pay_status: 支付状态(0:未支付,1:已支付)
pay_time: 支付时间
order_comment: 服务评价
order_rating: 服务评分
快速开始
环境要求
JDK 17+
Maven 3.6+
MySQL 8.0+
数据库配置
创建数据库
CREATE DATABASE meirong_yuyue DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
导入数据表
mysql -u root -p meirong_yuyue < db.sql
修改配置文件 编辑 src/main/resources/application.properties：
spring.datasource.url=jdbc:mysql://localhost:3306/meirong_yuyue?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=your_password
启动项目
# 编译项目
mvn clean install

# 启动项目
mvn spring-boot:run
项目启动后访问：http://localhost:8080/tiyuguan

API接口文档
用户相关接口
登录
接口: POST /yonghu/login
参数:
username: 用户名
password: 密码
返回:
{
  "code": 0,
  "msg": "登录成功",
  "token": "xxx",
  "username": "xxx",
  "userId": 1,
  "role": "user"
}
注册
接口: POST /yonghu/register
参数: YonghuEntity对象
返回:
{
  "code": 0,
  "msg": "注册成功"
}
服务相关接口
服务列表
接口: GET /meirong/list
参数:
page: 页码(默认1)
limit: 每页数量(默认10)
serviceName: 服务名称(可选)
serviceCategory: 服务分类(可选)
shangxiaTypes: 上下架状态(可选)
orderBy: 排序字段(可选)
sort: 排序方式(asc/desc,可选)
返回:
{
  "code": 0,
  "msg": "查询成功",
  "page": {
    "list": [...],
    "total": 100
  }
}
订单相关接口
创建订单
接口: POST /meirongOrder/save
参数: MeirongOrderEntity对象
返回:
{
  "code": 0,
  "msg": "预约成功"
}
默认账号
管理员账号
用户名: admin
密码: admin
测试用户
用户名: test
密码: 123456
开发说明
