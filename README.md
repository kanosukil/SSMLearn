# Spring + SpringMVC + MyBatis 入门学习

+ Maven 依赖

  [pom.xml](pom.xml)

+ web.xml 配置

  [web.xml](src\main\webapp\WEB-INF\web.xml)

  + 加载 Spring : org.springframework.web.context.ContextLoaderListener

    ```xml
    <!--加载 Spring 配置文件-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <!--指定 Spring 的配置文件-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <!--默认Spring配置文件applicationContext.xml-->
        <param-value>classpath:springmvc.xml</param-value>
    </context-param>
    ```

  + 加载 Spring MVC : org.springframework.web.servlet.DispatcherServlet

    ```xml
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!--没有指定 SpringMVC 的 xml 路径时, 默认寻找 xxx-servlet.xml 文件-->
        <!--init-param 标签可以设定 Spring MVC 的配置文件路径和指定配置文件-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <!--SpringMVC配置文件dispatcher-servlet.xml(xxx-servlet.xml)-->
            <param-value>classpath:dispatcher-servlet.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <!--可以设置多个 dispatcher 来处理不同 URL 的请求, 区分不同的应用场景-->
    ```

## Spring

### Bean 的 注入方式:

> 固定框架

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd">
    <bean>...</bean>
    ...
</beans>
```

1. 扫描注入

   1. `<context:annotation-config>` 开启注解功能

      > 扫描, 根据注解注入 javaBean

   2. `<context:component-scan base-package="指定扫描的目录"/>`

      > 包括上者, 还可以指定在 base-package 下扫描并注入 javaBean

   + 注解

     ```java
     // 标注 Bean
     @Repository
     @Service
     @Controller // Spring 框架不应该扫描此注解, 因此需要排除
     @Configuration
     @Component
     ...
     
     // 注入需要使用的 Bean
     @Autowired
     @Aualifier("注入的 Bean id")
     // or
     @Resource
     ```

   + **注**: 

     > 自动扫描 com.bookstore.spring 下除 controller 注解标注的类

     ```xml
     <context:component-scan base-package="com.bookstore.spring">
         <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
     </context:component-scan>
     ```

2. 手动注入

   > Bean Scope: 单例 Singleton & 多例 Prototype
   >
   > 1. 单列: 同一个 context 对象(扫描对象/构建 Bean 的对象)创建同一个 bean 元素时,创建的所有实例都指向**同一个**; 从 Spring IOC 容器中创建一个 Bean 的实例对象都是同一个.
   > 2. 多例: 同一个context对象创建同一个bean元素时,创建的实例**皆不同**; 每从 Spring IOC 容器中创建一个 Bean 的实例对象都是重新创建一个.
   >
   > 可以通过输入对象的 id 验证.
   >
   > 使用: 
   >
   >    `<bean id="自定义" class="类的完全限定名" scope="singleton|prototype">`
   >
   > 默认使用: 单例

   1. 无参构建 & 有参构建

      > 构建 Bean 的对应类需要带有无参构造方法
      >
      > 主要作用: 构建实例对象 Entities

      + 无参构建

        ```xml
        <bean id="自定义" class="对应类的完全限定名" ></bean>
        ```

      + 有参构建

        ```xml
        <bean id="自定义" class="对应类的完全限定名" >
            <constructor-arg name="String" value="自定义" /> <!--参数名-->
            <!--或者-->
            <constructor-arg index="Integer" value="自定义" /> <!--参数位置-->
        </bean>
        ```

        > index 以 0 为第一个参数位置.

      + Bean 标签的属性 property

        ```xml
        <!--应用类使用, 注入需要的 Bean-->
        <property name="需要注入的对象" ref="当前文件中配置的 Bean"> </property>
        <!--name 为需要注入到此Bean中的对象名-->
        
        <!--实体类使用, 设置指定属性的初值-->
        <property name="内置私有属性名" value="传入值" />
        <!--通过 Setter (前提是有对应属性的 setter )为 Entity 设置初始值-->
        <!--value 可为类的完全限定名, 也可为之前定义的 Bean-->
        ```

   2. 静态工厂构建

      > 并不要大量创建 Bean 时可以使用

      + 创建 BeanFactory 类(可自定义类名)

        ```java
        public class BeanFactory {
            ...
        }
        ```

      + 在类中使用静态方法返回需要注入到 Spring IOC 容器中的类

        ```java
        public static xxxController getxxxController() {
            return new xxxController();
        }
        ```

      + 在配置文件中创建 Bean

        ```xml
        <bean id="xxxController" factory-bean="beanFactory" factory-method="getxxxService"> </bean>
        ```

   3. 实例工厂构建

      > 需要大量创建 Bean 时可使用

      + 创建 BeanFacotory 类

      + 在类中使用普通方法返回需要注入到 Spring IOC 容器中的类

      + 在配置文件中配置

        ```xml
         <bean id="beanFactory" class="beanFactory 的完全限定名"> </bean>
         <bean id="Bean 的 id" factory-bean="beanFactory" factory-method="BeanFactory中返回此 Bean 的方法"> </bean>
        ```

### 使用 Bean (Entity & Service & 数据库操作类 的开发)

+ 配置了 `<property name="需要注入的对象" ref="当前文件中配置的 Bean"> </property>` 的 Bean 可直接引用对应的 对象 (private 类名 变量名;)

+ 没有配置上述属性的: 

  ```java
  @ContextConfiguration("classpath:applicationContext.xml")
  public class ClassName {
      static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
      public 返回值类型 方法名(参数列表) {
          context.getBean("bean id"); // 需要强制类型转换
          ...
      }
  }
  ```

## SpringMVC

### SpringMVC 配置文件: xxx-servlet.xml

+ 扫描 Controller 注解注册 Bean

  ```xml
  <!--只管理controller 其他交由Spring管理-->
  <context:component-scan base-package="com.bookstore.spring.controller"/>
  <!--配置注解驱动 可以将request参数与绑定到controller参数上 -->
  <mvc:annotation-driven/>
  ```

+ 静态资源映射 (SpringMVC 中使用 mapping 中的值, 直接映射到 location 中)

  ```xml
  <!--静态资源映射-->
  <mvc:resources mapping="/css/**" location="../webapp/statics/css/"/>
  <mvc:resources mapping="/js/**" location="../webapp/statics/js/"/>
  <mvc:resources mapping="/images/**" location="../webapp/statics/images/"/>
  <mvc:default-servlet-handler/>
  ```

+ 配置显示层映射

  > 默认情况下, SpringMVC 只能使用 HTML & JSP 中的其中一种
  >
  > 需要同时使用 HTML & JSP 的话, 需要引入模板引擎 (以 freemarker 模板引擎为例)
  >
  > ```xml
  > <dependency>
  >     <groupId>org.springframework</groupId>
  >     <artifactId>spring-context-support</artifactId>
  >     <version>5.3.13</version>
  > </dependency>
  > <dependency>
  >     <groupId>org.freemarker</groupId>
  >     <artifactId>freemarker</artifactId>
  >     <version>2.3.31</version>
  > </dependency>
  > ```

  ```xml
  <!--JSP-->
  <bean id="jspViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
      <property name="contentType" value="text/html;charset=UTF-8"/> <!--编码-->
      <!--搜索前缀-->
      <property name="prefix" value="/views/JSP/"/> <!--JSP 文件的位置 默认为webapp文件夹下的位置-->
      <property name="order" value="1"/> <!--执行顺序, 越小优先级越高-->
      <!--搜索后缀-->
      <property name="suffix" value=".jsp"/> 
      <property name="viewClass" value="org.springframework.web.servlet.view.JstlView"/>
  </bean>
  <!--HTML-->
  <!--freemarker 模板引擎配置-->
  <bean id="freemarkerConfiguration" class="org.springframework.beans.factory.config.PropertiesFactoryBean">
      <property name="location" value="classpath:freemarker.properties"/> <!--配置配置文件-->
      <!--还可以配置编码信息 等-->
  </bean>
  <bean id="freemarkerConfig" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
      <property name="freemarkerSettings" ref="freemarkerConfiguration"/>
      <property name="templateLoaderPath" value="/views/HTML/"/> <!--HTML 文件的位置(默认在webapp目录下)-->
  </bean>
  <bean id="htmlViewResolver" class="org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver"><!--使用FreeMarker作为HTML文件的解析器-->
      <property name="suffix" value=".html"/>
      <property name="order" value="0"/> <!--执行顺序-->
      <property name="contentType" value="text/html;charset=UTF-8"/>
  </bean>
  ```

### Controller 开发

+ 请求方法: `@RequestMapping(value="url请求路径", method = RequestMethod.GET)` // method 常用方法有 GET POST PUT DELETE

  > 上述方法等同于: `@GetMapping("url请求路径")`
  >
  > 同理, 其他方法也有对应的简写
  >
  > `@PostMapping("path")`
  >
  > `@PutMapping("path")`
  >
  > `@DeleteMapping("path")`
  >
  > ...

+ 请求参数: 

  > `@RequestParam("var") // http://localhoset:8080/path?var=1`
  >
  > `@PathVariable("var") // http://localhost:8080/path/1`
  >
  > `@ResquestBody  // 请求体参数`
  >
  > 注: ?var= 也可以使用 HttpServletRequest 传入参数获得(使用 Servlet 的方法获得请求, 也可以响应请求)
  >
  > request.getParameter("var")
  >
  > 需要依赖: 
  >
  > ```xml
  > <!--Servlet 依赖-->
  > <dependency>
  >  <groupId>javax.servlet</groupId>
  >  <artifactId>servlet-api</artifactId>
  >  <version>2.5</version>
  > </dependency>
  > ```
  >
  > Spring 框架兼容使用 Servlet 的两个参数 HttpServletRequest & HttpServletResponse 进行请求的处理和响应的设置.

+ 返回值: 

  + 返回 String , 返回的值和前面的 MVC 配置的前后缀结合搜索匹配的 JSP | HTML 文件输出显示.

    1. 返回数据: 使用 **Model 对象**(作为参数传入的) 的 addAttribute("name", Object) 方法传数据给 JSP 页面 (HTML 不能使用)
  
    2. 也可以使用 **ModelAndView 对象**(但是返回值不是 String, 而是 ModelAndView 对象): 作为参数获得 ModelAndView 对象, 使用 setViewName("String") 方法设置返回的页面, 使用 addObject("name", Object) 方法设置传给 JSP 页面的数据.
  
    + 上述两者的传入对象若为自建的实体类, 则对应实体类必须带有每个属性的 getter() 方法.
  
  + 返回非 String, 使用 `@ResponseBody` 则返回的为 Json 字符串
    1. 需要引入 Json 依赖: `com.fasterxml.jackson.core:jackson-databind` 或使用阿里的 fastjson
    2. 对应类必须实现 toString() 方法
  
+ Example

  ```java
  @Controller
  @RequestMapping("/book")
  public class BookController {
      @Autowired
      private BookService bookService;
      // 不同路径风格的处理方法
      
      // http://localhoset:8080/book?id=1
      @GetMapping("")
      public String showBookByCategoryId(@RequestParam("id") String id, Model model) {
          model.addAttribute("book", bookService.findBookByCategoryId(id));
          return "main";
      }
  
      // http://localhost:8080/book/path/1
      @GetMapping(value = "path/{id}")
      public String getBookByCategoryId(@PathVariable("id")String id, Model model) {
          model.addAttribute("book", bookService.findBookByCategoryId(id));
          return "main";
      }
  
      // 通过传统的 ServletRequest 方法获得传入值
      // http://localhost:8080/book/view?id=1
      @GetMapping("view")
      public String findBookByCategoryID(HttpServletRequest request) {
          String id = request.getParameter("id");
          request.setAttribute("books", bookService.findBookByCategoryId(id));
          return "main";
      }
  
  
  }
  // 返回的都是 webapp 目录下的 main.jsp 或 main.html 页面
  ```


## MyBatis

> Spring 配置 MyBatis 环境: 引入依赖(版本自定)
>
> ```xml
> <!--Mybatis-core-->
> <dependency>
>     <groupId>org.mybatis</groupId>
>     <artifactId>mybatis</artifactId>
>     <version>3.5.8</version>
> </dependency>
> <!--Mybatis-Spring-->
> <dependency>
>     <groupId>org.mybatis</groupId>
>     <artifactId>mybatis-spring</artifactId>
>     <version>2.0.6</version>
> </dependency>
> ```
>
> 整合后, mybatis-config.xml 文件即可弃用, 直接在 Spring 配置文件中配置 SqlSessionFactory 的 Bean

### MyBatis 引入(Spring 的配置文件中编写)

1. 设置 数据源 / 数据库连接池对象

   ```xml
   <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
       <property name="driverClassName" value="org.sqlite.JDBC"/>
       <property name="url" value="jdbc:sqlite:DataBase\bookstore.db"/>
       <!--数据库文件已放置在同目录下的 DataBase 下-->
       <!-- 数据池中最大连接数和最小连接数-->
       <property name="maxActive" value="20"/>
       <property name="minIdle" value="5"/>
       <!--可在此标签下设置数据库连接的账户和密码-->
       <!--<property name="username" value="${username}"/>-->
       <!--<property name="password" value="${password}"/>-->
   </bean>
   <!--此处使用的是 阿里的 druid 数据源-->
   
   <!--JDBC 的源-->
   <bean id="dataSource" class="org.springframework.jdbc.datasource.DreiverManagerDataSource">
       <property name="driverClassName" value="org.sqlite.JDBC" />
       <property name="url" value="jdbc:sqlite:DataBase\bookstore.db">
   </bean>
   ```

2. 获取 SqlSessionFactory 对象

   > **MyBatis 框架主要是围绕 SqlSessionFactory 进行**
   >
   > 获得 SqlSessionFactory 对象:
   >
   > 1. 定义一个configuration对象，其中包括数据源、mapper文件、事务和影响数据库行为属性设置。
   > 2. 通过 configuration 对象，创建一个 SqlSessionFactoryBuilder 对象
   > 3. 通过 SqlSessionFactoryBuilder 获得 SqlSessionFactory 实例
   >
   > **SqlSessionFactory 实例可以获得操作数据的 SqlSession 实例，通过该实例对数据库进行操作.**

   ```xml
   <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
       <!-- 注入连接池
           给 sqlsessionFactory 的属性 dataSource 赋值
              ref="引用的 database 的 id"
           -->
       <property name="dataSource" ref="dataSource"/>
       <!-- 注入 映射文件 mapper
           给 sqlsessionFactory 的属性 mapperLocation 赋值
           value="mybatis 的 mapper 文件位置"
           -->
       <property name="mapperLocations" value="classpath:mapper/**.xml"/>
   </bean>
   ```

3. 获取 SqlSession 对象

   ```xml
   <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
       <constructor-arg index="0" ref="sqlSessionFactory"/>
   </bean>
   ```

4. 为 数据库操作类 映射

   + 单个映射

     ```xml
         <bean id="数据操作类的类名" class="数据库操作类的完全限定名">
             <property name="sqlSessionFactory" ref="sqlSessionFactory"/>
         </bean>
     ```

   + 批量扫描映射

     ```xml
         <bean id="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
             <property name="basePackage" value="数据库操作类的包名"/>
             <!--多个包之间可以使用逗号或者分号进行分隔-->
             <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
         </bean>
     ```

### Mapper.xml 的编写

> 官方中文文档 :https://mybatis.org/mybatis-3/zh/sqlmap-xml.html

> 固定 Mapper.xml 框架
>
> ```xml
> <?xml version="1.0" encoding="utf-8" ?>
> <!DOCTYPE mapper
>         PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
>         "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
> <mapper namespace="数据库操作类的完全限定名">
>     ...
> </mapper>
> ```
>
> 占位符: 
>
>     #{} 表示一位占位符;#{id}表示接收参数的名称为id
>     #{} ${} 占位符
>     #{} 表示传入的数据将当作为一个字符串(自动为自动传入的数据加一个双引号)
>     ${} 表示将传入的数据直接显示在SQL中
>     
>     #{property,javaType=int,jdbcType=NUMERIC} 输入数据类型转换成输出数据类型

+ mapper 标签: 接口类和映射文件的关系, 使用 namespace 属性指明接口类(使用类的完全限定名)

  > 其他操作(包括 SQL 操作 和 缓存 等设置都在此标签下进行)

+ select 标签

  + 属性 properties:

    1. **id  唯一标识符**
    2. **parameterType 传入参数的类全限定名或别名(可选)**
    3. **resultType 返回结果的期望的类全限定名或别名(结果为集合, 则为集合包含的类型)**
    4. **resultMap 对外部 resultMap 的命名引用(resultMap 和 resultType 两者之间只能存在一个)**
    5. flushCache 设置为 true 只要语句被调用就会导致本地缓存和二级缓存被清空(*默认:false*)
    6. useCache 设置为 true 将会把语句结果被二级缓存缓存起来(默认:select为true)
    7. timeout 抛出异常前,驱动等待返回结果的秒数(默认:unset)(依据数据库驱动)
    8. fetchSize 给驱动的建议值(驱动每次*批量返回结果*的行数等于该值)(默认:unset)(依赖数据库驱动)
    9. statementType 可选:STATEMENT(Statement) PREPARED(PreparedStatement) CALLABLE(CallableStatement) (默认:PREPARED)
    10. resultType 可选:FORWARD_ONLY SCROOL_SENSITIVE SCROLL_INSENSITIVE DEFAULT(==unset)(默认:unset)(依赖数据库驱动)
    11. databaseId 若配置了数据库厂商标识,MyBatis会加载所有不带databaseId或匹配当前databaseId的语句(同一个语句不带和带的都有,忽略不带的)
    12. resultOrdered 仅针对嵌套结果select语句(true:将会假设包含了嵌套结果集或是分组，当返回一个主结果行时，就不会产生对前面结果集的引用。 这就使得在获取嵌套结果集的时候不至于内存不够用。)(默认:false)
    13. resultSets 仅使用多结果集的情况(列出执行后的返回结果集并赋予每一个结果集一个名称 ','分隔)

  + 使用

    ```xml
    <select properties="value">
        SQL select 语句;
    </select>
    ```

+ update & insert & delete 标签

  + 属性

    1. **id**
    2. **parameterType**
    3. flushCache 设置为true 只要语句被调用就会导致本地缓存和二级缓存被清空(*默认:true*)
    4. timeout
    5. statementType
    6. useGeneratedKeys: *仅适用insert update* 使数据库的主键自增(默认:false)
    7. keyProperty: *仅适用insert update* 指定能够唯一识别对象的属性(可用主键直接套用)(默认:unset)
    8. keyColumn: *仅适用insert update* 设置主键在表中的列名(主键不是第一列时,必须设置)
    9. databaseId 

    > keyProperty & keyColumn 可以设置多个值(对应多个主键, 使用 ',' 分隔).
    >
    > **keyProperty & keyColumn 的使用需要设置 useGeneratedKeys 为 true**
    >
    > keyProperty & keyColumn 不能同时使用.

  + 多行插入(需要数据库&数据库驱动支持)

    + 传入一个 `Author` 数组或集合, 并返回自动生成的主键.

      ```xml
      <insert id="insertAuthor" useGeneratedKeys="true"
          keyProperty="id">
        insert into Author (username, password, email, bio) values
        <foreach item="item" collection="list" separator=",">
          (#{item.username}, #{item.password}, #{item.email}, #{item.bio})
        </foreach>
      </insert>
      ```

  + 对于**不支持自动生成主键列的数据库**和**可能不支持自动生成主键的 JDBC 驱动**, MyBatis 有另外一种方法来生成主键.

    ***使用内嵌标签: selectKey***

    ```xml
    <insert id="insertAuthor">
      <selectKey keyProperty="id" resultType="int" order="BEFORE">
        select CAST(RANDOM()*1000000 as INTEGER) a from SYSIBM.SYSDUMMY1
          <!--不建议实际使用, 这里只是为了展示 MyBatis 处理问题的灵活性和宽容度-->
      </selectKey>
      insert into Author
        (id, username, password, email,bio, favourite_section)
      values
        (#{id}, #{username}, #{password}, #{email}, #{bio}, #{favouriteSection,jdbcType=VARCHAR})
    </insert>
    ```

  + 内嵌标签: selectKey

    + 主要作用: 自动生成主键的值
    + 主要针对 insert 标签
    + 属性
      1. keyProperty: 该标签的返回值的名称/结果应该被设置到的目标属性(不止一个 ',' 逗号分隔)
      2. keyColumn: 返回结果集中生成列属性的列名(不止一个 ',' 逗号分隔)
      3. resultType: 返回结果的类型(一般 MyBatis 可以推断出来), 若生成的列不止一个, 可使用 包含期望属性的 Object 或 Map.
      4. order: BEFORE | AFTER 是在 insert 语句执行前执行(BEFORE), 还是 insert 语句执行后执行(AFTER).
      5. statementType: 同前

+ SQL 标签

  + 定义可以重复使用的 SQL 片段(可以在 Select Insert Update Delete 标签中重复使用 SQL 片段(可以是不完整的 SQL 语句))

  + 使用

    ```xml
    <!--定义-->
    <sql id="userColumns"> 
        ${alias}.id,${alias}.username,${alias}.password 
    </sql>
    <!--使用-->
    <select id="selectUsers" resultType="map">
      select
        <include refid="userColumns"><property name="alias" value="t1"/></include>,
        <include refid="userColumns"><property name="alias" value="t2"/></include>
      from some_table t1 cross join some_table t2
    </select>
    ```

+ resultMap 结果映射

  ```xml
  <!-- mybatis-config.xml 中 -->
  <typeAlias type="com.someapp.model.User" alias="User"/>
  <!-- SQL 映射 XML 中 -->
  <select id="selectUsers" resultType="User">
    select 
      user_id             as "id",
      user_name           as "userName",
      hashed_password     as "hashedPassword"
    from some_table
    where id = #{id}
  </select>
  
  <!-- 相当于使用了 -->
  <resultMap id="userResultMap" type="User">
    <id property="id" column="user_id" />
    <result property="username" column="user_name"/>
    <result property="password" column="hashed_password"/>
  </resultMap>
  <!-- 若显式使用 ResultMap, 则需要删去 resultType, 使用 resultMap 属性 -->
  <select id="selectUsers" resultMap="userResultMap">
    select user_id, user_name, hashed_password
    from some_table
    where id = #{id}
  </select>
  ```

  + 完全可以不用显式地配置 resultMap

  + 子标签: 

    + id & result

      > *id* 和 *result* 元素都将一个列的值映射到一个简单数据类型 (String, int, double, Date 等) 的属性或字段.

      + `<id property="id" column="post_id"/>`
      + `<result property="subject" column="post_subject"/>`
      + 属性: 
        + property 映射的类的属性名
        + column 数据库中的列名或别名
        + javaType 一个 Java 类的全限定名或别名(写基本类型, 默认为包装类, 例如: javaType=int 映射的为 Integer, 而 javaType=_int 映射的为 int)
        + jdbcType JDBC 类型
        + typeHandler 属性值是一个类型处理器实现类的全限定名, 使用此属性将覆盖默认的类型处理器

    + 构造方法

      ```xml
      <constructor>
          <idArg column="id" javaType="int"/>
          <arg column="username" javaType="String"/>
          <arg column="age" javaType="_int"/>
      </constructor>
      ```

    + 关联

      ```xml
      <association property="author" column="blog_author_id" javaType="Author">
          <id property="id" column="author_id"/>
          <result property="username" column="author_username"/>
      </association>
      
      <!--可以直接应用已经定义好的 Select 语句-->
      <resultMap id="blogResult" type="Blog">
        <association property="author" column="author_id" javaType="Author" select="selectAuthor"/>
      </resultMap>
      <select id="selectAuthor" resultType="Author">
        SELECT * FROM AUTHOR WHERE ID = #{id}
      </select>
      
      <!--也可以直接应用已经定义好的 ResultMap (select 换成对应的 resultMap="" 即可)-->
      ```

    + 集合

      ```xml
      <collection property="posts" ofType="domain.blog.Post">
        <id property="id" column="post_id"/>
        <result property="subject" column="post_subject"/>
        <result property="body" column="post_body"/>
      </collection>
      
      <!--同关联-->
      ```

+ typeAlias 类型别名标签

  `<typeAlias type="类的完全限定名", alias="别名">`

+ cache 缓存

  + 默认启用 一级缓存, 使用 `<cache/>`开启二级缓存

  + 属性:

    1. eviction 清除策略:
       1. LRU: 最近最少使用 (默认)
       2. FIFO: 先进先出
       3. SOFT: 软引用 基于垃圾回收器状态和软引用规则移除对象
       4. WEAK: 弱引用 更积极地基于垃圾收集器状态和弱引用规则移除对象
    2. flushInterval 刷新间隔: 毫秒的时间 (默认没有, 仅在语句调用时刷新)
    3. size 引用数目: 需注意缓存对象的大小和可用内存资源 (默认值: 1024)
    4. readOnly 只读: true/false 给所有调用者相同的实例.

  + 自定义缓存

    ```java
    public interface Cache {
      String getId();
      int getSize();
      void putObject(Object key, Object value);
      Object getObject(Object key);
      boolean hasKey(Object key);
      Object removeObject(Object key);
      void clear();
    }
    ```

    应用: 

    ```xml
    <cache type="Cache的完全限定名"/>
    ```

+ Example

```xml
<!--接口类和映射文件的关系:使用namespace指明对应接口类(namespace的值为包的全路径)-->
<mapper namespace="experiment.seven.dao.BookDao">
    <!--id:接口中的方法名(函数名)一致[将子元素中的语句对应接口中的方法]
        parameterType:输入参数类型[可选]
        resultType:返回值的全类名或别名(若为集合,则为集合包含的数据类型)-->
    <select id="getBookByCategoryId" parameterType="String"
            resultType="experiment.seven.vo.Book">
        select * from book where category_id=#{category_id};
    </select>
    <select id="findAll" resultType="experiment.seven.vo.Book">
        select * from book;
    </select>
    <!--插入的表中有自增字段则需要设置useGeneratedKeys="true" 再将主键属性设置为该字段keyProperty="主键"-->
    <insert id="add" useGeneratedKeys="true" keyProperty="id"
            parameterType="experiment.seven.vo.Book">
        insert into Book (name, author, price, image, description, category_id)
        values (#{name}, #{author}, #{price}, #{image}, #{description}, #{category_id});
    </insert>
    <update id="update" parameterType="experiment.seven.vo.Book">
        update book set name=#{name}, author=#{author}, price=#{price}, image=#{image},
        description=#{description}, category_id=#{category_id}
        where id=#{id};
    </update>
    <delete id="delete" parameterType="Integer">
        delete from book where id = #{id};
    </delete>
</mapper>
```

### Mapper 映射接口文件

> 使用myBatis不需要实现接口, 只需要编写相应的 mapper.xml 文件

```java
@Repository // 注入到 Spring IOC 容器中
public interface BookDao {
    List<Book> selectAll(); // 和 Mapper 文件中 mapper 标签内的各个标签对应
    // mapper 文件中的 id 对应接口文件中的 方法名
    // mapper 文件中使用 resultMap 作为返回值, 接口文件可使用 List 等可以获得多个值的对象最为返回值
    List<Book> selectByCategoryId(@Param("id") String id); // 传入参数需要和 mapper 文件中占位符中的设定名一致
    // 或者时传入一个对象, mapper 文件中的占位符设定名跟对象的各属性名一致
}

```

### 使用 (Service 开发)

```java
@Service // 注入到 Spring IOC 容器中
public class BookService {
    @Autowired
    private BookDao bookDao; // 注入 BookDao 数据库操作接口
    public List<Book> selectAll() {
        return bookDao.selectAll();
    }
    public List<Book> selectByCategoryId(String id) {
        return bookDao.selectByCategoryId(id);
    }
}
```

