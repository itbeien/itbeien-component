package cn.itbeien.example.controller;


import cn.itbeien.example.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@RestController
public class TestController {

    @Autowired
    private IUserService userService;

    @GetMapping("user")
    public String user() {
        return userService.getUserById(1l).toString();
    }
}
