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
 * 顾客接口
 */
@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "顾客管理")
public class CustomerController {

    /**
     * 获取顾客信息
     */
    @GetMapping("/info")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "获取顾客信息")
    public ResultVO<Map<String, Object>> getCustomerInfo(@AuthenticationPrincipal SecurityUser user) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("userId", user.getUserId());
        data.put("identityType", user.getIdentityType());
        return ResultVO.success(data);
    }

    /**
     * 创建订单
     */
    @PostMapping("/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "创建订单")
    public ResultVO<String> createOrder() {
        return ResultVO.success("订单创建成功");
    }

    /**
     * 获取我的订单列表
     */
    @GetMapping("/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "获取我的订单列表")
    public ResultVO<Map<String, Object>> myOrders() {
        Map<String, Object> data = new HashMap<>();
        data.put("orders", new Object[]{});
        return ResultVO.success(data);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "获取订单详情")
    public ResultVO<Map<String, Object>> getOrderDetail(@PathVariable Long orderId) {
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        return ResultVO.success(data);
    }

    /**
     * 取消订单
     */
    @DeleteMapping("/orders/{orderId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "取消订单")
    public ResultVO<String> cancelOrder(@PathVariable Long orderId) {
        return ResultVO.success("订单已取消");
    }
}
