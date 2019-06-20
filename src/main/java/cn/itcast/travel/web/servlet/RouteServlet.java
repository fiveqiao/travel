package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;
import cn.itcast.travel.util.BaseServlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    //声明RouteService业务对象
    private RouteService routeService = new RouteServiceImpl();
    //声明FavoriteService业务对象
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) {
        //接收前台参数，当前页面，页面显示数据，分类
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        /**
         * 使用tomcat8之前的版本，需要是由以下注释的内容
         */
        if (rname != null && !"".equals(rname)) {
            try {
                //maven默认的是tomcat7，并没有对请求的中文做解码解决，需要手动去解决
                rname = new String(rname.getBytes("iso8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        //处理参数
        //类别id
        int cid = 0;
        if (!"null".equals(cidStr) && cidStr != null && cidStr.length() > 0) {
            cid = Integer.parseInt(cidStr);
        }
        //当前页，如果不传递，默认为第一页
        int currentPage = 0;
        if (!"null".equals(currentPageStr) && currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        //每页显示条数，如果不传递，默认显示5条
        int pageSize = 5;
        if (!"null".equals(pageSizeStr) && pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        //调用service业务层
        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize, rname);
        try {
            //调用父类的writeValue方法，把对象写回前台
            writeValue(pageBean, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询单个route
     *
     * @param request
     * @param response
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) {
        Route route = new Route();
        String rid = request.getParameter("rid");
        if (!"null".equals(rid) && rid != null && !"".equals(rid)) {
            route = routeService.findOne(Integer.parseInt(rid));
            try {
                writeValue(route, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断当前用户是否收藏过该线路
     *
     * @param request
     * @param response
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        String rid = request.getParameter("rid");
        boolean flag = false;
        if (user == null) {
            try {
                writeValue(flag, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //用户未登录
        } else {
            // 用户已登录
            flag = favoriteService.findByFavorite(rid, user.getUid());
            try {
                writeValue(flag, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加收藏
     *
     * @param request
     * @param response
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        favoriteService.addFavorite(rid, user.getUid());
    }

    /**
     * 删除收藏
     *
     * @param request
     * @param response
     */
    public void removeFavorite(HttpServletRequest request, HttpServletResponse response) {
        String rid = request.getParameter("rid");
        User user = (User) request.getSession().getAttribute("user");
        favoriteService.removeFavorite(rid, user.getUid());
    }

    /**
     * 收藏分页查询
     */
    public void favoritePageQuery(HttpServletRequest request, HttpServletResponse response) {
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        int currentPage = 0;
        if (!"null".equals(currentPageStr) && currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 12;
        if (!"null".equals(pageSizeStr) && pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            PageBean<Route> pageBean = routeService.favoritePageQuery(currentPage, pageSize, user.getUid());
            try {
                writeValue(pageBean, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 收藏排行查询
     *
     * @param request
     * @param response
     */
    public void hot(HttpServletRequest request, HttpServletResponse response) {
        String countStr = request.getParameter("count");
        if (countStr != null && !"".equals(countStr) && !"null".equals(countStr)) {
            int count = Integer.parseInt(countStr);
            List<Route> list = routeService.findhot(count);
            try {
                writeValue(list, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 所有收藏商品分页查询
     *
     * @param request
     * @param response
     */
    public void hotPageQuery(HttpServletRequest request, HttpServletResponse response) {
        //获取数据
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String minStr = request.getParameter("min");
        String maxStr = request.getParameter("max");
        //防止数据异常
        int currentPage = 0;
        if (!"null".equals(currentPageStr) && currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        }
        //如果没有传递每页显示数，默认8
        int pageSize = 8;
        if (!"null".equals(pageSizeStr) && pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        /*        *//**
         * 使用tomcat8之前的版本，需要是由以下注释的内容
         *//*
        if (rname != null && rname.length() > 0) {
            try {
                //maven默认的是tomcat7，并没有对请求的中文做解码解决，需要手动去解决
                rname = new String(rname.getBytes("iso8859-1"), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/
        PageBean<Route> pageBean = routeService.findHotPageQuery(currentPage, pageSize, rname, minStr, maxStr);
        try {
            writeValue(pageBean, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 最新上架route
     *
     * @param request
     * @param response
     */
    public void newRoute(HttpServletRequest request, HttpServletResponse response) {
        String countStr = request.getParameter("count");
        if (!"null".equals(countStr) && countStr != null && !"".equals(countStr)) {
            int count = Integer.parseInt(countStr);
            List<Route> list = routeService.findNewRoute(count);
            try {
                writeValue(list, response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
