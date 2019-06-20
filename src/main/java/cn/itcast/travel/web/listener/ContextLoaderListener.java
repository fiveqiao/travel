package cn.itcast.travel.web.listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
//使用注解配置监听器
//@WebListener
public class ContextLoaderListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        //获取配置文件中的contextConfigLocation配置文件
        String contextConfigLocation = servletContext.getInitParameter("contextConfigLocation");
        System.out.println(contextConfigLocation);
//        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        //使用监听器，在服务器初始启动的时候加载Spring配置文件，创建Spring容器，避免多次创建
        ApplicationContext app = new ClassPathXmlApplicationContext(contextConfigLocation);
        servletContext.setAttribute("app", app);
        System.out.println("创建成功");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
