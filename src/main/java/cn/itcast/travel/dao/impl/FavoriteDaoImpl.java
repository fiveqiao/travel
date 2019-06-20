package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private String sql = "";

    @Override
    public int findByCount(int rid) {
        sql = "select count(*) from tab_favorite where rid = ?";
        return template.queryForObject(sql,Integer.class,rid);
    }

    @Override
    public Favorite findByFavorite(int rid, int uid) {
        sql = "select * from tab_favorite where rid = ? and uid = ?";
        try {
            return template.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public void add(int rid, int uid) {
        sql = "insert into tab_favorite values(?,?,?)";
        try {
            template.update(sql,rid,new Date(),uid);
        } catch (DataAccessException e) {

        }
    }

    @Override
    public void remove(int rid, int uid) {
        sql = "delete from tab_favorite where rid = ? and uid = ?";
        template.update(sql,rid,uid);
    }

    @Override
    public int findUidByRidTotalCount(int uid) {
        sql = "select count(*) from tab_favorite where uid = ?";
        return template.queryForObject(sql,Integer.class,uid);
    }

}
