package com.bugjc.ea.auth.core.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * 重新包装request，解决request.getInputStream()只能获取一次值得问题
 * @author qingyang
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 报文
     */
    private byte[] body = null;
    private final static int BUFFER_SIZE = 4096;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public MyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        body = inputStreamToByte(request.getInputStream());
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener listener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }

    /**
     * 将InputStream转换成byte数组
     * @param in
     * @return
     * @throws IOException
     */
    private static byte[] inputStreamToByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1){
            outStream.write(data, 0, count);
        }
        data = null;
        return outStream.toByteArray();

    }

}
