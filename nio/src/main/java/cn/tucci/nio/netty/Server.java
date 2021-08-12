package cn.tucci.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 半包解决
 * FixedLengthFrameDecoder         固定长度解决半包
 * LineBasedFrameDecoder           换行符解决半包
 * LengthFieldBasedFrameDecoder    类似于http协议
 *
 * @author tucci.lee
 */
public class Server {

    public static void main(String[] args) {
        new ServerBootstrap()
                // 事件循环组（主要维护Thread和Selector）
                .group(new NioEventLoopGroup())
                // 选择服务器的Channel实现
                .channel(NioServerSocketChannel.class)
                // 处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() { // Channel初始化处理器
                    // 连接成功后初始化
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 添加解码器
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
                        // 自定义handler
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            // 读事件处理
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
                    }
                })
                // 绑定端口
                .bind(8080);
    }
}
