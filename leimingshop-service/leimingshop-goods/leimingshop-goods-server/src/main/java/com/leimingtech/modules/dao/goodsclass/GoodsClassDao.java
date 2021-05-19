/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goodsclass;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goodsclass.*;
import com.leimingtech.modules.entity.goodsclass.GoodsClassEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品分类表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Mapper
public interface GoodsClassDao extends BaseDao<GoodsClassEntity> {

    /**
     * 通过父id查找名字
     *
     * @param s 父id
     * @return java.util.List<java.lang.String>
     * @Description 通过父id查找名字
     * @Author huangkeyuan
     * @Date 14:48 2019-05-24
     */
    List<String> findByParentId(Long s);

    /**
     * 根据分类id查询分类关联的规格、属性、品牌信息
     *
     * @param id
     * @return
     */
    GoodsClassDetailDTO selectDetailByGcId(Long id);

    /**
     * 查询当前分类下所有子级分类信息
     *
     * @param id
     * @return
     */
    List<GoodsClassDTO> selectListByParentId(Long id);

    /**
     * 查询分类信息
     *
     * @param classIds 分类id
     * @return 返回分类信息
     */
    List<GoodsClassQueryNameDTO> selectListByClassIds(@Param("list") List<Long> classIds);

    /**
     * 查询所有分类(分层级关系)
     *
     * @param
     * @return 返回分类信息
     * @author huangkeyuan
     */
    List<GoodsClassListDTO> selectAllGoodClass();

    /**
     * 根据classID查询IDPath
     *
     * @param goodsClassId
     * @return
     */
    String queryIdPathById(Long goodsClassId);

    /**
     * 返回分类名称数量
     *
     * @param dto 查询参数
     * @return 返回分类名称数量
     * @Author: weixianchun
     * @Description: 查询分类名称数量(修改时)
     * @Date :2019/7/3 19:58
     * @Version V1.0
     **/
    int gcNameConutCheck(GoodsClassUpdateDTO dto);

    /**
     * 查询分类名称数量(保存时)
     *
     * @param gcName     分类名称
     * @param gcParentId 父id
     * @return 返回分类名称数量
     * @Author: weixianchun
     * @Description: 查询分类名称数量(保存时)
     * @Date :2019/7/3 20:40
     * @Version V1.0
     **/
    Integer findByGcName(@Param("gcName") String gcName, @Param("gcParentId") Long gcParentId);


    /**
     * 根据分类id查询品牌信息, 并模糊查询品牌名称
     *
     * @param ids       分类id
     * @param brandName 品牌名称
     * @return 返回分类品牌关联信息
     * @Author: weixianchun
     * @Description: 根据分类id查询品牌信息, 并模糊查询品牌名称
     * @Date :2019/7/4 11:55
     * @Version V1.0
     **/
    List<GoodsClassBrandDTO> brandByGcId(@Param("ids") String[] ids, @Param("brandName") String brandName);

    /**
     * 根据店铺分类查询出分类信息
     *
     * @param longs 分类id
     * @return 返回分类信息
     * @Author: chengqian
     * @Description: 根据店铺分类查询出分类信息
     * @Date :2019/7/16 11:55
     * @Version V1.0
     **/
    List<GoodsClassListDTO> selectListByStoreClass(@Param("ids") List<Long> longs);

    /**
     * 校验子分组名称不能和父分组名称一样
     *
     * @param parentId
     * @param gcName
     * @return
     */
    Integer findParentName(@Param("parentId") Long parentId, @Param("gcName") String gcName);

    /**
     * 查询分类信息
     *
     * @param classLists 分类id
     * @return 返回分类信息
     */
    List<Map<String, Object>> selectNameByCustomIds(@Param("classLists") List<Long> classLists);

    /**
     * 功能描述:
     * 〈根据一级分类id集合获取所有三级分类〉
     *
     * @param classIds 一级分类id
     * @return 返回商品分类
     * @author : 刘远杰
     */
    List<GoodsClassDTO> selectThreeByOneIds(@Param("classIds") List<Long> classIds);

    /**
     * 根据名称和父类id查找
     *
     * @param gcName   分类名称
     * @param parentId 父类的id
     * @return Long 查到的父类的id
     * @author lishuo
     * @date 2020/6/12
     */
    Long findByGcNameAndParentId(@Param("gcName") String gcName, @Param("parentId") Long parentId);
}
