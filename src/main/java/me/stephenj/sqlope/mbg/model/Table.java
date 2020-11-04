package me.stephenj.sqlope.mbg.model;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

public class Table implements Serializable {
    @ApiModelProperty(value = "数据表序号(自增)")
    private Integer id;

    @ApiModelProperty(value = "数据表名")
    private String name;

    @ApiModelProperty(value = "数据表描述")
    private String description;

    @ApiModelProperty(value = "表格创建人编号")
    private Integer authorId;

    @ApiModelProperty(value = "数据表创建时间")
    private Date createTime;

    @ApiModelProperty(value = "数据表中有效记录的行数")
    private Integer rowCount;

    @ApiModelProperty(value = "外键")
    private Integer fk;

    @ApiModelProperty(value = "是否可用")
    private Boolean status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getRowCount() {
        return rowCount;
    }

    public void setRowCount(Integer rowCount) {
        this.rowCount = rowCount;
    }

    public Integer getFk() {
        return fk;
    }

    public void setFk(Integer fk) {
        this.fk = fk;
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
        sb.append(", name=").append(name);
        sb.append(", description=").append(description);
        sb.append(", authorId=").append(authorId);
        sb.append(", createTime=").append(createTime);
        sb.append(", rowCount=").append(rowCount);
        sb.append(", fk=").append(fk);
        sb.append(", status=").append(status);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}