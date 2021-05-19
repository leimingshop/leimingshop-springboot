/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.articleclass;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.articleclass.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 分类管理 CmsClassService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsClassService {
    /**
     * 查询分类分页
     *
     * @param params 查询条件
     * @return 返回查询信息
     */

    PageData<CmsClassPageDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询分类信息
     *
     * @param params 查询条件
     * @return 返回分类信息
     */

    List<CmsClassListDTO> classList(@RequestParam Map<String, Object> params);

    /**
     * 查询文章最上级分类接口
     *
     * @param params 查询条件
     * @return 返回分类信息
     */

    List<CmsClassListDTO> firstList(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询分类详情
     *
     * @param id 主键id
     * @return 返回分类详情
     */

    CmsClassDTO get(Long id);

    /**
     * 圈子分类新增
     *
     * @param dto 新增参数
     */

    void circleClassSave(@RequestBody CmsClassSaveDTO dto);

    /**
     * 文章分类新增
     *
     * @param dto 新增参数
     * @return 返回新增结果
     */

    Map<String, Object> articleClassSave(@RequestBody CmsClassSaveDTO dto);

    /**
     * 更新分类信息
     *
     * @param dto 更新参数
     */

    void update(@RequestBody CmsClassDTO dto);

    /**
     * 删除分类信息
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 圈子分类删除
     *
     * @param ids 主键id
     * @return 返回删除结果
     */

    Map<String, Object> deleteQzClassIds(@RequestBody Long[] ids);

    /**
     * 服务指南/资讯分类删除
     *
     * @param ids 主键id
     * @return 返回删除结果
     */

    Map<String, Object> deleteClassIds(@RequestBody Long[] ids);

    /**
     * 封装树形结构
     *
     * @param params 查询参数
     * @return
     * @Author pixiaoyong
     */

    List<CmsClassTreeListDTO> getTree(@RequestParam Map<String, Object> params);

    /**
     * 查找资讯所有分类信息，树形数据结构
     *
     * @return 返回分类信息
     */

    List<CmsZxClassListDto> selectZxClassList();

    /**
     * 资讯新增时上级分类下拉树控制为二级
     *
     * @return 返回分类信息
     */

    List<CmsZxClassListDto> selectZxClassSecondLevel();

    /**
     * 根据父id查询信息
     *
     * @param id 父id
     * @return 返回子集分类
     */

    List<CmsChildClassListDto> zxChildClassList(Long id);

    /**
     * 资讯一级分类集合
     *
     * @param params 查询参数
     * @return 返回一级分类信息
     */

    List<CmsClassTreeListDTO> firstClassList(@RequestParam Map<String, Object> params);

    /**
     * 子分类集合
     *
     * @param params 查询参数
     * @return 返回子集分类
     */

    List<CmsClassTreeListDTO> childClassList(@RequestParam Map<String, Object> params);

    /**
     * H5圈子分类
     *
     * @return 返回分类信息
     */

    List<CmsFrontClassDTO> frontCircleClassList();
}
