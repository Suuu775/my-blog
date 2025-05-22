package org.example.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.blog.constant.SystemConstants;
import org.example.blog.dao.LinkMapper;
import org.example.blog.domain.ResponseResult;
import org.example.blog.domain.entity.Link;
import org.example.blog.domain.vo.LinkVo;
import org.example.blog.service.ILinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 友链 服务实现类
 * </p>
 *
 * @author SSJ
 * @since 2025-05-20
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

    @Autowired
    LinkMapper linkMapper;

    @Override
    public ResponseResult getAllLink() {
//        只展示审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.Link_STATUS_NORMAL);

        List<Link> links = linkMapper.selectList(queryWrapper);

        ArrayList < LinkVo> linkVos = new ArrayList<>();
        for (Link link :links){
            LinkVo linkVo = new LinkVo();
            BeanUtils.copyProperties(link,linkVo);
            linkVos.add(linkVo);
        }
    return ResponseResult.okResult(linkVos);
    }
}
