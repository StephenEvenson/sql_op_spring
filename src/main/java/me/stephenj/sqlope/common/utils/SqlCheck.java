package me.stephenj.sqlope.common.utils;

import me.stephenj.sqlope.Exception.*;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.mbg.mapper.*;
import me.stephenj.sqlope.mbg.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName SqlCheck.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 10:52
 * @Field :
 */
@Service
public class SqlCheck {
    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private RowMapper rowMapper;
    @Autowired
    private DataMapper dataMapper;

    public boolean checkCreateTable(TableParam tableParam) throws TableExistException, TableNotExistException, FieldNotExistException, ParameterLackException {
        TableExample tableExample = new TableExample();
        tableExample.createCriteria().andNameEqualTo(tableParam.getName());
        if (!tableMapper.selectByExample(tableExample).isEmpty()) {
            throw new TableExistException("该数据表已存在");
        }
        Optional<List<FieldDomain>> fieldsOptional = Optional.ofNullable(tableParam.getFields());
        if (!fieldsOptional.isPresent()) {
            throw new ParameterLackException("缺少字段的参数");
        } else if (fieldsOptional.get().isEmpty()){
            throw new ParameterLackException("缺少字段的参数");
        }
        for (FieldDomain fieldDomain : tableParam.getFields()) {
            checkFk(fieldDomain);
        }
        return true;
    }

    public boolean checkFk(FieldDomain fieldDomain) throws TableNotExistException, FieldNotExistException {
        if (fieldDomain.isForeignkey()) {
            Optional<Table> tableOptional = Optional.ofNullable(tableMapper.selectByPrimaryKey(fieldDomain.getTargetTable()));
            if (!tableOptional.isPresent()) {
                throw new TableNotExistException("外键所指向的表格不存在");
            }
            Optional<Field> fieldOptional = Optional.ofNullable(fieldMapper.selectByPrimaryKey(fieldDomain.getTargetField()));
            if (!fieldOptional.isPresent()) {
                throw new FieldNotExistException("外键所指向的字段不存在");
            }
            if (!fieldOptional.get().getTableId().equals(fieldDomain.getTargetTable())) {
                throw new FieldNotExistException("外键所指向的字段不在外键所指向的表格中");
            }
        }
        return true;
    }

    public boolean checkDropTable(int tableId) throws TableNotExistException, ForeignKeyExistException {
        checkTableExist(tableId);
        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria().andTableIdEqualTo(tableId);
        Integer[] fieldIds = Arrays
                .stream(
                fieldMapper.selectByExample(fieldExample)
                .stream()
                .mapToInt(Field::getId)
                .toArray()
                ).boxed()
                .toArray(Integer[]::new);
        for (Field field: fieldMapper.selectByExample(new FieldExample())) {
            if (Arrays.asList(fieldIds).contains(field.getFk())) {
                throw new ForeignKeyExistException("该表无法删除，存在指向该表的外键: 表" +
                        tableMapper.selectByPrimaryKey(field.getTableId()).getName() +
                        "的字段" +
                        field.getName() +
                        "指向该表的字段" +
                        fieldMapper.selectByPrimaryKey(field.getFk()).getName());
            }
        }
        return true;
    }

    public boolean checkRenameTable(int tableId, String tableName) throws TableNotExistException, TableExistException {
        checkTableExist(tableId);
        TableExample tableExample = new TableExample();
        tableExample.createCriteria().andNameEqualTo(tableName);
        Optional<List<Table>> tableOptional = Optional.ofNullable(tableMapper.selectByExample(tableExample));
        if (tableOptional.isPresent()) {
            if (!tableOptional.get().isEmpty()) {
                throw new TableExistException("已存在同名的表格");
            }
        }
        return true;
    }


    public boolean checkTableExist(int tableId) throws TableNotExistException {
        Optional<Table> tableOptional = Optional.ofNullable(tableMapper.selectByPrimaryKey(tableId));
        if (!tableOptional.isPresent() || !tableOptional.get().getStatus()) {
            throw new TableNotExistException("该表不存在或已被删除");
        }
        return true;
    }

    public boolean checkTableExist(Table table) throws TableNotExistException {
        TableExample tableExample = new TableExample();
        tableExample.createCriteria().andNameEqualTo(table.getName()).andStatusEqualTo(true);
        List<Table> tables = tableMapper.selectByExample(tableExample);
        if (tables.size() < 1) {
            throw new TableNotExistException("表格" + table.getName() + "不存在或已被删除");
        }
        BeanUtils.copyProperties(tables.get(0), table);
        return true;
    }

    public boolean checkCreateField(FieldParam fieldParam) throws TableNotExistException, FieldExistException, ParameterLackException, FieldNotExistException {
        checkTableExist(fieldParam.getTableId());

        Optional<List<FieldDomain>> fieldsOptional = Optional.ofNullable(fieldParam.getFieldDomains());
        if (!fieldsOptional.isPresent()) {
            throw new ParameterLackException("缺少字段的参数");
        } else if (fieldsOptional.get().isEmpty()){
            throw new ParameterLackException("缺少字段的参数");
        }

        List<String> fieldNames = fieldParam.getFieldDomains()
                .stream()
                .map(FieldDomain::getName)
                .collect(Collectors.toList());
        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria()
                .andTableIdEqualTo(fieldParam.getTableId())
                .andStatusEqualTo(true)
                .andNameIn(fieldNames);
        Optional<List<Field>> fieldOptional = Optional.ofNullable(fieldMapper.selectByExample(fieldExample));
        if (fieldOptional.isPresent()) {
            if (!fieldOptional.get().isEmpty()) {
                throw new FieldExistException("表格中已经存在同名的字段");
            }
        }
        for (FieldDomain fieldDomain: fieldParam.getFieldDomains()) {
            checkFk(fieldDomain);
        }
        return true;
    }

    public boolean checkDropField(int fieldId) throws ForeignKeyExistException, TableEmptyException, FieldNotExistException {
        checkField(fieldId);

        FieldExample srcFieldExample = new FieldExample();
        srcFieldExample.createCriteria().andStatusEqualTo(true).andFkEqualTo(fieldId);
        List<Field> srcFields = fieldMapper.selectByExample(srcFieldExample);
        if (!srcFields.isEmpty()) {
            Field srcField = srcFields.get(0);
            Table srcTable = tableMapper.selectByPrimaryKey(srcField.getTableId());
            throw new ForeignKeyExistException(srcTable.getName(), srcField.getName());
        }

        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria().andStatusEqualTo(true).andTableIdEqualTo(fieldMapper.selectByPrimaryKey(fieldId).getTableId());
        List<Field> fields = fieldMapper.selectByExample(fieldExample);
        if (fields.size() < 2) {
            throw new TableEmptyException("数据表中的字段个数不能为0");
        }
        return true;
    }

    public boolean checkField(int fieldId) throws FieldNotExistException {
        Optional<Field> fieldOptional = Optional.ofNullable(fieldMapper.selectByPrimaryKey(fieldId));
        if (!fieldOptional.isPresent() || fieldOptional.get().getStatus().equals(false)) {
            throw new FieldNotExistException("该数据列不存在或已被删除");
        }
        return true;
    }

    public boolean checkRow(int rowId) throws RowNotExistException {
        Optional<Row> rowOptional = Optional.ofNullable(rowMapper.selectByPrimaryKey(rowId));
        if (!rowOptional.isPresent() || rowOptional.get().getStatus().equals(false)) {
            throw new RowNotExistException("该数据行不存在或已被删除");
        }
        return true;
    }

    public boolean checkModifyField(FieldWithId fieldWithId) throws FieldNotExistException, DataNotExistException, TableNotExistException {
        checkField(fieldWithId.getId());
        FieldDomain fieldDomain = new FieldDomain();
        BeanUtils.copyProperties(fieldWithId, fieldDomain);
        checkFk(fieldDomain);
        return true;
    }

    public boolean checkAddRow(RowAddParam rowAddParam) throws TableNotExistException, FieldNotExistException {
        checkTableExist(rowAddParam.getTableId());
        for (List<DataDomain> row: rowAddParam.getFields()) {
            for (DataDomain dataDomain : row) {
                checkField(dataDomain.getFieldId());
            }
        }
        return true;
    }

    public boolean checkUpdateRow(RowUpdateParam rowUpdateParam) throws RowNotExistException, FieldNotExistException {
        checkRow(rowUpdateParam.getRowId());
        for (DataDomain dataDomain: rowUpdateParam.getFields()) {
            checkField(dataDomain.getFieldId());
        }
        return true;
    }

    public boolean checkDeleteRow(int rowId) throws RowNotExistException, ForeignKeyExistException {
        checkRow(rowId);
        DataExample dataExample = new DataExample();
        dataExample.createCriteria().andRowIdEqualTo(rowId).andLockedNotEqualTo(-1);
        List<Data> dataList = dataMapper.selectByExample(dataExample);
        if (dataList.size() != 0) {
            Data data = dataList.get(0);
            Data srcData = dataMapper.selectByPrimaryKey(data.getLocked());
            Field srcField = fieldMapper.selectByPrimaryKey(srcData.getFieldId());
            Table srcTable = tableMapper.selectByPrimaryKey(srcData.getTableId());
            throw new ForeignKeyExistException(srcTable.getName(), srcField.getName());
        }
        return true;
    }

    public boolean checkConditions(List<ConditionCell> conditionCells) throws ConditionsException {
        String[] symbols = {"=", "!=", ">", "<", "<=", ">="};
        Optional<List<ConditionCell>> conditionCellOptional = Optional.ofNullable(conditionCells);
        if (!conditionCellOptional.isPresent()) {
            return true;
        }
        for (ConditionCell conditionCell: conditionCells) {
            if (conditionCell.getLogic() < 0 || conditionCell.getLogic() > 1) {
                throw new ConditionsException("逻辑'AND'或者'OR'错误");
            }
            if (!Arrays.asList(symbols).contains(conditionCell.getSymbol())) {
                throw new ConditionsException("符号错误");
            }
        }
        return true;
    }

    public boolean checkConditions(List<ConditionCell> conditionCells, List<Integer> fields) throws ConditionsException, FieldNotExistException {
        checkConditions(conditionCells);
        List<Integer> conditionFields = conditionCells.stream()
                .map(ConditionCell::getFieldId)
                .collect(Collectors.toList());
        if (!fields.containsAll(conditionFields)) {
            throw new FieldNotExistException("条件中存在表中不包含的字段");
        }
        return true;
    }
}
