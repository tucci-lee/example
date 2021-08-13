package cn.tucci.nio.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 对ByteBuf解码编码
 *
 * @author tucci.lee
 */
public class MessageCodec extends ByteToMessageCodec<Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        byte[] bytes = msg.getContent().getBytes();
        out.writeByte(msg.getType());
        out.writeInt(msg.getFrom());
        out.writeInt(msg.getTo());
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte type = in.readByte();
        int from = in.readInt();
        int to = in.readInt();
        int length = in.readInt();
        ByteBuf buf = in.readBytes(length);

        Message msg = new Message();
        msg.setType(type);
        msg.setFrom(from);
        msg.setTo(to);
        msg.setContent(buf.toString(StandardCharsets.UTF_8));

        out.add(msg);
    }
}
