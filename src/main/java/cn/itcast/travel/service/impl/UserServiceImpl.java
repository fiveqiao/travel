package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserServiceImpl implements UserService {

    //    private static UserDao userDao = new UserDaoImpl();
    @Resource(name = "userDao")
    private UserDao userDao;

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        User us = userDao.findByUsername(user.getUsername());
        if (us == null) {
            //用UuidUtil工具类获取一个字符串
            String code = UuidUtil.getUuid();
            //设置唯一标识，设置为code
            user.setCode(code);
            //默认未激活状态
            user.setStatus("N");
            userDao.save(user);
            //激活邮件发送，邮件正文
            String content = "<h1>来自天堂黑马黑马旅游的激活邮件:</h1><h3><a href='http://localhost/travel/user/active?code=" + user.getCode() + "'>请点击激活!</a></h3>";
            MailUtils.sendMail(user.getEmail(), content, "激活邮件");
            return true;
        }
        return false;
    }

    /**
     * 用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 激活用户
     *
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        User user = userDao.findByCode(code);
        if (user != null) {
            userDao.updateStatus(user);
            return true;
        }
        return false;
    }

    /**
     * 用户登录
     *
     * @param us
     * @return
     */
    @Override
    public User login(User us) {
        User user = userDao.findByUsernameAndPassword(us.getUsername(), us.getPassword());
        return user;
    }
}
