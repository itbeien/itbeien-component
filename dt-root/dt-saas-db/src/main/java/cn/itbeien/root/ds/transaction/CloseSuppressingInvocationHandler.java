package cn.itbeien.root.ds.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class CloseSuppressingInvocationHandler implements InvocationHandler {
    private final ConnectionHolder holder;

// 构造函数，传入一个ConnectionHolder对象
    CloseSuppressingInvocationHandler(ConnectionHolder holder) {
        // 将传入的ConnectionHolder对象赋值给成员变量
        this.holder = holder;
        // 调用ConnectionHolder对象的requested方法，ref++，表示请求次数加1
        this.holder.requested();//ref++
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        // 获取连接
        Connection connection = holder.getConnection();

        // 判断方法名
        if (method.getName().equals("getTargetConnection")) {
            // 返回连接
            return connection;
        } else if (method.getName().equals("getTargetSource")) {
            // 返回数据源
            return this.holder.getDataSource();
        } else if (method.getName().equals("toString")) {
            // 返回字符串
            return this.holder.toString();
        } else if (method.getName().equals("equals")) {
            // 判断代理对象是否等于参数对象
            return proxy == args[0];
        } else if (method.getName().equals("hashCode")) {
            // 返回代理对象的哈希码
            return System.identityHashCode(proxy);
        } else if (method.getName().equals("close")) {
            // 关闭连接
            if (holder.isOpen()) {
                holder.released();//ref--
            }
            return null;
        }

        try {
            // 调用连接的方法
            if(args == null){
                return method.invoke(connection);
            }else{
                return method.invoke(connection,args);
            }

        } catch (InvocationTargetException ex) {
            // 抛出目标异常
            throw ex.getTargetException();
        }
    }
}
