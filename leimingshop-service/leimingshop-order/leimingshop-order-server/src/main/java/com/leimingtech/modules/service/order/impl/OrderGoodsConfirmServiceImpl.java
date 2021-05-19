/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dao.order.OrderGoodsConfirmDao;
import com.leimingtech.modules.dto.order.OrderGoodsConfirmDTO;
import com.leimingtech.modules.entity.order.OrderGoodsConfirmEntity;
import com.leimingtech.modules.service.order.OrderGoodsConfirmService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 订单商品确定表
 *
 * @author LX lixiangx@leimingtech.com
 * @since v1.0.0 2019-06-22
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class OrderGoodsConfirmServiceImpl extends BaseServiceImpl<OrderGoodsConfirmDao, OrderGoodsConfirmEntity>
        implements OrderGoodsConfirmService {


    /**
     * 查询订单商品确认集合
     *
     * @param params: 封装的查询条件
     * @return 订单商品确认信息集合
     * @date 2019/6/22 14:08
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public List<OrderGoodsConfirmDTO> list(Map<String, Object> params) {

        List<OrderGoodsConfirmEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, OrderGoodsConfirmDTO.class);
    }

    /**
     * 获取QueryWrapper
     *
     * @param params: 封装的查询条件
     * @date 2019/6/22 14:09
     * @author LX lixiangx@leimingtech.com
     **/
    private QueryWrapper<OrderGoodsConfirmEntity> getWrapper(Map<String, Object> params) {

        Long id = (Long) params.get("id");

        QueryWrapper<OrderGoodsConfirmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(id != null, "id", id);

        return wrapper;
    }

    /**
     * 查询订单商品确认信息
     *
     * @param id: 主键
     * @return 订单商品确认信息
     * @date 2019/6/22 14:10
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public OrderGoodsConfirmDTO findById(String id) {
        OrderGoodsConfirmEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, OrderGoodsConfirmDTO.class);
    }

    /**
     * 保存订单商品确认信息
     *
     * @param dto: 订单商品确认实体
     * @date 2019/6/22 14:11
     * @author LX lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody OrderGoodsConfirmDTO dto) {

        OrderGoodsConfirmEntity entity = ConvertUtils.sourceToTarget(dto, OrderGoodsConfirmEntity.class);

        insert(entity);
    }

    /**
     * 批量保存订单商品确认数据
     *
     * @param dtoList: 订单商品确认数据集合
     * @date 2019/6/22 16:27
     * @author LX lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void saveList(@RequestBody List<OrderGoodsConfirmDTO> dtoList) {
        List<OrderGoodsConfirmEntity> orderGoodsConfirmEntityList = ConvertUtils.
                sourceToTarget(dtoList, OrderGoodsConfirmEntity.class);
        insertBatch(orderGoodsConfirmEntityList);
    }


    /**
     * 修改订单商品确认信息
     *
     * @param dto: 订单商品确认实体
     * @date 2019/6/22 14:11
     * @author LX lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody OrderGoodsConfirmDTO dto) {
        OrderGoodsConfirmEntity entity = ConvertUtils.sourceToTarget(dto, OrderGoodsConfirmEntity.class);

        updateById(entity);
    }

    /**
     * 批量删除
     *
     * @param ids: 批量删除主键ID集合
     * @date 2019/6/22 14:11
     * @author LX lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        logicDelete(ids, OrderGoodsConfirmEntity.class);

        //物理删除
        //baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 根据订单确认ID查询确认订单商品数据
     *
     * @param orderConfrimId: 订单确认表ID
     * @return 订单商品确认集合
     * @date 2019/6/22 17:31
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public List<OrderGoodsConfirmDTO> findListByOrderConfirmId(Long orderConfrimId) {

        AssertUtils.isNull(orderConfrimId, "订单确认ID不能为空");

        QueryWrapper<OrderGoodsConfirmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("order_confirm_id", orderConfrimId);
        List<OrderGoodsConfirmEntity> orderGoodsConfirmEntityList = baseDao.selectList(wrapper);

        return ConvertUtils.sourceToTarget(orderGoodsConfirmEntityList, OrderGoodsConfirmDTO.class);
    }

}
