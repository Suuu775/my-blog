package org.example.blog.constant;

public class SystemConstants{
    /**
     * 文章是草稿状态
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;
    /**
     * 文章是正常发布状态
     */
    public static final int ARTICLE_STATUS_NORMAL = 0;

    /**
     * 友链审核通过
     */
    public static final int Link_STATUS_NORMAL = 0;

    /**
     * 文章评论
     */
    public static final String ARTICLE_COMMENT = "0";

    /**
     * 友链评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * redis中viewCount数据的key
     */
    public static final String VIEW_CONT  = "viewCount";

    public static final int STATUS_NORMAL = 0;
//    分类正常状态
    public static final String NORMAL = "0";
}
