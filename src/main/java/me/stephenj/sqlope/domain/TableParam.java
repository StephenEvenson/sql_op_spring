package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class TableParam {
    @ApiModelProperty(value = "数据表名称")
    private String name;

    @ApiModelProperty(value = "数据列列表")
    private List<FieldDomain> fields;

    @ApiModelProperty(value = "数据表描述")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldDomain> getFields() {
        return fields;
    }

    public void setFields(List<FieldDomain> fields) {
        this.fields = fields;
    }

}
