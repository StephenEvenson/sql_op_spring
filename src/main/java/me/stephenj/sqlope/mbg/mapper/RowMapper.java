package me.stephenj.sqlope.mbg.mapper;

import java.util.List;
import me.stephenj.sqlope.mbg.model.Row;
import me.stephenj.sqlope.mbg.model.RowExample;
import org.apache.ibatis.annotations.Param;

public interface RowMapper {
    long countByExample(RowExample example);

    int deleteByExample(RowExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Row record);

    int insertSelective(Row record);

    List<Row> selectByExample(RowExample example);

    Row selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Row record, @Param("example") RowExample example);

    int updateByExample(@Param("record") Row record, @Param("example") RowExample example);

    int updateByPrimaryKeySelective(Row record);

    int updateByPrimaryKey(Row record);
}