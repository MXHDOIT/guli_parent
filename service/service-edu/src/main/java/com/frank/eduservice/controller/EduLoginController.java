package com.frank.eduservice.controller;

import com.frank.commonutils.R;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin //解决跨域
public class EduLoginController {

    //login
    @PostMapping("/login")
    public R login() {
        return R.ok().data("token","admin");
    }

    //info
    @GetMapping("/info")
    public R info() {
        return R.ok().data("roles","[admin]").data("name","admin")
                .data("avatar","https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363295869,2467511306&fm=26&gp=0.jpg");
    }
}
