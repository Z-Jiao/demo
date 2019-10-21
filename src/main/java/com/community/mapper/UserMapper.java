package com.community.mapper;

import com.community.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{account_id},#{token},#{gmt_create},#{gmt_modified},#{avatarUrl})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name = #{name},token = #{token},gmt_modified=#{gmt_modified},avatar_url=#{avatar_url} where id =#{id}")
    void update(User user);
}
