/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.cart;


import com.leimingtech.modules.dto.cart.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 购物车表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */

public interface CartService {
    /**
     * 分页查询购物车列表
     *
     * @param memberId 用户ID
     * @return
     */

    List<CartDTO> getList(@RequestParam("memberId") Long memberId);

    /**
     * 根据ID 获取信息
     *
     * @param id 主键ID
     * @return
     */

    CartDTO get(Long id);

    /**
     * 加入购物车
     *
     * @param dto      参数
     * @param memberId 用户ID
     */

    void save(@RequestBody SaveCartDTO dto, @RequestParam("memberId") Long memberId);

    /**
     * 修改购物车数据
     *
     * @param dto 参数实体
     */

    void update(@RequestBody CartDTO dto);

    /**
     * 根据ID删除购物车
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);


    /**
     * 去结算服务层方法
     *
     * @param memberId 用户ID
     * @return SettlementDTO 去结算与提交订单返回DTO
     * @Author: LX 17839193044@162.com
     * @Description: 去结算服务层方法
     * @Date: 2019/6/14 16:37
     * @Version: V1.0
     */

    SettlementDTO settlement(@RequestParam("memberId") Long memberId);

    /**
     * 购物车结算切换活动
     *
     * @param storeCouponsMap 店铺会员优惠券
     * @param memberId        会员id
     * @param addressId       地址id
     * @param useBalance      否使用余额付款 0不使用 1使用
     * @return
     * @date
     * @author 刘远杰
     **/

    SettlementDTO recountSettlementPrice(@RequestBody Map<String, Long> storeCouponsMap,
                                         @RequestParam("memberId") Long memberId,
                                         @RequestParam(value = "addressId", required = false) Long addressId,
                                         @RequestParam(value = "useBalance", required = false) Integer useBalance);

    /**
     * 获取选中购物车的价钱
     *
     * @param memberId: 用户ID
     * @return
     */

    BigDecimal findAllSelect(@RequestParam("memberId") Long memberId);

    /**
     * 同步购物车
     *
     * @param cartKey:  用户未登录情况下购物车key
     * @param memberId: 用户ID
     */

    void synchCart(@RequestParam("cartKey") String cartKey,
                   @RequestParam("memberId") Long memberId);


    /**
     * 立即购买
     *
     * @param specId:   规格ID
     * @param number:   规格数量
     * @param memberId: 用户ID
     * @return SettlementDTO 去结算实体
     * @Author: LX 17839193044@162.com
     * @Description: 立即购买
     * @Date: 2019/6/17 14:14
     * @Version: V1.0
     */

    SettlementDTO buyNow(
            @RequestParam("memberId") Long memberId,
            @RequestParam("specId") Long specId,
            @RequestParam("number") int number);

    /**
     * 立即购买切换活动
     *
     * @param dto
     * @return
     * @date
     * @author 刘远杰
     **/

    SettlementDTO recountSettlementPrice(@RequestBody NowSettlementActivtyDTO dto);

    /**
     * 封装购物车信息
     *
     * @param list 购物车集合
     * @param type 0 查询全部购物车 1 查询购物车内商品正常的购物车
     * @return
     */

    ResultCartDTO getRedisCart(@RequestBody List<CartDTO> list,
                               @RequestParam(value = "type", required = false) Integer type);

    /**
     * 查询用户ID下选中的购物车数据
     *
     * @param memberId: 用户ID
     * @return List<CartDTO> 选中的购物车数据
     * @Author: LX 17839193044@162.com
     * @Description: 查询用户ID下选中的购物车数据
     * @Date: 2019/6/14 17:27
     * @Version: V1.0
     */

    List<CartDTO> findListBySelect(@RequestParam("memberId") Long memberId);


    /**
     * 功能描述:
     * 〈校验购物车是否合法，库存、价格变动，规格不存在，商品未展示，返回不通过的购物车集合〉
     *
     * @param goodsConverOrderList 购物车集合
     * @return 返回不通过的购物车集合
     * @author : 刘远杰
     */

    Map<String, List<CartGoodsDTO>> cartValidation(@RequestBody List<GoodsConverOrderDTO> goodsConverOrderList);

    /**
     * 删除用户所选中的购物车
     *
     * @param memberId 用户ID
     */

    void deleteIsSelectCart(@RequestParam("memberId") Long memberId);


    /**
     * 用户选中的购物车集合
     *
     * @param cartDTOList: 用户选中的购物车集合
     * @return 转化后的订单实体
     * @Author: LX 17839193044@162.com
     * @Description: 根据店铺拆分购物车（方便后期增加店铺活动校验等操作）
     * @Date: 2019/6/20 14:31
     * @Version: V1.0
     */

    List<GoodsConverOrderDTO> findVoListByCartIds(@RequestBody List<CartDTO> cartDTOList);

    /**
     * 查询购物车中是否有相同的商品
     *
     * @param specId   规格ID
     * @param memberId 用户ID
     * @return
     */

    CartDTO findCartGoodsNum(@RequestParam("specId") Long specId, @RequestParam("memberId") Long memberId);

    /**
     * 全选或者取消全选
     *
     * @param memberId 用户ID
     * @param storeId  店铺ID
     * @param state    选中状态
     */

    void updateIsSelect(@RequestParam("memberId") Long memberId,
                        @RequestParam(value = "storeId", required = false) Long storeId,
                        @RequestParam("state") Integer state);

    /**
     * 修改购物车商品状态
     *
     * @param updateCartStatusDTO 参数
     */

    void updateCartStatus(@RequestBody List<UpdateCartStatusDTO> updateCartStatusDTO);

    /**
     * 获取购物车中选中的商品id
     *
     * @param memberId
     * @return
     */

    List<FavoriteCartDTO> findGoodsIdsByMemberId(@RequestParam("memberId") Long memberId);

    /**
     * 查询用户购物车内商品数量
     *
     * @param memberId 用户ID
     * @return
     */

    Integer findCartNum(@RequestParam("memberId") Long memberId);

    /**
     * 更新购物车商品
     *
     * @param dto 参数
     */

    void updateCartGoods(@RequestBody SaveCartDTO dto);

    /**
     * 更新购物车，不更新更新时间
     *
     * @param cartDTOs
     */

    void updateCart(@RequestBody CartDTO cartDTO);

    /**
     * 根据店铺ID修改购物车商品状态
     *
     * @param storeId
     */

    void updateCartGoodsStatusByStoreId(@RequestParam("storeId") String storeId);

    /**
     * 活动结束删除购物车活动信息
     *
     * @param activityIds  活动id
     * @param activityType 活动类型
     * @return
     * @date
     * @author 刘远杰
     **/

    void deleteCartActivity(@RequestBody List<Long> activityIds, @RequestParam("activityType") Integer activityType);

    /**
     * 未登录加入购物车
     *
     * @param dto      j加入购物车实体
     * @param redisKey redisKey
     */

    void savaRedisCart(@RequestBody SaveCartDTO dto, @RequestParam("redisKey") String redisKey);

    /**
     * 未登录购物车切换规格
     *
     * @param dto
     * @param redisKey
     */

    void updateRedisCartSpec(@RequestBody SaveCartDTO dto, @RequestParam("redisKey") String redisKey);

    /**
     * 已登录切换规格
     *
     * @param dto
     * @param memberId
     */

    void updateEsCartSpec(@RequestBody SaveCartDTO dto, @RequestParam("memberId") Long memberId);

    /**
     * 功能描述：
     * 购物车列表
     *
     * @param memberId 用户id
     * @param type     H5 查询所有购物车， pc  正常购物车和失效购物车分别查询
     * @return 返回购物车列表信息
     * @author 宋文豪
     * @email: songwenhao@leimingtech.com
     * @Date : 2020/3/16
     **/

    ResultCartDTO getCartList(@RequestParam("memberId") Long memberId,
                              @RequestParam(value = "type", required = false) Integer type);

    /**
     * 发起拼团
     *
     * @param memberId  会员id
     * @param specId    商品规格id
     * @param number    购买数量
     * @param groupId   拼团活动id
     * @param addressId 会员地址id
     * @return
     * @date 2020-03-20 11:38
     * @author huangkeyuan@leimingtech.com
     **/

    SettlementDTO groupBuyNow(@RequestParam("memberId") Long memberId, @RequestParam("specId") Long specId, @RequestParam("number") int number,
                              @RequestParam("groupId") Long groupId, @RequestParam(value = "addressId", required = false) Long addressId);

    /**
     * 用户购物车商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */

    List<CartCmsDTO> getCartGoodIds(@RequestParam("memberId") Long memberId);

    /**
     * pc首页购物车弹窗列表
     *
     * @param cartDTOList 购物车数据
     * @param memberId    用户id
     * @return
     */

    PcIndexCartDTO pcIndexCart(@RequestBody List<CartDTO> cartDTOList,
                               @RequestParam(value = "memberId", required = false) Long memberId);

    /**
     * 查询失效购物车
     *
     * @param memberId
     * @return
     */

    ResultCartDTO disabledCart(@RequestParam("memberId") Long memberId);
}
