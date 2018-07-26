package com.king.sso.server.dao;

import com.king.sso.server.core.entity.UserInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Auther: king
 * @Date: 2018/7/26 10:30
 * @Description:
 */
@Mapper
public interface UserInfoDao {

    int insert(@Param("userInfo") UserInfo userInfo);
    int delete(@Param("id") int id);
    int update(@Param("userInfo") UserInfo userInfo);
    List<UserInfo> findAll();
    UserInfo loadById(@Param("id") int id);

    UserInfo findByUsername(@Param("username") String username);
}
