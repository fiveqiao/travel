package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 线路Service
 */
public interface RouteService {
    /**
     * 根据类别进行分页查询
     *
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageBean<Route> pageQuery(int cid,int currentPage,int pageSize,String cname);

    /**
     * 根据tab_route表的rid查找单个route
     * @param rid
     * @return
     */
    Route findOne(int rid);
    /**
     * 用户收藏进行查询
     */
    PageBean<Route> favoritePageQuery(int currentPage,int pageSize,int uid);

    /**
     * 查找出收藏最高的route
     * @return
     */
    List<Route> findhot(int count);

    /**
     * 根据收藏最热，还有条件分页查询
     * @param currentPage
     * @param pageSize
     * @param rname
     * @param minStr
     * @param maxStr
     * @return
     */
    PageBean<Route> findHotPageQuery(int currentPage, int pageSize, String rname, String minStr, String maxStr);

    /**
     * 最新上架时间查询
     * @param count
     * @return
     */
    List<Route> findNewRoute(int count);
}
