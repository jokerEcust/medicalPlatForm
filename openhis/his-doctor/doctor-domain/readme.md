**对于业务层逻辑的编写：**
按照接口文档编写，需明确两点：1，前端需要什么样的数据。2，前端发送给后端怎样的数据
对于实体类的构建：建议构建前端需要的数据类型，对于前端发送至后端的数据需要酌情考虑。
通常情况下前端传过来的数据有一些参数，对于存在多个参数的时候往往和前端所需要的数据类型有所重合，
这样的情况下就不需要在额外创建新的数据类型来增加代码的复杂程度了。
但是如果不一致，还是需要编写额外的Dto类。
命名：后->前————xxxDto
     前->后————xxxQueryDto