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
 * 骑手接口
 */
@RestController
@RequestMapping("/api/v1/riders")
@Tag(name = "骑手管理")
public class RiderController {

    /**
     * 获取骑手信息
     */
    @GetMapping("/info")
    @PreAuthorize("hasRole('RIDER')")
    @Operation(summary = "获取骑手信息")
    public ResultVO<Map<String, Object>> getRiderInfo(@AuthenticationPrincipal SecurityUser user) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("userId", user.getUserId());
        data.put("identityType", user.getIdentityType());
        return ResultVO.success(data);
    }

    /**
     * 获取待接订单列表
     */
    @GetMapping("/orders/pending")
    @PreAuthorize("hasRole('RIDER')")
    @Operation(summary = "获取待接订单列表")
    public ResultVO<Map<String, Object>> getPendingOrders() {
        Map<String, Object> data = new HashMap<>();
        data.put("orders", new Object[]{});
        return ResultVO.success(data);
    }

    /**
     * 骑手抢单
     */
    @PostMapping("/orders/{orderId}/grab")
    @PreAuthorize("hasRole('RIDER')")
    @Operation(summary = "骑手抢单")
    public ResultVO<String> grabOrder(@PathVariable Long orderId) {
        return ResultVO.success("抢单成功");
    }

    /**
     * 获取骑手配送中的订单
     */
    @GetMapping("/orders/delivering")
    @PreAuthorize("hasRole('RIDER')")
    @Operation(summary = "获取配送中订单")
    public ResultVO<Map<String, Object>> getDeliveringOrders() {
        Map<String, Object> data = new HashMap<>();
        data.put("orders", new Object[]{});
        return ResultVO.success(data);
    }

    /**
     * 完成配送
     */
    @PutMapping("/orders/{orderId}/complete")
    @PreAuthorize("hasRole('RIDER')")
    @Operation(summary = "完成配送")
    public ResultVO<String> completeDelivery(@PathVariable Long orderId) {
        return ResultVO.success("配送完成");
    }
}
