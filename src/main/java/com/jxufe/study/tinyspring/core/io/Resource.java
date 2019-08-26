package com.jxufe.study.tinyspring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * hong.zheng
 *
 * @Date: 2019-08-26 18:39
 **/
public interface Resource {

    InputStream getInputStream() throws IOException;
}
