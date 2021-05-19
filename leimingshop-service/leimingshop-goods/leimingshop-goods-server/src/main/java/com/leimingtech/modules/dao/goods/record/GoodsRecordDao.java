/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.record;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.goods.record.GoodsRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: weixianchun
 * @Description: 商品上下架状态记录
 * @Date :2019/6/4 17:55
 * @Version V1.0
 **/
@Mapper
public interface GoodsRecordDao extends BaseDao<GoodsRecordEntity> {

}