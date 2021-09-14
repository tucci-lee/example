package cn.tucci.spring.scan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author tucci.lee
 */
public class UrlResource implements Resource{

    private URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    @Override
    public URL getUrl() throws IOException{
        return url;
    }

    @Override
    public File getFile() throws IOException {
        return new File(url.getFile());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return url.openConnection().getInputStream();
    }
}
