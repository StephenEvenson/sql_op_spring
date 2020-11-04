package me.stephenj.sqlope.service;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.domain.TableParam;
import me.stephenj.sqlope.mbg.model.Table;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @InterfaceName TbService.java
 * @Description
 * @author 张润天
 * @Time 2020/10/13 19:13
 */
public interface TableService {
    List<Table> listTables();

    int createTable(TableParam tableParam, HttpServletRequest request) throws TableExistException, ParameterLackException, FieldNotExistException, TableNotExistException;

    int dropTable(int tableId) throws TableNotExistException, ForeignKeyExistException;

    int renameTable(int tableId, String newName) throws TableNotExistException, TableExistException;
}
