/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.search;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @Author: SWH ab4856812@163.com
 * @Description: 初始化ES索引
 * @Date: 2019/7/16 18:43
 * @Version: V1.0
 */
@RestController
@RequestMapping("search")
@Api(tags = "索引管理")
public class SearchController {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 商品索引批量同步（商品搜索结果集）
     *
     * @return 操作结果
     */
    @GetMapping("goods")
    @ApiOperation("商品索引批量更新")
    public Result goodsAttrIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_DATE, "");
        return new Result().ok(null, "商品属性批量更新成功");
    }


    /**
     * 商品规格索引批量同步（商品详情页结果集）
     *
     * @return 操作结果
     */
    @GetMapping("spec")
    @ApiOperation("商品规格索引批量更新")
    public Result goodsSpecIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_DATE, "");
        return new Result().ok(null, "商品规格批量更新成功");
    }

    /**
     * 店铺索引批量更新
     *
     * @return
     */
    @GetMapping("store")
    @ApiOperation("店铺索引批量更新")
    public Result storeIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE, "");
        return new Result().ok(null, "店铺索引批量更新成功");
    }

    @GetMapping("coupons")
    @ApiOperation("优惠券活动索引批量更新")
    public Result couponsActivityIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_COUPONS_ACTIVITY_SYNC_ES_QUEUE, System.currentTimeMillis() + "");
        return new Result().ok(null, "优惠券活动索引批量更新成功");
    }

    @GetMapping("reduce")
    @ApiOperation("满减活动索引批量更新")
    public Result reduceActivityIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_REDUCE_ACTIVITY_SYNC_ES_QUEUE, System.currentTimeMillis() + "");
        return new Result().ok(null, "满减活动索引批量更新成功");
    }

    @GetMapping("seckill")
    @ApiOperation("秒杀活动索引批量更新")
    public Result seckillActivityIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SECKILL_ACTIVITY_SYNC_ES_QUEUE, System.currentTimeMillis() + "");
        return new Result().ok(null, "秒杀活动索引批量更新成功");
    }

    @GetMapping("group")
    @ApiOperation("拼团活动索引批量更新")
    public Result groupActivityIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GROUP_ACTIVITY_SYNC_ES_QUEUE, System.currentTimeMillis() + "");
        return new Result().ok(null, "拼团活动索引批量更新成功");
    }

    @GetMapping("goodsInfo")
    @ApiIgnore("商品收藏评论浏览批量更新")
    public Result updateGoodsInfoIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_GOODS_INFO_INDEX_UPDATE_DATE, "time");
        return new Result().ok(null, "商品收藏品论浏览更新完成");
    }

    @GetMapping("flash/sale")
    @ApiIgnore("限时购活动索引批量更新")
    public Result flashSaleInfoIndex() {
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_FLASH_SALE_ACTIVITY_SYNC_ES_QUEUE, "time");
        return new Result().ok(null, "限时购活动索引批量更新成功");
    }
}
