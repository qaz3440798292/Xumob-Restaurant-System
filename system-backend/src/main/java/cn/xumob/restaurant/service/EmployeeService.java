package cn.xumob.restaurant.service;

import cn.xumob.restaurant.entity.Employee;

import java.util.List;

/**
 * 员工服务接口
 */
public interface EmployeeService {

    /**
     * 根据员工ID查询员工信息
     *
     * @param id 员工ID
     * @return 员工信息
     */
    Employee getEmployeeById(Long id);

    /**
     * 创建员工
     *
     * @param employee 员工信息
     * @return 是否创建成功
     */
    boolean createEmployee(Employee employee);

    /**
     * 查询所有员工
     *
     * @return 员工列表
     */
    List<Employee> getAllEmployees();

    /**
     * 根据用户名查询员工
     *
     * @param username 用户名
     * @return 员工信息
     */
    Employee getEmployeeByUsername(String username);
}
