/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.entity.AftersaleGoodsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 售后商品表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Mapper
public interface AftersaleGoodsDao extends BaseDao<AftersaleGoodsEntity> {

}