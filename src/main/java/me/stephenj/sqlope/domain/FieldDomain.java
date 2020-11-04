package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName FieldDomain.java
 * @Description
 * @author 张润天
 * @Time 2020/11/4 00:30
 * @Field :
 */
public class FieldDomain {

    @ApiModelProperty(value = "字段名称")
    private String name;

    @ApiModelProperty(value = "字段类型")
    private TypeEnum type;

    @ApiModelProperty(value = "字段描述")
    private String description;

    @ApiModelProperty(value = "true->有外键;false->无外键")
    private boolean foreignkey;

    @ApiModelProperty(value = "外键指向的数据表")
    private int targetTable;

    @ApiModelProperty(value = "外键指向的字段")
    private int targetField;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isForeignkey() {
        return foreignkey;
    }

    public void setForeignkey(boolean foreignkey) {
        this.foreignkey = foreignkey;
    }

    public int getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(int targetTable) {
        this.targetTable = targetTable;
    }

    public int getTargetField() {
        return targetField;
    }

    public void setTargetField(int targetField) {
        this.targetField = targetField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }
}
