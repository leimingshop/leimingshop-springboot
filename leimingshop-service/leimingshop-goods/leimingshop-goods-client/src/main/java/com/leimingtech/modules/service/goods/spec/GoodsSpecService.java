/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.PartGoodsSpecDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsMobileDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSkuDetailDTO;
import com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO;
import com.leimingtech.modules.dto.goods.modify.ValidSpecSerial;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceAndStorageListDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecPriceUpdateDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecStorageUpdateDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecSaveDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecUpdateSkuDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecVersionDTO;
import com.leimingtech.modules.dto.goods.spec.list.GoodsSpecListDTO;
import com.leimingtech.modules.dto.spec.GoodsSpecShowDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品规格管理
 * @Date :2019/6/4 17:57
 * @Version V1.0
 **/
public interface GoodsSpecService {

    /**
     * 查询商品SKU列表并分页
     *
     * @param params 可变参数
     * @return 返回商品规格列表
     * @Author: xuzhch
     * @Description: 查询商品SKU列表并分页
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    PageData<GoodsSpecListDTO> pageSpec(@RequestParam Map<String, Object> params);

    /**
     * 查询所有商品规格信息
     *
     * @param params 可变参数
     * @return 返回商品规格信息
     * @Author: weixianchun
     * @Description: 查询所有商品规格信息
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    List<GoodsSpecDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据商品规格id查询信息
     *
     * @param id 商品规格id
     * @return 返回商品规格信息
     * @Author: weixianchun
     * @Description: 根据商品规格id查询信息
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    GoodsSpecDTO get(Long id);

    /**
     * 功能描述：
     * <根据规格id集合查询>
     *
     * @param ids 规格id集合
     * @return 返回商品规格信息
     * @date 2020/3/10 17:38
     * @author 刘远杰
     **/

    List<GoodsSpecDTO> getByIds(@RequestBody List<Long> ids);

    /**
     * 批量保存商品规格信息
     *
     * @param dtoList 实体
     * @Author: weixianchun
     * @Description: 批量保存商品规格信息
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void saveBatch(@RequestBody List<GoodsSpecSaveDTO> dtoList);

    /**
     * 修改商品规格信息
     *
     * @param dto 实体
     * @return 返回规格id
     * @Author: weixianchun
     * @Description: 修改商品规格信息
     * @Date :2019/6/7 1:53
     * @Version V1.0
     **/

    Long update(@RequestBody GoodsSpecUpdateSkuDTO dto);

    /**
     * 根据商品规格id删除商品规格信息
     *
     * @param ids 商品规格id
     * @Author: weixianchun
     * @Description: 根据商品规格id删除商品规格信息
     * @Date :2019/5/28 19:53
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 修改商品规格上下架状态
     *
     * @param dto 修改参数
     * @return 返回规格id
     * @Author: weixianchun
     * @Description: 修改商品规格上下架状态
     * @Date :2019/6/6 18:27
     * @Param specShow 规格上下架状态
     * @Version V1.0
     **/

    Long updateShow(@RequestBody GoodsSpecShowDTO dto);

    /**
     * 修改规格信息
     *
     * @param dto 实体
     */

    void updateSpec(@RequestBody GoodsSpecSaveDTO dto);

    /**
     * 根据商品ID查询商品规格集合
     *
     * @param goodsId 商品id
     * @return 返回规格信息
     */

    List<GoodsSpecDetailDTO> selectByGoodsId(Long goodsId);

    /**
     * 根据规格ID 查询规格详情
     *
     * @param specId 规格id
     * @return 返回规格信息
     */

    GoodsSkuDetailDTO getBySpecId(Long specId);

    /**
     * 根据规格id修改上下架状态(定时)
     *
     * @return 返回商品id
     * @Author: weixianchun
     * @Description: 根据规格id修改上下架状态(定时)
     * @Date :2019/6/14 10:02
     * @Version V1.0
     **/

    List<Long> updateShowTiming();

    /**
     * 根据商品id修改商品规格上下架状态
     *
     * @param goodsId  商品id
     * @param specShow 规格状态
     * @Author: weixianchun
     * @Description: 根据商品id修改商品规格上下架状态
     * @Date :2019/6/13 21:50
     * @Version V1.0
     **/

    void updateShowByGoodsId(@RequestParam("goodsId") Long goodsId, @RequestParam("specShow") int specShow);

    /**
     * 根据商品ID查询SKU价格和库存列表
     *
     * @param goodsId 商品id
     * @return
     */

    GoodsSpecPriceAndStorageListDTO selectListByGoodsId(Long goodsId);

    /**
     * 批量保存规格价格(修改操作)
     *
     * @param goodsSpecPriceUpdateDTOList 批量保存规格价格list
     * @return 返回商品id
     */

    Long updateSpecPrice(@RequestBody List<GoodsSpecPriceUpdateDTO> goodsSpecPriceUpdateDTOList);

    /**
     * 批量保存规格库存(修改操作)
     *
     * @param goodsSpecStorageUpdateDTOList 批量保存规格库存list
     */

    void updateSpecStorageLog(@RequestBody List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList);

    /**
     * 校验SKU是否重复
     *
     * @param specSerial
     * @return 返回規格编号
     */

    List<String> checkSpecSerial(@RequestBody ValidSpecSerial specSerial);

    /**
     * 功能描述:
     * 〈根据规格id查询商品〉
     *
     * @param specId 规格ID
     * @return 返回商品详情
     * @author : 许志成
     */

    GoodsMobileDTO findMobeilGoodsInfo(Long specId);

    /**
     * 功能描述:
     * 〈订单更新商品规格库存，保存库存修改记录〉
     *
     * @param params key 规格id value 修改数量，正数为扣除，复数为增加
     * @param type   0扣减库存操作 1增加库存操作（规格删除不操做）
     * @return 返回是否修改成功
     * @author : 刘远杰
     */

    boolean updateStorage(@RequestBody Map<String, String> params, @RequestParam("type") Integer type);

    /**
     * 功能描述:
     * 〈更新商品规格销量〉
     *
     * @param params key 规格id value 修改数量，正数为扣除，复数为增加
     * @param type   0规格不存在异常 1规格不存在放弃当前商品操作
     * @return 返回是否修改成功
     * @author : 刘远杰
     */

    boolean updateSale(@RequestBody Map<String, String> params, @RequestParam("type") Integer type);


    /**
     * 获取规格表中的乐观锁版本
     *
     * @param specId: 规格ID
     * @return 乐观锁版本号
     * @date 2019/6/26 23:37
     * @author lixiang
     **/

    GoodsSpecVersionDTO findVersionById(Long specId);

    /**
     * 根据商品id 删除规格
     *
     * @param id
     */

    void delBygoodsId(Long id);

    /**
     * 根据商品ids 删除规格
     *
     * @param goodsIds 商品ID数组
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);

    /**
     * 查询规格总库存
     *
     * @param goodsId 商品id
     * @return 返回库存
     */

    Integer selectCount(Long goodsId);

    /**
     * 修改商品主图
     *
     * @param id
     * @param specMainPic
     */

    void updateSpecMainPic(@RequestParam("id") Long id, @RequestParam("specMainPic") String specMainPic);

    /**
     * 查询规格id
     *
     * @param goodsIds 商品id
     * @return 返回规格id
     */

    List<Long> selectByGoodsIds(@RequestBody List<Long> goodsIds);

    /**
     * 根据规格ID 查询出商品ID 可以查询出已删除的商品
     *
     * @param specId 规格id
     * @return 返回商品id
     */

    Long findGoodsIdBySpecId(@RequestParam("specId") Long specId);

    /**
     * 根据规格ID 查询出店铺ID
     *
     * @param specId
     * @return 返回商品信息
     */

    GoodsDTO getStoreIdBySpecId(@RequestParam("specId") Long specId);

    /**
     * 查询当前店铺下预警数量
     *
     * @param storeId 店铺id
     * @param storage 库存值
     * @return 返回预警数量
     */

    Integer findCount(@RequestParam("storeId") Long storeId, @RequestParam("storage") Integer storage);

    /**
     * 根据商品ID 上下架规格信息
     *
     * @param ids       商品ID集合
     * @param goodsShow 上下架状态（2未上架,0下架状态，1上架状态）
     * @return
     * @date 2020/3/4/004 18:23
     * @author xuzhch
     **/

    void updateShowByGoodsIds(@RequestBody List<Long> ids, @RequestParam("goodsShow") Integer goodsShow);

    /**
     * 修改规格状态
     *
     * @param dto 修改参数
     */

    void updateSpecShow(@RequestBody GoodsSpecShowDTO dto);

    /**
     * 定时修改规格状态
     *
     * @param dto 修改参数
     */

    void timedUpdateSpecShow(@RequestBody GoodsSpecShowDTO dto);

    /**
     * 功能描述 根据goodsId查找spec
     *
     * @param goodsId
     * @return java.util.List<com.leimingtech.modules.dto.goods.detail.GoodsSpecDetailDTO>
     * @author lishuo
     * @date 28/6/2020
     */

    List<GoodsSpecDetailDTO> findSpecByGoodsId(@RequestParam("goodsId") Long goodsId);

    /**
     * 查询商品信息
     *
     * @param id
     * @return 返回商品信息
     */

    PartGoodsSpecDTO findGoodsSpecById(@RequestParam("id") Long id);

    /**
     * 功能描述 批量保存
     *
     * @param saveGoodsSpecTotalListDTO 保存参数
     * @return void
     * @author lishuo
     * @date 22/7/2020
     */

    void insertBatch(@RequestParam("saveGoodsSpecDTOListTotal") List<GoodsSpecSaveDTO> saveGoodsSpecTotalListDTO);

    /**
     * 获取商品总重量
     *
     * @param specList 规格集合
     * @return
     */

    Double getWeight(@RequestBody List<Long> specList);
}
