/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.log.point;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.log.point.MemberGrowthValueDTO;
import com.leimingtech.modules.dto.log.point.PointLogDTO;
import com.leimingtech.modules.entity.log.point.PointLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 积分日志DAO
 *
 * @author lixiang lixiangx@leimingtech.com
 * @since v1.0.0 2019-12-24
 */
@Mapper
public interface PointLogDao extends BaseDao<PointLogEntity> {


    /**
     * 根据时间查询用户成长值集合
     *
     * @param startTime: 开始时间
     * @param endTime:   结束时间
     * @return 用户成长值集合
     * @date 2019/12/26 9:58
     * @author lixiangx@leimingtech.com
     **/
    List<MemberGrowthValueDTO> findGrowthbyTime(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据时间查询用户登录记录
     *
     * @param params 时间查询条件
     * @return 用户登录日志信息
     * @author huangkeyuan @leimingtech.com
     * @date 2020 -01-14 16:03
     */
    List<PointLogDTO> findByTime(@Param("params") Map<String, Object> params);

}
