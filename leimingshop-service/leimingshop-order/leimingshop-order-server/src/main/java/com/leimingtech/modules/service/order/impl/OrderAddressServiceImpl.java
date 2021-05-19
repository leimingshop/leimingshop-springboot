/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.modules.dao.order.OrderAddressDao;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.order.OrderAddressDTO;
import com.leimingtech.modules.entity.order.OrderAddressEntity;
import com.leimingtech.modules.service.address.MemberAddressService;
import com.leimingtech.modules.service.order.OrderAddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 订单地址信息管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */
@Transactional

@Service
public class OrderAddressServiceImpl extends CrudServiceImpl<OrderAddressDao, OrderAddressEntity, OrderAddressDTO> implements OrderAddressService {

    @Autowired
    private MemberAddressService memberAddressService;

    /**
     * 功能描述:
     * 〈保存订单地址〉
     *
     * @param dto 订单地址信息
     * @author : 刘远杰
     */

    @Override
    public int saveOrderAddress(@RequestBody OrderAddressDTO dto) {
        OrderAddressEntity entity = ConvertUtils.sourceToTarget(dto, OrderAddressEntity.class);
        return baseDao.insert(entity);
    }

    /**
     * 功能描述:
     * 〈编辑订单地址，修改空值〉
     *
     * @param dto 订单地址信息
     * @author : 刘远杰
     */

    @Override
    public int editOrderAddress(@RequestBody OrderAddressDTO dto) {
        OrderAddressEntity entity = ConvertUtils.sourceToTarget(dto, OrderAddressEntity.class);
        return baseDao.editOrderAddress(entity);
    }

    /**
     * 功能描述:
     * 〈修改订单地址，空值不修改原字段〉
     *
     * @param dto 订单地址信息
     * @author : 刘远杰
     */

    @Override
    public void update(@RequestBody OrderAddressDTO dto) {
        super.update(dto);
    }

    /**
     * 功能描述:
     * 〈根据订单地址id查询订单地址〉
     *
     * @param id 订单地址实体
     * @author : 刘远杰
     */

    @Override
    public OrderAddressDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据主键生成订单地址〉
     *
     * @param id 订单地址主键
     * @author : 刘远杰
     */

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    @Override
    public QueryWrapper<OrderAddressEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<OrderAddressEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    /**
     * 保存订单地址信息
     *
     * @param buyId:   用户Id
     * @param buyName: 用户名称
     * @return 订单地址ID
     * @date 2019/6/22 17:52
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public Long saveOrderAddress(@RequestParam("addressId") Long addressId,
                                 @RequestParam("buyId") Long buyId,
                                 @RequestParam("buyName") String buyName,
                                 @RequestParam("mobPhone") String mobPhone) {
        OrderAddressDTO orderAddressDTO = new OrderAddressDTO();

        if (null != mobPhone) {
            // 虚拟订单保存用户地址
            orderAddressDTO.setMobPhone(mobPhone);
            orderAddressDTO.setTrueName(buyName);
        } else {
            // 查询用户地址信息
            MemberAddressDTO memberAddressDTO = memberAddressService.get(addressId);
            BeanCopier.create(memberAddressDTO.getClass(), orderAddressDTO.getClass(), false)
                    .copy(memberAddressDTO, orderAddressDTO, null);
        }

        orderAddressDTO.setBuyerId(buyId);
        orderAddressDTO.setId(IdGenerator.defaultSnowflakeId());
        this.saveOrderAddress(orderAddressDTO);
        return orderAddressDTO.getId();
    }

}
