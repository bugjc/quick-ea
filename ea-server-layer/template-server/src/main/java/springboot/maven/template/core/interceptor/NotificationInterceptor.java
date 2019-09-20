package springboot.maven.template.core.interceptor;

import com.alibaba.fastjson.JSON;
import com.bugjc.ea.opensdk.http.core.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 通知拦截,检查token
 * @Author yangqing
 * @Date 2019/7/4 15:21
 **/
@Slf4j
@Component
public class NotificationInterceptor implements HandlerInterceptor {

    /**
     * 进入对应的controller方法之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //拦截接口
        //responseResult(response, ResultGenerator.genFailResult("无效的请求参数"));
        return true;
    }

    /**
      * 应答结果
      * @param response
      * @param result
      * @author yangqing
      * @date 2019/7/4
      **/
    private static void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.write(JSON.toJSONString(result));
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    /**
     * controller处理之后，返回对应的视图之前
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * 整个请求结束后调用，视图渲染后，主要用于资源的清理
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
