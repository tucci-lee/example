package cn.tucci.nio.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author tucci.lee
 */
public class HttpServer {

    public static void main(String[] args) {
        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // http 解码编码
                        pipeline.addLast(new HttpServerCodec());
                        // 解码成FullHttpRequest
                        pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
                        pipeline.addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                System.out.println(request.uri());
                                FullHttpResponse response = new DefaultFullHttpResponse(
                                        request.protocolVersion(),
                                        HttpResponseStatus.OK);
                                byte[] msg = "hello".getBytes();
                                response.content().writeBytes(msg);
                                HttpHeaders headers = response.headers();
                                headers.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
                                headers.set(HttpHeaderNames.CONTENT_LENGTH, msg.length);
                                headers.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                                ctx.writeAndFlush(response);
                            }
                        });
                    }
                })
                .bind(8080);
    }
}
