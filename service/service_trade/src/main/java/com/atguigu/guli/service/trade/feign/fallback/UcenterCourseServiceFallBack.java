package com.atguigu.guli.service.trade.feign.fallback;

import com.atguigu.guli.service.base.DTO.MemberDto;
import com.atguigu.guli.service.trade.feign.UcenterCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UcenterCourseServiceFallBack implements UcenterCourseService {
    @Override
    public MemberDto getMemberDtoByMemberId(String memberId) {
        log.info("熔断保护机制");
        return null;
    }
}
