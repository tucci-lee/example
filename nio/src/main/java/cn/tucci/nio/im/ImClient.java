package cn.tucci.nio.im;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * @author tucci.lee
 */
public class ImClient {
    // 启动多个client，id不同即可
    static final int id = 1;

    public static void main(String[] args) throws InterruptedException {
        Channel channel = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ProtocolLengthFieldBasedFrameDecoder());
                        ch.pipeline().addLast(new MessageCodec());
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 获取服务端发送的数据
                                Message message = (Message) msg;
                                System.out.println(message.getFrom() + ":" + message.getContent());
                            }
                        });
                    }
                })
                .connect(new InetSocketAddress(8080))
                .sync()
                .channel();

        // 发送连接请求
        Message conn = new Message();
        conn.setType((byte) 1);
        conn.setFrom(id);
        conn.setTo(id);
        conn.setContent("conn");
        channel.writeAndFlush(conn);

        while (true) {
            try {
                // 消息发送以to:content格式
                // 如发送给1用户hello --> 1:hello
                Scanner scanner = new Scanner(System.in);
                String next = scanner.next();
                String[] s = next.split(":");
                int to = Integer.parseInt(s[0]);

                // 发送单对单消息
                Message message = new Message();
                message.setType((byte) 2);
                message.setFrom(id);
                message.setTo(to);
                message.setContent(s[1]);
                channel.writeAndFlush(message);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
