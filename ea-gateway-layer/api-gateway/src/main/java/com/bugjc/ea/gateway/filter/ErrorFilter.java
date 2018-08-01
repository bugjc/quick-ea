package com.bugjc.ea.gateway.filter;

import com.bugjc.ea.gateway.core.dto.ResultGenerator;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author aoki
 * @date ${date}
 */
@Slf4j
@Component
public class ErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            ZuulException exception = this.findZuulException(context.getThrowable());
            log.error("系统异常拦截", exception);

            HttpServletResponse response = context.getResponse();
            response.setContentType("application/json; charset=utf8");
            response.setStatus(exception.nStatusCode);
            try (PrintWriter writer = response.getWriter()) {
                writer.print(ResultGenerator.genFailResult(exception.nStatusCode, exception.getMessage()).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception var5) {
            ReflectionUtils.rethrowRuntimeException(var5);
        }

        return null;

    }

    private ZuulException findZuulException(Throwable throwable) {
        if (ZuulException.class.isInstance(throwable.getCause())) {
            return (ZuulException)throwable.getCause().getCause();
        } else if (ZuulException.class.isInstance(throwable.getCause())) {
            return (ZuulException)throwable.getCause();
        } else {
            return ZuulException.class.isInstance(throwable) ? (ZuulException)throwable : new ZuulException(throwable, 500, (String)null);
        }
    }

}
