package cn.tucci.aop.test;

/**
 * @author tucci.lee
 */
public class Bean {

    public void a() {
        System.out.println("a()");
    }

    public void a(String s) {
        System.out.printf("a(%s)%n", s);
    }

    public Object b() {
        System.out.println("b()");
        return "b()";
    }

    public Object b(String s) {
        String format = String.format("b(%s)", s);
        System.out.println(format);
        return format;
    }
}
