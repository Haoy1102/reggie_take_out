package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author haoy
 * @description
 * @date 2022/11/30 15:50
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    private static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    /*
      1、荻取本次情求的URI
      2、判断本次情状是否需要处理
      3、如果不秀要处理，则直接成行
      4、判断登灵状态，如果己登录，则直接成行
      5、如果夫登灵则返回夫登陆结果，结合前端，返回一个输出流
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        1、获取本次情求的URI
        String requestURI=request.getRequestURI();

        log.info("拦截的请求：{}",requestURI);

//        定义不需要处理的URI
        String[] urls={
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };

//        2、判断本次情状是否需要处理
//        3、如果不秀要处理，则直接放行
        if (check(urls,requestURI)){
            log.info("本次{}请求不需要处理",requestURI);
            chain.doFilter(request,response);
            return;
        }
//      4-1、判断后台登灵状态，如果己登录，则直接放行
        if (null!=request.getSession().getAttribute("employee")){
            Long employeeId = (Long) request.getSession().getAttribute("employee");
            log.info("用户已登陆，用户id为：{}", employeeId);
//          long id = Thread.currentThread().getId();
//          log.info("当前线程：{}",id);
            BaseContext.setCurrentId(employeeId);

            chain.doFilter(request,response);
            return;
        }
//      4-2、判断前台登录状态
        if (null!=request.getSession().getAttribute("user")){
            Long userId = (Long) request.getSession().getAttribute("user");
            log.info("用户已登陆，用户id为：{}", userId);
            BaseContext.setCurrentId(userId);

            chain.doFilter(request,response);
            return;
        }
 //      5、如果未登陆则返回夫登陆结果，结合前端，返回一个输出流
        log.info("用户未登陆");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    private boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match=PATH_MATCHER.match(url,requestURI);
            if (match) return true;
        }
        return false;
    }



}
