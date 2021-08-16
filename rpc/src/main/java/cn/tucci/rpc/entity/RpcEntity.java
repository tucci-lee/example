package cn.tucci.rpc.entity;

import java.io.Serializable;

/**
 * @author tucci.lee
 */
public class RpcEntity implements Serializable {

    private String id;

    private Class<?> className;

    private String methodName;

    private Class<?>[] methodTyps;

    private Object[] methodParams;

    private Object result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Class<?> getClassName() {
        return className;
    }

    public void setClassName(Class<?> className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getMethodTyps() {
        return methodTyps;
    }

    public void setMethodTyps(Class<?>[] methodTyps) {
        this.methodTyps = methodTyps;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public void setMethodParams(Object[] methodParams) {
        this.methodParams = methodParams;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
