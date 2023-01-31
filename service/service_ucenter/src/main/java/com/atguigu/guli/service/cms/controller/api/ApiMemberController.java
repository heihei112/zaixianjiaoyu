package com.atguigu.guli.service.cms.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.utils.JwtInfo;
import com.atguigu.guli.common.base.utils.JwtUtils;
import com.atguigu.guli.service.base.DTO.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.cms.entity.vo.LoginVo;
import com.atguigu.guli.service.cms.entity.vo.RegisterVo;
import com.atguigu.guli.service.cms.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "会员管理")
//@CrossOrigin
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class ApiMemberController {

    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVo loginVo) {
        String token = memberService.login(loginVo);
        return R.ok().data("token",token);
    }

    @ApiOperation(value = "根据token获取登录信息")
    @GetMapping("get-login-info")
    public R getLoginInfo(HttpServletRequest request) {
        try {
            JwtInfo jwtToken = JwtUtils.getMemberIdByJwtToken(request);
            return R.ok().data("userInfo",jwtToken);
        }catch (Exception e) {
            log.error("解析用户失败"+ e.getMessage());
            throw new GuliException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }

    }

    @ApiOperation(value = "根据会员id查询会员信息")
    @GetMapping("inner/get-member-dto/{memberId}")
    public MemberDto getMemberDtoById(@PathVariable("memberId") String memberId){
         MemberDto memberDto = memberService.getMemberDtoById(memberId);
         return memberDto;
    }
}