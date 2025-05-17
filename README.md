<div align="center">

# SimpleProto

</div>
<div align="center">
A Simple Framework for Java Business Project
</div>

### Description
本项目提供了一系列基础对象用于快速实现Java业务对象的存储管理，你只需要在继承这些基础对象，并定义号属性编号和属性名称，即可自动在数据库中存储下这些数据，并且提供了id构造方法以方便的获取已保存的对象。

> *This project provides a series of basic objects for quickly implementing the storage and management of Java business
objects. You only need to inherit these basic objects and define the attribute number and attribute name to
automatically store these data in the database, and provide an id constructor to easily obtain the saved objects.*


### Required Docker Deployments
- PostgresSQL
- OpenSearch
- Redis
- MinIO

### Maven Dependency





### API Use

1. 继承基础对象`cn.wanfeng.sp.api.domain.SpBaseObject`，使用@Type声明该对象的业务类型。
2. 在继承类中任意增加一些需要的业务字段，使用`@ProtoField`注解修饰。如果业务字段是一个枚举类，需要在枚举类中使用`@ProtoEnumValue`注解标记声明该字段保存时的值，和`@ProtoEnumConstructor`注解标记获取对象时该字段的值对应哪个枚举类对象）。
3. 在继承类的构造器中调用父类的构造器：
    - `public SpBaseObject(SpSession session, String type)`用于新建对象，指定该对象的类型
    - `public SpBaseObject(SpSession session, Long id)`用于从存储中根据id获取对象
4. 调用`store()`方法，即可把这些数据一并存储在数据库以及OpenSearch中（包括数据库的字段、OpenSearch的Mapping）
5. 调用父类的id构造器`public SpBaseObject(SpSession session, Long id)`即可从数据存储中获取所有属性值

> 1. *Inherit the basic object `cn.wanfeng.sp.api.domain.SpBaseObject`. Use `@Type` to declare the business type of the
     object.*
> 2. *Arbitrarily add some required business fields to the inherited class (decorated with the `@ProtoField`
     annotation). If the business field is an enumeration class, you need to use the `@ProtoEnumValue` annotation tag in
     the enumeration class to declare the value of the field when it is saved, and the `@ProtoEnumConstructor`
     annotation tag to obtain which enumeration class object the value of the field corresponds to when the object is
     obtained.*
> 3. *Call the parent class constructor in the derived class
     constructor`public SpBaseObject(SpSession session, String type)` is used to create a new object and specify the
     type of the
     object`public SpBaseObject(SpSession session, Long id)` is used to retrieve an object from storage based on id*
> 4. *Calling the `store()` method will store these data in the database and OpenSearch (including database fields and
     OpenSearch Mapping)*
> 5. *Call the parent class's id constructor `public SpBaseObject(SpSession session, Long id)` to retrieve all property
     values from the data storage*

**Example Code Using `cn.wanfeng.sp.api.domain.SpBaseObject`**

```java
@Type("borrow_form")
@Getter
@Setter
public class BorrowForm extends SpBaseObject {

    /**
     * ProtoField declare index and name of this property
     * [index] is used to build serialized data and store in database,
     * [index] is unique in a Class
     * [name] is used to build document and store in opensearch, 
     * [name] is unique in a Class
     */
    @ProtoField(index = 1, name = "form_no")
    private String formNo;

    @ProtoField(index = 2, name = "apply_days")
    private Integer applyDays;

    @ProtoField(index = 3, name = "expire_date")
    private Date expireDate;
    
    /**
     * Enum Type Field
     */
    @ProtoField(index = 4, name = "borrow_status")
    private BorrowStatus borrowStatus;

    /**
     * Constructor for Create Object
     */
    public BorrowForm(SpSession session, String formNo, Integer applyDays, Date expireDate) {
        super(session, "borrow_form");
        this.formNo = formNo;
        this.applyDays = applyDays;
        this.expireDate = expireDate;
    }
    
    /**
     * Constructor for Get Object
     */
    public BorrowForm(SpSession session, Long id) {
        super(session, id);
    }
}
```

```java
public enum BorrowStatus {
    DRAFT(0, "草稿"),
    EXAMINE(1, "审批中"),
    COMPLETE(2, "已完成");

    private Integer value;
    
    private String desc;

    BorrowStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
    
    @ProtoEnumValue
    public Integer getValue(){
        return this.value;
    }
    
    @ProtoEnumConstructor
    public static BorrowStatus toEnum(Integer value){
        for (BorrowStatus status : values()) {
            if(status.value.equals(value)){
                return status;
            }
        }
        return null;
    }
}
```

### MybatisPlus Use

如果你想要批量查询你保存的对象，可以使用Mybatis对OpenSearch进行查询，Mapper接口应在mapper.search包下，XML文件应在mapper/search路径下。用于接收数据的实体类使用默认的Mybatis注解即可。

在XML的SQL语句中，可以使用占位符`{data_table}`替代表名，该占位符会在查询时自动替换为配置文件中的数据表名`simpleproto.dataTable`的值。

> *To query your saved objects in batches, you can use Mybatis to query OpenSearch. The Mapper interface should be
in `mapper.search` package, the XML file should be under the `mapper/search` path. The entity class used to receive data
can use the default Mybatis annotation.*
>
> *In XML SQL statements, you can use the placeholder `{data_table}` to represent a table name, which will be
automatically replaced with the value of `simpleproto.dataTable` from the configuration file during the query.*

**Example Code**

```java
// cn.wanfeng.sp.base.mapper.search.BorrowFormMapper
@Mapper
public interface BorrowFormMapper extends BaseMapper<BorrowFormDO> {
    
    List<BorrowFormDO> findById(@Param("id") Long id);

    List<BorrowFormDO> findAll();
}
```

```xml
<!-- src/test/resources/mapper/search/BorrowFormMapper.xml -->
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="cn.wanfeng.sp.base.mapper.search.BorrowFormMapper">
    <sql id="all_field">
        id, name, form_no, create_date
    </sql>

    <select id="findById" resultType="cn.wanfeng.sp.base.BorrowFormDO">
        select
        <include refid="all_field"/>
        from {data_table}
        where id = #{id}
    </select>

    <select id="findAll" resultType="cn.wanfeng.sp.base.BorrowFormDO">
        select
        <include refid="all_field"/>
        from {data_table}
        where type = 'borrow_form'
    </select>
</mapper>
```

### Bulk Operation

版本1.1提供了批量操作数据的方式，你可以通过SpBulkOperator这个Bean来批量保存或批量删除你想要操作的多个数据

示例代码位置：`cn.wanfeng.sp.web.controller.TestController#testBulkStore`

> *Version 1.1 provides a way to batch manipulate data, and you can use the SpBulkOperator bean to save or delete
multiple data you want to manipulate in bulk*
> *Example code location: `cn.wanfeng.sp.web.controller.TestController#testBulkStore`*

```java
Long userNumber = session.increaseLongByName(SimpleProtoConfig.appName + ":" + USER_NUMBER_INCREASE_NAME);
String numberStr = NumberUtils.generateNumberFixLength(userNumber, 4);
SpUser spUser1 = new SpUser(session, "user" + numberStr);
SpUser spUser2 = new SpUser(session, "user" + numberStr + "-2");
SpUser spUser3 = new SpUser(session, "user" + numberStr + "-3");
spUser3.setName("第一次批量更新*****");

List<SpUser> objectList = new ArrayList<>();
objectList.add(spUser1);
objectList.add(spUser2);
objectList.add(spUser3);
bulkOperator.bulkStore(objectList);
```

### Examples Using the Project





