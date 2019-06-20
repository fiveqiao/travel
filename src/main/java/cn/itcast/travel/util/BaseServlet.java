package cn.itcast.travel.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    public static ServletContext servletContext;
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        servletContext = req.getServletContext();
        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();    //   /user/add  || /user/find
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        //3.获取方法对象Method
        //谁调用我，我就是谁，this代表对象本身
//        Class aClass = this.getClass();
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化为json，并且写回客户端
     *
     * @param obj
     */
    public void writeValue(Object obj, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        //设置响应数据方式
        response.setContentType("application/json;charset=utf-8");
        //直接把对象序列化
/*        String json = mapper.writeValueAsString(obj);
        response.getWriter().write(json);*/
        mapper.writeValue(response.getOutputStream(), obj);
    }

    /**
     * 将传入的对象序列化为json，返回
     *
     * @param obj
     * @return
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        //创建匿名ObjectMapper对象，将对象返回json
        return new ObjectMapper().writeValueAsString(obj);
    }
}
