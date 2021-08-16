package cn.tucci.rpc;

import cn.tucci.rpc.codec.RpcEntityCodec;
import cn.tucci.rpc.entity.RpcEntity;
import cn.tucci.rpc.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tucci.lee
 */
public class Client {

    static final Map<String, Promise<RpcEntity>> PROMISE_MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        HelloService service = (HelloService) Proxy.newProxyInstance(Client.class.getClassLoader(), new Class[]{HelloService.class}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcEntity entity = new RpcEntity();
                entity.setId(UUID.randomUUID().toString());
                entity.setClassName(HelloService.class);
                entity.setMethodName(method.getName());
                entity.setMethodTyps(method.getParameterTypes());
                entity.setMethodParams(args);
                RpcEntity result = invoke0(entity);
                return result.getResult();
            }
        });
        String hello = service.hello();
        System.out.println(hello);
    }

    static RpcEntity invoke0(RpcEntity entity) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new RpcEntityCodec());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<RpcEntity>() {
                            @Override
                            public void channelRead0(ChannelHandlerContext ctx, RpcEntity msg) throws Exception {
                                // 结果
                                Promise<RpcEntity> promise = PROMISE_MAP.get(msg.getId());
                                if (promise != null) {
                                    promise.setSuccess(msg);
                                }
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress(8080))
                .sync()
                .channel();
        channel.writeAndFlush(entity)
                // 发送失败监听
                .addListener(promise -> {
                    if (!promise.isSuccess()) {
                        promise.cause().printStackTrace();
                    }
                });
        // 等待返回结果
        Promise<RpcEntity> promise = new DefaultPromise<>(channel.eventLoop());
        PROMISE_MAP.put(entity.getId(), promise);
        promise.await();
        // 关闭连接
        group.shutdownGracefully();

        // 返回结果
        if (promise.isSuccess()) {
            return promise.getNow();
        } else {
            return new RpcEntity();
        }
    }
}
