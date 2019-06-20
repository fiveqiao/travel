package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private String sql = "";

    @Override
    public int findTotalCount(int cid, String rname) {
        sql = "select count(*) from tab_route where 1 = 1 ";
        List<Object> list = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder(sql);
        if (cid != 0) {
            sb.append(" and cid = ? ");
            list.add(cid);
        }
        if (!"null".equals(rname) && rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }
        try {
            return template.queryForObject(sb.toString(), Integer.class, list.toArray());
        } catch (DataAccessException e) {
            return 0;
        }
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        sql = "select * from tab_route where 1 = 1 ";
        List<Object> list = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder(sql);
        if (cid != 0) {
            sb.append(" and cid = ? ");
            list.add(cid);
        }
        if (!"null".equals(rname) && rname != null && rname.length() > 0) {
            sb.append(" and rname like ?");
            list.add("%" + rname + "%");
        }
        sb.append(" limit ?,?");
        list.add(start);
        list.add(pageSize);
        try {
            return template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public Route findOne(int rid) {
        sql = "select * from tab_route where rid = ?";
        return template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
    }

    @Override
    public void update(int rid, int count) {
        sql = "update tab_route set count = ? where rid = ?";
        template.update(sql, count, rid);
    }

    @Override
    public List<Route> findFavorite(int start, int pageSize, int uid) {
        sql = "select * from tab_route where rid in (select rid from tab_favorite where uid = ?) limit ?,?";
        return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid, start, pageSize);
    }

    @Override
    public List<Route> findhot(int count) {
//        sql = "SELECT * FROM tab_route where rid in(select rid from tab_favorite group by rid order by rid asc) limit 0,10";
//        sql = "SELECT * FROM tab_route r INNER JOIN tab_favorite f ON r.rid = f.rid GROUP BY f.rid ORDER BY count(*) DESC limit 0,10";
        sql = "select * from tab_route where count > 0 order by count desc limit 0,?";
        try {
            return template.query(sql, new BeanPropertyRowMapper<Route>(Route.class),count);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Route> findByTjHot(int start, int pageSize, String rname, String min, String max) {
        sql = "select * from tab_route where 1 = 1 and count > 0 ";
        StringBuilder sb = new StringBuilder(sql);
        List<Object> list = new ArrayList<Object>();
        if (!"null".equals(rname) && rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }
        /**
         * 如果两个价格都不为空，执行以下语句
         */
        if (!"null".equals(min) && min != null && min.length() > 0 && !"null".equals(max) && max != null && max.length() > 0) {
            sb.append(" and price between ? and ? ");
            list.add(min);
            list.add(max);
        }
        //如果只有最大值为空，执行一下语句
/*        if (!"null".equals(min) && min != null && min.length() > 0 || "null".equals(max) || max == null || max.length() == 0) {
            sb.append(" and price >= ? ");
            list.add(min);
        }
        //如果只有最小值为空，执行下面语句
        if ("null".equals(min) || min == null || min.length() == 0 && !"null".equals(max) && max != null && max.length() > 0) {
            sb.append(" and price <= ? ");
            list.add(max);
        }*/
        sb.append(" order by count desc limit ?,?");
        list.add(start);
        list.add(pageSize);
        try {
            return template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), list.toArray());
        } catch (DataAccessException e) {
            //不打印报错
            return null;
        }
    }

    @Override
    public int findByCollectAll(String rname, String min, String max) {
        sql = "select count(*) from tab_route where 1 = 1 and count > 0 ";
        StringBuilder sb = new StringBuilder(sql);
        List<Object> list = new ArrayList<Object>();
        if (!"null".equals(rname) && rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            list.add("%" + rname + "%");
        }
        /**
         * 如果两个价格都不为空，执行以下语句
         */
        if (!"null".equals(min) && min != null && min.length() > 0 && !"null".equals(max) && max != null && max.length() > 0) {
            sb.append(" and price between ? and ? ");
            list.add(min);
            list.add(max);
        }
        //如果只有最大值为空，执行一下语句
/*        if (!"null".equals(min) && min != null && min.length() > 0 || "null".equals(max) || max == null || max.length() == 0) {
            sb.append(" and price >= ? ");
            list.add(min);
        }
        //如果只有最小值为空，执行下面语句
        if ("null".equals(min) || min == null || min.length() == 0 && !"null".equals(max) && max != null && max.length() > 0) {
            sb.append(" and price <= ? ");
            list.add(max);
        }*/
        try {
            return template.queryForObject(sb.toString(), Integer.class, list.toArray());
        } catch (DataAccessException e) {
            //不打印
            return 0;
        }
    }

    @Override
    public List<Route> findNewRoute(int count) {
        sql = "select * from tab_route order by rdate desc limit 0,?";
        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),count);
    }
}
