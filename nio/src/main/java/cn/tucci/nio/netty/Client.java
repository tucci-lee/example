package cn.tucci.nio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author tucci.lee
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    // 连接成功后初始化
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect(new InetSocketAddress(8080)) // 连接服务器
                .sync() // 阻塞方法，直到连接成功
                .channel(); // 连接对象

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        byte[] a = "111".getBytes();
        buf.writeInt(1);
        buf.writeInt(3);
        buf.writeBytes(a);
        channel.writeAndFlush(buf);
        System.out.println(buf);
        channel.close();
    }
}
