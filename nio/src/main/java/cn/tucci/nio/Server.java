package cn.tucci.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author tucci.lee
 */
public class Server {

    private ByteBuffer buffer;

    public static void main(String[] args) throws IOException {
        // selector
        Selector selector = Selector.open();

        // channel
        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(8080)); // 绑定端口
        server.configureBlocking(false); // 非阻塞模式
        SelectionKey serverSelectionKey = server.register(selector, SelectionKey.OP_ACCEPT);// 注册selector并设置关注accept事件

        while (true) {
            selector.select(); // 线程阻塞，有事件才会继续运行。如果事件未处理会一直运行
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();// 包含了所有发生的事件
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();  // 删除迭代器中的事件
                if (key.isAcceptable()) { // 连接事件
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socket = channel.accept();
                    socket.configureBlocking(false);
                    SelectionKey socketSelectionKey = socket.register(selector, 0);
                    socketSelectionKey.interestOps(SelectionKey.OP_READ);
                    System.out.println(socket);

                    StringBuilder sb = new StringBuilder();
                    for (int i=0;i<3000000;i++){
                        sb.append("a");
                    }
                    ByteBuffer wirteBuffer = Charset.defaultCharset().encode(sb.toString());
                    int write = socket.write(wirteBuffer);
                    System.out.println("init w:" + write);
                    if(wirteBuffer.hasRemaining()){
                        socketSelectionKey.attach(wirteBuffer);
                        socketSelectionKey.interestOps(socketSelectionKey.interestOps() + SelectionKey.OP_WRITE);
                    }
                } else if (key.isReadable()) { // 读取事件
                    try {
                        SocketChannel socket = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);

                        int read = socket.read(buffer);
                        if (read == -1) {
                            key.cancel();
                            break;
                        }
                        System.out.println(new String(buffer.array(), StandardCharsets.UTF_8));
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                } else if (key.isWritable()) { // 写事件
                    try {
                        SocketChannel socket = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int write = socket.write(buffer);
                        System.out.println("w:" + write);
                        if(!buffer.hasRemaining()){ // 如果内容写完了，清除buffer
                            key.attach(null);
                            key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }
}
