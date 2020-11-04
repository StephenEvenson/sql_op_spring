package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RowListParam {
    @ApiModelProperty(value = "数据表序号")
    private int tableId;

    @ApiModelProperty(value = "展示的数据字段序号，不填默认展示所有")
    private List<Integer> fields;

    @ApiModelProperty(value = "条件，不填默认无条件")
    private List<ConditionCell> conditions;

    @ApiModelProperty(value = "一页行数，不填默认为10")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "第几页，不填默认为1")
    private Integer pageNum = 1;

    public List<ConditionCell> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionCell> conditions) {
        this.conditions = conditions;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public List<Integer> getFields() {
        return fields;
    }

    public void setFields(List<Integer> fields) {
        this.fields = fields;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
