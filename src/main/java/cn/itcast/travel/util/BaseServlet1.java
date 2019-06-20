package cn.itcast.travel.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 通用的SErvlet的编写:
 * @author admin
 *
 */
public class BaseServlet1 extends HttpServlet {

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// http://localhost:8080/store_v2.0/UserSErvlet?method=regist
		// 接收参数:
		req.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html;charset=UTF-8");
		String methodName = req.getParameter("method");
		if(methodName == null || "".equals(methodName)){
			resp.getWriter().println("method参数为null!!!");
			return;
		}
		// 获得子类的Class对象：
		Class clazz = this.getClass();
		// 获得子类中的方法了:
		try {
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			// 使方法执行:
			String path = (String)method.invoke(this, req,resp);
			if(path != null){
				req.getRequestDispatcher(path).forward(req, resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
