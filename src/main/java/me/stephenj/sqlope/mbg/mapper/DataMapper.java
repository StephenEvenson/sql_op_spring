package me.stephenj.sqlope.mbg.mapper;

import java.util.List;
import me.stephenj.sqlope.mbg.model.Data;
import me.stephenj.sqlope.mbg.model.DataExample;
import org.apache.ibatis.annotations.Param;

public interface DataMapper {
    long countByExample(DataExample example);

    int deleteByExample(DataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Data record);

    int insertSelective(Data record);

    List<Data> selectByExample(DataExample example);

    Data selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Data record, @Param("example") DataExample example);

    int updateByExample(@Param("record") Data record, @Param("example") DataExample example);

    int updateByPrimaryKeySelective(Data record);

    int updateByPrimaryKey(Data record);
}