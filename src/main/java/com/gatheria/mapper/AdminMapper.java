package com.gatheria.mapper;

import com.gatheria.domain.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

  @Select("SELECT * FROM admins WHERE username = #{username}")
  Admin findByUsername(@Param("username") String username);

  @Select("SELECT COUNT(*) > 0 FROM admins WHERE username = #{username}")
  boolean existsByUsername(@Param("username") String username);

  @Insert("INSERT INTO admins (username, password, role) VALUES (#{username}, #{password}, #{role})")
  @Options(useGeneratedKeys = true, keyProperty = "id")
  void insertAdmin(Admin admin);
}
