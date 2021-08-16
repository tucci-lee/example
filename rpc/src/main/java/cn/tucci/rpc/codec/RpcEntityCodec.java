package cn.tucci.rpc.codec;

import cn.tucci.rpc.entity.RpcEntity;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author tucci.lee
 */
public class RpcEntityCodec extends ByteToMessageCodec<RpcEntity> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] arr = new byte[in.readableBytes()];
        in.readBytes(arr);

        ByteArrayInputStream bai = new ByteArrayInputStream(arr);
        ObjectInputStream ois = new ObjectInputStream(bai);
        Object o = ois.readObject();
        out.add(o);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcEntity msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bao);
        oos.writeObject(msg);
        out.writeBytes(bao.toByteArray());
    }
}
