package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

public class FieldWithId extends FieldDomain{
    @ApiModelProperty(value = "字段序号")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
