/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.label;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goodslable.GoodsLabelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelUpdateDTO;
import com.leimingtech.modules.entity.label.GoodsLabelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品标签GoodsLabelDao
 * @Date :2019/5/20 14:23
 * @Version V1.0
 **/
@Mapper
public interface GoodsLabelDao extends BaseDao<GoodsLabelEntity> {

    /**
     * 标签列表分页查询
     *
     * @param params 查询条件
     * @return 返回商品标签数据
     * @Author weixianchun
     * @Description
     * @Date 2019/12/10 15:34
     * @Return java.util.List<com.leimingtech.modules.dto.goodslable.GoodsLabelDTO>
     * @version 1.0
     */
    List<GoodsLabelDTO> findListPage(Map<String, Object> params);

    /**
     * 修改时校验标签名称是否重复
     *
     * @param labelUpdateDTO 查询参数
     * @return 返回标签名称数量
     * @Author weixianchun
     * @Description 修改时校验标签名称是否重复
     * @Date 2019/12/11 15:02
     * @Return int
     * @version 1.0
     */
    int checkLabelNameUpate(GoodsLabelUpdateDTO labelUpdateDTO);

    /**
     * 保存时校验标签名称是否重复
     *
     * @param labelName 标签名称
     * @return 返回标签名称数量
     * @Author weixianchun
     * @Description 保存时校验标签名称是否重复
     * @Date 2019/12/11 15:02
     * @Return int
     * @version 1.0
     */
    int checkLabelNameSave(@Param("labelName") String labelName);

    /**
     * 功能描述 根据goodsId查找标签名
     *
     * @param * @param goodsId
     * @return java.lang.String 标签名
     * @author lishuo
     * @date 28/6/2020
     */
    String findByGoodsId(Long goodsId);
}
