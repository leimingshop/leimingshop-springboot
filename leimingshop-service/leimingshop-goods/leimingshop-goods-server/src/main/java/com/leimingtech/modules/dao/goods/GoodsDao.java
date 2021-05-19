/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.attribute.AttributeDTO;
import com.leimingtech.modules.dto.esgoods.EsGoodsDTO;
import com.leimingtech.modules.dto.esgoods.GoodsAddAttributeDTO;
import com.leimingtech.modules.dto.goods.*;
import com.leimingtech.modules.dto.goods.cms.CircleGoodsSearchDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsDatailDTO;
import com.leimingtech.modules.dto.goods.index.IndexGoodsDataDTO;
import com.leimingtech.modules.entity.goods.GoodsEntity;
import com.leimingtech.modules.excel.goods.EasyGoodsCheckExcel;
import com.leimingtech.modules.excel.goods.EasyGoodsExcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Mapper
public interface GoodsDao extends BaseDao<GoodsEntity> {

    /**
     * 更新商品状态
     *
     * @param id        商品id
     * @param goodsShow 商品状态
     */
    void updateShow(@Param("id") Long id, @Param("goodsShow") int goodsShow);

    /**
     * 查询列表
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回商品信息
     */
    List<GoodsListDTO> selectGoodsList(Page page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述：
     * <添加活动商品分页>
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return 返回活动商品
     * @date 2020/3/10 10:38
     * @author 刘远杰
     **/
    List<ActivitityGoodsInfoDTO> selectGoodsAndStorageList(IPage page, @Param("params") Map<String, Object> params);

    /**
     * 获取商品基础信息
     *
     * @param goodsId 商品id
     * @return 返回商品信息
     * @author xuzhch
     */
    GoodsDatailDTO getDetail(Long goodsId);

    /**
     * 批量修改商品状态
     *
     * @param idList      商品id
     * @param goodsStatus 商品状态
     */
    void updateBatch(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList, @Param("goodsStatus") Integer goodsStatus);

    /**
     * 查询所有商品
     *
     * @param page 分页信息
     * @return 返回商品信息
     */
    List<EsGoodsDTO> findAllGoods(IPage page);

    /**
     * 查询属性信息
     *
     * @param item
     * @return 返回商品属性信息
     */
    List<GoodsAddAttributeDTO> goodsAttr(List item);

    /**
     * 查询属性信息
     *
     * @param page 分页信息
     * @return 返回属性信息
     */
    List<AttributeDTO> attrIndex(IPage page);

    /**
     * 功能描述:
     * 〈根据规格id查询商品〉
     *
     * @param specId 规格id
     * @return 返回商品信息
     * @author : 刘远杰
     */
    GoodsEntity findGoodsBySpecId(Long specId);

    /**
     * 根据品牌id查询品牌数量
     *
     * @param brandId 品牌id
     * @return 返回品牌数量
     * @Author: weixianchun
     * @Description: 根据品牌id查询品牌数量
     * @Date :2019/6/26 10:41
     * @Version V1.0
     **/
    int findCountByBrandId(Long brandId);

    /**
     * 根据分类id查询分类数量
     *
     * @param classIds 分类id
     * @return 返回分类数量
     * @Author: weixianchun
     * @Description: 根据分类id查询分类数量
     * @Date :2019/7/8 11:51
     * @Version V1.0
     **/
    List<String> findCountByClassId(Long[] classIds);


    /**
     * 更新店铺名称
     *
     * @param storeName 商品实体
     * @param storeId   店铺ID
     * @param storeType 店铺类型
     */
    void updateStoreName(@Param("storeName") String storeName,
                         @Param("storeId") Long storeId,
                         @Param("storeType") Integer storeType);


    /**
     * 根据(默认)规格id修改商品价格
     *
     * @param specId        规格id
     * @param specSellPrice 规格销售价
     * @param specCostPrice 规格成本价
     */
    void updatePriceBySpecId(@Param("specId") Long specId,
                             @Param("specSellPrice") BigDecimal specSellPrice,
                             @Param("specCostPrice") BigDecimal specCostPrice);


    /**
     * 删除店铺下的商品
     *
     * @param ids 店铺ID
     */
    void deleteByStoreId(@Param("ids") Long[] ids);


    /**
     * 商品上架更新上架时间
     *
     * @param id        商品ID
     * @param uptime    上架时间
     * @param goodsShow 上架状态
     */
    void updateUpShow(@Param("id") Long id, @Param("uptime") Date uptime, @Param("goodsShow") Integer goodsShow);

    /**
     * 、
     * 下架所有商品
     *
     * @param storeId
     */
    void updateShowByStoreId(@Param("storeId") Long storeId);

    /**
     * 拒绝所有待审核商品
     *
     * @param storeId
     */
    void updateStatus(Long storeId);

    /**
     * 删除定时上下架
     *
     * @param storeId
     */
    void deleteGoodsTime(Long storeId);

    /**
     * 下架所有规格
     *
     * @param storeId
     */
    void updateGoodsSpecShow(Long storeId);

    /**
     * 查询分类信息
     *
     * @param classLists 分类id
     * @return 返回分类信息
     */
    List<Map<String, Object>> selectCountByClassIds(@Param("classLists") List<Long> classLists);

    /**
     * 查询店铺商品数量
     *
     * @param storeId 店铺id
     * @param type    查询类型
     * @return 返回店铺下商品数量
     */
    Integer findStoreGoodsNum(@Param("storeId") Long storeId, @Param("type") Integer type);

    /**
     * 查询商品审核信息
     *
     * @param page   分页信息
     * @param params 查询参数
     * @return 返回商品信息
     */
    List<GoodsListDTO> pageVerify(Page page, @Param("params") Map<String, Object> params);


    /**
     * 功能描述:
     * <根据标签推荐id查询关联商品信息并分页>
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 标签关联的商品信息
     * @date 2020/1/10 9:46
     * @author weixianchun
     **/
    List<GoodsListDTO> findByLabelId(Page page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述:
     * <查询未关联商品信息并分页>
     *
     * @param params 查询调价
     * @param page   分页信息
     * @return 标签未关联的商品信息
     * @date 2020/1/10 15:28
     * @author weixianchun
     **/
    List<GoodsListDTO> labelUnassociated(Page page, @Param("params") Map<String, Object> params);

    /**
     * 批量更新商品状态
     *
     * @param dto  更新参数
     * @param date 日期
     */
    void updateGoodsShowBatch(@Param("dto") GoodsShowDTO dto, @Param("date") Date date);

    /**
     * 商品导出
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回商品信息
     */
    List<EasyGoodsExcel> goodsExport(Page<EasyGoodsExcel> page, @Param("params") Map<String, Object> params);

    /**
     * 查询店铺商品实况信息
     *
     * @param storeId 店铺id
     * @return 返回商品实况信息
     */
    GoodsLiveDTO goodsLive(Long storeId);

    /**
     * 查询首页商品数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 返回首页商品数据
     */
    IndexGoodsDataDTO selectIndexGoodsData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 指定商品分页搜索
     *
     * @param page    分页信息
     * @param goodIds 商品id
     * @return
     * @Author: yuhaifeng
     */
    List<CircleGoodsSearchDTO> circleGoodsSearch(Page page, @Param("goodIds") Set<Long> goodIds);

    /**
     * 查询商品审核数据导出
     *
     * @param page   分页信息
     * @param params 查询条件
     * @return 返回商品审核数据
     */
    List<EasyGoodsCheckExcel> goodsCheckExport(Page<EasyGoodsCheckExcel> page, Map<String, Object> params);

    /**
     * 修改商品数据
     *
     * @param entity 修改参数有
     * @return 返回修改数量
     */
    int editGoods(GoodsEntity entity);

    /**
     * 查询店铺商品分类下是否关联商品
     *
     * @param ids 分类id
     * @return
     */
    List<String> findClassName(@Param("ids") Long[] ids);

    /**
     * '
     * 查询店铺商品分类下的商品数量
     *
     * @param classLists 分类id
     * @return
     */
    List<Map<String, Object>> findClassGoodsCount(@Param("list") List<Long> classLists);

    /**
     * 查询楼层相关商品
     *
     * @param collect
     * @return
     */
    List<GoodsDTO> findNamePricePic(@Param("list") List<String> collect);

    /**
     * 查询店铺商品推荐
     *
     * @param storeId             店铺id
     * @param limit               分页信息
     * @param storeGoodsFirstGcId 店铺商品分类
     * @return 返回商品信息
     */
    List<GoodsDTO> storeGoodsRecommend(@Param("storeId") Long storeId,
                                       @Param("limit") Integer limit,
                                       @Param("storeGoodsFirstGcId") Long storeGoodsFirstGcId);


    /**
     * 功能描述 根据品牌的名称 找id
     *
     * @param goodsBrandName 品牌的名称
     * @return Long  品牌的id
     * @author lishuo
     * @date 24/6/2020
     */
    Long findBrandByName(String goodsBrandName);

    /**
     * 功能描述 根据店铺id 查找商品的信息
     *
     * @param page   分页信息
     * @param params 查询参数
     * @return java.util.List<com.leimingtech.modules.dto.goods.GoodsExportDTO>
     * @author lishuo
     * @date 2/7/2020
     */
    List<GoodsExportDTO> selectGoodsToExcel(Page<GoodsExportDTO> page, @Param("params") Map<String, Object> params);

    /**
     * 购物车校验商品信息
     *
     * @param goodsId 商品ID
     * @param specId  规格ID
     * @return 返回商品信息
     */
    ValidateGoodsDTO findValidationGoods(@Param("goodsId") Long goodsId, @Param("specId") Long specId);


    /**
     * 查询已关联的商品信息
     *
     * @param goodsId 商品id
     * @return 返回商品信息
     */
    GoodsListDTO getGoodsInfo(Long goodsId);

    /**
     * 功能描述 批量保存goods list
     *
     * @param goodsEntityList 保存参数
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */

    void insertBatch(@Param("goodsEntityList") List<GoodsEntity> goodsEntityList);

    /**
     * 查询商品导出数据
     *
     * @param params 查询参数
     * @return 返回商品数据
     */
    List<GoodsExportDTO> selectGoodsToExcelTest(@Param("params") Map<String, Object> params);

    /**
     * 查询推荐商品
     *
     * @param idList 商品id
     * @return 返回商品信息
     */
    List<GoodsRecommendDTO> getGoodsList(@Param("list") List<String> idList);

    /**
     * 查询店铺
     *
     * @param goodsIds 商品id
     * @return 返回数量
     */
    Integer selectStoreCountByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 功能描述 分页查询goods
     *
     * @param page:   分页参数
     * @param params: 查询条件
     * @return java.util.List<com.leimingtech.modules.dto.goods.GoodsDTO>
     * @author lishuo
     * @date 2020/7/27 14:23
     **/
    List<GoodsDTO> selectByPage(Page page, @Param("params") Map<String, Object> params);

    /**
     * 功能描述 根据商品id 查询商品的信息
     *
     * @param goodsId: 商品id
     * @author lishuo
     * @date 2020/8/26 13:56
     * @return: com.leimingtech.modules.dto.goods.GoodsDTO
     **/
    GoodsDTO selectByIdToRec(Long goodsId);

    /**
     * 根据商品id查询商品信息
     *
     * @param goodsList 商品id集合
     * @return
     */
    List<TopicGoodsDTO> selectGoodsInfoByGoodsId(@Param("list") List<Long> goodsList);

    /**
     * 楼层关联商品分页查询
     *
     * @param page   分页信息
     * @param params 查询参数
     * @return
     */
    List<GoodsListDTO> floorGoodsPage(Page page, @Param("params") Map<String, Object> params);
}
