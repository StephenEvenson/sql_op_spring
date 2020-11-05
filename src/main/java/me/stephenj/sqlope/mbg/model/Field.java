package me.stephenj.sqlope.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class Field implements Serializable {
    @ApiModelProperty(value = "字段序号(自增)")
    private Integer id;

    @ApiModelProperty(value = "数据表序号")
    private Integer tableId;

    @ApiModelProperty(value = "字段名")
    private String name;

    @ApiModelProperty(value = "字段描述")
    private String description;

    @ApiModelProperty(value = "字段类型")
    private String type;

    @ApiModelProperty(value = "外键指向的字段")
    private Integer fk;

    @ApiModelProperty(value = "被几个字段锁定了")
    private Integer locked;

    @ApiModelProperty(value = "数据字段创建时间")
    private Date createTime;

    @ApiModelProperty(value = "数据字段修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "是否可用")
    private Boolean status;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFk() {
        return fk;
    }

    public void setFk(Integer fk) {
        this.fk = fk;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", tableId=").append(tableId);
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", type=").append(type);
        sb.append(", fk=").append(fk);
        sb.append(", locked=").append(locked);
        sb.append(", createTime=").append(createTime);
        sb.append(", modifyTime=").append(modifyTime);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}