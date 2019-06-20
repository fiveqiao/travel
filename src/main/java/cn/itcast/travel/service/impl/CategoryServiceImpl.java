package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //可使用sortedset排序查询
//        Set<String> categorys = jedis.zrange("category", 0, -1);
        //查询sortedset中的分数(cid)和值(cname)
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        //2.判断查询的结果是否为空
        List<Category> categoryAll = null;
        if (categorys == null || categorys.size() == 0) {
            //为空需要从数据库查询，再将数据存入redis
            categoryAll = categoryDao.findAll();
            for (Category category : categoryAll) {
                jedis.zadd("category", category.getCid(), category.getCname());
            }
        }else {
            //4.如果不为空，将set的数据存入List因为返回结果为List
            categoryAll = new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCid((int)tuple.getScore());
                category.setCname(tuple.getElement());
                categoryAll.add(category);
            }
        }
        return categoryAll;
    }
}
