package cn.tucci.nio.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author tucci.lee
 */
public class ImServer {

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

    static class SocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {
        // 存储连接的SocketChannel
        private Map<Integer, NioSocketChannel> socketMap = new ConcurrentHashMap<>();

        @Override
        protected void initChannel(NioSocketChannel ch) throws Exception {
            // 协议
            ch.pipeline().addLast(new ProtocolLengthFieldBasedFrameDecoder());
            ch.pipeline().addLast(new MessageCodec());
            // 自定义handler
            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                // 读事件处理
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    System.out.println(msg);
                    Message readMsg = (Message) msg;
                    byte type = readMsg.getType();
                    if (type == 1) { // 连接消息
                        socketMap.put(readMsg.getFrom(), ch);
                    } else if (type == 2) { // 单聊消息
                        NioSocketChannel toSocket = socketMap.get(readMsg.getTo());
                        if (toSocket == null) {
                            return;
                        }
                        toSocket.writeAndFlush(readMsg);
                    }
                    // 组消息类似
                }

                /**
                 * 通道关闭从map中删除
                 * @param ctx
                 * @throws Exception
                 */
                @Override
                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                    Channel channel = ctx.channel();
                    for (Map.Entry<Integer, NioSocketChannel> entry : socketMap.entrySet()) {
                        if (entry.getValue().equals(channel)) {
                            socketMap.remove(entry.getKey());
                            break;
                        }
                    }
                    super.channelInactive(ctx);
                }

            });
        }
    }

}
