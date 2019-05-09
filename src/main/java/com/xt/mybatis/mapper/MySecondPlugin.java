package com.xt.mybatis.mapper;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * 完成插件签名：
 *		告诉MyBatis当前插件用来拦截哪个对象的哪个方法
 */
@Intercepts(
        {
                @Signature(type = StatementHandler.class, method = "parameterize", args = java.sql.Statement.class)
        }
)
public class MySecondPlugin implements Interceptor {
    /**
     * intercept：拦截：拦截目标对象的目标方法的执行；
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MySecondPlugin...intercept:" + invocation.getMethod());

        //执行目标方法
        Object proceed = invocation.proceed();

        //返回执行后的返回值
        return proceed;
    }

    /**
     * plugin：包装目标对象的：包装：为目标对象创建一个代理对象
     */
    @Override
    public Object plugin(Object target) {
        //我们可以借助Plugin的wrap方法来使用当前Interceptor包装我们目标对象
        System.out.println("MySecondPlugin...plugin:mybatis将要包装的对象" + target);

        Object wrap = Plugin.wrap(target, this);

        //返回为当前target创建的动态代理
        return wrap;
    }

    /**
     * setProperties：将插件注册时 的property属性设置进来
     */
    @Override
    public void setProperties(Properties properties) {
        System.out.println("插件配置的信息：" + properties);
    }
}
