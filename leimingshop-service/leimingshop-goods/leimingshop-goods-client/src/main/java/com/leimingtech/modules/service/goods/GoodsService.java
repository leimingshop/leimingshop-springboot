/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.*;
import com.leimingtech.modules.dto.goods.check.GoodsCheckSaveDTO;
import com.leimingtech.modules.dto.goods.cms.CircleGoodsSearchDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsDatailDTO;
import com.leimingtech.modules.dto.goods.index.IndexGoodsDataDTO;
import com.leimingtech.modules.dto.goods.modify.GoodsModifyDTO;
import com.leimingtech.modules.dto.goods.modify.ValidSpecSerial;
import com.leimingtech.modules.excel.goods.EasyGoodsCheckExcel;
import com.leimingtech.modules.excel.goods.EasyGoodsExcel;
import com.leimingtech.modules.excel.goods.GoodsTemplateExcel;
import com.leimingtech.modules.vo.GoodsImportResultVO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 商品表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */

public interface GoodsService {

    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    PageData<GoodsListDTO> pageGoods(@RequestParam Map<String, Object> params);


    /**
     * 根据商品id获取商品信息
     *
     * @param goodsList 商品id集合
     * @return
     */

    List<TopicGoodsDTO> selectGoodsInfoByGoodsId(@RequestBody List<Long> goodsList);

    /**
     * 功能描述：
     * <添加活动商品分页>
     *
     * @param params 查询条件
     * @return
     * @date 2020/3/10 10:43
     * @author 刘远杰
     **/

    PageData<ActivitityGoodsInfoDTO> selectGoodsAndStoragePage(@RequestParam Map<String, Object> params);

    /**
     * 列表
     *
     * @param params
     * @return
     */

    List<GoodsDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */

    GoodsDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据ids查询商品集合〉
     *
     * @param goodsIds 商品spuid
     * @param storeId  店铺id
     * @return 返回商品信息
     * @author : 刘远杰
     */

    List<GoodsDTO> getByGoodsIds(@RequestBody List<Long> goodsIds,
                                 @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 保存
     *
     * @param dto 保存参数
     * @return 返回商品id
     */

    Long save(@RequestBody GoodsConserveDTO dto);

    /**
     * 修改商品基本信息
     *
     * @param dto 修改参数
     * @return 返回商品id
     */

    Long update(@RequestBody GoodsModifyDTO dto);

    /**
     * 查询详情
     *
     * @param goodsId 商品id
     * @return 返回商品详情
     */

    GoodsDatailDTO getDetail(Long goodsId);

    /**
     * 删除用户
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);


    /**
     * 定时任务调用修改上下架/发布 状态
     * 1、先判断是否有可执行的定时任务
     * 2、修改上下架或发布状态
     *
     * @return 店铺ID集合
     * @date 2019/12/31/031 10:15
     * @author xuzhch
     **/

    List<Long> updateShowTiming();

    /**
     * 暂未使用该接口
     *
     * @param id           商品ID
     * @param operatorType 操作类型
     * @return
     * @date 2019/12/31/031 10:20
     * @author xuzhch
     **/

    void updateStatusTiming(Long id, @RequestParam("operatorType") Integer operatorType);


    /**
     * 审核商品
     *
     * @param dto 商品审核对象
     * @return
     * @date 2019/12/31/031 10:19
     * @author xuzhch
     **/

    void checkGoodsStatus(@RequestBody GoodsCheckSaveDTO dto);

    /**
     * 校验货号是否重复
     *
     * @param id 主键id
     * @return 返回是否重复
     */

    Map<String, Object> checkGoodsSerial(Long id);


    /**
     * 根据店铺Id查询商品数量
     *
     * @param storeId 店铺id
     * @return 返回商品数量
     */

    Integer selectCount(Long storeId);

    /**
     * 功能描述:
     * 〈根据规格id查询商品〉
     *
     * @param specId 规格id
     * @return 返回商品信息
     * @author : 刘远杰
     */


    GoodsDTO findGoodsBySpecId(@RequestParam("specId") Long specId);

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

    int findCountByBrandId(@RequestParam("brandId") Long brandId);

    /**
     * 根据分类id查询分类名称
     *
     * @param classIds 分类id
     * @return 返回分类名称
     * @Author: weixianchun
     * @Description: 根据分类id查询分类名称
     * @Date :2019/7/8 11:51
     * @Version V1.0
     **/

    List<String> findCountByClassId(@RequestBody Long[] classIds);


    /**
     * 修改商品表中店铺名称
     *
     * @param storeName 店铺名称
     * @param storeId   店铺ID
     * @param storeType 店铺类型
     * @return
     * @date 2019/12/31/031 10:25
     * @author xuzhch
     **/

    void updateStoreName(@RequestParam("storeName") String storeName,
                         @RequestParam("storeId") Long storeId,
                         @RequestParam("storeType") Integer storeType);


    /**
     * 根据(默认)规格id修改商品价格
     *
     * @param specId        规格id
     * @param specSellPrice 销售价
     * @param specCostPrice 成本价
     * @Author: weixianchun
     * @Description: 根据(默认)规格id修改商品价格
     * @Date :2019/7/16 21:33
     * @Version V1.0
     **/

    void updatePriceBySpecId(@RequestParam("specId") Long specId,
                             @RequestParam("specSellPrice") BigDecimal specSellPrice,
                             @RequestParam("specCostPrice") BigDecimal specCostPrice);


    /**
     * 删除店铺下的商品
     *
     * @param ids 店铺ID
     */

    void deleteByStoreId(@RequestBody Long[] ids);

    /**
     * 修改上下架状态
     *
     * @param id        商品id
     * @param goodsShow 商品状态
     */

    void updateShowNow(@RequestParam("id") Long id, @RequestParam("goodsShow") Integer goodsShow);

    /**
     * 校验店铺已发布的商品数量是否超出店铺等级允许的已发布数量
     *
     * @param goodsIds 商品ID集合
     * @return 返回是否超出限制
     */

    Boolean validatePublishGoodsNum(@RequestBody Long[] goodsIds);

    /**
     * 查询发布商品数量
     *
     * @param ids 主键id
     * @return
     */

    Boolean adminValidatePublishGoodsNum(@RequestBody Long[] ids);


    /*************************************2019-11-18 接口新增************************************************/

    /**
     * 校验商品货号是否重复
     *
     * @param validSpecSerial 商品货号
     * @return 返回重复的货号
     * @author xuzhch 2019年11月18日
     */

    List<String> validSpecSerials(@RequestBody ValidSpecSerial validSpecSerial);


    /**
     * 校验商品参数是否合法
     *
     * @param dto 查询参数
     * @return 返回状态码和信息
     * @author xuzhch 2019年11月18日
     */

    Map<String, String> validGoodsParams(@RequestBody GoodsConserveDTO dto);

    /**
     * 根据店铺ID 下架所有的商品，规格
     *
     * @param storeId 店铺id
     */

    void updateShowByStoreId(@RequestParam("storeId") Long storeId);


    /**
     * 查询店铺下的商品数量
     *
     * @param storeId 店铺ID
     * @param type    商品状态 1 审核拒绝 2 审核通过
     * @return 商品数量
     * @date 2019年12月31日10:52
     * @author xuzhch
     */

    Integer findGoodsCount(@RequestParam("storeId") Long storeId, @RequestParam(value = "type", required = false) Integer type);

    /**
     * 根据后台分类ID集合 查询商品数量 并按照分类ID分组
     *
     * @param classLists 分类ID集合
     * @return
     * @date 2019/12/31/031 10:49
     * @author xuzhch
     **/

    List<Map<String, Object>> selectCountByClassIds(@RequestBody List<Long> classLists);

    /**
     * 商品审核列表
     *
     * @param params 参数
     * @return
     * @date 2020/1/7/007 14:27
     * @author xuzhch
     **/

    PageData<GoodsListDTO> pageVerify(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * <根据标签推荐id查询关联商品信息>
     *
     * @param params 查询参数
     * @return 标签关联的商品信息
     * @date 2020/1/10 9:46
     * @author weixianchun
     **/

    PageData<GoodsListDTO> findByLabelId(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * <查询未关联商品信息并分页>
     *
     * @param params 查询参数
     * @return 标签未关联的商品信息
     * @date 2020/1/10 15:28
     * @author weixianchun
     **/

    PageData<GoodsListDTO> labelUnassociated(@RequestParam Map<String, Object> params);


    /**
     * 商品上下架操作
     *
     * @param dto
     * @return
     * @date 2020/3/4 15:15
     * @author xuzhch
     **/

    void updateGoodsShow(@RequestBody GoodsShowDTO dto);

    /**
     * 商品导出
     *
     * @param params 查询参数
     * @return 返回商品数据
     */

    PageData<EasyGoodsExcel> goodsExport(@RequestParam Map<String, Object> params);

    /**
     * 商品上下架操作
     *
     * @param dto
     * @return
     * @date 2020/3/4 15:15
     * @author xuzhch
     **/

    void timedUpdateGoodsShow(@RequestBody GoodsShowDTO dto);

    /**
     * 店铺商品实况
     *
     * @param storeId
     * @param type    是否刷新数据 1 刷新
     * @return 返回商品信息
     */

    GoodsLiveDTO goodsLive(@RequestParam("storeId") Long storeId, @RequestParam(value = "type", required = false) Integer type);

    /**
     * 首页>基础概况>商品数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexGoodsDataDTO
     * @date 2020/4/7/007 12:00
     * @author xuzhch
     */

    IndexGoodsDataDTO indexGoodsData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr);


    /**
     * 指定商品分页搜索
     *
     * @param params
     * @param goodIds
     * @return
     * @Author: yuhaifeng
     */

    PageData<CircleGoodsSearchDTO> circleGoodsSearch(@RequestParam Map<String, Object> params, @RequestParam Set<Long> goodIds);

    /**
     * 商品审核导出
     *
     * @param params 查询参数
     * @return 返回商品审核信息
     */

    PageData<EasyGoodsCheckExcel> goodsCheckExport(@RequestParam Map<String, Object> params);

    /**
     * 功能描述：
     * <查询运费模板关联商品数量>
     *
     * @param freightTemplateId 运费模板id
     * @return
     * @date 2020/4/24 14:43
     * @author 刘远杰
     **/

    Integer countGoodsByFreightTemplateId(@RequestParam("freightTemplateId") Long freightTemplateId);

    /**
     * 功能描述：
     * <更新商品运费模板>
     *
     * @param oldFreightTemplateId
     * @param newFreightTemplateId
     * @param storeId
     * @return
     * @date 2020/4/24 17:31
     * @author 刘远杰
     **/

    void updateGoodsFreightTemplate(@RequestParam("oldFreightTemplateId") Long oldFreightTemplateId,
                                    @RequestParam("newFreightTemplateId") Long newFreightTemplateId,
                                    @RequestParam("storeId") Long storeId);

    /**
     * 查询店铺商品分类下面是否关联商品
     *
     * @param ids
     * @return
     */

    List<String> findClassName(@RequestBody Long[] ids);

    /**
     * 查询店铺分类下的商品数量
     *
     * @param classLists
     * @return
     */

    List<Map<String, Object>> findClassGoodsCount(@RequestBody List<Long> classLists);

    /**
     * 查询楼层关联的商品
     *
     * @param collect
     * @return
     */

    List<GoodsDTO> findNamePricePic(@RequestBody List<String> collect);

    /**
     * 店铺商品推荐
     *
     * @param storeId             店铺Id
     * @param storeGoodsFirstGcId 店铺商品分类Id
     * @param limit               显示条数
     * @return
     */

    List<GoodsDTO> storeGoodsRecommend(@RequestParam("storeId") Long storeId,
                                       @RequestParam(value = "storeGoodsFirstGcId", required = false) Long storeGoodsFirstGcId,
                                       @RequestParam("limit") Integer limit);

    /**
     * 更新商品信息
     *
     * @param goodsDTO 更新参数
     */

    void updateGoods(@RequestBody GoodsDTO goodsDTO);

    /**
     * 功能描述 从excel中导入商品信息
     *
     * @param file    要导入的excel文件
     * @param storeId 卖家信息
     * @return GoodsImportResultVO 成功导入与失败
     * @author lishuo
     * @date 23/6/2020
     */
    GoodsImportResultVO importGoods(@RequestPart("file") MultipartFile file, @RequestParam(name = "storeId") Long storeId);

    /**
     * 功能描述 导出商品的数据到模板中
     *
     * @param params 店铺id 分页参数
     * @return java.util.List<com.leimingtech.modules.excel.goods.GoodsTemplateExcel>
     * @author lishuo
     * @date 24/6/2020
     */

    List<GoodsTemplateExcel> exportGoodsToExcel(@RequestParam Map<String, Object> params);

    /**
     * 购物车校验商品信息
     *
     * @param goodsId 商品ID
     * @param specId  规格ID
     * @return
     */

    ValidateGoodsDTO findValidationGoods(@RequestParam("goodsId") Long goodsId, @RequestParam("specId") Long specId);

    /**
     * 功能描述 根据商品的id 查询商品的信息
     *
     * @param goodsId
     * @return com.leimingtech.modules.dto.goods.GoodsDTO
     * @author lishuo
     * @date 2020/7/27
     */

    GoodsDTO findByGoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 功能描述 分页查找
     *
     * @param params: 参数
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goods.GoodsDTO>
     * @author lishuo
     * @date 2020/7/27 14:12
     **/

    PageData<GoodsDTO> pageGoodsDTO(@RequestParam Map<String, Object> params);

    /**
     * 商品推荐
     *
     * @param goodsId 商品ID
     * @param userId  用户ID
     * @param addFlag 是否补充商品（0：不补充 1：补充）
     * @return 返回推荐商品
     */
//
//    List<GoodsRecommendDTO> hotRecommend(@RequestParam(value = "goodsId", required = false) Long goodsId,
//                                         @RequestParam(value = "userId", required = false) Long userId,
//                                         @RequestParam("addFlag") Integer addFlag);

    /**
     * 查询店铺数量
     *
     * @param goodsIds 商品id
     * @return 返回店铺数量
     */

    Integer selectStoreCountByGoodsIds(@RequestBody List<Long> goodsIds);

    /**
     * 批量删除信息
     *
     * @param goodsIds 商品id
     */

    void deleteBatch(@RequestBody Long[] goodsIds);
}
