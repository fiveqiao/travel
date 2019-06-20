package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

import java.util.List;

public interface FavoriteDao {
    /**
     * 查询收藏次数
     */
    int findByCount(int rid);

    /**
     * 查询用户是否收藏
     * @param i
     * @param uid
     * @return
     */
    Favorite findByFavorite(int rid, int uid);

    /**
     * 添加收藏记录
     * @param i
     * @param uid
     */
    void add(int rid, int uid);

    /**
     * 删除收藏
     * @param i
     * @param uid
     */
    void remove(int rid, int uid);
    /**
     * 根据用户id查询出所有对应收藏的总记录数
     * @param uid
     * @return
     */
    int findUidByRidTotalCount(int uid);


}
