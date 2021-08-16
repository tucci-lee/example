package cn.tucci.rpc.service.impl;

import cn.tucci.rpc.service.HelloService;

/**
 * @author tucci.lee
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello() {
        return "hello world";
    }
}
