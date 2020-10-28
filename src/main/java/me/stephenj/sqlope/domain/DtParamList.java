package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @ClassName DtParam.java
 * @Description
 * @author 张润天
 * @Time 2020/10/20 11:08
 * @Field :
 */
public class DtParamList {

    @ApiModelProperty(value = "数据表序号")
    private int tbId;

    @ApiModelProperty(value = "数据列（多个）")
    private List<DtDomain> dtDomains;

    public int getTbId() {
        return tbId;
    }

    public void setTbId(int tbId) {
        this.tbId = tbId;
    }

    public List<DtDomain> getDtDomains() {
        return dtDomains;
    }

    public void setDtDomains(List<DtDomain> dtDomains) {
        this.dtDomains = dtDomains;
    }
}
