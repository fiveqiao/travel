package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import cn.itcast.travel.util.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    //生命CategoryService业务对象
    private CategoryService service = new CategoryServiceImpl();

    /**
     * 查询出所有分类
     */
    public void findAll(HttpServletRequest request, HttpServletResponse response){
        List<Category> list = service.findAll();
        try {
            writeValue(list,response );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
