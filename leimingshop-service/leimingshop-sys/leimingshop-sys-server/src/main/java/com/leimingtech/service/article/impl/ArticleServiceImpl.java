/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.article.ArticleDao;
import com.leimingtech.dto.article.ArticleDTO;
import com.leimingtech.dto.article.ArticleSaveDTO;
import com.leimingtech.dto.article.ArticleUpdateDTO;
import com.leimingtech.entity.article.ArticleEntity;
import com.leimingtech.service.article.ArticleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 文章管理
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Service
public class ArticleServiceImpl extends CrudServiceImpl<ArticleDao, ArticleEntity, ArticleDTO> implements ArticleService {

    @Resource
    private ArticleDao articleDao;

    /**
     * 条件构造器
     *
     * @param params
     * @author xuzhch
     */
    @Override
    public QueryWrapper<ArticleEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ArticleEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 查询文章详情
     *
     * @param id
     * @author xuzhch
     */

    @Override

    public ArticleDTO get(Long id) {
        return articleDao.selectDetail(id);
    }

    /**
     * 分页条件查询
     *
     * @param params
     * @author xuzhch
     */
    @Override

    public PageData<ArticleDTO> page(@RequestParam Map<String, Object> params) {
        IPage<ArticleEntity> page = getPage(params, "la.sort", false);
        List<ArticleDTO> articleList = articleDao.selectArticleList(params);
        return new PageData<>(articleList, page.getTotal());
    }

    /**
     * 修改文章
     *
     * @param dto 文章修改实体
     * @author xuzhch
     */
    @Override

    public void update(@RequestBody ArticleUpdateDTO dto) {
        ArticleEntity articleEntity = ConvertUtils.sourceToTarget(dto, ArticleEntity.class);
        articleDao.updateById(articleEntity);

    }

    /**
     * 保存文章
     *
     * @param dto 文章保存实体
     * @author xuzhch
     */
    @Override

    public void save(@RequestBody ArticleSaveDTO dto) {
        ArticleEntity articleEntity = ConvertUtils.sourceToTarget(dto, ArticleEntity.class);
        articleDao.insert(articleEntity);
    }

    /**
     * 根据ID删除文章
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出文章
     *
     * @param params
     * @return
     */

    @Override
    public List<ArticleDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

}