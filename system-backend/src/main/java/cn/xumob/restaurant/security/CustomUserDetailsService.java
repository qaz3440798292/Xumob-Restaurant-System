package cn.xumob.restaurant.security;

import cn.xumob.restaurant.entity.Employee;
import cn.xumob.restaurant.entity.Position;
import cn.xumob.restaurant.entity.Rider;
import cn.xumob.restaurant.entity.SysUser;
import cn.xumob.restaurant.mapper.EmployeeMapper;
import cn.xumob.restaurant.mapper.PositionMapper;
import cn.xumob.restaurant.mapper.RiderMapper;
import cn.xumob.restaurant.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户详情服务 - 支持多身份登录
 * 
 * 登录类型由前端传入：EMPLOYEE(员工), RIDER(骑手), CUSTOMER(顾客)
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final EmployeeMapper employeeMapper;
    private final RiderMapper riderMapper;
    private final PositionMapper positionMapper;

    /**
     * 根据登录类型加载用户
     * 
     * @param username 用户名
     * @param loginType 登录类型：EMPLOYEE, RIDER, CUSTOMER
     */
    public UserDetails loadUserByUsername(String username, String loginType) throws UsernameNotFoundException {
        // 根据登录类型查询对应表
        return switch (loginType.toUpperCase()) {
            case "EMPLOYEE" -> loadEmployee(username);
            case "RIDER" -> loadRider(username);
            case "CUSTOMER" -> loadCustomer(username);
            default -> throw new UsernameNotFoundException("无效的登录类型: " + loginType);
        };
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 默认实现，兼容旧代码
        throw new UsernameNotFoundException("请指定登录类型");
    }

    /**
     * 加载员工用户
     */
    private UserDetails loadEmployee(String username) {
        Employee employee = employeeMapper.selectByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("员工账号不存在");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        // 获取职位信息
        Long positionId = null;
        String positionCode = null;
        String positionName = null;
        Integer positionLevel = null;

        if (employee.getPositionId() != null) {
            Position position = positionMapper.selectById(employee.getPositionId());
            if (position != null) {
                positionId = position.getId();
                positionCode = position.getCode();
                positionName = position.getName();
                positionLevel = position.getLevel();

                if (positionCode != null) {
                    authorities.add(new SimpleGrantedAuthority("POSITION_" + positionCode));
                }
            }
        }

        return new SecurityUser(
                employee.getUsername(),
                employee.getPassword(),
                authorities,
                employee.getId(),  // 员工使用自己的ID
                "employee",
                employee.getId(),
                positionId,
                positionCode,
                positionName,
                positionLevel
        );
    }

    /**
     * 加载骑手用户
     */
    private UserDetails loadRider(String username) {
        Rider rider = riderMapper.selectByUsername(username);
        if (rider == null) {
            throw new UsernameNotFoundException("骑手账号不存在");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_RIDER"));

        return new SecurityUser(
                rider.getUsername(),
                rider.getPassword(),
                authorities,
                rider.getId(),  // 骑手使用自己的ID
                "rider",
                rider.getId(),
                null,
                null,
                null,
                null
        );
    }

    /**
     * 加载顾客用户
     */
    private UserDetails loadCustomer(String username) {
        SysUser sysUser = sysUserMapper.selectByUsername(username);
        if (sysUser == null) {
            throw new UsernameNotFoundException("顾客账号不存在");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        return new SecurityUser(
                sysUser.getUsername(),
                sysUser.getPassword(),
                authorities,
                sysUser.getId(),
                "customer",
                null,
                null,
                null,
                null,
                null
        );
    }
}
