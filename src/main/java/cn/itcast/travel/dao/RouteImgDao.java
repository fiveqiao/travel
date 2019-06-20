package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    /**
     * 根据rid查找结果集
     * @param rid
     * @return
     */
    List<RouteImg> findAll(int rid);
}
