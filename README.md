# 基于SpringBoot和MyBatis-Plus的博客系统

## 如何运行

1. 启动`myqsl`和`redis`

2. 使用mysql创建一个名为`blog`的数据库，在`blog`数据库下运行[blog.sql](./blog.sql) ，并修改[application.yaml](./blog/src/main/resources/application.yml) 和 [另一个application.yaml](./admin/src/main/resources/application.yml) 中的`datasource.url`和`oss` ，该项目oss使用七牛云

3. 使用IDEA打开my-blog,并运行`blog`/`admin`，启动该项目后端

4. 在`ptu-admin-vue`或`ptu-blog-vue`运行

   ```bash
   npm install
   npm run dev
   ```

   
