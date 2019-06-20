package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
    private static JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    private static String sql = "";

    /**
     * 根据用户名查询用户信息
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        sql = "select * from tab_user where username = ?";
        try {
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 用户注册保存
     *
     * @param user
     * @return
     */
    @Override
    public void save(User user) {
        sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) " +
                "values(?,?,?,?,?,?,?,?,?)";
        template.update(sql, user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    /**
     * 根据code查找唯一用户
     * @param code
     * @return
     */
    @Override
    public User findByCode(String code) {
        sql = "select * from tab_user where code = ?";
        try {
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    /**
     * 修改为激活状态
     * @param user
     */
    @Override
    public void updateStatus(User user) {
        sql = "update tab_user set status = 'Y' where uid = ?";
        template.update(sql,user.getUid());
    }

    /**
     * 根据username和password查找
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        sql = "select * from tab_user where username = ? and password = ?";
        try {
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),username,password);
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }
}
