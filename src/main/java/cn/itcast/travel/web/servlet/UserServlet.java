package cn.itcast.travel.web.servlet;


import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import cn.itcast.travel.util.BaseServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
//@Controller
public class UserServlet extends BaseServlet {
    //声明UserServlet业务对象
//    private UserService service = new UserServiceImpl();
/*    @Autowired
    @Qualifier("userService")*/
    private static UserService service;

    static {
//        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(servletContext);
//使用自定义的工具类获取Spring容器对象
//        ApplicationContext app = cn.itcast.travel.util.WebApplicationContextUtils.getWebApplicationContext(servletContext);
        //使用Spring封装好的
        WebApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        service = (UserService) app.getBean("userService");
    }

    /**
     * 用户登录
     */

    public void login(HttpServletRequest request, HttpServletResponse response) {
        //首先获取验证码
        String check = request.getParameter("check");
        HttpSession session = request.getSession();
        //获取验证码内容
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //删除验证码，防止复用
        session.removeAttribute("CHECKCODE_SERVER");
        //创建返回结果对象
        ResultInfo info = new ResultInfo();
        //创建转换JSON格式的对象，这里使用的Jackson
        ObjectMapper mapper = new ObjectMapper();
        //判断验证码是否正确
        if (checkcode_server != null && checkcode_server.equalsIgnoreCase(check)) {
            User us = new User();
            Map<String, String[]> map = request.getParameterMap();
            try {
                BeanUtils.populate(us, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //调用servlet登录用户
            User user = service.login(us);
            if (user == null) {
                info.setFlag(false);
                info.setErrorMsg("用户名或密码错误！");
            }
            //判断用户是否激活
            if (user != null && "Y".equals(user.getStatus())) {
                info.setFlag(true);
                session.setAttribute("user", user);
            }
            //判断用户是否激活
            if (user != null && !"Y".equals(user.getStatus())) {
                info.setFlag(false);
                info.setErrorMsg("该用户未激活，请进入注册邮箱激活！");
            }
           /* String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);*/
            //调用父类的方法，传入对象，并写回客户端
            try {
                writeValue(info, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
/*            String json = mapper.writeValueAsString(info);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);*/
            try {
                writeValue(info, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 用户注册
     */
    public void regisUser(HttpServletRequest request, HttpServletResponse response) {
        //设置响应编码格式
        response.setContentType("application/json;charset=utf-8");
        //获取用户输入的验证码内容
        String check = request.getParameter("check");
        //获取验证码图片的内容
        String checkcode_server = (String) request.getSession().getAttribute("CHECKCODE_SERVER");
        //删除验证码信息，防止复用
        request.getSession().removeAttribute("CHECKCODE_SERVER");
        //创建返回数据结果信息对象
        ResultInfo info = new ResultInfo();
        if (!"null".equals(checkcode_server) && checkcode_server != null && checkcode_server.equalsIgnoreCase(check)) {
            //获取数据
            Map<String, String[]> map = request.getParameterMap();
            //封装数据
            User user = new User();
            try {
                BeanUtils.populate(user, map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            //调用service完成注册
            boolean flag = service.regist(user);
            //响应结果
            if (flag) {
                info.setFlag(flag);
            } else {
                info.setFlag(flag);
                info.setErrorMsg("注册失败！");
            }
            //将info对象序列化,数据写回客户端
            try {
                writeValue(info, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将info对象序列化,数据写回客户端
            try {
                writeValue(info, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查找用户名是否已存在
     * 已存在就是已经注册了
     */
    public void findByUsername(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        //获取用户名的内容
        String username = request.getParameter("username");
        User user = service.findByUsername(username);
        //创建结果对象，返回数据
        ResultInfo info = new ResultInfo();
        if (user != null) {
            info.setFlag(false);
            info.setErrorMsg("用户名已注册");
        } else {
            info.setFlag(true);
        }
        try {
            writeValue(info, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找session记录已登录的用户信息
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        //获取已经登录的用户信息
        Object user = request.getSession().getAttribute("user");
        //创建json转换对象,将user写回客户端
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(response.getOutputStream(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出用户信息，删除session中用户信息
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        try {
            response.sendRedirect(request.getContextPath() + "/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户激活的状态
     */
    public void active(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        boolean flag = service.active(code);
        if (flag) {
            //激活成功
            try {
                response.sendRedirect(request.getContextPath() + "/register_ok2.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //未激活
            try {
                response.sendRedirect(request.getContextPath() + "/register_ok3.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
