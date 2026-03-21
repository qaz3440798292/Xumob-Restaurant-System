package cn.xumob.restaurant.controller;

import cn.xumob.restaurant.security.SecurityUser;
import cn.xumob.restaurant.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工后台管理接口
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "员工后台管理")
public class AdminController {

    /**
     * 获取当前登录用户信息 - 任何员工都能访问
     */
    @GetMapping("/auth/info")
    @Operation(summary = "获取当前用户信息")
    public ResultVO<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal SecurityUser user) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("userId", user.getUserId());
        data.put("employeeId", user.getEmployeeId());
        data.put("positionId", user.getPositionId());
        data.put("positionCode", user.getPositionCode());
        data.put("positionName", user.getPositionName());
        data.put("positionLevel", user.getPositionLevel());
        data.put("identityType", user.getIdentityType());
        data.put("authorities", user.getAuthorities());
        return ResultVO.success(data);
    }

    /**
     * 值班主管及以上权限 - 获取值班概要
     */
    @GetMapping("/duty/summary")
    @PreAuthorize("@ss.hasPositionAbove('QSC_SHIFT_LEADER')")
    @Operation(summary = "值班主管及以上 - 获取值班概要")
    public ResultVO<Map<String, Object>> getDutySummary() {
        Map<String, Object> data = new HashMap<>();
        data.put("todayOrders", 128);
        data.put("pendingOrders", 5);
        data.put("activeRiders", 3);
        return ResultVO.success(data);
    }

    /**
     * 值班主管及以上 - 员工管理
     */
    @PostMapping("/employees/manage")
    @PreAuthorize("@ss.hasPositionAbove('QSC_SHIFT_LEADER')")
    @Operation(summary = "值班主管及以上 - 员工管理")
    public ResultVO<String> manageEmployee() {
        return ResultVO.success("员工管理功能");
    }

    /**
     * 顾客体验经理及以上 - 顾客体验管理
     */
    @GetMapping("/customer-experience/stats")
    @PreAuthorize("@ss.hasPositionAbove('CUSTOMER_EXPERIENCE_MANAGER')")
    @Operation(summary = "顾客体验经理及以上 - 顾客体验统计")
    public ResultVO<Map<String, Object>> getCustomerExperienceStats() {
        Map<String, Object> data = new HashMap<>();
        data.put("satisfactionScore", 4.8);
        data.put("complaintCount", 2);
        return ResultVO.success(data);
    }

    /**
     * 餐厅总经理 - 财务报表
     */
    @GetMapping("/financial/report")
    @PreAuthorize("@ss.hasPosition('RESTAURANT_GENERAL_MANAGER')")
    @Operation(summary = "餐厅总经理 - 获取财务报表")
    public ResultVO<Map<String, Object>> getFinancialReport() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalRevenue", 56800.00);
        data.put("totalCost", 23400.00);
        data.put("profit", 33400.00);
        return ResultVO.success(data);
    }

    /**
     * 餐厅总经理 - 人事管理
     */
    @PostMapping("/employees/hire")
    @PreAuthorize("@ss.hasPosition('RESTAURANT_GENERAL_MANAGER')")
    @Operation(summary = "餐厅总经理 - 人事管理")
    public ResultVO<String> hireEmployee() {
        return ResultVO.success("人事管理功能");
    }

    /**
     * 精英员工及以上 - 库存管理
     */
    @GetMapping("/stock/warning")
    @PreAuthorize("@ss.hasPositionAbove('CREW_TRAINER')")
    @Operation(summary = "精英员工及以上 - 获取库存预警")
    public ResultVO<Map<String, Object>> getStockWarning() {
        Map<String, Object> data = new HashMap<>();
        data.put("warningItems", 3);
        return ResultVO.success(data);
    }

    /**
     * 普通员工 - 基本工作
     */
    @GetMapping("/task/list")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @Operation(summary = "员工 - 获取任务列表")
    public ResultVO<Map<String, Object>> getTaskList() {
        Map<String, Object> data = new HashMap<>();
        data.put("tasks", new Object[]{});
        return ResultVO.success(data);
    }

    /**
     * 测试职位权限
     */
    @GetMapping("/auth/check-position")
    @PreAuthorize("@ss.hasPositionAbove('CREW')")
    @Operation(summary = "检查职位权限")
    public ResultVO<Map<String, Object>> checkPosition(@AuthenticationPrincipal SecurityUser user) {
        Map<String, Object> data = new HashMap<>();
        
        Map<String, Boolean> positions = new HashMap<>();
        positions.put("CREW", user.hasPosition("CREW"));
        positions.put("CREW_TRAINER", user.hasPosition("CREW_TRAINER"));
        positions.put("QSC_SHIFT_LEADER", user.hasPosition("QSC_SHIFT_LEADER"));
        positions.put("CUSTOMER_EXPERIENCE_MANAGER", user.hasPosition("CUSTOMER_EXPERIENCE_MANAGER"));
        positions.put("RESTAURANT_GENERAL_MANAGER", user.hasPosition("RESTAURANT_GENERAL_MANAGER"));
        
        data.put("hasPosition", positions);
        data.put("positionLevel", user.getPositionLevel());
        
        return ResultVO.success(data);
    }
}
