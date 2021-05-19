/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.seckill;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.seckill.SeckillSessionDTO;

import java.util.List;
import java.util.Map;

/**
 * 秒杀场次管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-06
 */

public interface SeckillSessionService {


    PageData<SeckillSessionDTO> page(Map<String, Object> params);

    /**
     * 功能描述：
     * <秒杀场次条件查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/9 9:21
     * @author 刘远杰
     **/

    List<SeckillSessionDTO> list(Map<String, Object> params);


    SeckillSessionDTO get(Long id);


    Boolean save(SeckillSessionDTO dto);

    /**
     * 功能描述：
     * <秒杀场次批量保存>
     *
     * @param dtoList 秒杀场次集合
     * @return
     * @date 2020/3/6 19:53
     * @author 刘远杰
     **/

    Boolean saveBatch(List<SeckillSessionDTO> dtoList);


    Boolean update(SeckillSessionDTO dto);


    void delete(Long[] ids);

    /**
     * 功能描述：
     * <秒杀创次创建定时任务>
     *
     * @param time 定时任务执行时间
     * @return
     * @date 2020/3/9 10:09
     * @author 刘远杰
     **/

    void createSeckillSessionTiming(Long time);

    /**
     * 功能描述：
     * <查询某天开始的场次>
     *
     * @param startDay 场次开始时间
     * @return
     * @date 2020/3/9 11:20
     * @author 刘远杰
     **/

    List<SeckillSessionDTO> getSeckillSessionListByStartDay(String startDay);
}
