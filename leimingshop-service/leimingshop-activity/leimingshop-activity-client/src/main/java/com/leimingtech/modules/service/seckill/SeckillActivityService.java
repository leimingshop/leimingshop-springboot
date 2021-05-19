/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.seckill;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.seckill.AdminSeckillActivityDetailDTO;
import com.leimingtech.modules.dto.seckill.AdminSeckillActivityPageDTO;
import com.leimingtech.modules.dto.seckill.SeckillActivityDTO;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */

public interface SeckillActivityService {


    PageData<SeckillActivityDTO> page(Map<String, Object> params);

    /**
     * 功能描述：
     * <后台秒杀活动分页查询>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/9 16:44
     * @author 刘远杰
     **/

    PageData<AdminSeckillActivityPageDTO> adminPage(Map<String, Object> params);


    List<SeckillActivityDTO> list(Map<String, Object> params);


    SeckillActivityDTO get(Long id);

    /**
     * 功能描述：
     * <后台秒杀活动详情>
     *
     * @param id      秒杀活动id
     * @param storeId 店铺id
     * @return
     * @date 2020/3/9 17:35
     * @author 刘远杰
     **/

    AdminSeckillActivityDetailDTO adminDetail(Long id,
                                              Long storeId);

    /**
     * 功能描述：
     * <查询店铺指定id秒杀活动>
     *
     * @param id      秒杀活动id
     * @param storeId 店铺id
     * @return
     * @date 2020/3/9 15:30
     * @author 刘远杰
     **/

    SeckillActivityDTO getByIdOrStoreId(Long id,
                                        Long storeId);


    Boolean save(SeckillActivityDTO dto);


    Boolean update(SeckillActivityDTO dto);

    /**
     * 功能描述：
     * <秒杀活动浏览记录+1>
     *
     * @param ids
     * @return
     * @date 2020/3/16 16:09
     * @author 刘远杰
     **/

    void increaseBrowserNum(List<Long> ids);

    /**
     * 功能描述：
     * <秒杀活动订单+1>
     *
     * @param ids
     * @return
     * @date 2020/3/30
     * @author 刘远杰
     **/

    void increaseOrderNum(List<Long> ids);

    /**
     * 功能描述：
     * <编辑秒杀活动>
     *
     * @param dto 编辑秒杀活动实体
     * @return
     * @date 2020/3/9 16:20
     * @author 刘远杰
     **/

    Boolean editSeckillActivity(SeckillActivityDTO dto);

    /**
     * 功能描述：
     * <秒杀活动删除 删除活动及活动商品 更新购物车商品es>
     *
     * @param ids 活动id集合
     * @return
     * @date 2020/3/25 14:32
     * @author 刘远杰
     **/

    void delete(Long[] ids);

    /**
     * 功能描述：
     * <秒杀活动停止>
     *
     * @param id      秒杀活动id
     * @param storeId 店铺id
     * @return
     * @date 2020/3/9 17:50
     * @author 刘远杰
     **/

    Boolean stop(Long id,
                 Long storeId);

    /**
     * 功能描述：
     * <秒杀活动定时开始>
     *
     * @param time 定时执行时间
     * @return
     * @date 2020/3/11 17:07
     * @author 刘远杰
     **/

    void startSeckillActivityTiming(Long time);

    /**
     * 功能描述：
     * <秒杀活动定时结束>
     *
     * @param time 定时执行时间
     * @return
     * @date 2020/3/11 17:07
     * @author 刘远杰
     **/

    void stopSeckillActivityTiming(Long time);
}
