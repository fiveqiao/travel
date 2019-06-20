package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {
    /**
     * 根据sid查找
     * @param sid
     * @return
     */
    Seller findSeller(int sid);
}
