package com.jxufe.study.tinyspring.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 22:28
 **/
public class ClassPathResource implements Resource{
    private final String path;
    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this.path = path;
        classLoader = Thread.currentThread().getContextClassLoader();
    }

    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        }
        else {
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }
        if (is == null) {
            throw new FileNotFoundException(path + " cannot be opened because it does not exist");
        }
        return is;
    }
}
