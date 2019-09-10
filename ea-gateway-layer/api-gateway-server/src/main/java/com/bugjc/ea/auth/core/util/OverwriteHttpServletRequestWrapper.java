package com.bugjc.ea.auth.core.util;

import com.netflix.zuul.http.ServletInputStreamWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * @author aoki
 */
public class OverwriteHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public OverwriteHttpServletRequestWrapper(byte[] body,HttpServletRequest request) {
        super(request);
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStreamWrapper(body);
    }
    @Override
    public int getContentLength() {
        return body.length;
    }
    @Override
    public long getContentLengthLong() {
        return body.length;
    }
}
