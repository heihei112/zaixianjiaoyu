package com.atguigu.guli.service.edu.controller.login;

import com.atguigu.guli.common.base.result.R;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin //跨域
@RestController
@RequestMapping("/user")
public class AdminLoginController {

    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public R login() {
        return R.ok().data("token","admin");
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public R info() {
        return R.ok()
                .data("roles","[admin]")
                .data("name","admin")
                .data("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public R logout(){
        return R.ok();
    }
}