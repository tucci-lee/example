package cn.tucci.spring.scan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author tucci.lee
 */
public interface Resource {

    URL getUrl() throws IOException;

    File getFile() throws IOException;

    InputStream getInputStream() throws IOException;
}
