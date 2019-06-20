package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 用户注册保存
     *
     * @param user
     * @return
     */
    void save(User user);

    /**
     * 根据code查找唯一用户
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 修改激活状态
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据username和password查找
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}
