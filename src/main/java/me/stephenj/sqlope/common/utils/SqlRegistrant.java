package me.stephenj.sqlope.common.utils;

import me.stephenj.sqlope.Exception.FieldNotExistException;
import me.stephenj.sqlope.Exception.ForeignKeyExistException;
import me.stephenj.sqlope.Exception.RowNotExistException;
import me.stephenj.sqlope.Exception.TableNotExistException;
import me.stephenj.sqlope.domain.*;
import me.stephenj.sqlope.mbg.mapper.*;
import me.stephenj.sqlope.mbg.model.*;
import me.stephenj.sqlope.service.RowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SqlRegistrant {

    @Autowired
    private TableMapper tableMapper;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private RowMapper rowMapper;
    @Autowired
    private RowService rowService;

    public void createTable(TableParam tableParam, HttpServletRequest request) {
        Table table = new Table();
        BeanUtils.copyProperties(tableParam, table);
        table.setAuthorId(userUtils.getIdByToken(request));
        TableExample tableExample = new TableExample();
        tableExample.createCriteria().andNameEqualTo(tableParam.getName());
        table.setFk((int) tableParam.getFields().stream().filter(FieldDomain::isForeignkey).count());
        tableMapper.insertSelective(table);

        FieldParam fieldParam = new FieldParam();
        fieldParam.setTableId(table.getId());
        fieldParam.setFieldDomains(tableParam.getFields());
        createField(fieldParam);
    }

    public void setTableLocked(int targetTableId) {
        Table targetTable = tableMapper.selectByPrimaryKey(targetTableId);
        targetTable.setLocked(targetTable.getLocked() + 1);
        tableMapper.updateByPrimaryKeySelective(targetTable);
    }

    public void setTableUnlocked(int targetTableId) {
        Table targetTable = tableMapper.selectByPrimaryKey(targetTableId);
        targetTable.setLocked(targetTable.getLocked() - 1);
        tableMapper.updateByPrimaryKeySelective(targetTable);
    }

    public void dropTable(int tableId) {
        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria().andTableIdEqualTo(tableId).andStatusEqualTo(true);
        Field field = new Field();
        field.setStatus(false);
        Optional<List<Field>> fieldOptional = Optional.ofNullable(fieldMapper.selectByExample(fieldExample));
        fieldOptional.ifPresent(fields ->
                fields.forEach(f -> {
                    setFieldUnlocked(f.getFk());
                    deleteData(f.getId(), null);
        }));
        fieldMapper.updateByExampleSelective(field, fieldExample);

        RowExample rowExample = new RowExample();
        rowExample.createCriteria().andTableIdEqualTo(tableId).andStatusEqualTo(true);
        Row row = new Row();
        row.setStatus(false);
        rowMapper.updateByExampleSelective(row, rowExample);

        Table table = new Table();
        table.setStatus(false);
        table.setId(tableId);
        tableMapper.updateByPrimaryKeySelective(table);
    }

    public void renameTable(int tableId, String tableName) {
        Table table = new Table();
        table.setId(tableId);
        table.setName(tableName);
        tableMapper.updateByPrimaryKeySelective(table);
    }

    public void setFieldLocked(int targetFieldId) {
        Field targetField = fieldMapper.selectByPrimaryKey(targetFieldId);
        targetField.setLocked(targetField.getLocked() + 1);
        fieldMapper.updateByPrimaryKeySelective(targetField);
        setTableLocked(targetField.getTableId());
    }

    public void setFieldUnlocked(int targetFieldId) {
        if (targetFieldId > 0) {
            Field targetField = fieldMapper.selectByPrimaryKey(targetFieldId);
            targetField.setLocked(targetField.getLocked() - 1);
            fieldMapper.updateByPrimaryKeySelective(targetField);
            setTableUnlocked(targetField.getTableId());
        }
    }

    public void createField(FieldParam fieldParam) {
        int tableId = fieldParam.getTableId();
        for (FieldDomain fieldDomain: fieldParam.getFieldDomains()) {
            Field field = new Field();
            BeanUtils.copyProperties(fieldDomain, field);
            field.setType(fieldDomain.getType().name());
            field.setTableId(tableId);
            if (fieldDomain.isForeignkey()) {
                field.setFk(fieldDomain.getTargetField());
                setFieldLocked(fieldDomain.getTargetField());
            }
            fieldMapper.insert(field);
        }
    }

    public void dropField(int fieldId) {
        Field field = fieldMapper.selectByPrimaryKey(fieldId);
        field.setStatus(false);
        fieldMapper.updateByPrimaryKeySelective(field);
        setFieldUnlocked(field.getFk());

        deleteData(fieldId, null);
    }

    public void modifyField(FieldWithId fieldWithId) {
        Field oldField = fieldMapper.selectByPrimaryKey(fieldWithId.getId());
        Field field = new Field();
        BeanUtils.copyProperties(fieldWithId, field);
        field.setType(fieldWithId.getType().name());
        if (fieldWithId.isForeignkey() && oldField.getFk() != fieldWithId.getTargetField()) {
            field.setFk(fieldWithId.getTargetField());
            if (oldField.getFk() > 0) {
                setFieldUnlocked(oldField.getFk());
            }
            setTableLocked(fieldWithId.getTargetField());
        } else if (oldField.getFk() > 0) {
            setFieldUnlocked(oldField.getFk());
            field.setFk(-1);
        }
        field.setModifyTime(new Date(System.currentTimeMillis()));
        fieldMapper.updateByPrimaryKeySelective(field);
    }

    public List<List<Data>> listRows(RowListParam rowListParam) {
        Table table = tableMapper.selectByPrimaryKey(rowListParam.getTableId());
        int pageSize = rowListParam.getPageSize();
        int pageNum = rowListParam.getPageNum();
        int rowCount = table.getRowCount();
        if ((pageNum-1)*pageSize > rowCount) {
            return null;
        }
        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria()
                .andTableIdEqualTo(rowListParam.getTableId())
                .andIdIn(rowListParam.getFields())
                .andStatusEqualTo(true);
        List<Integer> fields = fieldMapper.selectByExample(fieldExample)
                .stream()
                .map(Field::getId)
                .collect(Collectors.toList());

        List<Integer> rows;
        if (rowListParam.getConditions() == null || rowListParam.getConditions().isEmpty()) {
            RowExample rowExample = new RowExample();
            rowExample.createCriteria()
                    .andTableIdEqualTo(rowListParam.getTableId())
                    .andStatusEqualTo(true);
            rows = rowMapper.selectByExample(rowExample)
                    .stream()
                    .map(Row::getId)
                    .collect(Collectors.toList());
        } else {
            Set<Integer> rowSet = new HashSet<>();
            for (ConditionCell conditionCell : rowListParam.getConditions()) {
                DataExample dataExample = new DataExample();
                dataExample.createCriteria()
                        .andFieldIdEqualTo(conditionCell.getFieldId())
                        .andValueAnyTo(conditionCell.getSymbol(), conditionCell.getValue());
                List<Integer> data = dataMapper.selectByExample(dataExample).stream().map(Data::getRowId).collect(Collectors.toList());
                if (conditionCell.getLogic() == 0) {
                    rowSet.addAll(data);
                } else {
                    rowSet.retainAll(data);
                }
            }
            rows = new ArrayList<>(rowSet);
        }

        List<List<Data>> tableContent = new ArrayList<>();
        for (int i = (pageNum-1)*pageSize; i<rows.size() && i<pageNum*pageSize; i++) {
            DataExample dataExample = new DataExample();
            dataExample.createCriteria()
                    .andFieldIdIn(fields)
                    .andRowIdEqualTo(rows.get(i));
            tableContent.add(dataMapper.selectByExample(dataExample));
        }

        return tableContent;
    }

    public void setDataLocked(int targetDataId) {
        if (targetDataId > 0) {
            Data data = dataMapper.selectByPrimaryKey(targetDataId);
            data.setLocked(data.getLocked() + 1);
            dataMapper.updateByPrimaryKeySelective(data);
        }
    }

    public void setDataUnlocked(int targetDataId) {
        if (targetDataId > 0) {
            Data data = dataMapper.selectByPrimaryKey(targetDataId);
            data.setLocked(data.getLocked() - 1);
            dataMapper.updateByPrimaryKeySelective(data);
        }
    }

    public void addRows(RowAddTemp rowAddTemp) {
        int tableId = rowAddTemp.getTableId();
        HashMap<String, Integer> targetDataIdMap = rowAddTemp.getTargetDataIdMap();
        for (List<DataDomain> dataRow: rowAddTemp.getFields()) {
            Row row = new Row();
            row.setTableId(tableId);
            rowMapper.insertSelective(row);
            for (DataDomain dataDomain : dataRow) {
                int targetDataId = targetDataIdMap.get(dataDomain.getValue());
                Data data = new Data();
                BeanUtils.copyProperties(dataDomain, data);
                data.setRowId(row.getId());
                data.setTableId(tableId);
                data.setFk(targetDataId);
                setDataLocked(targetDataId);
                dataMapper.insertSelective(data);
            }
        }
    }

    public void updateRow(RowUpdateTemp rowUpdateTemp) {
        int rowId = rowUpdateTemp.getRowId();
        for (DataDomain dataDomain: rowUpdateTemp.getFields()) {
            int targetDataId = rowUpdateTemp.getTargetDataIdMap().get(dataDomain.getValue());
            DataExample dataExample = new DataExample();
            dataExample.createCriteria()
                    .andFieldIdEqualTo(dataDomain.getFieldId())
                    .andRowIdEqualTo(rowId);
            Data data = new Data();
            data.setValue(dataDomain.getValue());
            data.setModifyTime(new Date(System.currentTimeMillis()));
            data.setFk(targetDataId);
            Data oldData = dataMapper.selectByExample(dataExample).get(0);
            setDataUnlocked(oldData.getFk());
            setDataLocked(targetDataId);
            dataMapper.updateByExampleSelective(data, dataExample);
        }
        Row row = rowMapper.selectByPrimaryKey(rowId);
        row.setModifyTime(new Date(System.currentTimeMillis()));
    }

    public void deleteRow(int rowId) {
        Row row = rowMapper.selectByPrimaryKey(rowId);
        row.setStatus(false);
        rowMapper.updateByPrimaryKeySelective(row);

        deleteData(null, rowId);
    }

    public void deleteData(Integer fieldId, Integer rowId) {
        DataExample dataExample = new DataExample();
        if (rowId == null) {
            dataExample.createCriteria().andFieldIdEqualTo(rowId).andStatusEqualTo(true);
        } else {
            dataExample.createCriteria().andRowIdEqualTo(rowId).andStatusEqualTo(true);
        }
        List<Data> dataList = dataMapper.selectByExample(dataExample);
        dataList.forEach(data -> {
            setDataUnlocked(data.getFk());
            data.setStatus(false);
        });
    }

    public void importExcel(List<Map<String, Object>> excel, Table table) throws FieldNotExistException, TableNotExistException, RowNotExistException, ForeignKeyExistException {
        Set<String> fieldNames = excel.get(0).keySet();

        FieldExample fieldExample = new FieldExample();
        fieldExample.createCriteria()
                .andTableIdEqualTo(table.getId())
                .andStatusEqualTo(true)
                .andNameIn(new ArrayList<>(fieldNames));
        List<Field> fields = fieldMapper.selectByExample(fieldExample);
        if (excel.get(0).get("_id") == null) {
            List<List<DataDomain>> dataDomains = new ArrayList<>();
            for (Map<String, Object> row: excel) {
                List<DataDomain> dataRow = new ArrayList<>();
                fields.forEach(field -> dataRow.add(new DataDomain(field.getId(), String.valueOf(row.get(field.getName())))));
                dataDomains.add(dataRow);
            }
            RowAddParam rowAddParam = new RowAddParam(table.getId(), dataDomains);
            rowService.addRows(rowAddParam);
            return;
        }

        List<Integer> rowIds = excel.stream()
                .map(map -> Integer.parseInt(String.valueOf(map.get("_id"))))
                .collect(Collectors.toList());
        RowExample rowExample = new RowExample();
        rowExample.createCriteria().andTableIdEqualTo(table.getId()).andStatusEqualTo(true).andIdIn(rowIds);
        List<Row> rows = rowMapper.selectByExample(rowExample);
        List<Integer> updateRowIds = rows.stream().map(Row::getId).collect(Collectors.toList());
        List<List<DataDomain>> dataAdd = new ArrayList<>();
        for (Map<String, Object> row: excel) {
            List<DataDomain> dataRow = new ArrayList<>();
            fields.forEach(field -> dataRow.add(new DataDomain(field.getId(), String.valueOf(row.get(field.getName())))));
            if (updateRowIds.contains(row.get("_id"))) {
                RowUpdateParam rowUpdateParam = new RowUpdateParam(Integer.parseInt(String.valueOf(row.get("_id"))), dataRow);
                rowService.updateRow(rowUpdateParam);
            } else {
                dataAdd.add(dataRow);
            }
        }
        if (dataAdd.size() > 0) {
            RowAddParam rowAddParam = new RowAddParam(table.getId(), dataAdd);
            rowService.addRows(rowAddParam);
        }
    }
}
