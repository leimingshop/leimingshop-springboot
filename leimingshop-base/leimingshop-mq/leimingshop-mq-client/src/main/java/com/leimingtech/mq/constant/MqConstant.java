/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.mq.constant;

/**
 * @Author: LX 17839193044@162.com
 * @Description: MQ消息队列常量
 * @Date: 21:40 2019/5/11
 * @Version: V1.0
 */
public interface MqConstant {

    /**
     * 修改商品es活动库存修改通知
     */
    String LEIMINGTECH_UPDATE_ES_GOODS_ACTIVITY_SURPLUS_STORAGE_QUEUE = "leimingtech.update.es.goods.activity.surplus.storage.queue";

    /**
     * 秒杀活动索引同步
     */
    String LEIMINGTECH_SECKILL_ACTIVITY_SYNC_ES_QUEUE = "leimingtech.seckill.activity.sync.es.queus";
    /**
     * 限时购活动索引同步
     */
    String LEIMINGTECH_FLASH_SALE_ACTIVITY_SYNC_ES_QUEUE = "leimingtech.flash.sale.activity.sync.es.queus";

    /**
     * 拼团活动索引同步
     */
    String LEIMINGTECH_GROUP_ACTIVITY_SYNC_ES_QUEUE = "leimingtech.group.activity.sync.es.queus";

    /**
     * 满减活动索引同步
     */
    String LEIMINGTECH_REDUCE_ACTIVITY_SYNC_ES_QUEUE = "leimingtech.reduce.activity.sync.es.queus";

    /**
     * 优惠券活动索引同步
     */
    String LEIMINGTECH_COUPONS_ACTIVITY_SYNC_ES_QUEUE = "leimingtech.coupons.activity.sync.es.queus";

    /**
     * 保存店铺审核记录
     */
    String LEIMINGTECH_SAVE_SOTRE_AUDIT_LOG_QUEUE = "leimingtech.save.store.audit.log.queue";

    /**
     * 更新会员优惠券ES
     */
    String LEIMINGTECH_MEMBER_COUPONS_UPDATE_ES_QUEUE = "leimingtech.member.coupons.update.es.queus";

    /**
     * 优惠券活动结束删除优惠券ES
     */
    String LEIMINGTECH_COUPONS_ACTIVITY_END_ES_QUEUE = "leimingtech.coupons.activity.end.es.queus";

    /**
     * 后台用户登录日志队列
     */
    String LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE = "leimingtech.admin.login.log.queus";

    /**
     * seller后台用户登录日志队列
     */
    String LEIMINGTECH_SELLER_LOGIN_LOG_QUEUE = "leimingtech.seller.login.log.queue";

    /**
     * 错误日志
     */
    String LEIMINGTECH_ADMIN_ERROR_LOG_QUEUE = "leimingtech.error.log.queue";

    /**
     * 操作日志
     **/
    String LEIMINGTECH_ADMIN_OPERATION_LOG_QUEUE = "leimingtech.operation.log.queue";
    /**
     * 库存预警消息
     */
    String LEIMINGTECH_STORAGE_WARNING_QUEUE = "leimingtech.storage.warning.queue";

    /**
     * 购物车商品库存不足
     */
    String LEIMINGTECH_STORAGE_REMIND_QUEUE = "leimingtech.storage.remind.queue";

    /**
     * 用户登陆队列名称
     */
    String LEIMINGTECH_USERLOGIN_QUEUE = "leimingtech.user.login.queue";

    /**
     * 用户登陆维护用户登陆信息
     */
    String LEIMINGTECH_USER_LOGIN_MESSAGE_QUEUE = "leimingtech.user.message.login.queue";

    /**
     * 站内信发送
     */
    String LEIMINGTECH_MESSAGE_QUEUE = "leimingtech.message.queue";

    /**
     * 价格记录保存
     */
    String LEIMINGTECH_PRICE_RECORD_QUEUE = "leimingtech.price.record.queue";

    /**
     * 库存记录保存
     */
    String LEIMINGTECH_STOCK_RECORD_QUEUE = "leimingtech.stock.record.queue";

    /**
     * 文件上传日志队列名称
     */
    String LEIMINGTECH_UPLOAD_RECORD_QUEUE = "leimingtech.upload.record.queue";

    /**
     * 足迹记录队列
     */
    String LEIMINGTECH_BROWSE_QUEUE = "leimingtech.browse.queue";

    /**
     * 商品上下架保存记录
     */
    String LEIMINGTECH_GOODS_SHOW_QUEUE = "leimingtech.goods.show.queue";
    /**
     * 保存售后日志
     */
    String LEIMINGTECH_APPLY_SAVE_QUEUE = "leimingtech.apply.save.queue";

    /**
     * 购物车提交订单队列名称
     */
    String LEIMINGTECH_CART_CONFIRM_ORDER_QUEUE = "leimingtech.cart.confirm.order.queue";

    /**
     * 立即购买提交订单队列名称
     */
    String LEIMINGTECH_NOW_CONFIRM_ORDER_QUEUE = "leimingtech.now.confirm.order.queue";

    /**
     * 保存售后申请日志
     */
    String LEIMINGTECH_APPLY_SAVE_AUDIT_QUEUE = "leimingtech.apply.audit.save.queue";

    /**
     * 微信退款
     */
    String LEIMINGTECH_WEIXIN_REFUND_QUEUE = "leimingtech.weixin.refund.queue";

    /**
     * 保存订单日志
     */
    String LEIMINGTECH_SAVE_ORDERLOG_QUEUE = "leimingtech.save.orderlog.queue";

    /**
     * 异步回调修改订单状态
     */
    String LEIMINGTECH_NOTIFY_UPDATE_ORDER_QUEUE = "leimingtech.notify.upate.order.queue";

    /**
     * 取消订单
     */
    String LEIMINGTECH_CANCEL_ORDERLOG_QUEUE = "leimingtech.cancel.orderlog.queue";
    /**
     * 保存订单队列名称
     */
    String LEIMINGTECH_CONFIRM_ORDER_QUEUE = "leimingtech.confirm.order.queue";

    /**
     * 保存拼团订单队列名称
     */
    String LEIMINGTECH_CONFIRM_GROUP_ORDER_QUEUE = "leimingtech.confirm.group.order.queue";

    /***
     * 添加商品满减标签
     */
    String LEIMINGTECH_GOODS_INDEX_ADD_REDUCE_TAG = "leimingtech.goods.index.add.reduce.tag";

    /***
     * 移除商品满减标签
     */
    String LEIMINGTECH_GOODS_INDEX_REMOVE_REDUCE_TAG = "leimingtech.goods.index.remove.reduce.tag";

    /***
     * 商品索引批量更新队列
     */
    String LEIMINGTECH_GOODS_INDEX_UPDATE_DATE = "leimingtech.goods.index.update.date";
    /***
     * 增量的更新商品的 收藏量 评价数量 浏览数量
     */
    String LEIMINGTECH_GOODS_INFO_INDEX_UPDATE_DATE = "leimingtech.goods.info.index.update.date";

    /***
     * 商品索引批量更新队列
     */
    String LEIMINGTECH_GOODS_INDEX_BATCH_UPDATE = "leimingtech.goods.index.batch.update";

    /**
     * 商品规格批量更新队列
     */
    String LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_DATE = "leimingtech.goods.spec.index.update.date";

    /**
     * 单个商品更新队列
     */
    String LEIMINGTECH_GOODS_INDEX_UPDATE_SINGLE = "leimingtech.goods.index.update.single";

    /**
     * 根据商品ID集合更新索引
     */
    String LEIMINGTECH_GOODS_INDEX_UPDATE_BY_GOODS_IDS = "leimingtech.goods.index.update.by.goods.ids";
    /**
     * 商品规格索引更新队列
     */
    String LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE = "leimingtech.goods.spec.index.update";
    /**
     * 商品规格价格索引更新队列
     */
    String LEIMINGTECH_GOODS_SPEC_PRICE_INDEX_UPDATE = "leimingtech.goods.spec.price.index.update";
    /**
     * 商品规格库存索引更新队列
     */
    String LEIMINGTECH_GOODS_SPEC_STORAGE_INDEX_UPDATE = "leimingtech.goods.spec.storage.index.update";
    /**
     * 商品规格上下架索引更新队列
     */
    String LEIMINGTECH_GOODS_SPEC_SHOW_INDEX_UPDATE = "leimingtech.goods.spec.show.index.update";


    /**
     * 单个商品规格索引更新队列
     */
    String LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_SINGLE = "leimingtech.goods.spec.index.update.single";

    /**
     * 更新某个店铺的所有商品索引
     */
    String LEIMINGTECH_STORE_GOODS_INDEX_UPDATE = "leimingtech.store.goods.index.update";

    /**
     * 更新店铺索引
     */
    String LEIMINGTECH_STORE_INDEX_UPDATE = "leimingtech.store.index.update";

    /**
     * 更新单个店铺索引
     */
    String LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE = "leimingtech.store.index.update.single";

    /**
     * 删除店铺索引
     */
    String LEIMINGTECH_DELETE_STORE_INDEX = "leimingtech.delete.store.index";

    /**
     * 修改购物车商品上下架
     */
    String LEIMINGTECH_CART_GOODS_STATUS = "leimingtech.cart.goods.status";

    /**
     * 商品降价给用户发送消息
     */
    String LEIMINGTECH_GOODS_REDUCE_PRICE_REMIND = "leimingtech.goods.reduce.price.remind";

    /**
     * 店铺禁用根据店铺ID 更新购物车上下架
     */
    String LEIMINGTECH_STORE_ID_CART_GOODS_STATUS = "leimingtech.store.id.cart.goods.status";

    /**
     * 订阅物流消息推送信息
     */
    String LEIMINGTECH_LOGISTICS_PUSH_QUEUE = "leimingtech.logistics.push.queue";

    /**
     * 保存订单支付表
     */
    String LEIMINGTECH_SAVE_ORDER_PAY_QUEUE = "leimingtech.orderpay.save.queue";

    /**
     * 保存满减活动浏览量
     */
    String LEIMINGTECH_COUPONS_PV_QUEUE = "leimingtech.coupons.pv.queue";

    /**
     * 更新评论次数
     */
    String LEIMINGTECH_UPDATE_EVALUATE_COUNT_QUEUE = "leimingtech.update.evaluate.count.queue";

    /**
     * 同步搜索同义词队列名称
     */
    String LEIMINGTECH_SYNC_SEARCH_SYNONYM_QUEUE = "leimingtech.sync.search.synonym.queue";

    /**
     * 保存H5搜索历史队列名称
     */
    String LEIMINGTECH_SAVE_SEACH_HISTORY_QUEUE = "leimingtech.save_seach.history.queue";

    /**
     * 保存浏览量
     */
    String LEIMINGTECH_SAVE_PV_QUEUE = "leimingtech.save.pv.queue";

    /**
     * 保存积分/成长值日志队列名称
     */
    String LEIMINGTECH_SAVE_POINT_LOG_QUEUE = "leimingtech.save.point.log.queue";

    /**
     * 导出商品
     */
    String LEIMINGTECH_GOODS_EXPORT_QUEUE = "leimingtech.goods.export.queue";

    /**
     * 导出日志
     */
    String LEIMINGTECH_LOG_EXPORT_QUEUE = "leimingtech.log.export.queue";
    /**
     * 导出商品审核列表
     */
    String LEIMINGTECH_GOODS_CHECK_EXPORT_QUEUE = "leimingtech.goods.check.export.queue";
    /**
     * 导出订单列表
     */
    String LEIMINGTECH_ORDER_EXPORT_QUEUE = "leimingtech.order.export.queue";
    /**
     * 导出会员列表
     */
    String LEIMINGTECH_MEMBER_EXPORT_QUEUE = "leimingtech.member.export.queue";
    /**
     * 发送消息模板队列名称
     */
    String LEIMINGTECH_SEND_MESSAGE_QUEUE = "leimingtech.send.message.queue";

    /**
     * 单独更新规格销量ES索引队列名称
     */
    String LEIMINGTECH_GOODS_SPEC_SALE_NUM_UPDATE = "leimingtech.spec.salenum.update.queue";

    /**
     * 单独更新商品销量ES索引队列名称
     */
    String LEIMINGTECH_GOODS_INFO_SALE_NUM_UPDATE = "leimingtech.goods.info.salenum.update.queue";

    /**
     * 更新商品索引运费模板队列名称
     */
    String LEIMINGTECH_GOODS_FREIGHT_TEMPLATE_UPDATE = "leimingtech.goods.freight.template.update.queue";


    /**
     * 更新商品es的评价数量和好评率
     */
    String LEIMINGTECH_GOODS_ES_EVALUATE_UPDATE = "leimingtech.goods.es.evaluate.update.queue";

    /**
     * cms @功能模块
     */
    String LEIMINGTECH_CMS_REMIND_MESSAGE_QUEUE = "leimingtech.cms.remind.message.queue";

    /**
     * 邮件发送队列
     */
    String LEIMINGTECH_SEND_EMAIL_QUEUE = "leimingtech.send.email.queue";

    /**
     * 智能推荐系统相关队列名称
     */
    String RECOMMEND_FAVORITE_QUEUE = "leiming.recommend.favorite.queue";
    String RECOMMEND_LOG_QUEUE = "leiming.recommend.log.queue";
    String RECOMMEND_PRODUCT_PORTARIT_QUEUE = "leiming.recommend.product.portarit.queue";
    String RECOMMEND_HISTORY_QUEUE = "leiming.recommend.history.queue";
    String RECOMMEND_TOP_QUEUE = "leiming.recommend.top.queue";
    String RECOMMEND_INTEREST_QUEUE = "leiming.recommend.interest.queue";
    String RECOMMEND_USER_PORTRAIT_QUEUE = "leiming.recommend.user.portrait.queue";

    String RECOMMEND_QUEUE = "leiming.recommend.";
    /**
     * 智能推荐交换机
     */
    String RECOMMEND_EXCHANGE_NAME = "recommend.exchange";
    /**
     * 智能推荐rabbitmq 交换机与队列的 bingingkey
     */
    String RECOMMEND_ROUTINGKEY = "leiming.recommend.#";

    /**
     * 默认的交换机名称
     */
    String DEFAULT_EXCHANGE_NAME = "default.exchange";

    /**
     * 默认的路由key
     */
    String DEFAULT_ROUTING_KEY = "default.routing";

    /**
     * 默认的队列名称
     */
    String DEFAULT_QUEUE = "default.queue";

    /**
     * 是否持久队列，消息队列丢数据
     */
    Boolean DURABLE = true;

    /**
     * 声明一个独立队列
     */
    Boolean EXCLUSIVE = false;

    /**
     * 服务器不再使用时是否自动删除交换器
     */
    Boolean AUTO_DELETE = false;

    /**
     * 消息发送成功返回信息
     */
    String MESSAGE_SEND_SUCCESS = "SUCCESS";
}
