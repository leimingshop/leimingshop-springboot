/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.articleclass;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.articleclass.*;
import com.leimingtech.modules.entity.articleclass.CmsClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分类管理 CmsClassDao
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Mapper
public interface CmsClassDao extends BaseDao<CmsClassEntity> {

    /**
     * 圈子分类查询
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回圈子分类信息
     */
    List<CmsClassPageDTO> selectCmsCircleClassList(Page page, @Param("params") Map<String, Object> params);


    /**
     * 同标识分类重名查询
     *
     * @param params 查询条件
     * @return 返回分类信息
     */
    List<CmsClassEntity> selectByAcName(@Param("params") CmsClassSaveDTO params);

    /**
     * 查询文章一级分类
     *
     * @return 返回分类信息
     */
    List<CmsClassEntity> selectArticleFirstList();

    /**
     * 根据父ID查询子分类列表
     *
     * @param id 主键id
     * @return 返回子分类
     */
    List<CmsClassEntity> selectClassListByParentId(Long id);

    /**
     * 查询指定标识的所有分类
     *
     * @param params 查询参数是
     * @return 返回分类信息
     */
    List<CmsClassEntity> selectClassList(@Param("params") Map<String, Object> params);

    /**
     * 查询指定标识的所有分类
     *
     * @param params 查询参数
     * @return 返回分类信息
     */
    List<CmsClassTreeListDTO> selectClassListDto(@Param("params") Map<String, Object> params);

    /**
     * 查找资讯所有分类信息
     *
     * @return 返回分类信息
     */
    List<CmsZxClassListDto> selectZxClassList();

    /**
     * 资讯新增时上级分类下拉树控制为二级
     *
     * @return 返回 分类信息
     */
    List<CmsZxClassListDto> selectZxClassSecondLevel();

    /**
     * 一级分类集合
     *
     * @param params 查询信息
     * @return 返回一级分类信息
     */
    List<CmsClassTreeListDTO> selectChildClassList(@Param("params") Map<String, Object> params);

    /**
     * 查询H5分类信息
     *
     * @return 返回分类信息
     */
    List<CmsFrontClassDTO> selectFrontCircleClassList();
}
