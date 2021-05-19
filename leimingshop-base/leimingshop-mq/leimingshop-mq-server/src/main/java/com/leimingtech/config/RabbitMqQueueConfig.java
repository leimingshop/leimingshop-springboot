/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

import com.leimingtech.mq.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: LX 17839193044@162.com
 * @Description: RabbitMQ队列配置
 * @Date: 10:35 2019/5/8
 * @Version: V1.0
 */
@Configuration
public class RabbitMqQueueConfig {

    /**
     * 功能描述:
     * 〈修改商品es活动库存修改通知〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue updateEsGoodsActivitySurplusStorageQueue() {
        return new Queue(MqConstant.LEIMINGTECH_UPDATE_ES_GOODS_ACTIVITY_SURPLUS_STORAGE_QUEUE);
    }

    /**
     * 功能描述:
     * 〈秒杀活动索引同步〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue seckillActivitySyncEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_SECKILL_ACTIVITY_SYNC_ES_QUEUE);
    }

    /**
     * 功能描述:
     * 〈限时购活动索引同步〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue flashSaleActivitySyncEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_FLASH_SALE_ACTIVITY_SYNC_ES_QUEUE);
    }

    /**
     * 拼团活动索引同步
     *
     * @author xuzhch
     */
    @Bean
    public Queue groupActivitySyncEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_GROUP_ACTIVITY_SYNC_ES_QUEUE);
    }

    /**
     * 功能描述:
     * 〈满减活动索引同步〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue reduceActivitySyncEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_REDUCE_ACTIVITY_SYNC_ES_QUEUE);
    }

    /**
     * 功能描述:
     * 〈更新会员优惠券ES〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue memberCouponsUpdateEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_MEMBER_COUPONS_UPDATE_ES_QUEUE);
    }

    /**
     * 功能描述:
     * 〈优惠券活动索引同步〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue couponsActivitySyncEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_COUPONS_ACTIVITY_SYNC_ES_QUEUE);
    }

    @Bean
    public Queue saveStoreAuditLog() {
        return new Queue(MqConstant.LEIMINGTECH_SAVE_SOTRE_AUDIT_LOG_QUEUE);
    }

    /**
     * 功能描述:
     * 〈优惠券活动结束删除优惠券ES〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue couponsActivityEndEsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_COUPONS_ACTIVITY_END_ES_QUEUE);
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 创建错误日志队列Bean
     * @Date: 2019/5/12 10:32
     * @Version: V1.0
     */
    @Bean
    public Queue adminErrorlogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_ADMIN_ERROR_LOG_QUEUE);
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 登录日志队列
     * @Date: 2019/5/12 10:32
     * @Version: V1.0
     */
    @Bean
    public Queue adminLoginlogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE);
    }

    /**
     * @Author: WXC 17839193044@162.com
     * @Description: seller登录日志队列
     * @Date: 2019/5/12 10:32
     * @Version: V1.0
     */
    @Bean
    public Queue sellerLoginlogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_SELLER_LOGIN_LOG_QUEUE);
    }


    /**
     * @return org.springframework.amqp.core.Queue
     * @Author kuangweiguo
     * @Description //操作日志队列
     * @Date 2019/6/27  10:41 PM
     * @Param []
     **/
    @Bean
    public Queue adminOperationLogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_ADMIN_OPERATION_LOG_QUEUE);
    }

    /**
     * 库存预警消息队列
     *
     * @return
     */
    @Bean
    public Queue storageWarningQueue() {
        return new Queue(MqConstant.LEIMINGTECH_STORAGE_WARNING_QUEUE);
    }

    /**
     * 购物车商品库存不足
     *
     * @return
     */
    @Bean
    public Queue storageRemindQueue() {
        return new Queue(MqConstant.LEIMINGTECH_STORAGE_REMIND_QUEUE);
    }

    /**
     * @Author: xuzhch
     * @Description: 创建价格记录保存队列
     * @Date: 2019/5/12 10:32
     * @Version: V1.0
     */
    @Bean
    public Queue priceLogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_PRICE_RECORD_QUEUE);
    }

    /**
     * @Author: xuzhch
     * @Description: 创建库存记录保存队列
     * @Date: 2019/5/12 10:32
     * @Version: V1.0
     */
    @Bean
    public Queue stockLogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_STOCK_RECORD_QUEUE);
    }

    /**
     * @Author: DY
     * @Description: 创建站内信队列Bean
     * @Date: 2019/5/24
     * @Version: V1.0
     */
    @Bean
    public Queue messageQueue() {
        return new Queue(MqConstant.LEIMINGTECH_MESSAGE_QUEUE);
    }

    /**
     * @Author: DY
     * @Description: 创建保存商品上下架记录队列Bean
     * @Date: 2019/6/26
     * @Version: V1.0
     */
    @Bean
    public Queue goodsShowQueue() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE);
    }

    /**
     * @Author: tyl
     * @Description: 创建security认证成功后保存的用户信息
     * @Date: 2019/5/24
     * @Version: V1.0
     */
    @Bean
    public Queue securityUserQueue() {
        return new Queue(MqConstant.LEIMINGTECH_USERLOGIN_QUEUE);
    }


    /**
     * 用户登陆维护用户登陆信息
     *
     * @date 2019/11/11 10:06
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public Queue userLoginMessage() {
        return new Queue(MqConstant.LEIMINGTECH_USER_LOGIN_MESSAGE_QUEUE);
    }

    /**
     * @Author: chengqian
     * @Description: 创建上传队列Bean
     * @Date: 2019/5/24
     * @Version: V1.0
     */
    @Bean
    public Queue uploadQueue() {
        return new Queue(MqConstant.LEIMINGTECH_UPLOAD_RECORD_QUEUE);
    }

    /**
     * @Author: chengqian
     * @Description: 创建修改评论次数队列
     * @Date: 2019/5/24
     * @Version: V1.0
     */
    @Bean
    public Queue evaluateNumQueue() {
        return new Queue(MqConstant.LEIMINGTECH_UPDATE_EVALUATE_COUNT_QUEUE);
    }

    /**
     * @Author: xuzhch
     * @Description: 保存PV浏览量
     * @Date: 2019/5/24
     * @Version: V1.0
     */
    @Bean
    public Queue pvSaveQueue() {
        return new Queue(MqConstant.LEIMINGTECH_SAVE_PV_QUEUE);
    }

    /**
     * @Author: chengqian
     * @Description: 足迹记录队列Bean
     * @Date: 2019/6/4
     * @Version: V1.0
     */
    @Bean
    public Queue browseQueue() {
        return new Queue(MqConstant.LEIMINGTECH_BROWSE_QUEUE);
    }


    /**
     * @Author: chengqian
     * @Description: 修改购物车商品状态
     * @Date: 2019/6/4
     * @Version: V1.0
     */
    @Bean
    public Queue cartGoodsQueue() {
        return new Queue(MqConstant.LEIMINGTECH_CART_GOODS_STATUS);
    }

    /**
     * 店铺禁用，根据店铺ID 修改购物车商品状态
     *
     * @return
     */
    @Bean
    public Queue updateCartGoodsStatusQueue() {
        return new Queue(MqConstant.LEIMINGTECH_STORE_ID_CART_GOODS_STATUS);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:保存售后审核表日志
     * @Date: 2019/6/19 16:13
     * @Version: V1.0
     */
    @Bean
    public Queue applySaveQueue() {
        return new Queue(MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE);
    }

    /**
     * @return org.springframework.amqp.core.Queue
     * @Description 售后申请添加日志
     * @Param * @param :
     * @Author huangkeyuan
     * @Date 20:55 2019-06-20
     */
    @Bean
    public Queue applyAuditSaveQueue() {
        return new Queue(MqConstant.LEIMINGTECH_APPLY_SAVE_AUDIT_QUEUE);
    }

    /**
     * 功能描述:
     * 〈保存订单日志bean〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue saveOrderLogQueue() {
        return new Queue(MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE);
    }

    /**
     * 功能描述:
     * 〈异步回调修改订单状态bean〉
     *
     * @author : 刘远杰
     */
    @Bean
    public Queue notifyUpdateOrderQueue() {
        return new Queue(MqConstant.LEIMINGTECH_NOTIFY_UPDATE_ORDER_QUEUE);
    }

    /**
     * 购物车保存订单队列
     *
     * @date 2019/6/22 16:48
     * @author LX lixiangx@leimingtech.com
     **/
    @Bean
    public Queue cartConfirmOrder() {
        return new Queue(MqConstant.LEIMINGTECH_CONFIRM_ORDER_QUEUE);
    }

    /**
     * 保存拼团订单队列
     */
    @Bean
    public Queue groupConfirmOrder() {
        return new Queue(MqConstant.LEIMINGTECH_CONFIRM_GROUP_ORDER_QUEUE);
    }

    @Bean
    public Queue cancelOrder() {
        return new Queue(MqConstant.LEIMINGTECH_CANCEL_ORDERLOG_QUEUE);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description:微信退款
     * @Date: 2019/6/24 21:44
     * @Version: V1.0
     */
    @Bean
    public Queue wxrefund() {
        return new Queue(MqConstant.LEIMINGTECH_WEIXIN_REFUND_QUEUE);
    }

    /**
     * 添加商品满减标签
     *
     * @return
     */
    @Bean
    public Queue addGoodsReduceTag() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INDEX_ADD_REDUCE_TAG);
    }

    /**
     * 移除商品满减标签
     *
     * @return
     */
    @Bean
    public Queue removeGoodsReduceTag() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INDEX_REMOVE_REDUCE_TAG);
    }

    /**
     * 商品批量更新队列
     *
     * @return
     */
    @Bean
    public Queue goodsUpdate() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_DATE);
    }

    /**
     * 商品信息批量更新队列 收藏 评论 浏览
     */
    @Bean
    public Queue goodsInfoUpdate() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INFO_INDEX_UPDATE_DATE);
    }

    /**
     * 商品批量更新索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsBatchUpdate() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INDEX_BATCH_UPDATE);
    }

    /**
     * 商品批量更新队列
     *
     * @return
     */
    @Bean
    public Queue goodsSingleUpdate() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_SINGLE);
    }

    /**
     * 根据商品ID集合更新索引
     *
     * @return
     */
    @Bean
    public Queue goodsIndexUpdateByGoodsIds() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_BY_GOODS_IDS);
    }

    /**
     * 商品详情同步队列
     *
     * @return
     */
    @Bean
    public Queue goodsSpecSingleUpdate() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_DATE);
    }

    /**
     * 商品规格同步索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsIndexSpecSync() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE);
    }

    /**
     * 商品规格价格同步索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsSpecPriceIndexSync() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_PRICE_INDEX_UPDATE);
    }

    /**
     * 商品规格库存同步索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsSpecStorageIndexSync() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_STORAGE_INDEX_UPDATE);
    }

    /**
     * 商品规格上下架同步索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsSpecShowIndexSync() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_SHOW_INDEX_UPDATE);
    }


    /**
     * 商品规格同步索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsIndexSpecBySpecIdsSync() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_SINGLE);
    }

    /**
     * 根据店铺Id商品同步索引队列
     *
     * @return
     */
    @Bean
    public Queue goodsIndexByStoreId() {
        return new Queue(MqConstant.LEIMINGTECH_STORE_GOODS_INDEX_UPDATE);
    }

    /**
     * 同步店铺索引
     *
     * @return
     */
    @Bean
    public Queue storeIndexSync() {
        return new Queue(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE);
    }

    /**
     * 同步单个店铺索引
     *
     * @return
     */
    @Bean
    public Queue storeIndexSyncSingle() {
        return new Queue(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE);
    }

    /**
     * 删除店铺索引
     *
     * @return
     */
    @Bean
    public Queue deleteIndex() {
        return new Queue(MqConstant.LEIMINGTECH_DELETE_STORE_INDEX);
    }

    @Bean
    public Queue logisticsPushQueue() {
        return new Queue(MqConstant.LEIMINGTECH_LOGISTICS_PUSH_QUEUE);
    }

    /**
     * 商品降价给用户发送消息
     */
    @Bean
    public Queue goodsReduceRemind() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_REDUCE_PRICE_REMIND);
    }

    /**
     * 保存订单支付表
     *
     * @return
     */
    @Bean
    public Queue saveOrderPayQueue() {
        return new Queue(MqConstant.LEIMINGTECH_SAVE_ORDER_PAY_QUEUE);
    }

    /**
     * 保存满减活动浏览量
     *
     * @return
     */
    @Bean
    public Queue saveReducePvQueue() {
        return new Queue(MqConstant.LEIMINGTECH_COUPONS_PV_QUEUE);
    }

    /**
     * 同步搜索同义词队列
     *
     * @date 2019/12/13 16:58
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public Queue syncSearchSynonym() {
        return new Queue(MqConstant.LEIMINGTECH_SYNC_SEARCH_SYNONYM_QUEUE);
    }

    /**
     * 保存H5搜索历史队列名称
     *
     * @date 2019/12/16 12:04
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public Queue saveSearchHistory() {

        return new Queue(MqConstant.LEIMINGTECH_SAVE_SEACH_HISTORY_QUEUE);
    }

    /**
     * 保存积分/成长值日志队列名称
     *
     * @date 2019/12/24 11:11
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public Queue savePointLog() {
        return new Queue(MqConstant.LEIMINGTECH_SAVE_POINT_LOG_QUEUE);
    }

    /**
     * 商品导出队列名称
     *
     * @date 2019/12/24 11:11
     * @author xuzhch
     **/
    @Bean
    public Queue goodsExport() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_EXPORT_QUEUE);

    }

    /**
     * 功能描述 日志导出队列名称
     *
     * @author lishuo
     * @date 2020/8/27 13:58
     * @return: org.springframework.amqp.core.Queue
     **/
    @Bean
    public Queue logExport() {
        return new Queue(MqConstant.LEIMINGTECH_LOG_EXPORT_QUEUE);
    }


    /**
     * 订单导出队列名称
     *
     * @date 2019/12/24 11:11
     * @author xuzhch
     **/
    @Bean
    public Queue orderExport() {
        return new Queue(MqConstant.LEIMINGTECH_ORDER_EXPORT_QUEUE);
    }

    /**
     * 会员导出队列名称
     *
     * @date 2020年10月16日
     * @author xuzhch
     **/
    @Bean
    public Queue memberExport() {
        return new Queue(MqConstant.LEIMINGTECH_MEMBER_EXPORT_QUEUE);
    }

    /**
     * 审核导出队列名称
     *
     * @date 2019/12/24 11:11
     * @author xuzhch
     **/
    @Bean
    public Queue goodsCheckExport() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_CHECK_EXPORT_QUEUE);
    }

    /**
     * 单独更新规格销量ES索引队列名称
     */
    @Bean
    public Queue updateSpecSaleNum() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_SPEC_SALE_NUM_UPDATE);
    }

    /**
     * 单独更新商品销量ES索引队列名称
     */
    @Bean
    public Queue updateGoodsInfoSaleNum() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_INFO_SALE_NUM_UPDATE);
    }

    /**
     * 更新商品索引运费模板队列名称
     */
    @Bean
    public Queue updateFreightTemplate() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_ES_EVALUATE_UPDATE);
    }

    /**
     * 更新商品索引商品评价数量
     */
    @Bean
    public Queue updateGoodsEvaluateNum() {
        return new Queue(MqConstant.LEIMINGTECH_GOODS_FREIGHT_TEMPLATE_UPDATE);
    }

    /**
     * 按照消息模板发送短信队列名称
     *
     * @date 2020/4/9 15:54
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public Queue sendMessage() {
        return new Queue(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE);
    }


    /**
     * cms @功能模块
     *
     * @return
     */
    @Bean
    public Queue cmsRemindMessageQueue() {
        return new Queue(MqConstant.LEIMINGTECH_CMS_REMIND_MESSAGE_QUEUE);
    }

    /**
     * 邮件发送
     *
     * @return
     */
    @Bean
    public Queue sendEmailQueue() {
        return new Queue(MqConstant.LEIMINGTECH_SEND_EMAIL_QUEUE);
    }


    /**
     * 智能推荐系统队列声明
     */
    @Bean
    public Queue favoriteQueue() {
        return new Queue(MqConstant.RECOMMEND_FAVORITE_QUEUE);
    }

    @Bean
    public Queue logQueue() {
        return new Queue(MqConstant.RECOMMEND_LOG_QUEUE);
    }

    @Bean
    public Queue productPortaritQueue() {
        return new Queue(MqConstant.RECOMMEND_PRODUCT_PORTARIT_QUEUE);
    }

    @Bean
    public Queue historyQueue() {
        return new Queue(MqConstant.RECOMMEND_HISTORY_QUEUE);
    }

    @Bean
    public Queue topQueue() {
        return new Queue(MqConstant.RECOMMEND_TOP_QUEUE);
    }

    @Bean
    public Queue interestQueue() {
        return new Queue(MqConstant.RECOMMEND_INTEREST_QUEUE);
    }

    @Bean
    public Queue userPortraitQueue() {
        return new Queue(MqConstant.RECOMMEND_USER_PORTRAIT_QUEUE);
    }

    /**
     * 智能推荐RabbitMQ交换机（topic模式）
     *
     * @date 2020/7/16 18:38
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    public TopicExchange recommendExchange() {
        return new TopicExchange(MqConstant.RECOMMEND_EXCHANGE_NAME);
    }

    /**
     * @param logQueue          队列
     * @param recommendExchange 交换机
     *                          bindings 绑定交换机队列信息
     */
    @Bean
    Binding bindingLogExchangeMessage(Queue logQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(logQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }

    @Bean
    Binding bindingProductPortaritExchangeMessage(Queue productPortaritQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(productPortaritQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }

    @Bean
    Binding bindingTopExchangeMessage(Queue topQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(topQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }

    @Bean
    Binding bindingHistoryExchangeMessage(Queue historyQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(historyQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }

    @Bean
    Binding bindingInterestExchangeMessage(Queue interestQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(interestQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }

    @Bean
    Binding bindingUserPortraitExchangeMessage(Queue userPortraitQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(userPortraitQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }

    @Bean
    Binding bindingFavoriteExchangeMessage(Queue favoriteQueue, TopicExchange recommendExchange) {
        return BindingBuilder.bind(favoriteQueue).to(recommendExchange).with(MqConstant.RECOMMEND_ROUTINGKEY);
    }
}
