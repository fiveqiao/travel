package cn.itcast.travel.service;

public interface FavoriteService {
    /**
     * 查找用户是否收藏
     * @param rid
     * @param uid
     * @return
     */
    boolean findByFavorite(String rid,int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    void addFavorite(String rid,int uid);

    /**
     * 删除收藏
     * @param rid
     * @param uid
     */
    void removeFavorite(String rid, int uid);
}
