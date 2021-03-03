# Spring Bean生命周期管理

## 如图所示
![1661614782768_ pic](https://user-images.githubusercontent.com/69717405/109822815-48893680-7c72-11eb-8b44-b9efc37c3f32.jpg)


## 概括
Bean 的生命周期概括起来就是 4 个阶段：

* 实例化（Instantiation）
* 属性赋值（Populate）
* 初始化（Initialization）
* 销毁（Destruction）

<img width="631" alt="WeChat65058645595e17bfbf09e15ee21dc850" src="https://user-images.githubusercontent.com/69717405/109824282-a4a08a80-7c73-11eb-9f96-84e81c1ac3f8.png">

下面我们结合代码来直观的看下，在 doCreateBean() 方法中能看到依次执行了这 4 个阶段：

    // AbstractAutowireCapableBeanFactory.java
    protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
        throws BeanCreationException {

        // 1. 实例化
        BeanWrapper instanceWrapper = null;
        if (instanceWrapper == null) {
            instanceWrapper = createBeanInstance(beanName, mbd, args);
        }

        Object exposedObject = bean;
        try {
            // 2. 属性赋值
            populateBean(beanName, mbd, instanceWrapper);
            // 3. 初始化
            exposedObject = initializeBean(beanName, exposedObject, mbd);
        }

        // 4. 销毁-注册回调接口
        try {
            registerDisposableBeanIfNecessary(beanName, bean, mbd);
        }

        return exposedObject;
    }

由于初始化包含了第 3~7步，较复杂，所以我们进到 initializeBean() 方法里具体看下其过程（注释的序号对应图中序号）：

    // AbstractAutowireCapableBeanFactory.java
    protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
        // 3. 检查 Aware 相关接口并设置相关依赖
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                invokeAwareMethods(beanName, bean);
                return null;
            }, getAccessControlContext());
        }
        else {
            invokeAwareMethods(beanName, bean);
        }

        // 4. BeanPostProcessor 前置处理
        Object wrappedBean = bean;
        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        }

        // 5. 若实现 InitializingBean 接口，调用 afterPropertiesSet() 方法
        // 6. 若配置自定义的 init-method方法，则执行
        try {
            invokeInitMethods(beanName, wrappedBean, mbd);
        }
        catch (Throwable ex) {
            throw new BeanCreationException(
                (mbd != null ? mbd.getResourceDescription() : null),
                beanName, "Invocation of init method failed", ex);
        }
        // 7. BeanPostProceesor 后置处理
        if (mbd == null || !mbd.isSynthetic()) {
            wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        }

        return wrappedBean;
    }

在 invokInitMethods() 方法中会检查 InitializingBean 接口和 init-method 方法，销毁的过程也与其类似：

    // DisposableBeanAdapter.java
    public void destroy() {
        // 9. 若实现 DisposableBean 接口，则执行 destory()方法
        if (this.invokeDisposableBean) {
            try {
                if (System.getSecurityManager() != null) {
                    AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
                        ((DisposableBean) this.bean).destroy();
                        return null;
                    }, this.acc);
                }
                else {
                    ((DisposableBean) this.bean).destroy();
                }
            }
        }

      // 10. 若配置自定义的 detory-method 方法，则执行
        if (this.destroyMethod != null) {
            invokeCustomDestroyMethod(this.destroyMethod);
        }
        else if (this.destroyMethodName != null) {
            Method methodToInvoke = determineDestroyMethod(this.destroyMethodName);
            if (methodToInvoke != null) {
                invokeCustomDestroyMethod(ClassUtils.getInterfaceMethodIfPossible(methodToInvoke));
            }
        }
    }


3. 扩展点的作用

参考：[如何记忆 Spring Bean 的生命周期](https://juejin.cn/post/6844904065457979405)
