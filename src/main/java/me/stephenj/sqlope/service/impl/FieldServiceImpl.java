package me.stephenj.sqlope.service.impl;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.common.utils.*;
import me.stephenj.sqlope.domain.FieldParam;
import me.stephenj.sqlope.domain.FieldWithId;
import me.stephenj.sqlope.mbg.mapper.FieldMapper;
import me.stephenj.sqlope.mbg.model.Field;
import me.stephenj.sqlope.mbg.model.FieldExample;
import me.stephenj.sqlope.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName FieldServiceImpl.java
 * @Description
 * @author 张润天
 * @Time 2020/11/3 23:36
 * @Field :
 */
@Service
public class FieldServiceImpl implements FieldService {
    @Autowired
    private SqlCheck sqlCheck;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private SqlRegistrant sqlRegistrant;

    @Override
    public List<Field> listFields(int tableId) throws TableNotExistException {
        sqlCheck.checkTableExist(tableId);
        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria().andTableIdEqualTo(tableId).andStatusEqualTo(true);
        return fieldMapper.selectByExample(fieldExample);
    }

    @Override
    public int createField(FieldParam fieldParam) throws TableNotExistException, FieldExistException, ParameterLackException, FieldNotExistException {
        sqlCheck.checkCreateField(fieldParam);
        sqlRegistrant.createField(fieldParam);
        return 1;
    }

    @Override
    public int dropField(int fieldId) throws ForeignKeyExistException, TableEmptyException, FieldNotExistException {
        sqlCheck.checkDropField(fieldId);
        sqlRegistrant.dropField(fieldId);
        return 1;
    }

    @Override
    public int modifyField(FieldWithId fieldWithId) throws DataNotExistException, TableNotExistException, FieldNotExistException {
        sqlCheck.checkModifyField(fieldWithId);
        sqlRegistrant.modifyField(fieldWithId);
        return 1;
    }

}
