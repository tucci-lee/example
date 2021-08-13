package cn.tucci.nio.socket;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author tucci.lee
 */
public class ProtocolLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 字节
     * 1    type    消息类型
     * 4    from    消息发送方
     * 4    to      消息接收方
     * 4    length  消息内容长度
     * xx   content 消息内容
     *
     * 最大解码长度1024个字节
     * length从第9个字节开始，长度4个字节
     */
    public ProtocolLengthFieldBasedFrameDecoder(){
        this(1024, 9, 4, 0, 0);
    }

    public ProtocolLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
