/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.PartGoodsSpecDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsMobileDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSkuDetailDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceAndStorageDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecSaveDTO;
import com.leimingtech.modules.dto.goods.spec.list.GoodsSpecListDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:54
 * @Version V1.0
 **/
@Mapper
public interface GoodsSpecDao extends BaseDao<GoodsSpecEntity> {

    /**
     * 查询商品SKU列表并分页
     *
     * @param params 查询条件
     * @return 返回规格信息
     * @Author: xuzhch
     * @Description: 查询商品SKU列表并分页
     * @Date :2019/6/6 18:17
     * @Version V1.0
     **/
    List<GoodsSpecListDTO> findListSku(Map<String, Object> params);

    /**
     * 根据规格id修改商品规格上下架状态
     *
     * @param specId   规格id
     * @param specShow 规格上下架状态
     * @Author: weixianchun
     * @Description: 根据规格id修改商品规格上下架状态
     * @Date :2019/6/27 15:40
     * @Version V1.0
     **/
    void updateShowBySpecId(@Param("id") Long specId, @Param("specShow") int specShow);

    /**
     * 根据商品id修改商品规格上下架状态
     *
     * @param goodsId  商品ID
     * @param specShow 规格状态
     * @Author: weixianchun
     * @Description: 根据商品id修改商品规格上下架状态
     * @Date :2019/6/13 21:50
     * @Version V1.0
     **/
    void updateShowByGoodsId(@Param("goodsId") Long goodsId, @Param("specShow") int specShow);

    /**
     * 根据商品id查询规格详细信息
     *
     * @param goodsId 商品ID
     * @return 返回规格信息
     * @Author: weixianchun
     * @Description: 根据商品id查询规格详细信息
     * @Date :2019/6/14 9:40
     * @Version V1.0
     **/
    List<GoodsSpecDetailDTO> selectByGoodsId(Long goodsId);


    /**
     * 根据商品ID查询SKU价格和库存列表
     *
     * @param goodsId 商品ID
     * @return 返回规格信息
     * @Description: 根据商品ID查询SKU价格和库存列表
     * @Date :2019/6/14 9:41
     * @Version V1.0
     **/
    List<GoodsSpecPriceAndStorageDTO> selectListByGoodsId(Long goodsId);

    /**
     * 根据商品id查询默认sku和库存列表
     *
     * @param goodsId 商品id
     * @return 返回规格信息
     */
    List<GoodsSpecPriceAndStorageDTO> selectDefaultByGoodsId(Long goodsId);

    /**
     * 手机端商品详情
     *
     * @param specId 规格id
     * @return 返回手机端商品详情
     */
    GoodsMobileDTO findMobeilGoodsInfo(Long specId);


    /**
     * SKU 编辑回显
     *
     * @param specId 规格Id
     * @return GoodsSkuDetailDTO 对象
     */
    GoodsSkuDetailDTO selectSkuDetailById(Long specId);

    /**
     * 查询总库存
     *
     * @param goodsId 商品id
     * @return 返回库存
     */
    Integer selectCountStorage(Long goodsId);

    /**
     * 修改商品默认主图
     *
     * @param id          商品id
     * @param specMainPic 规格主图
     */
    void updateSpecMainPic(@Param("id") Long id, @Param("specMainPic") String specMainPic);

    /**
     * 功能描述:
     * 〈批量修改库存，销量〉
     *
     * @param entityList
     * @return 返回修改成功数量
     * @author : 刘远杰
     */
    Integer updateStockBatchByIdAndVersion(@Param("entityList") List<GoodsSpecEntity> entityList);

    /**
     * 功能描述:
     * 〈批量修改库存，销量〉
     *
     * @param entityList 修改才桉树
     * @return 返回修改成功数量
     * @author : 刘远杰
     */
    Integer updateSaleBatchByIdAndVersion(@Param("entityList") List<GoodsSpecEntity> entityList);

    /**
     * 查询规格id
     *
     * @param goodsIds 商品id
     * @return 返回规格id
     */
    List<Long> selectSpecIdsByGoodsIds(@Param("goodsIds") List<Long> goodsIds);

    /**
     * 根据规格ID 查询出商品ID包括已经删除的商品
     *
     * @param specId 规格id
     * @return 返回 商品id
     */
    Long findGoodsIdBySpecId(Long specId);

    /**
     * 根据规格Id查询出店铺信息
     *
     * @param specId 规格id
     * @return 返回商品信息
     */
    GoodsDTO getStoreIdBySpecId(Long specId);

    /**
     * 查询当前店铺下库存预警商品数量
     *
     * @param storeId 店铺id
     * @param storage 库存
     * @return 返回山沟数量
     */
    Integer findCount(@Param("storeId") Long storeId, @Param("storage") Integer storage);

    /**
     * 查询规格信息
     *
     * @param id 主键id
     * @return 返回商品信息
     */
    PartGoodsSpecDTO findGoodsSpecById(Long id);

    /**
     * 批量插入
     *
     * @param goodsSpecEntities 保存信息
     */
    void insertBatch(@Param("goodsSpecEntities") List<GoodsSpecSaveDTO> goodsSpecEntities);

    /**
     * 查询商品总重量
     *
     * @param specList 规格集合
     * @return
     */
    Double getWeight(@Param("list") List<Long> specList);
}
