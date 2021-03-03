# spring 循环依赖解决原理

如图所示：

![1671614785069_ pic](https://user-images.githubusercontent.com/69717405/109829358-66f23080-7c78-11eb-800e-8af79f89aa9a.jpg)

说明：

* 构造方法循环依赖无法解决，直接报错；
* 上图中的执行过程：
  *  实例化lagouBean，将lagouBeanFactory放到三级缓存SingletonFactories（单例工厂对象）；
  *  为lagouBean设置属性，实例化itBean，将itBeanFactory放到三级缓存SingletonFactories；
  *  为itBean设置属性，从SingletonFactories中取出lagouBeanFactory，返回lagouBean对象，此时销毁lagouBeanFactory，并将lagouBean放入二级缓存earlySingletonObjects中；
  *  itBean初始化完毕，将itBean放入一级缓存SingletonObjects中，同时销毁三级缓存中的itBeanFactory；
  *  将itBean设置进lagouBean中，lagouBean也初始化完毕，将lagouBean从二级缓存移到一级缓存。


设置三级缓存涉及单一职责原则，每个缓存容器存放某一类对象。

  
