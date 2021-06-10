package cn.tucci.aop;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author tucci.lee
 */
public class Point {

    private List<AspectInfo> aspectInfos;   // aop增强信息
    private Object target;  // 目标元对象
    private Method method; // 目标元方法
    private Object[] args; // 方法参数
    private int index = 0; // aop方法执行的下标

    public Point(Object target, Method method, Object[] args, List<AspectInfo> aspectInfos){
        this.target = target;
        this.method = method;
        this.args = args;
        this.aspectInfos = aspectInfos;
    }

    public Object invoke() throws Exception {
        // 如果当前下标和aop增强信息相等，则说明没有增强方法了，执行代理的方法
        if(this.index == aspectInfos.size()){
            return method.invoke(target, args);
        }
        // 获取index下标的增强方法
        AspectInfo aspectInfo = this.aspectInfos.get(index);
        // index下标增加
        this.index++;
        // 获取增强的类和方法
        Method aspectMethod = aspectInfo.getAspectMethod();
        Class<?> aspectClass = aspectInfo.getAspectClass();
        // 由于没有spring环境管理bean，使用反射创建一个aop的类
        Object aspectObj = aspectClass.newInstance();
        // 执行增强方法
        return aspectMethod.invoke(aspectObj, this);
    }

}
