/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.seckill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.seckill.AdminSeckillActivityPageDTO;
import com.leimingtech.modules.entity.seckill.SeckillActivityEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动管理
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2020-03-09
 */
@Mapper
public interface SeckillActivityDao extends BaseDao<SeckillActivityEntity> {

    /**
     * 功能描述：
     * <后台秒杀活动分页查询>
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return
     * @date 2020/3/9 16:46
     * @author 刘远杰
     **/
    List<AdminSeckillActivityPageDTO> adminPage(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述：
     * <秒杀活动弄编辑>
     *
     * @param entity 秒杀活动编辑实体
     * @return
     * @date 2020/3/9 16:19
     * @author 刘远杰
     **/
    int editSeckillActivity(SeckillActivityEntity entity);

    /**
     * 功能描述：
     * <秒杀活动商品浏览记录+1>
     *
     * @param ids 活动id集合
     * @return
     * @date 2020/3/16 16:11
     * @author 刘远杰
     **/
    int increaseBrowserNum(@Param("ids") List<Long> ids);

    /**
     * 功能描述：
     * <秒杀订单+1>
     *
     * @param ids
     * @return
     * @date 2020/3/30 10:53
     * @author 刘远杰
     **/
    int increaseOrderNum(@Param("ids") List<Long> ids);

}
