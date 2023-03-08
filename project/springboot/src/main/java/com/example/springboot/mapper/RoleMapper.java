package com.example.springboot.mapper;

import com.example.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 李兆年
 * @since 2022-09-12
 */
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select id from sys_role where name = #{flag}")
    Integer selectByFlag(@Param("flag") String name);
}
