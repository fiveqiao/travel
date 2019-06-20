package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.*;
import cn.itcast.travel.dao.impl.*;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.RouteService;

import java.util.List;

/**
 * 线路Service
 */
public class RouteServiceImpl implements RouteService {
    //创建RouteDao业务声明
    private RouteDao routeDao = new RouteDaoImpl();
    //创建RouteImgDao业务声明
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    //创建SellerDao业务声明
    private SellerDao sellerDao = new SellerDaoImpl();
    //创建Category业务声明
    private CategoryDao categoryDao = new CategoryDaoImpl();
    //创建Favorite业务声明
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    /**
     * 根据类别进行分页查询
     *
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //查询出总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        //根据每页显示条数和总记录数计算出总页数,如果不能整除，总页数就向上取整
        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;
/*        //如果当前页大于总页数
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }*/
        //计算出从哪里开始查询，公式：当前页减一再乘以每页显示条数
        int start = (currentPage - 1) * pageSize;
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname);
        //封装PageBean
        PageBean<Route> pageBean = new PageBean<>();
        //设置属性值
        pageBean.setPageSize(pageSize);
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalCount(totalCount);
        pageBean.setTotalPage(totalPage);
        pageBean.setList(list);
        return pageBean;
    }

    /**
     * 根据tab_route表的rid查询单个route
     *
     * @param rid
     * @return
     */
    @Override
    public Route findOne(int rid) {
        Route route = routeDao.findOne(rid);
        //调用routeImgDao,传入rid做为查询条件
        List<RouteImg> list = routeImgDao.findAll(rid);
        route.setRouteImgList(list);
        //调用SellerDao,传入sid做为查询条件
        Seller seller = sellerDao.findSeller(route.getSid());
        route.setSeller(seller);
        //调用Categroy，传入cid作为查询条件
        Category category = categoryDao.findOne(route.getCid());
        route.setCategory(category);
        int count = favoriteDao.findByCount(rid);
        routeDao.update(rid, count);
        route.setCount(count);
        return route;
    }

    /**
     * 根据用户的uid查询收藏route
     * 分页查询
     *
     * @param uid
     * @return
     */
    @Override
    public PageBean<Route> favoritePageQuery(int currentPage, int pageSize, int uid) {
        //根据uid查询收藏总记录数
        int totalCount = favoriteDao.findUidByRidTotalCount(uid);
        //根据每页显示条数和总记录数计算出总页数,如果不能整除，总页数就向上取整
        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;

        //查询每页显示条数,先计算出从哪开始查询,当前页减一然后乘每页显示条数
        if (currentPage <= 0) {
            currentPage = 1;
        }
        int start = (currentPage - 1) * pageSize;
        List<Route> list = routeDao.findFavorite(start, pageSize, uid);
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setTotalPage(totalPage);
        pageBean.setPageSize(pageSize);
        pageBean.setTotalCount(totalCount);
        pageBean.setList(list);
        return pageBean;
    }

    @Override
    public List<Route> findhot(int count) {
        return routeDao.findhot(count);
    }

    @Override
    public PageBean<Route> findHotPageQuery(int currentPage, int pageSize, String rname, String minStr, String maxStr) {
        //查询出所有记录
        int totalCount = routeDao.findByCollectAll(rname, minStr, maxStr);
        //根据所有记录计算出总页数
        int totalPage = totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;
        //根据当前页计算从哪里开始查找数据
        if (currentPage < 1) {
            currentPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }
        int strat = (currentPage - 1) * pageSize;
        List<Route> list = routeDao.findByTjHot(strat, pageSize, rname, minStr, maxStr);
        //创建分页对象，把数据赋值
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        pageBean.setList(list);
        pageBean.setTotalPage(totalPage);
        pageBean.setTotalCount(totalCount);
        return pageBean;
    }

    @Override
    public List<Route> findNewRoute(int count) {
        return routeDao.findNewRoute(count);
    }
}
