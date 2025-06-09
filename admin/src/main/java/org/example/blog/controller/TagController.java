package org.example.blog.controller;

import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.dto.TagListDto;
import org.example.blog.domain.entity.Tag;
import org.example.blog.domain.vo.CategoryVo;
import org.example.blog.domain.vo.PageVo;
import org.example.blog.domain.vo.TagVo;
import org.example.blog.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 标签 前端控制器
 * </p>
 *
 * @author SSJ
 * @since 2025-06-09
 */
@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private ITagService tagService;
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteTag(@PathVariable("id") Long id){
        return tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable("id") Long id){
        return tagService.getTag(id);
    }

    @PutMapping
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllCategory(){
        List<TagVo> list = tagService.listAllCategory();
        return ResponseResult.okResult(list);
    }
}
