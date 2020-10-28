package me.stephenj.sqlope.domain;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RcAddParamList extends TbTemp{

    @ApiModelProperty(value = "数据记录")
    private List<List<ResultCell>> rows;

    public List<List<ResultCell>> getRows() {
        return rows;
    }

    public void setRows(List<List<ResultCell>> rows) {
        this.rows = rows;
    }
}
