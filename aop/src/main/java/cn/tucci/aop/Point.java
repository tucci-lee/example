package cn.tucci.aop;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author tucci.lee
 */
public class Point {

    private List<AspectInfo> aspectInfos;   // 增强方法信息
    private Object target;  // 目标对象
    private MethodProxy method; // 目标方法
    private Object[] args; // 目标方法参数
    private int index = 0; // 增强方法执行的下标

    public Point(Object target, MethodProxy method, Object[] args, List<AspectInfo> aspectInfos){
        this.target = target;
        this.method = method;
        this.args = args;
        this.aspectInfos = aspectInfos;
    }

    public Object invoke() throws Throwable {
        // 如果当前下标和增强方法信息数量相等，则说明没有增强方法了，执行目标方法
        if(this.index == aspectInfos.size()){
            return method.invokeSuper(target, args);
        }
        // 获取index下标的增强方法信息
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
