package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    boolean regist(User user);

    /**
     * 根据用户名查找用户
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 激活用户
     * @param code
     * @return
     */
    boolean active(String code);
    /**
     * 用户登录
     * @param us
     * @return
     */
    User login(User us);
}
