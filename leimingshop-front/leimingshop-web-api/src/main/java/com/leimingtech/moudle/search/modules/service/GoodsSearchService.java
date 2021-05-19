/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.search.modules.service;

import com.leimingtech.moudle.search.modules.dto.*;
import com.leimingtech.moudle.search.modules.dto.optimize.OptimizeSpecGoodsDetailVO;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 商品搜索
 *
 * @author chengqian
 */
public interface GoodsSearchService {

    /**
     * 根据条件做商品搜索
     *
     * @param goodsSearchDTO
     * @return
     */
    GoodsSearchVO goodsSerarch(GoodsSearchDTO goodsSearchDTO);


    /**
     * 商品筛选条件搜索（只返回商品搜素接口）
     *
     * @param filterDTO: 商品搜索筛选条件
     * @return: 商品搜索结果集
     * @date 2019/7/26 15:01
     * @author lixiang
     **/
    GoodsSearchFilterVO goodsFilterSerarch(GoodsSearchFilterDTO filterDTO);

    /**
     * 功能描述:
     * 〈优惠券去凑单商品列表页面〉
     *
     * @param goCollectBillsDTO 去凑单实体
     * @param memberId          会员id
     * @param request           请求信息
     * @return 返回优惠券商品
     * @author : 刘远杰
     */
    CouponsGoodsPageDTO collectBillsGoods(GoCollectBillsDTO goCollectBillsDTO, Long memberId, HttpServletRequest request);

    /**
     * 功能描述:
     * 〈满减活动去凑单商品列表页面〉
     *
     * @param goCollectBillsDTO 去凑单实体
     * @param memberId          会员id
     * @param request           请求信息
     * @return 返回满减商品信息
     * @author : 刘远杰
     */
    ReduceGoodsPageDTO collectBillsReduceGoods(GoCollectBillsDTO goCollectBillsDTO, Long memberId, HttpServletRequest request);

    /**
     * 查询所有sku
     *
     * @param specId 规格id
     * @throws IOException
     */
//    void findSkuAll(Long specId) throws IOException;

    /**
     * 规格id查询商品详情
     *
     * @param specId  规格id
     * @param goodsId 商品id
     * @return 返回商品详情
     */
    SpecGoodsDetailVO goodsSerarchSpec(Long specId, Long goodsId);


    /**
     * 搜索店铺
     *
     * @param storeSearchDTO 搜索参数
     * @return 返回店铺信息
     */
    StoreSearchFilterVO storeSerarch(SearchStoreDTO storeSearchDTO);

    /**
     * 根据关键字智能提示（支持中文、拼音）
     *
     * @param keyword: 搜索关键字
     * @return 智能提示集合
     * @date 2019/12/17 16:00
     * @author lixiangx@leimingtech.com
     **/
    List<String> intellisenseWord(String keyword);

    /**
     * 商品详情
     *
     * @param specId  规格id
     * @param goodsId 商品id
     * @return 返回商品详情
     */
    OptimizeSpecGoodsDetailVO goodsSerarchSpecOptimize(Long specId, Long goodsId);
}
