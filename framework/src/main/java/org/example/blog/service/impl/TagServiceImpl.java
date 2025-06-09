package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.blog.dao.TagMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.dto.TagListDto;
import org.example.blog.domain.entity.Tag;
import org.example.blog.domain.vo.PageVo;
import org.example.blog.domain.vo.TagVo;
import org.example.blog.enums.AppHttpCodeEnum;
import org.example.blog.exception.SystemException;
import org.example.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 标签 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-06-09
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //封装查询条件
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getRemark,tagListDto.getRemark());
        //分⻚查询
        Page<Tag> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);

        page(page,queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        tagMapper.insert(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteTag(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        tagMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Long id) {
        if (Objects.isNull(id)){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        Tag tag = tagMapper.selectById(id);
        return ResponseResult.okResult(tag);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        tagMapper.updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public List<TagVo> listAllCategory() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }
}
