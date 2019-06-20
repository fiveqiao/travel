package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {
    /**
     * 根据cid查询总记录数
     * @param cid
     * @return
     */
    int findTotalCount(int cid,String rname);

    /**
     * 根据cid,start,pageSize查询当前页的数据集合
     * @param cid
     * @param start
     * @param pageSize
     * @return
     */
    List<Route> findByPage(int cid,int start,int pageSize,String rname);

    /**
     * 根据tab_route表的rid查找单个route
     * @param rid
     * @return
     */
    Route findOne(int rid);

    /**
     * 修改收藏次数
     * @param rid
     * @param count
     */
    void update(int rid, int count);

    /**
     * 根据用户id查找所有收藏的route
     * 分页查询
     * @param uid
     * @return
     */
    List<Route> findFavorite(int start,int pageSize,int uid);

    /**
     * 查找同个rid收藏次数最多的route
     * @return
     */
    List<Route> findhot(int count);

    List<Route> findByTjHot(int start,int pageSize,String rname,String min,String max);

    /**
     * 查询所有商品有过收藏,并满足搜索条件的数量
     * @return
     */
    int findByCollectAll(String rname, String minStr, String maxStr);

    /**
     * 根据最新上架数据倒叙查询，参数为显示条数
     * @param count
     * @return
     */
    List<Route> findNewRoute(int count);
}
