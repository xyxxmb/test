package com.buptyouth.mybatis.dao;

import com.buptyouth.mybatis.model.Apply;
import com.buptyouth.mybatis.model.ApplyExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ApplyMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int countByExample(ApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int deleteByExample(ApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer applyId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int insert(Apply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int insertSelective(Apply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    List<Apply> selectByExample(ApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    Apply selectByPrimaryKey(Integer applyId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") Apply record, @Param("example") ApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") Apply record, @Param("example") ApplyExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(Apply record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table apply
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(Apply record);
}