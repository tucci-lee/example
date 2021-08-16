package cn.tucci.rpc;

import cn.tucci.rpc.codec.RpcEntityCodec;
import cn.tucci.rpc.entity.RpcEntity;
import cn.tucci.rpc.service.impl.HelloServiceImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Method;

/**
 * @author tucci.lee
 */
public class Server {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            Channel channel = new ServerBootstrap()
                    // 事件循环组（主要维护Thread和Selector）
                    .group(boss, work)
                    // 选择服务器的Channel实现
                    .channel(NioServerSocketChannel.class)
                    // 处理器
                    .childHandler(new SocketChannelInitializer())
                    // 绑定端口
                    .bind(8080)
                    .sync()
                    .channel();
            // 阻塞等待服务关闭
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}

class SocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        // 解码
        ch.pipeline().addLast(new RpcEntityCodec());
        // 自定义handler
        ch.pipeline().addLast(new SimpleChannelInboundHandler<RpcEntity>() {
            // 读事件处理
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, RpcEntity msg) throws Exception {
                try {
                    Class<?> className = msg.getClassName();
                    String methodName = msg.getMethodName();
                    Method method = HelloServiceImpl.class.getMethod(methodName, msg.getMethodTyps());
                    Object invoke = method.invoke(new HelloServiceImpl(), msg.getMethodParams());

                    msg.setResult(invoke);
                    ctx.channel().writeAndFlush(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}