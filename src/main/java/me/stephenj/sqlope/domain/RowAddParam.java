package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RowAddParam {
    @ApiModelProperty(value = "数据表序号")
    private int tableId;

    @ApiModelProperty(value = "展示的数据字段序号，不填默认展示所有")
    private List<List<DataDomain>> fields;

    public RowAddParam() {
    }

    public RowAddParam(int tableId, List<List<DataDomain>> fields) {
        this.tableId = tableId;
        this.fields = fields;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public List<List<DataDomain>> getFields() {
        return fields;
    }

    public void setFields(List<List<DataDomain>> fields) {
        this.fields = fields;
    }

}
