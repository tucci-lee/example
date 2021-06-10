package cn.tucci.aop.test;

import cn.tucci.aop.Point;

/**
 * @author tucci.lee
 */
public class Aspect {

    public Object around1(Point point) throws Exception {
        System.out.println("aroun1 begin");
        Object invoke = point.invoke();
        System.out.println("around1 end");
        return invoke;
    }

    public Object around2(Point point) throws Exception {
        System.out.println("aroun2 begin");
        Object invoke = point.invoke();
        System.out.println("around2 end");
        return invoke;
    }
}
