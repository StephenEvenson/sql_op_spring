package me.stephenj.sqlope.service;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.domain.FieldParam;
import me.stephenj.sqlope.domain.FieldWithId;
import me.stephenj.sqlope.mbg.model.Field;

import java.util.List;

/**
 * @InterfaceName FieldService.java
 * @Description
 * @author 张润天
 * @Time 2020/11/3 23:36
 * @Field :
 */
public interface FieldService {
    List<Field> listFields(int tableId) throws TableNotExistException;

    int createField(FieldParam fieldParam) throws TableNotExistException, FieldExistException, ParameterLackException, FieldNotExistException;

    int dropField(int fieldId) throws ForeignKeyExistException, TableEmptyException, FieldNotExistException;

    int modifyField(FieldWithId fieldWithId) throws DataNotExistException, TableNotExistException, FieldNotExistException;
}
