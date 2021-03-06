package com.buptyouth.mybatis.dao;

import com.buptyouth.mybatis.model.UserAccess;
import com.buptyouth.mybatis.model.UserAccessExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserAccessMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int countByExample(UserAccessExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int deleteByExample(UserAccessExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer role);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int insert(UserAccess record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int insertSelective(UserAccess record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    List<UserAccess> selectByExample(UserAccessExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    UserAccess selectByPrimaryKey(Integer role);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") UserAccess record, @Param("example") UserAccessExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") UserAccess record, @Param("example") UserAccessExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UserAccess record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table userAccess
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UserAccess record);
}