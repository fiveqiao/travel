package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteService;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    @Override
    public boolean findByFavorite(String rid, int uid) {
            Favorite favorite =  favoriteDao.findByFavorite(Integer.parseInt(rid),uid);
            return favorite != null;
    }

    @Override
    public void addFavorite(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid),uid);
    }

    @Override
    public void removeFavorite(String rid, int uid) {
        favoriteDao.remove(Integer.parseInt(rid),uid);
    }
}
