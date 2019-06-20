import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.SocketHandler;
public class test {
    @Test
    public void test() throws SQLException {
        ApplicationContext app = new ClassPathXmlApplicationContext("applicationContext.xml");
 /*       DataSource dataSource = (DataSource) app.getBean("dataSource");
        Connection connection = dataSource.getConnection();
        System.out.println(connection);*/
        JdbcTemplate jdbcTemplate = app.getBean(JdbcTemplate.class);
//        jdbcTemplate.update("insert into categ (cid,cname) values(?,?)","cid","ad");
        List<String> list = jdbcTemplate.queryForList("select cname from categ",String.class);
        System.out.println(list);
//        ComboPooledDataSource bean = app.getBean(ComboPooledDataSource.class);
        ComboPooledDataSource bean = (ComboPooledDataSource) app.getBean("dataSourcea");
        Connection connection = bean.getConnection();
        System.out.println(connection);
    }
}
