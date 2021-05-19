/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.loginlog;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.entity.loginlog.StoreLoginLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: weixianchun
 * @Description: 店铺登录日志
 * @Date :2019/6/28 10:24
 * @Version V1.0
 **/
@Mapper
public interface StoreLoginLogDao extends BaseDao<StoreLoginLogEntity> {

}