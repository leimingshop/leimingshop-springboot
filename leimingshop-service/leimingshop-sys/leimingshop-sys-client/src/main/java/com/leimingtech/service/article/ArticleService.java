/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.article.ArticleDTO;
import com.leimingtech.dto.article.ArticleSaveDTO;
import com.leimingtech.dto.article.ArticleUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 文章管理
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */

public interface ArticleService {

    /**
     * 分页条件查询
     *
     * @param params
     * @author xuzhch
     */


    PageData<ArticleDTO> page(@RequestParam("params") Map<String, Object> params);

    /*
     * 根据ID查询文章详情
     * @param [id]
     * @author xuzhch
     */

    ArticleDTO get(Long id);

    /*
     * 修改文章
     * @param [dto]
     * @author xuzhch
     */

    void update(@RequestBody ArticleUpdateDTO dto);

    /**
     * 保存文章
     *
     * @param dto
     * @author xuzhch
     */

    void save(@RequestBody ArticleSaveDTO dto);

    /**
     * 根据ID删除文章
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出文章
     *
     * @param params
     * @return
     */

    List<ArticleDTO> list(@RequestParam Map<String, Object> params);
}