package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.DTO.MemberDto;
import com.atguigu.guli.service.trade.feign.fallback.UcenterCourseServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
@Service
@FeignClient(value = "service-ucenter",fallback = UcenterCourseServiceFallBack.class)
public interface UcenterCourseService {

    @GetMapping("/api/ucenter/member/inner/get-member-dto/{memberId}")
    MemberDto getMemberDtoByMemberId(@PathVariable("memberId") String memberId);
}
