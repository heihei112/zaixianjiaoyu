package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.DTO.CourseDto;
import com.atguigu.guli.service.base.DTO.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.feign.EduCourseService;
import com.atguigu.guli.service.trade.feign.UcenterCourseService;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.utils.OrderNoUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-08-07
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UcenterCourseService ucenterCourseService;

    @Autowired
    private EduCourseService eduCourseService;

    @Override
    public String saveOrder(String courseId, String memberId) {

        // 查询当前用户是否存在订单
        Order orderExist = baseMapper.selectOne(new QueryWrapper<Order>().eq("course_id", courseId)
                .eq("member_id", memberId));
        if (orderExist!=null){
            return orderExist.getId();
        }
        // 查询课程信息
        CourseDto courseDto = eduCourseService.getCourseDtoById(courseId);
        if (courseDto==null){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        // 查询用户信息
        MemberDto memberDto = ucenterCourseService.getMemberDtoByMemberId(memberId);
        if (memberDto == null) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        // 创建订单
        //创建订单
        Order order = new Order();
        order.setOrderNo(OrderNoUtils.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        order.setTeacherName(courseDto.getTeacherName());
        order.setTotalFee(courseDto.getPrice().multiply(new BigDecimal(100)));//分
        order.setMemberId(memberId);
        order.setMobile(memberDto.getMobile());
        order.setNickname(memberDto.getNickname());
        order.setStatus(0);//未支付
        order.setPayType(1);//微信支付
        baseMapper.insert(order);
        return order.getId();
    }

    @Override
    public Order getByOrderInfo(String orderId, String memberId) {
        Order order = baseMapper.selectOne(new QueryWrapper<Order>().eq("id", orderId).eq("member_id", memberId));
        return order;
    }

    @Override
    public Boolean isBuyByCourseId(String courseId, String id) {
        Integer integer = baseMapper.selectCount(new QueryWrapper<Order>().eq("member_id", id).eq("course_id", courseId).eq("status", 1));
        return integer > 0;
    }
}
