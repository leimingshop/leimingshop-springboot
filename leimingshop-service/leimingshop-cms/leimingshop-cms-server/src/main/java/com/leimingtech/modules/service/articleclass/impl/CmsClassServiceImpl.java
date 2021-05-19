/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.articleclass.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.modules.dao.article.CmsArticleDao;
import com.leimingtech.modules.dao.articleclass.CmsClassDao;
import com.leimingtech.modules.dao.circle.CmsCircleDao;
import com.leimingtech.modules.dto.articleclass.*;
import com.leimingtech.modules.entity.articleclass.CmsClassEntity;
import com.leimingtech.modules.enums.articleclass.ArticleClassErrorCodeEnum;
import com.leimingtech.modules.service.articleclass.CmsClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类管理 CmsClassServiceImpl
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsClassServiceImpl extends BaseServiceImpl<CmsClassDao, CmsClassEntity> implements CmsClassService {

    @Resource
    private CmsClassDao cmsClassDao;

    @Resource
    private CmsArticleDao cmsArticleDao;

    @Resource
    private CmsCircleDao cmsCircleDao;

    @Override

    public PageData<CmsClassPageDTO> page(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsClassPageDTO> page = new Page<>(pageNum, limit);
        List<CmsClassPageDTO> list = baseDao.selectCmsCircleClassList(page, params);
        return new PageData(list, page.getTotal());
    }


    /**
     * 查询所有分类
     *
     * @param params
     * @return
     */
    @Override

    public List<CmsClassListDTO> classList(@RequestParam Map<String, Object> params) {
        List<CmsClassEntity> entityList = cmsClassDao.selectClassList(params);

        return ConvertUtils.sourceToTarget(entityList, CmsClassListDTO.class);
    }

    /**
     * 查询文章最上级分类接口
     *
     * @param params
     * @return
     */
    @Override

    public List<CmsClassListDTO> firstList(@RequestParam Map<String, Object> params) {
        List<CmsClassEntity> entityList = cmsClassDao.selectArticleFirstList();
        return ConvertUtils.sourceToTarget(entityList, CmsClassListDTO.class);
    }


    @Override

    public CmsClassDTO get(Long id) {
        CmsClassEntity entity = baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, CmsClassDTO.class);
    }

    /**
     * 圈子分类新增
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void circleClassSave(@RequestBody CmsClassSaveDTO dto) {
        CmsClassEntity entity = ConvertUtils.sourceToTarget(dto, CmsClassEntity.class);
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setCreator(userName);
        entity.setUpdater(userName);
        entity.setAcLevel(1);
        entity.setAcIdpaths("0");
        entity.setAcCode(1);
        insert(entity);
    }

    /**
     * 文章分类新增
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Map<String, Object> articleClassSave(@RequestBody CmsClassSaveDTO dto) {
        Map<String, Object> res = new HashMap<>(10);
        //同标识分类重名查询
        List<CmsClassEntity> cmsClassEntityList = cmsClassDao.selectByAcName(dto);
        if (cmsClassEntityList.size() != 0) {
            res.put("code", ArticleClassErrorCodeEnum.ACNAME_REPEAT.value());
            res.put("msg", "存在同名分类，创建失败");
            return res;
        }
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        if (Long.valueOf(0L).equals(dto.getAcParentId())) {
            //一级分类
            dto.setAcLevel(1);
        } else {
            //子分类
            CmsClassEntity cmsClassEntity = cmsClassDao.selectById(dto.getAcParentId());
            dto.setAcIdpaths(cmsClassEntity.getAcIdpaths());
            dto.setAcLevel(cmsClassEntity.getAcLevel() + 1);
        }
        CmsClassEntity entity = ConvertUtils.sourceToTarget(dto, CmsClassEntity.class);
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setCreator(userName);
        entity.setUpdater(userName);
        insert(entity);
        //修改acIdpaths
        String parentNode = "0";
        if (parentNode.equals(entity.getAcIdpaths())) {
            entity.setAcIdpaths(entity.getId().toString());
        } else {
            entity.setAcIdpaths(entity.getAcIdpaths() + "," + entity.getId());
        }
        updateById(entity);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody CmsClassDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsClassEntity entity = ConvertUtils.sourceToTarget(dto, CmsClassEntity.class);
        entity.setUpdateDate(date);
        entity.setUpdater(userName);
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        logicDelete(ids);
    }

    /**
     * 圈子分类删除
     *
     * @param ids
     * @return
     */
    @Override

    public Map<String, Object> deleteQzClassIds(@RequestBody Long[] ids) {
        Map<String, Object> res = new HashMap<>(10);
        //判断待删除分类是否有关联话题和关联文章
        //根据分类id查询关联话题名称
        List<String> topicList = cmsCircleDao.findCountByClassId(ids);
        //根据分类id查询关联文章名称
        List<String> articleList = cmsArticleDao.findCountByClassId(ids);
        //下级分类和关联文章都没有则删除分类
        if (!CollectionUtils.isEmpty(topicList)) {
            res.put("code", ArticleClassErrorCodeEnum.CLASS_RELATION_TOPIC.value());
            res.put("msg", "存在关联话题，分类不可删除");
        } else if (!CollectionUtils.isEmpty(articleList)) {
            res.put("code", ArticleClassErrorCodeEnum.CLASS_RELATION_ARTICLE.value());
            res.put("msg", "存在关联文章，分类不可删除");
        } else {
            //逻辑删除
            logicDelete(ids);
        }
        return res;
    }

    /**
     * 服务指南/资讯分类删除
     *
     * @param ids
     * @return
     */
    @Override

    public Map<String, Object> deleteClassIds(@RequestBody Long[] ids) {
        Map<String, Object> res = new HashMap<>(10);
        //判断待删除分类是否有下级分类和关联文章
        for (Long id : ids) {
            //查询此分类下是否存在子分类
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("ac_parent_id", id);
            wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
            Integer childNum = baseDao.selectCount(wrapper);
            if (childNum != null && childNum > 0) {
                res.put("code", ArticleClassErrorCodeEnum.DELETE_CLASS_FAIL.value());
                res.put("msg", "存在下级分类，分类不可删除");
                return res;
            }
        }
        //根据分类id查询关联文章名称
        List<String> list = cmsArticleDao.findCountByClassId(ids);
        //下级分类和关联文章都没有则删除分类
        if (CollectionUtils.isEmpty(list)) {
            //逻辑删除
            logicDelete(ids);
        } else {
            res.put("code", ArticleClassErrorCodeEnum.CLASS_RELATION_ARTICLE.value());
            res.put("msg", "存在关联文章，分类不可删除");
        }
        return res;
    }

    /**
     * 树形分类列表接口
     *
     * @param params acCode是必传参数
     */
    @Override

    public List<CmsClassTreeListDTO> getTree(@RequestParam Map<String, Object> params) {
        //查询所有分类列表
        List<CmsClassTreeListDTO> listDto = cmsClassDao.selectClassListDto(params);
        //分组
        List<CmsClassTreeListDTO> treeList = TreeUtils.build(listDto, 0L);
        return treeList;
    }


    /**
     * @Author zhangyuhao
     * 查找资讯所有分类信息，树形数据结构
     */
    @Override

    public List<CmsZxClassListDto> selectZxClassList() {
        List<CmsZxClassListDto> listDto = cmsClassDao.selectZxClassList();
        List<CmsZxClassListDto> zxClassListDto = TreeUtils.build(listDto, 0L);
        return zxClassListDto;
    }

    /**
     * 资讯新增时上级分类下拉树控制为二级
     *
     * @return
     */
    @Override

    public List<CmsZxClassListDto> selectZxClassSecondLevel() {
        List<CmsZxClassListDto> listDto = cmsClassDao.selectZxClassSecondLevel();
        List<CmsZxClassListDto> zxClassListDto = TreeUtils.build(listDto, 0L);
        return zxClassListDto;
    }


    @Override
    public List<CmsChildClassListDto> zxChildClassList(Long parentId) {
        List<CmsClassEntity> list = baseDao.selectList(
                new QueryWrapper<CmsClassEntity>().eq("ac_parent_id", parentId)
                        .eq("status", 1)
                        .eq("del_flag", 0));
        return ConvertUtils.sourceToTarget(list, CmsChildClassListDto.class);
    }

    /**
     * @Author yuhaifeng
     * 一级分类集合
     */
    @Override

    public List<CmsClassTreeListDTO> firstClassList(@RequestParam Map<String, Object> params) {
        List<CmsClassEntity> list = baseDao.selectList(
                new QueryWrapper<CmsClassEntity>()
                        .eq("ac_code", params.get("acCode"))
                        .eq("ac_level", 1)
                        .eq("status", 1)
                        .eq("del_flag", 0)
                        .orderByAsc("sort")
                        .orderByDesc("update_date")
        );
        return ConvertUtils.sourceToTarget(list, CmsClassTreeListDTO.class);
    }

    /**
     * @Author yuhaifeng
     * 子分类集合
     */
    @Override

    public List<CmsClassTreeListDTO> childClassList(@RequestParam Map<String, Object> params) {
        //查询子分类集合
        List<CmsClassTreeListDTO> listDto = cmsClassDao.selectChildClassList(params);
        //分组
        List<CmsClassTreeListDTO> treeList = TreeUtils.build(listDto, Long.parseLong(params.get("parentId").toString()));
        return ConvertUtils.sourceToTarget(treeList, CmsClassTreeListDTO.class);
    }


    /**
     * @Author pixiaoyong
     * H5圈子分类列表
     */
    @Override

    public List<CmsFrontClassDTO> frontCircleClassList() {
        List<CmsFrontClassDTO> list = cmsClassDao.selectFrontCircleClassList();
        return list;
    }

}
