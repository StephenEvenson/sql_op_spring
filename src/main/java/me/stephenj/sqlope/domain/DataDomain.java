package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassName DataAddDomain.java
 * @Description
 * @author 张润天
 * @Time 2020/11/4 19:10
 * @Field :
 */
public class DataDomain {

    @ApiModelProperty(value = "数据字段序号")
    private Integer fieldId;

    @ApiModelProperty(value = "数据值")
    private String value;

    private static final long serialVersionUID = 1L;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public DataDomain() {
    }

    public DataDomain(Integer fieldId, String value) {
        this.fieldId = fieldId;
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", value=").append(value);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
