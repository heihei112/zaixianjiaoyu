package com.atguigu.guli.service.cms.service.impl;

import com.atguigu.guli.common.base.utils.FormUtils;
import com.atguigu.guli.common.base.utils.JwtInfo;
import com.atguigu.guli.common.base.utils.JwtUtils;
import com.atguigu.guli.common.base.utils.MD5;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.DTO.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.cms.entity.Member;
import com.atguigu.guli.service.cms.entity.vo.LoginVo;
import com.atguigu.guli.service.cms.entity.vo.RegisterVo;
import com.atguigu.guli.service.cms.mapper.MemberMapper;
import com.atguigu.guli.service.cms.service.MemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author limou
 * @since 2022-08-05
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVo registerVo) {


        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();
        String code = registerVo.getCode();

        // 校验参数
        //校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(code)
                || StringUtils.isEmpty(nickname)) {
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        // 校验验证码
        String checkCode = (String)redisTemplate.opsForValue().get(mobile);
        // 判断是否已被注册
        Integer result = baseMapper.selectCount(new QueryWrapper<Member>().eq("mobile", mobile));
        if (result > 0) {
            throw  new GuliException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }
        // 注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDeleted(false);
        member.setAvatar("https://guli-file-7-9.oss-cn-hangzhou.aliyuncs.com/access/2022/16/09/0c2637db-ade1-4194-99dd-dd0c401aadfa.jpg");
        baseMapper.insert(member);
    }

    @Override
    public String login(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        // 校验参数
        if (StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile) || StringUtils.isEmpty(password)){
            throw new GuliException(ResultCodeEnum.PARAM_ERROR);
        }
        // 校验手机号
        Member member = baseMapper.selectOne(new QueryWrapper<Member>().eq("mobile", mobile));
        if (member == null) {
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        // 校验密码
        if (!MD5.encrypt(password).equals(member.getPassword())) {
            throw new GuliException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        // 检验用户是否被禁用
        //检验用户是否被禁用
        if(member.getDisabled()){
            throw new GuliException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }

        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setNickname(member.getNickname());
        jwtInfo.setAvatar(member.getAvatar());
        String jwtToken = JwtUtils.getJwtToken(jwtInfo, 1880);

        return jwtToken;
    }

    @Override
    public Member getByOneId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<Member>().eq("openId",openId));
    }

    @Override
    public MemberDto getMemberDtoById(String memberId) {
        Member member = baseMapper.selectById(memberId);
        MemberDto memberDto = new MemberDto();
        BeanUtils.copyProperties(member,memberDto);
        return memberDto;
    }
}
