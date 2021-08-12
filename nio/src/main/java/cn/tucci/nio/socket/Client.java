package cn.tucci.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author tucci.lee
 */
public class Client {

    public static void main(String[] args) throws IOException {
        SocketChannel client = SocketChannel.open(new InetSocketAddress(8080));
        new Thread(() -> {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (true) {
                try {
                    int read = client.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        System.out.println(read);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String next = scanner.next();
            client.write(StandardCharsets.UTF_8.encode(next));
        }
    }

}
