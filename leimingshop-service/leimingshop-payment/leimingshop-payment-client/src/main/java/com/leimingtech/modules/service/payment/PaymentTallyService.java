/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.payment.PaymentTallyDTO;
import com.leimingtech.modules.dto.payment.PaymentTallyExcelDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 支付流水管理
 * @Date :2019/6/18 11:26
 * @Version V1.0
 **/

public interface PaymentTallyService {

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    PageData<PaymentTallyDTO> page(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付流水信息
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    List<PaymentTallyDTO> list(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 导出list
     * @Date :2019/6/19 10:04
     * @Param params 查询条件
     * @Version V1.0
     **/

    List<PaymentTallyExcelDTO> listExport(@RequestParam Map<String, Object> params);

    /**
     * @Author: weixianchun
     * @Description: 根据id查询支付流水信息
     * @Date :2019/5/28 19:52
     * @Param id 支付流水id
     * @Version V1.0
     **/

    PaymentTallyDTO get(Long id);

    /**
     * @Author: weixianchun
     * @Description: 保存支付流水信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    void save(@RequestBody PaymentTallyDTO dto);

    /**
     * 功能描述:
     * 〈批量保存支付流水〉
     *
     * @param dtoList 支付流水集合
     * @author : 刘远杰
     */

    void saveBatch(@RequestBody List<PaymentTallyDTO> dtoList);

    /**
     * @Author: weixianchun
     * @Description: 修改支付流水信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    void update(@RequestBody PaymentTallyDTO dto);

    /**
     * @Author: weixianchun
     * @Description: 根据id删除支付流水信息
     * @Date :2019/5/28 19:53
     * @Param ids 支付流水id
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈根据交易单号查询交易流水记录〉
     *
     * @param paySn 交易单号
     * @author : 刘远杰
     */

    PaymentTallyDTO getByPaySn(@RequestParam("paySn") Long paySn);


}
