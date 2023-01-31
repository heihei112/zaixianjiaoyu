package com.atguigu.guli.service.cms.service;

import com.atguigu.guli.service.base.DTO.MemberDto;
import com.atguigu.guli.service.cms.entity.Member;
import com.atguigu.guli.service.cms.entity.vo.LoginVo;
import com.atguigu.guli.service.cms.entity.vo.RegisterVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author limou
 * @since 2022-08-05
 */
public interface MemberService extends IService<Member> {

    void register(RegisterVo registerVo);

    String login(LoginVo loginVo);

    Member getByOneId(String openId);

    MemberDto getMemberDtoById(String memberId);

}
