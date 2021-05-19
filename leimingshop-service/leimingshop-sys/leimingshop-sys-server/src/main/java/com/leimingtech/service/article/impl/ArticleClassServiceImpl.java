/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.article.ArticleClassDao;
import com.leimingtech.dto.article.ArticleClassDTO;
import com.leimingtech.dto.article.ArticleClassSaveDTO;
import com.leimingtech.dto.article.ArticleClassUpdateDTO;
import com.leimingtech.entity.article.ArticleClassEntity;
import com.leimingtech.service.article.ArticleClassService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 文章分类管理
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Slf4j
@Service
public class ArticleClassServiceImpl extends CrudServiceImpl<ArticleClassDao, ArticleClassEntity, ArticleClassDTO> implements ArticleClassService {

    @Resource
    private ArticleClassDao articleClassDao;

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    @Override
    public QueryWrapper<ArticleClassEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String acParentId = (String) params.get("acParentId");

        QueryWrapper<ArticleClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(acParentId), "ac_parent_id", acParentId);

        return wrapper;
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    @Override
    public PageData<ArticleClassDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 修改文章分类
     *
     * @param dto
     * @return
     */

    @Override
    public void update(@RequestBody ArticleClassUpdateDTO dto) {
        ArticleClassEntity articleClassEntity = ConvertUtils.sourceToTarget(dto, ArticleClassEntity.class);
        articleClassDao.updateById(articleClassEntity);
    }

    /**
     * 保存文章分类
     *
     * @param dto
     * @return
     */

    @Override
    public void save(@RequestBody ArticleClassSaveDTO dto) {
        ArticleClassEntity articleClassEntity = ConvertUtils.sourceToTarget(dto, ArticleClassEntity.class);
        articleClassDao.insert(articleClassEntity);

    }

    /**
     * 根据ID查询子级分类
     *
     * @param id
     * @return
     */

    @Override
    public List<ArticleClassDTO> selectListByParentId(Long id) {
        List<ArticleClassEntity> articleClassEntities = articleClassDao.selectList(new QueryWrapper<ArticleClassEntity>().eq(true, "ac_parent_id", id));
        return ConvertUtils.sourceToTarget(articleClassEntities, ArticleClassDTO.class);
    }

    /**
     * 根据ID查询分类名称
     *
     * @param id
     * @return
     */

    @Override
    public String selectNameById(Long id) {
        ArticleClassEntity articleClassEntity = articleClassDao.selectById(id);
        if (null != articleClassEntity && StringUtils.isNotBlank(articleClassEntity.getAcName())) {
            return articleClassEntity.getAcName();
        }
        return null;
    }

    /**
     * 根据ID查询分类详情
     *
     * @param id
     * @return
     */

    @Override
    public ArticleClassDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 删除分类
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出分类
     *
     * @param params
     * @return
     */

    @Override
    public List<ArticleClassDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }
}