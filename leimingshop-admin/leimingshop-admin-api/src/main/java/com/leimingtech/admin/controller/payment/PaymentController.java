/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.payment;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.payment.PaymentConfigUpdateDTO;
import com.leimingtech.modules.dto.payment.PaymentDTO;
import com.leimingtech.modules.dto.payment.PaymentFindDTO;
import com.leimingtech.modules.dto.payment.PaymentUpdatStatusDTO;
import com.leimingtech.modules.service.payment.PaymentService;
import com.leimingtech.modules.statuscode.PaymentStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 支付方式管理PaymentController
 * @Date :2019/5/20 14:38
 * @Version V1.0
 **/
@RestController
@RequestMapping("payment")
@Api(tags = "支付方式管理")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * @Author: weixianchun
     * @Description: 查询所有支付信息
     * @Date :2019/6/3 16:38
     * @Param params
     * @Version V1.0
     **/
    @GetMapping("list")
    @ApiOperation("查询所有支付分组信息")
    @LogOperation("查询所有支付分组信息")
    @LogContext(code = PaymentStatusCode.ADMIN_PAYMENT_FIND_SUCCESS_CODE, note = PaymentStatusCode.ADMIN_PAYMENT_FIND_SUCCESS_MESSAGE)
    public Result<List<PaymentDTO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("type", "admin");
        List<PaymentDTO> list = paymentService.list(params);

        return new Result<List<PaymentDTO>>().ok(list, "查询成功");
    }

    @GetMapping("list/all")
    @ApiOperation("查询全部支付信息")
    @LogOperation("查询全部支付信息")
    @LogContext(code = PaymentStatusCode.SELLER_PAYMENT_FIND_SUCCESS_CODE, note = PaymentStatusCode.SELLER_PAYMENT_FIND_SUCCESS_MESSAGE)
    public Result<List<PaymentDTO>> alllist(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<PaymentDTO> list = paymentService.list(params);
        return new Result<List<PaymentDTO>>().ok(list, "查询成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 修改开启关闭状态
     * @Date :2019/5/20 14:41
     * @Param :[dto] 实体
     * @Version V1.0
     **/
    @PutMapping
    @ApiOperation("修改开启关闭状态")
    @LogOperation("修改开启关闭状态")
    @LogContext(code = PaymentStatusCode.ADMIN_PAYMENT_STATUS_UPDATE_CODE, note = PaymentStatusCode.ADMIN_PAYMENT_STATUS_UPDATE_MESSAGE)
    public Result update(@RequestBody PaymentUpdatStatusDTO dto) {

        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        //对象转换
        PaymentDTO paymentDTO = ConvertUtils.sourceToTarget(dto, PaymentDTO.class);
        //修改开启关闭状态
        paymentService.update(paymentDTO);

        return new Result().ok(null, "修改成功");
    }

    /**
     * @Author: weixianchun
     * @Description: 根据支付id, 查询支付接口信息
     * @Date :2019/5/20 14:42
     * @Param :[id] 支付接口id
     * @Version V1.0
     **/
    @GetMapping("payment/config")
    @ApiOperation("根据支付id, 查询支付接口信息")
    @LogOperation("根据支付id, 查询支付接口信息")
    @LogContext(code = PaymentStatusCode.ADMIN_PAYMENT_FIND_BY_ID_CODE, note = PaymentStatusCode.ADMIN_PAYMENT_FIND_BY_ID_MESSAGE)
    public Result<PaymentFindDTO> showPaymentConfig(@RequestParam("id") Long id) {

        PaymentFindDTO paymentFindDTO = paymentService.findById(id);

        return new Result<PaymentFindDTO>().ok(paymentFindDTO, "查询成功");

    }

    /**
     * @Author: weixianchun
     * @Description: 修改支付方式信息(页面保存)
     * @Date :2019/5/20 14:42
     * @Param :[paymentConfigUpdateDTO] 实体
     * @Version V1.0
     **/
    @PostMapping(value = "update/config")
    @ApiOperation("修改支付方式信息(页面保存)")
    @LogOperation("修改支付方式信息(页面保存)")
    @LogContext(code = PaymentStatusCode.ADMIN_PAYMENT_UPDATE_SAVE_CODE, note = PaymentStatusCode.ADMIN_PAYMENT_UPDATE_SAVE_MESSAGE)
    public Result setPaymentArgs(@RequestBody PaymentConfigUpdateDTO paymentConfigUpdateDTO) {

        //效验数据
        ValidatorUtils.validateEntity(paymentConfigUpdateDTO, UpdateGroup.class, DefaultGroup.class);

        //修改(保存)支付方式信息
        paymentService.updateConfig(paymentConfigUpdateDTO);

        return new Result().ok(null, "保存成功");

    }

}
