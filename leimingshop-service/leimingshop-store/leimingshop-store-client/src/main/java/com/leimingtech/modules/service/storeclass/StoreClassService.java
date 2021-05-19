/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.storeclass;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.store.UpdateStoreInfo;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺分类表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreClassService {

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<StoreClassDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreClassDTO get(Long id);

    /**
     * 新增数据
     *
     * @param storeClassDTOList 参数
     */

    void save(@RequestBody List<StoreClassDTO> storeClassDTOList);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody StoreClassDTO dto);


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据店铺 ID 获取店铺分类
     *
     * @param storeId 店铺ID
     * @return
     */

    List<StoreClassDTO> findByStoreId(@RequestParam("storeId") Long storeId);

    /**
     * 批量更新店铺分类
     *
     * @param dto 更新参数
     * @return
     */

    void updateBatch(@RequestBody UpdateStoreInfo dto);

    /**
     * 查询店铺关联的商品分类
     *
     * @param storeId
     * @return
     * @author xuzhch
     */

    List<StoreGoodsClassDTO> selectStoreClass(Long storeId);

    /**
     * 根据分类ID 删除当前分类关联的店铺
     *
     * @param ids 分类ID
     */

    void deleteByClassId(@RequestBody Long[] ids);

    /**
     * 根据店铺ID 删除分类
     *
     * @param storeId
     */

    void deleteByStoreId(@RequestParam("storeId") Long storeId);
}