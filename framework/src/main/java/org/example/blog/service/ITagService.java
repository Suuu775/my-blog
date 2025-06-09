package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.dto.TagListDto;
import org.example.blog.domain.entity.Tag;
import org.example.blog.domain.vo.PageVo;
import org.example.blog.domain.vo.TagVo;

import java.util.List;

/**
 * <p>
 * 标签 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-09
 */
public interface ITagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteTag(Long id);

    ResponseResult getTag(Long id);

    ResponseResult updateTag(Tag tag);

    List<TagVo> listAllCategory();
}
