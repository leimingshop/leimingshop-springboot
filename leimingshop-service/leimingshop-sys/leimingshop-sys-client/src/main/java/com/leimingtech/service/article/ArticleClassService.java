/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.article.ArticleClassDTO;
import com.leimingtech.dto.article.ArticleClassSaveDTO;
import com.leimingtech.dto.article.ArticleClassUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 文章分类管理
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */

public interface ArticleClassService {

    PageData<ArticleClassDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 修改文章分类
     *
     * @param dto
     */

    void update(@RequestBody ArticleClassUpdateDTO dto);

    /**
     * 保存文章分类
     *
     * @param dto
     */

    void save(@RequestBody ArticleClassSaveDTO dto);

    /**
     * 根据ID查询子级分类
     *
     * @param id
     * @return
     */

    List<ArticleClassDTO> selectListByParentId(Long id);

    /**
     * 根据ID查询分类名称
     *
     * @param id
     * @return
     */

    String selectNameById(Long id);

    /**
     * 根据ID查询分类详情
     *
     * @param id
     * @return
     */

    ArticleClassDTO get(Long id);

    /**
     * 删除分类
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出分类
     *
     * @param params
     * @return
     */

    List<ArticleClassDTO> list(@RequestParam Map<String, Object> params);
}