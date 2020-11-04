package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @ClassName FieldParam.java
 * @Description
 * @author 张润天
 * @Time 2020/11/4 00:19
 * @Field :
 */
public class FieldParam {

    @ApiModelProperty(value = "数据表序号")
    private int tableId;

    @ApiModelProperty(value = "数据列（多个）")
    private List<FieldDomain> fieldDomains;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public List<FieldDomain> getFieldDomains() {
        return fieldDomains;
    }

    public void setFieldDomains(List<FieldDomain> fieldDomains) {
        this.fieldDomains = fieldDomains;
    }
}
