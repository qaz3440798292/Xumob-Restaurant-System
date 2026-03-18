package cn.xumob.restaurant.mapper;

import cn.xumob.restaurant.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/** 顾客账号Mapper接口 */
@Mapper
public interface SysUserMapper {

    SysUser selectByUserId(Long id);

    SysUser selectByUsername(String username);

    int createUser(SysUser sysUser);

    List<SysUser> selectAllUsers();

}
