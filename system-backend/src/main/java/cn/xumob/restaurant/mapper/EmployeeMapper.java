package cn.xumob.restaurant.mapper;

import cn.xumob.restaurant.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 员工 Mapper 接口
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 根据员工ID查询员工信息
     *
     * @param id 员工ID
     * @return 员工信息
     */
    Employee selectByEmployeeId(Long id);

    /**
     * 根据用户ID查询员工信息
     *
     * @param userId 用户ID
     * @return 员工信息
     */
    Employee selectByEmployeeUserId(Long userId);

    /**
     * 创建员工
     *
     * @param employee 员工信息
     * @return 影响的行数
     */
    int createEmployee(Employee employee);

    /**
     * 查询所有员工
     *
     * @return 员工列表
     */
    List<Employee> selectAllEmployees();

    /**
     * 根据用户名查询员工
     *
     * @param username 用户名
     * @return 员工信息
     */
    Employee selectEmployeeByUsername(String username);
}
