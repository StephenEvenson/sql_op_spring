package me.stephenj.sqlope.service.impl;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.utils.*;
import me.stephenj.sqlope.domain.TableParam;
import me.stephenj.sqlope.mbg.mapper.TableMapper;
import me.stephenj.sqlope.mbg.model.*;
import me.stephenj.sqlope.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName TbServiceImpl.java
 * @Description
 * @author 张润天
 * @Time 2020/10/14 04:36
 * @Field :
 */
@Service
public class TableServiceImpl implements TableService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TableServiceImpl.class);
    @Value("${datasource.driver}")
    private String driver;
    @Value("${datasource.url}")
    private String url;
    @Value("${datasource.username}")
    private String username;
    @Value("${datasource.password}")
    private String password;

    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private SqlCheck sqlCheck;
    @Autowired
    private SqlRegistrant sqlRegistrant;

    @Override
    public List<Table> listTables() {
        TableExample tableExample = new TableExample();
        tableExample.createCriteria().andStatusEqualTo(true);
        return tableMapper.selectByExample(tableExample);
    }

    @Override
    public int createTable(TableParam tableParam, HttpServletRequest request) throws TableExistException, ParameterLackException, FieldNotExistException, TableNotExistException {
        sqlCheck.checkCreateTable(tableParam);
        sqlRegistrant.createTable(tableParam, request);
        return 1;
    }

    //删除table需要查看其他data有没有外键指向该table中的data
    @Override
    public int dropTable(int tableId) throws TableNotExistException, ForeignKeyExistException {
        sqlCheck.checkDropTable(tableId);
        sqlRegistrant.dropTable(tableId);
        return 1;
    }

    @Override
    public int renameTable(int tableId, String newName) throws TableNotExistException, TableExistException {
        sqlCheck.checkRenameTable(tableId, newName);
        sqlRegistrant.renameTable(tableId, newName);
        return 1;
    }


}
