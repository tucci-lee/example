package cn.tucci.nio.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * @author tucci.lee
 */
public class WebSocketServer {

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup work = new NioEventLoopGroup();
        try {
            Channel channel = new ServerBootstrap()
                    .group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketChannelInitializer())
                    .bind(8080)
                    .sync()
                    .channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}

class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    private Set<Channel> channels = new HashSet<>();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        // http 解码编码
        p.addLast(new HttpServerCodec());
        // 以块的方式来写的处理器
        p.addLast(new ChunkedWriteHandler());
        // http消息聚合器
        p.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        // websocket，请求 ip:port/ws
        p.addLast(new WebSocketServerProtocolHandler("/ws"));
        p.addLast(new SimpleChannelInboundHandler<TextWebSocketFrame>() {
                      @Override
                      protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
                          String text = msg.text();
                          System.out.println(text);
                          // 发送给所有人
                          channels.forEach(ch -> {
                              if (!ch.equals(ctx.channel())) {
                                  ch.writeAndFlush(new TextWebSocketFrame(text));
                              }
                          });
                      }

                      // 连接后添加到channelGroup
                      @Override
                      public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
                          System.out.println("用户登陆：" + ctx.channel());
                          channels.add(ctx.channel());
                      }

                      @Override
                      public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
                          System.out.println("用户退出：" + ctx.channel());
                          channels.remove(ctx.channel());
                      }
                  }
        );
        // 心跳
        p.addLast(new IdleStateHandler(4, 8, 12));
    }
}