package org.example.blog.controller;

import org.example.blog.domain.ResponseResult;
import org.example.blog.service.ILinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 友链 前端控制器
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Controller
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private ILinkService linkService;
    @GetMapping("/getAllLink")
    @ResponseBody
    public ResponseResult getAllLink(){
        return linkService.getAllLink();
    }

}
