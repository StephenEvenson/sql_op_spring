package me.stephenj.sqlope.service.impl;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.utils.SqlCheck;
import me.stephenj.sqlope.common.utils.SqlRegistrant;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.mbg.model.Data;
import me.stephenj.sqlope.mbg.model.Field;
import me.stephenj.sqlope.service.FieldService;
import me.stephenj.sqlope.service.RowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @ClassName RcServiceImpl.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 21:14
 * @Field :
 */
@Service
public class RowServiceImpl implements RowService {
    @Autowired
    private SqlCheck sqlCheck;
    @Autowired
    private FieldService fieldService;
    @Autowired
    private SqlRegistrant sqlRegistrant;

    @Override
    public List<List<Data>> listRows(RowListParam rowListParam) throws TableNotExistException, ConditionsException, FieldNotExistException {
        sqlCheck.checkTableExist(rowListParam.getTableId());
        List<Integer> fields;
        if (!Optional.ofNullable(rowListParam.getFields()).isPresent()) {
            fields = fieldService.listFields(rowListParam.getTableId()).stream()
                    .map(Field::getId)
                    .collect(Collectors.toList());
        } else {
            fields = rowListParam.getFields();
        }
        Optional<List<ConditionCell>> conditionCellOptional = Optional.ofNullable(rowListParam.getConditions());
        if (conditionCellOptional.isPresent()) {
            sqlCheck.checkConditions(conditionCellOptional.get(), fields);
        }

        return sqlRegistrant.listRows(rowListParam);
    }

    @Override
    public int addRows(RowAddParam rowAddParam) throws TableNotExistException, FieldNotExistException, ForeignKeyExistException {
        RowAddTemp rowAddTemp = new RowAddTemp();
        BeanUtils.copyProperties(rowAddParam, rowAddTemp);
        sqlCheck.checkAddRow(rowAddTemp);
        sqlRegistrant.addRows(rowAddTemp);
        return 1;
    }

    @Override
    public int updateRow(RowUpdateParam rowUpdateParam) throws RowNotExistException, FieldNotExistException, ForeignKeyExistException {
        RowUpdateTemp rowUpdateTemp = new RowUpdateTemp();
        BeanUtils.copyProperties(rowUpdateParam, rowUpdateTemp);
        sqlCheck.checkUpdateRow(rowUpdateTemp);
        sqlRegistrant.updateRow(rowUpdateTemp);
        return 1;
    }

    @Override
    public int deleteRow(int rowId) throws RowNotExistException, ForeignKeyExistException {
        sqlCheck.checkDeleteRow(rowId);
        sqlRegistrant.deleteRow(rowId);
        return 1;
    }
}
