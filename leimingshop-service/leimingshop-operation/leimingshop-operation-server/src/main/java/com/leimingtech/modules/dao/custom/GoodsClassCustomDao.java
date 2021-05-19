/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.custom;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.custom.GoodsClassCustomDetailDTO;
import com.leimingtech.modules.dto.custom.GoodsClassCustomListDTO;
import com.leimingtech.modules.dto.custom.GoodsClassCustomTreeDTO;
import com.leimingtech.modules.entity.custom.GoodsClassCustomEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品自定义分类表
 *
 * @author xuzhch
 * @email 170518@163.dao
 * @since 1.0.0 2019-05-22
 */
@Mapper
public interface GoodsClassCustomDao extends BaseDao<GoodsClassCustomEntity> {

    /**
     * 根据ID查询展示分类详情
     *
     * @param id 展示分类ID
     * @return 返回展示分类详情
     */
    GoodsClassCustomDetailDTO selectDetailById(Long id);

    /**
     * 查询所有分类
     *
     * @param showFlag
     * @param showType 0 H5展示分类  1 pc展示分类
     * @return
     */
    List<GoodsClassCustomListDTO> selectAllCustom(@Param("showFlag") Integer showFlag, @Param("showType") Integer showType);

    /**
     * 启用/禁用展示类目
     *
     * @param params
     */
    void updateShowFlag(Map<String, Object> params);

    /**
     * 查询分类名称是否重复
     *
     * @param gcParentId 当前分类父id
     * @param gcName     分类名称
     * @param showType   分类类型 0 H5展示分类，1 pc展示分类
     * @return 返回当前分类名称存在数量
     */
    Integer findClassName(@Param("gcParentId") Long gcParentId,
                          @Param("gcName") String gcName,
                          @Param("showType") Integer showType);

    /**
     * 更新时验证分类名称是否重复
     *
     * @param gcParentId 分类父iD
     * @param gcName     分类名称
     * @param id         分类id
     * @param showType   分类类型 0 H5展示分类，1 pc展示分类
     * @return 返回分类名称存在数量
     */
    Integer updateVerifyClassName(@Param("gcParentId") Long gcParentId,
                                  @Param("gcName") String gcName,
                                  @Param("id") Long id,
                                  @Param("showType") Integer showType);

    /**
     * 校验子级名称是否和父级名称一样
     *
     * @param parentId 父级ID
     * @param gcName   子名称
     * @return
     */
    Integer findParentName(@Param("parentId") Long parentId, @Param("gcName") String gcName);

    /**
     * 根据分类ID 删除展示类目
     *
     * @param longs
     */
    void deleteCustomByGoodsClassId(@Param("ids") List<Long> longs);

    /**
     * 首页楼层查询分类(返回树形结构)
     *
     * @param showType 分类类型 0 H5展示分类，1 pc展示分类
     * @return java.util.List<com.leimingtech.modules.dto.custom.GoodsClassCustomTreeDTO>
     * @Author: weixianchun
     * @Description: 首页楼层查询分类(返回树形结构)
     * @Date: 2019/8/15 14:32
     * @Version 1.0
     */
    List<GoodsClassCustomTreeDTO> sellectCustomTree(Integer showType);

    /**
     * 功能描述 根据父类id 分类名称 和 是否展示查找
     *
     * @param gcParentId 父类id
     * @param gcName     分类名称
     * @param showType   是否删除
     * @return GoodsClassCustomDao 实体类 id
     * @author lishuo
     * @date 2020/6/16
     */
    GoodsClassCustomEntity findByClassName(@Param("gcParentId") Long gcParentId,
                                           @Param("gcName") String gcName,
                                           @Param("showType") Integer showType);
}