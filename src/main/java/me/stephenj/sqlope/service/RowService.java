package me.stephenj.sqlope.service;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.mbg.model.Data;

import java.util.List;

/**
 * @InterfaceName RcService.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 21:14
 * @Field :
 */
public interface RowService {
    List<List<Data>> listRows(RowListParam rowListParam) throws TableNotExistException, ConditionsException, FieldNotExistException;

    int addRows(RowAddParam rowAddParam) throws TableNotExistException, FieldNotExistException;

    int updateRow(RowUpdateParam rcUpdateParam) throws RowNotExistException, FieldNotExistException;

    int deleteRow(int rowId) throws RowNotExistException, ForeignKeyExistException;
}
