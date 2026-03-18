package cn.xumob.restaurant.service.impl;

import cn.xumob.restaurant.entity.Employee;
import cn.xumob.restaurant.mapper.EmployeeMapper;
import cn.xumob.restaurant.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工服务实现类
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeMapper.selectByEmployeeId(id);
    }

    @Override
    public Employee getEmployeeByUserId(Long userId) {
        return employeeMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public boolean createEmployee(Employee employee) {
        int result = employeeMapper.createEmployee(employee);
        return result > 0;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeMapper.selectAllEmployees();
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return employeeMapper.selectByUsername(username);
    }
}
