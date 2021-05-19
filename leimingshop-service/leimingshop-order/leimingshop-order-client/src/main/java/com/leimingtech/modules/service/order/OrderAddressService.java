/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.order;


import com.leimingtech.modules.dto.order.OrderAddressDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 订单地址信息管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-13
 */

public interface OrderAddressService {

    /**
     * 功能描述:
     * 〈保存订单地址〉
     *
     * @param dto 订单地址信息
     * @author : 刘远杰
     */

    int saveOrderAddress(@RequestBody OrderAddressDTO dto);

    /**
     * 功能描述:
     * 〈编辑订单地址，保存空值〉
     *
     * @param dto 订单地址信息
     * @author : 刘远杰
     */

    int editOrderAddress(@RequestBody OrderAddressDTO dto);

    /**
     * 功能描述:
     * 〈修改订单地址，空值不修改原字段〉
     *
     * @param dto 订单地址信息
     * @author : 刘远杰
     */

    void update(@RequestBody OrderAddressDTO dto);

    /**
     * 功能描述:
     * 〈根据订单地址id查询订单地址〉
     *
     * @param id 订单地址实体
     * @author : 刘远杰
     */

    OrderAddressDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据主键生成订单地址〉
     *
     * @param id 订单地址主键
     * @author : 刘远杰
     */

    void deleteById(Long id);


    /**
     * 保存订单地址信息
     *
     * @param addressId: 用户地址ID
     * @param buyId:     用户Id
     * @param buyName:   用户名称
     * @return 订单地址ID
     * @date 2019/6/22 17:52
     * @author LX lixiangx@leimingtech.com
     **/

    Long saveOrderAddress(@RequestParam("addressId") Long addressId,
                          @RequestParam("buyId") Long buyId,
                          @RequestParam("buyName") String buyName,
                          @RequestParam("mobPhone") String mobPhone);
}
