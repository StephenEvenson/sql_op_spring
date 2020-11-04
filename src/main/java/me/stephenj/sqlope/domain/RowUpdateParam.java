package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RowUpdateParam {
    @ApiModelProperty(value = "数据行序号")
    private Integer rowId;

    @ApiModelProperty(value = "修改的字段")
    private List<DataDomain> fields;

    public RowUpdateParam() {
    }

    public RowUpdateParam(Integer rowId, List<DataDomain> fields) {
        this.rowId = rowId;
        this.fields = fields;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public List<DataDomain> getFields() {
        return fields;
    }

    public void setFields(List<DataDomain> fields) {
        this.fields = fields;
    }

}
