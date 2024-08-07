# sequence

> 序列生成器
>
> 本模块参考了项目[sections/sequence](https://gitee.com/sections/sequence)和项目[power4j/sequence](https://gitee.com/power4j/sequence)，并且大多核心代码来自[sections/sequence](https://gitee.com/sections/sequence) 非常感谢

## 核心逻辑

基于**数据库存储**序列信息，从内存或者Redis缓存中**获取序列号**。

### 核心组件

- DataAccessor
  - 操作数据库层的入口
  - 初始化表结构
  - 查找序列信息
  - 更新序列信息
- CacheProvider
  - 获取序列号的入口
  - 提供内存和Redis两种模式生成序列号
- SequenceGenerator
  - 获取序列号的API层
- SequenceManager
  - 获取序列号的API实现
  - 判断当前key是否已被加载到程序中，没有的话会去数据库中读取加载，完成当前key的初始化
  - 进行获取序列号
  - 根据序列的配置信息，会从不同的提供者中获取
- SequenceDefinition
  - 一个序列信息的定义
  - name业务名称
  - 业务key
  - initialValue初始值默认为1
  - setp步长默认值为1，不允许设置为0，设置为0表示不变化没有意义，为正表示递增，为负表示递减
  - cacheSize缓存大小默认值为100
  - cacheMode缓存模式：MEMORY内存模式和REDIS模式，在没有Redis组件的支持下可以使用内存实现。当在分布式环境下时使用内存模式需要注意作为单独服务提供

## 使用步骤

### 开箱即用

默认情况下添加依赖即可在内存模式下使用。

### Redis模式

当上下文有Redis依赖时，可以支持Redis缓存模式。

在Redis模式下，默认提供了一个名称为`seqCacheRedisTemplate`的RedisTemplate bean并做了序列化配置，如果不满足需求可以重新自定义。

### 属性配置

- 自定义表名，默认表名`seq_sequence`
- 自定义Redis缓存时间，默认时间7天



