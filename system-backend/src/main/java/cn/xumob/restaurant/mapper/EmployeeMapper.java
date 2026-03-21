package cn.xumob.restaurant.mapper;

import cn.xumob.restaurant.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 员工 Mapper
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 根据员工ID查询员工信息
     */
    Employee selectByEmployeeId(Long id);

    /**
     * 根据用户名查询员工信息
     */
    Employee selectByUsername(String username);

    /**
     * 创建员工
     */
    int createEmployee(Employee employee);

    /**
     * 查询所有员工
     */
    java.util.List<Employee> selectAllEmployees();
}
