package me.stephenj.sqlope.config;

import com.mysql.jdbc.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class DataSourceHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceHelper.class);
    @Value("${datasource.driver}")
    private String driver; // com.mysql.cj.jdbc.Driver
    @Value("${datasource.url}")
    private String url; // jdbc:mysql://localhost:3306/pybbs?useSSL=false&characterEncoding=utf8
    @Value("${datasource.username}")
    private String username; // root
    @Value("${datasource.password}")
    private String password; // password

    @PostConstruct
    public void init() {
        try {
            Class.forName(driver);
            URI uri = new URI(url.replace("jdbc:", ""));
            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false", username, password);
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + path.replace("/", "") + "` DEFAULT CHARACTER SET = `utf8` COLLATE `utf8_general_ci`;");
            statement.close();
            connection.close();
            Connection dbsc = (Connection) DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/dbs?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false", username, password);
            Statement dbss = dbsc.createStatement();
            dbss.executeUpdate("CREATE TABLE IF NOT EXISTS `admin` ( " +
                    "`id` INT NOT NULL AUTO_INCREMENT, " +
                    "`username` VARCHAR(64) DEFAULT NULL, " +
                    "`password` VARCHAR(64) DEFAULT NULL, " +
                    "`role` VARCHAR(64) DEFAULT 'user' COMMENT '用户角色', " +
                    "`create_time` DATETIME DEFAULT NOW() COMMENT '创建时间', " +
                    "`login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间', " +
                    "`status` INT DEFAULT '1' COMMENT '帐号启用状态：0->禁用；1->启用', " +
                    "PRIMARY KEY (`id`) " +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';");
            dbss.executeUpdate("CREATE TABLE IF NOT EXISTS `table` ( " +
                    "`id` INT AUTO_INCREMENT COMMENT '数据表序号(自增)', " +
                    "`name` VARCHAR(255) COMMENT '数据表名', " +
                    "`description` VARCHAR(255) COMMENT '数据表描述', " +
                    "`author_id` INT COMMENT '表格创建人编号', " +
                    "`create_time` DATETIME DEFAULT NOW() COMMENT '数据表创建时间', " +
                    "`row_count` INT DEFAULT 0 COMMENT '数据表中有效记录的行数', " +
                    "`fk` INT DEFAULT 0 COMMENT '外键', " +
                    "`locked` INT DEFAULT 0 COMMENT '被几张表锁定了', " +
                    "`status` BOOLEAN DEFAULT TRUE COMMENT '是否可用', " +
                    "PRIMARY KEY (`id`), " +
                    "CONSTRAINT tb_admin_id_fk FOREIGN KEY (`author_id`) REFERENCES `admin` (`id`)" +
                    ") ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT='数据表';");
            dbss.executeUpdate("CREATE UNIQUE INDEX table_name_uindex ON `table` (`name`);");
            dbss.executeUpdate("CREATE TABLE IF NOT EXISTS `field` ( " +
                    "`id` INT AUTO_INCREMENT COMMENT '字段序号(自增)', " +
                    "`table_id` INT COMMENT '数据表序号', " +
                    "`name` VARCHAR(255) COMMENT '字段名', " +
                    "`description` VARCHAR(255) COMMENT '字段描述', " +
                    "`type` VARCHAR(255) COMMENT '字段类型', " +
                    "`fk` INT DEFAULT -1 COMMENT '外键指向的字段', " +
                    "`locked` INT DEFAULT 0 COMMENT '被几个字段锁定了', " +
                    "`create_time` DATETIME DEFAULT NOW() COMMENT '数据字段创建时间', " +
                    "`modify_time` DATETIME DEFAULT NOW() COMMENT '数据字段修改时间', " +
                    "`status` BOOLEAN DEFAULT TRUE COMMENT '是否可用', " +
                    "PRIMARY KEY (`id`), " +
                    "CONSTRAINT field_table_id_fk FOREIGN KEY (`table_id`) REFERENCES `table` (`id`)" +
                    ") ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT='数据字段';");
            dbss.executeUpdate("CREATE TABLE IF NOT EXISTS `row` ( " +
                    "`id` INT AUTO_INCREMENT COMMENT '行序号(自增)', " +
                    "`table_id` INT COMMENT '数据表序号', " +
                    "`create_time` DATETIME DEFAULT NOW() COMMENT '数据字段创建时间', " +
                    "`modify_time` DATETIME DEFAULT NOW() COMMENT '数据字段修改时间', " +
                    "`status` BOOLEAN DEFAULT TRUE COMMENT '是否可用', " +
                    "PRIMARY KEY (`id`), " +
                    "CONSTRAINT row_table_id_fk FOREIGN KEY (`table_id`) REFERENCES `table` (`id`)" +
                    ") ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT='数据表行';");
            dbss.executeUpdate("CREATE TABLE IF NOT EXISTS `data` ( " +
                    "`id` INT AUTO_INCREMENT COMMENT '数据序号(自增)', " +
                    "`table_id` INT COMMENT '数据表序号', " +
                    "`field_id` INT COMMENT '数据字段序号', " +
                    "`row_id` INT COMMENT '数据行序号', " +
                    "`value` VARCHAR(255) COMMENT '数据值', " +
                    "`fk` INT DEFAULT -1 COMMENT '外键指向的数据', " +
                    "`locked` INT DEFAULT 0 COMMENT '被几个数据锁定了', " +
                    "`create_time` DATETIME DEFAULT NOW() COMMENT '数据字段创建时间', " +
                    "`modify_time` DATETIME DEFAULT NOW() COMMENT '数据字段修改时间', " +
                    "PRIMARY KEY (`id`), " +
                    "CONSTRAINT data_table_id_fk FOREIGN KEY (`table_id`) REFERENCES `table` (`id`), " +
                    "CONSTRAINT data_row_id_fk FOREIGN KEY (`row_id`) REFERENCES `row` (`id`), " +
                    "CONSTRAINT data_field_id_fk FOREIGN KEY (`field_id`) REFERENCES `field` (`id`)" +
                    ") ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT='数据';");
            dbss.executeUpdate("CREATE TABLE IF NOT EXISTS `logs` (" +
                    "`id` BIGINT NOT NULL AUTO_INCREMENT, " +
                    "`user_id` INT COMMENT '用户编号', " +
                    "`username` VARCHAR(64) COMMENT '用户名', " +
                    "`operation` VARCHAR(255) COMMENT '进行的操作', " +
                    "`time` DATETIME DEFAULT NOW() COMMENT '时间', " +
                    "PRIMARY KEY (`id`)" +
                    ")ENGINE = InnoDB DEFAULT CHARACTER SET = utf8 COMMENT='操作日志';;");
            dbss.executeUpdate("INSERT INTO `admin` " +
                    "(id, username, password, role) " +
                    "VALUE " +
                    "(1, 'admin', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', 'admin');");
            dbss.executeUpdate("INSERT INTO `admin` " +
                    "(id, username, password, role) " +
                    "VALUE " +
                    "(2, 'user', '$2a$10$NZ5o7r2E.ayT2ZoxgjlI.eJ6OEYqjH7INR/F.mXDbjZJi9HF0YCVG', 'user');");
            dbss.close();
            dbsc.close();
        } catch (ClassNotFoundException | SQLException | URISyntaxException e) {
            LOGGER.error(e.getMessage());
        }
    }

}