package com.bugjc.ea.opensdk.http.core.util;

/**
 * spring 路径匹配源码
 * @author aoki
 * @date 2019/11/26
 * **/
public interface PathMatcher {

    /**
     * Match the given {@code path} against the given {@code pattern},
     * according to this PathMatcher's matching strategy.
     * @param pattern the pattern to match against
     * @param path the path String to test
     * @return {@code true} if the supplied {@code path} matched,
     * {@code false} if it didn't
     */
    boolean match(String pattern, String path);
}
