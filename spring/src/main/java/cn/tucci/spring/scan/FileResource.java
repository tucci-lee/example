package cn.tucci.spring.scan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;

/**
 * @author tucci.lee
 */
public class FileResource implements Resource{

    private File file;

    public FileResource(File file) {
        this.file = file;
    }

    @Override
    public URL getUrl() throws IOException{
        return file.toURI().toURL();
    }

    @Override
    public File getFile() throws IOException{
        return file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(file.toPath());
    }
}
