package cn.tucci.aop;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author tucci.lee
 */
public class ProxyMethodInterceptor implements MethodInterceptor {

    private Object target; // 目标对象

    private Map<Method, List<AspectInfo>> aspectInfoMap; // 代理方法的增强信息

    public ProxyMethodInterceptor(Object target, Map<Method, List<AspectInfo>> aspectInfoMap) {
        this.target = target;
        this.aspectInfoMap = aspectInfoMap;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 如果当前调用的方法被增强，创建point，执行point的invoke方法
        if (aspectInfoMap.get(method) != null) {
            Point point = new Point(o, methodProxy, objects, aspectInfoMap.get(method));
            return point.invoke();
        }
        // 如果没有增强，则使用目标对象执行方法
        return method.invoke(target, objects);
    }
}
