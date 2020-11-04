package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

public class ConditionCell {
    @ApiModelProperty(value = "字段序号")
    private Integer fieldId;

    @ApiModelProperty(value = "值")
    private String value;

    @ApiModelProperty(value = "符号（包括 =, !=, >, <, <=, >=）")
    private String symbol;

    @ApiModelProperty(value = "逻辑关系（0->AND, 1->OR")
    private int logic;

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }
}
