package me.stephenj.sqlope.mbg.mapper;

import java.util.List;
import me.stephenj.sqlope.mbg.model.Field;
import me.stephenj.sqlope.mbg.model.FieldExample;
import org.apache.ibatis.annotations.Param;

public interface FieldMapper {
    long countByExample(FieldExample example);

    int deleteByExample(FieldExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Field record);

    int insertSelective(Field record);

    List<Field> selectByExample(FieldExample example);

    Field selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Field record, @Param("example") FieldExample example);

    int updateByExample(@Param("record") Field record, @Param("example") FieldExample example);

    int updateByPrimaryKeySelective(Field record);

    int updateByPrimaryKey(Field record);
}