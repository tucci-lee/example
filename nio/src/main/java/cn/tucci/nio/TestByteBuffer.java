package cn.tucci.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;

/**
 * @author tucci.lee
 */
public class TestByteBuffer {

    public static void main(String[] args) {
//        read("/Users/dean.lee/Downloads/a.txt");
//        write("/Users/dean.lee/Downloads/b.txt", "123easa");
        read2write("/Users/dean.lee/Downloads/a.txt", "/Users/dean.lee/Downloads/b.txt");
    }

    public static void read(String file) {
        try (FileChannel channel = new FileInputStream(file).getChannel()) {
            // 中文乱码
            CharsetDecoder charsetDecoder = StandardCharsets.UTF_8.newDecoder();

            // 缓存区大小
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int i;
            while ((i = channel.read(buffer)) != -1) {
                charsetDecoder.decode(buffer);

                byte[] array = buffer.array();
                System.out.print(new String(array, 0, i));

                // 切换读模式，翻转ByteBuffer
                buffer.flip();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read2write(String file, String newFile) {
        try (FileChannel readChannel = new FileInputStream(file).getChannel();
             FileChannel writeChannel = new FileOutputStream(newFile).getChannel()) {

            // 缓存区大小
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (readChannel.read(buffer) != -1) {
                byte[] array = buffer.array();
                // 切换写模式
                buffer.clear();
                writeChannel.write(buffer);
                // 切换读模式，翻转ByteBuffer
                buffer.flip();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(String file, String text) {
        try (FileChannel channel = new FileOutputStream(file).getChannel()) {
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            ByteBuffer wrap = ByteBuffer.wrap(bytes);
            channel.write(wrap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
