/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.brand;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.entity.brand.BrandEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: weixianchun
 * @Description: 品牌管理BrandDao
 * @Date :2019/5/20 14:07
 * @Version V1.0
 **/
@Mapper
public interface BrandDao extends BaseDao<BrandEntity> {

    /**
     * 保存时-根据品牌名称,品牌英文名称查询品牌信息
     *
     * @param brandName   品牌名称
     * @param brandNameEn 品牌英文名称
     * @return 返回品牌的信息
     * @Author: weixianchun
     * @Description: 保存时-根据品牌名称,品牌英文名称查询品牌信息
     * @Date :2019/5/20 14:06
     * @Version V1.0
     **/
//    BrandDTO findByName(@Param("brandName") String brandName, @Param("brandNameEn") String brandNameEn);

    /**
     * 修改时-查询品牌名称,品牌英文名称数量防止重复校验
     *
     * @param brandDTO
     * @return 返回品牌信息
     * @Author: weixianchun
     * @Description: 修改时-查询品牌名称,品牌英文名称数量防止重复校验
     * @Date :2019/5/20 14:07
     * @Version V1.0
     **/
//    int selectByBrandName(BrandDTO brandDTO);

    /**
     * 功能描述:
     * 〈根据分类id集合获取品牌〉
     *
     * @param classIds 分类id
     * @return 返回品牌信息
     * @author : 刘远杰
     */
    List<BrandDTO> selectByGcId(@Param("classIds") List<Long> classIds);

    Integer selectCountByCondition(BrandDTO brandDTO);
}
