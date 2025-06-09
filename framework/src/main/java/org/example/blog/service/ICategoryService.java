package org.example.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Category;
import org.example.blog.domain.vo.CategoryVo;

import java.util.List;

/**
 * <p>
 * 分类表 服务类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
public interface ICategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();
}
