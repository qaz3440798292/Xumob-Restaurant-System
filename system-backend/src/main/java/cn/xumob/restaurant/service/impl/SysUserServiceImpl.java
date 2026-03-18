package cn.xumob.restaurant.service.impl;

import cn.xumob.restaurant.entity.SysUser;
import cn.xumob.restaurant.mapper.SysUserMapper;
import cn.xumob.restaurant.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 顾客账号服务实现类 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> selectByUserId(Long id) {
        return sysUserMapper.selectByUserId(id);
    }

    @Override
    public List<SysUser> selectByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    @Transactional
    public boolean createUser(SysUser sysUser) {
        int result = sysUserMapper.createUser(sysUser);
        return result > 0;
    }

    @Override
    public List<SysUser> selectAllUsers() {
        return sysUserMapper.selectAllUsers();
    }
}
