/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.payment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.payment.PaymentTallyDao;
import com.leimingtech.modules.dto.payment.PaymentTallyDTO;
import com.leimingtech.modules.dto.payment.PaymentTallyExcelDTO;
import com.leimingtech.modules.entity.payment.PaymentTallyEntity;
import com.leimingtech.modules.enums.payment.PaymentCodeEnum;
import com.leimingtech.modules.service.payment.PaymentTallyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 支付流水管理
 * @Date :2019/6/18 11:26
 * @Version V1.0
 **/
@Service
public class PaymentTallyServiceImpl extends CrudServiceImpl<PaymentTallyDao, PaymentTallyEntity, PaymentTallyDTO>
        implements PaymentTallyService {

    /**
     * @Author: weixianchun
     * @Description: 条件构造器
     * @Date :2019/6/24 14:33
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override
    public QueryWrapper<PaymentTallyEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        //交易流水号
        String tradeSn = (String) params.get("tradeSn");
        //交易渠道
        String paymentCode = (String) params.get("paymentCode");
        //交易编号
        String paymentSn = (String) params.get("paymentSn");
        //买家名称
        String buyerName = (String) params.get("buyerName");
        //收支状态
        String transactionStatus = (String) params.get("transactionStatus");
        //交易开始时间
        String startDate = (String) params.get("startDate");
        //交易结束时间
        String endDate = (String) params.get("endDate");

        QueryWrapper<PaymentTallyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(paymentCode), "payment_code", paymentCode);
        wrapper.like(StringUtils.isNotBlank(tradeSn), "trade_sn", tradeSn);
        wrapper.like(StringUtils.isNotBlank(paymentSn), "payment_sn", paymentSn);
        wrapper.like(StringUtils.isNotBlank(buyerName), "buyer_name", buyerName);
        wrapper.like(StringUtils.isNotBlank(transactionStatus), "transaction_status", transactionStatus);
        wrapper.ge(StringUtils.isNotBlank(startDate), "create_date", startDate);
        wrapper.le(StringUtils.isNotBlank(endDate), "create_date", endDate);


        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public PageData<PaymentTallyDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付流水信息
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public List<PaymentTallyDTO> list(@RequestParam Map<String, Object> params) {

        return super.list(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 导出list
     * @Date :2019/6/19 10:04
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public List<PaymentTallyExcelDTO> listExport(@RequestParam Map<String, Object> params) {

        //按时间排序
        params.put(Constant.ORDER_FIELD, Constant.CREATE_DATE);
        params.put(Constant.ORDER, "desc");
        List<PaymentTallyDTO> list = super.list(params);
        ArrayList<PaymentTallyExcelDTO> excelList = new ArrayList<>();
        //收支状态
        for (PaymentTallyDTO paymentTallyDTO : list) {
            //转换
            PaymentTallyExcelDTO paymentTallyExcelDTO = ConvertUtils.sourceToTarget(paymentTallyDTO, PaymentTallyExcelDTO.class);
            if (paymentTallyDTO.getTransactionStatus() != null) {
                //根据状态判断并设值
                if (paymentTallyDTO.getTransactionStatus() == PaymentCodeEnum.TRANSACTION_STATUS_IN.value()) {
                    paymentTallyExcelDTO.setTransactionStatus("收入");
                } else if (paymentTallyDTO.getTransactionStatus() == PaymentCodeEnum.TRANSACTION_STATUS_OUT.value()) {
                    paymentTallyExcelDTO.setTransactionStatus("支出");
                }
            }
            excelList.add(paymentTallyExcelDTO);
        }
        return excelList;
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询支付流水信息
     * @Date :2019/5/28 19:52
     * @Param id 支付流水id
     * @Version V1.0
     **/

    @Override
    public PaymentTallyDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存支付流水信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void save(@RequestBody PaymentTallyDTO dto) {
        super.save(dto);
    }

    /**
     * 功能描述:
     * 〈批量保存支付流水〉
     *
     * @param dtoList 支付流水集合
     * @author : 刘远杰
     */

    @Override
    public void saveBatch(@RequestBody List<PaymentTallyDTO> dtoList) {
        List<PaymentTallyEntity> paymentTallyEntityList = ConvertUtils.sourceToTarget(dtoList, PaymentTallyEntity.class);
        this.insertBatch(paymentTallyEntityList);
    }

    /**
     * @Author: weixianchun
     * @Description: 修改支付流水信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void update(@RequestBody PaymentTallyDTO dto) {
        super.update(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id批量删除支付流水信息
     * @Date :2019/5/28 19:53
     * @Param ids 支付流水id
     * @Version V1.0
     **/

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 功能描述:
     * 〈根据交易单号查询交易流水记录〉
     *
     * @param paySn 交易单号
     * @author : 刘远杰
     */

    @Override
    public PaymentTallyDTO getByPaySn(@RequestParam("paySn") Long paySn) {
        // paysn
        QueryWrapper<PaymentTallyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(paySn != null, "payment_sn", paySn);
        PaymentTallyEntity paymentTallyEntity = baseDao.selectOne(wrapper);
        return ConvertUtils.sourceToTarget(paymentTallyEntity, PaymentTallyDTO.class);
    }

}
