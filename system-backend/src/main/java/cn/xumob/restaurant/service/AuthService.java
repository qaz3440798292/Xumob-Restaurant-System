package cn.xumob.restaurant.service;

import cn.xumob.restaurant.dto.RegisterDTO;
import cn.xumob.restaurant.entity.Employee;
import cn.xumob.restaurant.entity.Rider;
import cn.xumob.restaurant.entity.SysUser;
import cn.xumob.restaurant.mapper.EmployeeMapper;
import cn.xumob.restaurant.mapper.RiderMapper;
import cn.xumob.restaurant.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 认证服务 - 处理注册逻辑
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserMapper sysUserMapper;
    private final EmployeeMapper employeeMapper;
    private final RiderMapper riderMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 注册
     */
    public void register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        String password = passwordEncoder.encode(registerDTO.getPassword());
        String registerType = registerDTO.getRegisterType().toUpperCase();

        // 根据注册类型检查账号是否存在
        switch (registerType) {
            case "CUSTOMER" -> registerCustomer(username, password, registerDTO);
            case "RIDER" -> registerRider(username, password, registerDTO);
            case "EMPLOYEE" -> registerEmployee(username, password, registerDTO);
            default -> throw new RuntimeException("无效的注册类型: " + registerType);
        }
    }

    /**
     * 注册顾客 - 只检查 sys_user 表
     */
    private void registerCustomer(String username, String password, RegisterDTO registerDTO) {
        // 检查账号是否已存在
        if (sysUserMapper.selectByUsername(username) != null) {
            throw new RuntimeException("账号已被注册");
        }

        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(password);
        sysUser.setPhone(registerDTO.getPhone());
        sysUser.setNickname(registerDTO.getName());
        sysUser.setStatus(1);
        sysUser.setCreateTime(LocalDateTime.now());
        sysUser.setUpdateTime(LocalDateTime.now());

        sysUserMapper.createUser(sysUser);
    }

    /**
     * 注册骑手 - 只检查 rider 表
     */
    private void registerRider(String username, String password, RegisterDTO registerDTO) {
        // 检查账号是否已存在
        if (riderMapper.selectByUsername(username) != null) {
            throw new RuntimeException("账号已被注册");
        }

        Rider rider = new Rider();
        rider.setUsername(username);
        rider.setPassword(password);
        rider.setName(registerDTO.getName());
        rider.setPhone(registerDTO.getPhone());
        rider.setStatus(1);
        rider.setCreateTime(LocalDateTime.now());
        rider.setUpdateTime(LocalDateTime.now());

        riderMapper.createRider(rider);
    }

    /**
     * 注册员工 - 只检查 employee 表
     */
    private void registerEmployee(String username, String password, RegisterDTO registerDTO) {
        // 检查账号是否已存在
        if (employeeMapper.selectByUsername(username) != null) {
            throw new RuntimeException("账号已被注册");
        }

        Employee employee = new Employee();
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setName(registerDTO.getName());
        employee.setPhone(registerDTO.getPhone());
        employee.setPositionId(registerDTO.getPositionId());
        employee.setIdCard(registerDTO.getIdCard());
        employee.setEmploymentType(registerDTO.getEmploymentType());

        if (registerDTO.getHireDate() != null && !registerDTO.getHireDate().isEmpty()) {
            employee.setHireDate(LocalDate.parse(registerDTO.getHireDate()));
        }

        employee.setStatus(1);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        employeeMapper.createEmployee(employee);
    }
}
