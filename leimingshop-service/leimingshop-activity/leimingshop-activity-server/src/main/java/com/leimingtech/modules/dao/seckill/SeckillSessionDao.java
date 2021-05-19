/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.seckill;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.seckill.SeckillSessionDTO;
import com.leimingtech.modules.entity.seckill.SeckillSessionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 秒杀场次管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-06
 */
@Mapper
public interface SeckillSessionDao extends BaseDao<SeckillSessionEntity> {

    /**
     * 功能描述：
     * <查询某天开始的所有场次>
     *
     * @param startDay 开始日期
     * @return
     * @date 2020/3/9 11:15
     * @author 刘远杰
     **/
    List<SeckillSessionDTO> getSeckillSessionListByStartDay(@Param("startDay") String startDay);

}
