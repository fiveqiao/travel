package cn.itcast.travel.util;

import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

/**
 * 自定义获取Spring容器对象
 */
public class WebApplicationContextUtils {
    public static ApplicationContext getWebApplicationContext(ServletContext servletContext) {
        return (ApplicationContext) servletContext.getAttribute("app");
    }
}
