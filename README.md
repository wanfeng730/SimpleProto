<div align="center">

# SimpleProto

</div>
<div align="center">
A Simple Framework for Java Business Project
</div>

### Description
本项目提供了一系列基础对象用于快速实现Java业务对象的存储管理，你只需要在继承这些基础对象，并定义号属性编号和属性名称，即可自动在数据库中存储下这些数据，并且提供了id构造方法以方便的获取已保存的对象。

> This project provides a series of basic objects for quickly implementing the storage and management of Java business objects. You only need to inherit these basic objects and define the attribute number and attribute name to automatically store these data in the database, and provide an id constructor to easily obtain the saved objects.


### Required Docker Deployments
- PostgresSQL
- OpenSearch
- Redis
- MinIO

### Maven Dependency



### API Use

1. 继承基础对象cn.wanfeng.sp.api.domain.SpBaseObject，然后在继承类中任意增加一些需要的业务字段（使用@ProtoField注解修饰）。
2. 在继承类的构造器中调用父类的构造器：
    - public SpBaseObject(SpSession session, String type)用于新建对象，指定该对象的类型
    - public SpBaseObject(SpSession session, Long id)用于从存储中根据id获取对象

3. 调用store()方法，即可把这些数据一并存储在数据库以及OpenSearch中（包括数据库的字段、OpenSearch的Mapping）
4. 调用父类的id构造器public SpBaseObject(SpSession session, Long id)即可从数据存储中获取所有属性值

> 1. Inherit the basic object `cn.wanfeng.sp.api.domain.SpBaseObject`, and then arbitrarily add some required business fields to the inherited class (decorated with the @ProtoField annotation).

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

### Examples Using the Project





