/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.order.OrderConfirmDao;
import com.leimingtech.modules.dto.order.OrderConfirmDTO;
import com.leimingtech.modules.entity.order.OrderConfirmEntity;
import com.leimingtech.modules.service.order.OrderConfirmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 订单确定服务实现类
 *
 * @author LX lixiangx@leimingtech.com
 * @since v1.0.0 2019-06-22
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderConfirmServiceImpl extends BaseServiceImpl<OrderConfirmDao, OrderConfirmEntity>
        implements OrderConfirmService {

    /**
     * 列表查询
     *
     * @param params 参数
     * @return 数据集合
     * @author LX lixiangx@leimingtech.com
     */
    @Override

    public List<OrderConfirmDTO> list(Map<String, Object> params) {
        List<OrderConfirmEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, OrderConfirmDTO.class);
    }

    /**
     * 封装QueryWrapper
     *
     * @param params 参数集合
     * @return 封装的QueryWrapper
     * @author LX lixiangx@leimingtech.com
     **/
    private QueryWrapper<OrderConfirmEntity> getWrapper(Map<String, Object> params) {

        String id = (String) params.get("id");

        QueryWrapper<OrderConfirmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 根据ID查询订单确认实体
     *
     * @param id 主键ID
     * @return 订单确认实体
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public OrderConfirmDTO findById(Long id) {

        OrderConfirmEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, OrderConfirmDTO.class);
    }

    /**
     * 保存订单确认信息
     *
     * @param dto: 订单确认DTO
     * @author LX  lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody OrderConfirmDTO dto) {
        OrderConfirmEntity entity = new OrderConfirmEntity();
        BeanCopier.create(OrderConfirmDTO.class, OrderConfirmEntity.class, false)
                .copy(dto, entity, null);
        insert(entity);
    }

    /**
     * 修改订单确认信息
     *
     * @param dto: 订单确认DTO
     * @author LX lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody OrderConfirmDTO dto) {
        OrderConfirmEntity entity = ConvertUtils.sourceToTarget(dto, OrderConfirmEntity.class);

        updateById(entity);
    }

    /**
     * 批量删除订单确认信息
     *
     * @param ids: ID集合
     * @date 2019/6/22 14:05
     * @author LX lixiangx@leimingtech.com
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        logicDelete(ids, OrderConfirmEntity.class);
    }

}
