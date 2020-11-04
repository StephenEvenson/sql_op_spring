package me.stephenj.sqlope.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class Data implements Serializable {
    @ApiModelProperty(value = "数据序号(自增)")
    private Integer id;

    @ApiModelProperty(value = "数据表序号")
    private Integer tableId;

    @ApiModelProperty(value = "数据字段序号")
    private Integer fieldId;

    @ApiModelProperty(value = "数据行序号")
    private Integer rowId;

    @ApiModelProperty(value = "数据值")
    private String value;

    @ApiModelProperty(value = "被哪个数据锁定了")
    private Integer locked;

    @ApiModelProperty(value = "数据字段创建时间")
    private Date createTime;

    @ApiModelProperty(value = "数据字段修改时间")
    private Date modifyTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public Integer getRowId() {
        return rowId;
    }

    public void setRowId(Integer rowId) {
        this.rowId = rowId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tableId=").append(tableId);
        sb.append(", fieldId=").append(fieldId);
        sb.append(", rowId=").append(rowId);
        sb.append(", value=").append(value);
        sb.append(", locked=").append(locked);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}