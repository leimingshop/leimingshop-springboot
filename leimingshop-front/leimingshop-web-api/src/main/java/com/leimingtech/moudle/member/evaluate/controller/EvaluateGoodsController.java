/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.evaluate.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.modules.dto.evaluate.EvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.FindEvaluateGoodsDTO;
import com.leimingtech.modules.dto.evaluate.SaveEvaluateGoodsDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.order.EvaluateOrderPageDTO;
import com.leimingtech.modules.service.evaluate.EvaluateGoodsService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.vo.evaluate.EvaluateGoodsInfoVO;
import com.leimingtech.modules.vo.order.PcOrderGoodsInfoVO;
import com.leimingtech.moudle.member.evaluate.code.PcEvaluateCode;
import com.leimingtech.moudle.member.evaluate.vo.EvaluateOrderPageVO;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;


/**
 * 商品评价管理
 *
 * @author chengqian
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-15
 */
@Slf4j
@RestController
@RequestMapping("member/evaluate")
@Api(tags = "商品评价管理")
public class EvaluateGoodsController {

    @Autowired
    private EvaluateGoodsService evaluateGoodsService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询未评价的订单商品
     *
     * @param params 分页参数
     */
    @GetMapping("order/page")
    @ApiOperation("分页查询未评价的订单商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", paramType = "query", dataType = "Long")
    })
    @LogContext(code = PcEvaluateCode.NOT_EVALUATE_ORDER_GOODS_CODE, note = PcEvaluateCode.NOT_EVALUATE_ORDER_GOODS_DESC)
    public Result<PageData<EvaluateOrderPageVO>> orderGoodsPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取当前用户ID
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<PageData<EvaluateOrderPageVO>>().error("请先登录");
        }
        params.put("memberId", memberId.toString());
        //根据用户ID 去查询订单商品表中待评价的商品
        PageData<EvaluateOrderPageDTO> notEvaOrder = orderService.findNotEvaOrder(params);
        PageData<EvaluateOrderPageVO> date = new PageData<>();
        date.setList(ConvertUtils.sourceToTarget(notEvaOrder.getList(), EvaluateOrderPageVO.class));
        date.setTotal(notEvaOrder.getTotal());

        return new Result<PageData<EvaluateOrderPageVO>>().ok(date);
    }

    /**
     * 分页查询已评价商品
     *
     * @param params 分页参数
     */
    @GetMapping("page")
    @ApiOperation("分页查询已评价商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = PcEvaluateCode.YES_EVALUATE_ORDER_GOODS_CODE, note = PcEvaluateCode.YES_EVALUATE_ORDER_GOODS_DESC)
    public Result<PageData<EvaluateOrderPageVO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        // 获取当前用户ID
        Long memberId = null;
        try {
            memberId = SecurityCurrentUser.getUserDetail().getId();
        } catch (CustomException e) {
            return new Result<PageData<EvaluateOrderPageVO>>().error("请先登录");
        }
        params.put("memberId", memberId.toString());
        PageData<EvaluateOrderPageDTO> page = orderService.pcEvaPage(params);
        PageData<EvaluateOrderPageVO> date = new PageData<>();
        date.setList(ConvertUtils.sourceToTarget(page.getList(), EvaluateOrderPageVO.class));
        date.setTotal(page.getTotal());

        return new Result<PageData<EvaluateOrderPageVO>>().ok(date);
    }


    /**
     * 新增商品评价
     *
     * @param dto 商品评价实体类
     * @return
     */
    @PostMapping
    @ApiOperation("新增商品评价")
    @LogContext(code = PcEvaluateCode.SAVE_GOODS_EVALUATE_CODE, note = PcEvaluateCode.SAVE_GOODS_EVALUATE_DESC)
    public Result save(@RequestBody List<SaveEvaluateGoodsDTO> dto) {
        // 获取当前用户ID
        MemberDTO memberDTO = null;
        try {
            memberDTO = SecurityCurrentUser.getUserDetail();

        } catch (CustomException e) {
            return new Result<PageData<FindEvaluateGoodsDTO>>().error("请先登录");
        }
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        List<EvaluateGoodsDTO> list = ConvertUtils.sourceToTarget(dto, EvaluateGoodsDTO.class);
        for (EvaluateGoodsDTO evaluateGoodsDTO : list) {
            evaluateGoodsDTO.setMemberAvatar(memberDTO.getMemberAvatar());
            evaluateGoodsDTO.setMemberName(memberDTO.getMemberName());
            evaluateGoodsDTO.setMemberId(memberDTO.getId());
            evaluateGoodsService.saveEvaluateGoods(evaluateGoodsDTO);
        }


        return new Result().ok(null, "新增成功");
    }

    /**
     * 根据订单ID获取查看评价
     *
     * @param orderId
     */
    @GetMapping("{orderId}")
    @ApiOperation("根据订单ID获取查看评价")
    @LogContext(code = PcEvaluateCode.ORDER_GOODS_YEX_EVALUATE_CODE, note = PcEvaluateCode.ORDER_GOODS_YESEVALUATE_DESC)
    public Result<List<EvaluateGoodsInfoVO>> findById(@PathVariable(value = "orderId") Long orderId) {

        List<EvaluateGoodsInfoVO> evaluateGoodsDTOList = evaluateGoodsService.pcEvaInfo(orderId);
        return new Result<List<EvaluateGoodsInfoVO>>().ok(evaluateGoodsDTOList);
    }

    /**
     * 根据订单ID获取订单商品项
     *
     * @param orderId
     */
    @GetMapping("order/goods/{orderId}")
    @ApiOperation("根据订单ID获取订单商品项")
    @LogContext(code = PcEvaluateCode.ORDER_GOODS_NOT_EVALUATE_CODE, note = PcEvaluateCode.ORDER_GOODS_NOT_EVALUATE_DESC)
    public Result<List<PcOrderGoodsInfoVO>> findOrderGoods(@PathVariable(value = "orderId") Long orderId,
                                                           @RequestParam(value = "orderGoodsId", required = false) Long orderGoodsId) {

        List<PcOrderGoodsInfoVO> pcOrderGoodsInfoVOList = orderGoodsService.findOrderGoodsInfo(orderId, orderGoodsId);
        return new Result<List<PcOrderGoodsInfoVO>>().ok(pcOrderGoodsInfoVOList);
    }

}
