package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.utils.JwtInfo;
import com.atguigu.guli.common.base.utils.JwtUtils;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import io.jsonwebtoken.JwtHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

//@CrossOrigin
@RestController
@RequestMapping("/api/trade/order")
@Api(tags = "网站订单管理")
@Slf4j
public class ApiOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping("auth/save/{courseId}")
    public R save(@PathVariable("courseId") String courseId, HttpServletRequest request){
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        String orderId = orderService.saveOrder(courseId,jwtInfo.getId());
        return R.ok().data("orderId",orderId);
    }

    @ApiOperation("获取订单")
    @GetMapping("auth/get/{orderId}")
    public R get(@PathVariable("orderId") String orderId ,HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Order order =  orderService.getByOrderInfo(orderId,jwtInfo.getId());
        return R.ok().data("item",order);
    }

    @ApiOperation( "判断课程是否购买")
    @GetMapping("auth/is-buy/{courseId}")
    public R isBuyCourseById(@PathVariable String courseId,HttpServletRequest request) {
        JwtInfo jwtInfo = JwtUtils.getMemberIdByJwtToken(request);
        Boolean isBuy = orderService.isBuyByCourseId(courseId,jwtInfo.getId());
        return R.ok().data("isBuy",isBuy);
    }

}
