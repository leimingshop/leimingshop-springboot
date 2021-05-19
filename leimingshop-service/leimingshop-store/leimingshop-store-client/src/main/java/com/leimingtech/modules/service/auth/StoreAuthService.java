/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.auth;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.auth.StoreAuthDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 店铺认证信息表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreAuthService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<StoreAuthDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreAuthDTO get(Long id);

    /**
     * 新增数据
     *
     * @param dto 参数实体
     */

    void save(@RequestBody StoreAuthDTO dto);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody StoreAuthDTO dto);


    /**
     * 根据ID删除
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据店铺 ID 获取店铺认证
     *
     * @param storeId 店铺ID
     * @return
     */

    StoreAuthDTO findByStoreId(@RequestParam("storeId") Long storeId);

    /**
     * 根据店铺ID 删除店铺认证信息
     *
     * @param ids 店铺ID
     */

    void deleteByStoreId(@RequestBody Long[] ids);

    /**
     * 获取店铺公司id
     *
     * @param storeId
     * @return
     */

    Long getStoreAuthId(@RequestParam("storeId") Long storeId);

}