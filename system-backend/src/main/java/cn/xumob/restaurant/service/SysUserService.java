package cn.xumob.restaurant.service;

import cn.xumob.restaurant.entity.SysUser;

import java.util.List;

/** 顾客账号服务接口 */
public interface SysUserService {

    List<SysUser> selectByUserId(Long id);

    List<SysUser> selectByUsername(String username);

    boolean createUser(SysUser sysUser);

    List<SysUser> selectAllUsers();
}
