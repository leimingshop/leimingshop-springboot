/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.modules.dto.auth.SettingStoreAuthDTO;
import com.leimingtech.modules.dto.index.IndexDTO;
import com.leimingtech.modules.dto.store.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */

public interface StoreService {
    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<PageStoreDTO> findPage(@RequestParam Map<String, Object> params);

    /**
     * 入住待审核分页
     *
     * @param params
     * @return
     */

    PageData<PageStoreDTO> findAuditPage(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    StoreDTO get(Long id);

    /**
     * 新增店铺
     *
     * @param dto 新增参数
     * @return 返回店铺id
     */

    Long saveEntity(@RequestBody StoreDTO dto);

    /**
     * 修改信息
     *
     * @param dto 参数提示
     */

    void update(@RequestBody UpdateStoreInfo dto);


    /**
     * 根据ID删除
     *
     * @param ids 店铺ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出查询
     *
     * @param params 查询参数
     * @return
     */

    List<StoreDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 店铺首页
     *
     * @param storeId 店铺id
     * @param userId  用户id
     * @return 返回店铺首页数据
     */

    StoreIndexDTO selectStoreIndex(Long storeId, @RequestParam("userId") Long userId);

    /**
     * 、
     * 店铺详情
     *
     * @param storeId 店铺id
     * @param userId  用户id
     * @return 返回店铺详情
     */

    StoreInfoDTO selectStoreInfo(Long storeId, @RequestParam(value = "userId", required = false) Long userId);

    /**
     * 更新店铺信息
     *
     * @param settingStoreAuthDTO 更新参数
     */

    void updateSetting(@RequestBody SettingStoreAuthDTO settingStoreAuthDTO);

    /**
     * 移动端商品详情页 店铺数据
     *
     * @param storeId 店铺id
     * @param userId  用户id
     * @return 返回H5店铺详情
     */

    GoodsInfoStoreDTO selectGoodsInfoStore(Long storeId, @RequestParam(value = "userId", required = false) Long userId);

    /**
     * 获取用户下所有店铺
     *
     * @param userId 用户ID
     * @return
     */

    List<UserAllStoreDTO> findByUserId(@RequestParam("userId") Long userId);

    /**
     * 根据店铺等级查询是否有关联店铺
     *
     * @param gradeId
     * @return
     */

    Integer selectStoreByGrade(Long gradeId);

    /**
     * 查询店铺名称是否重复
     *
     * @param storeName 店铺名称
     * @param storeId   店铺ID
     * @return
     */

    Integer findStoreName(@RequestParam("storeName") String storeName,
                          @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 查询店铺等级关联的店铺数量
     *
     * @param gradeId 等级ID
     * @return 返回店铺数量
     */

    Integer findGradeCount(@RequestParam("gradeId") Long gradeId);

    /**
     * 根据店铺id查询信息
     *
     * @param storeIds 店铺id
     * @return 返回店铺信息
     */

    List<StoreDTO> selectStoresByIds(@RequestBody Long[] storeIds);

    /**
     * 导出所有数据
     *
     * @param params
     * @return
     */

    List<ExportStoreDTO> exportList(@RequestParam Map<String, Object> params);

    /**
     * 根据店铺ID 查询店铺状态
     *
     * @param storeId
     * @return 返回店铺状态
     */

    Integer findStoreStatus(@RequestParam("storeId") Long storeId);

    /**
     * seller 端创建店铺
     *
     * @param createStoreDTO 创建店铺参数
     * @param type           创建类型
     */

    void createStore(@RequestBody CreateStoreDTO createStoreDTO,
                     @RequestParam(required = false, value = "创建类型") Integer type);

    /**
     * 修改店铺基本信息
     *
     * @param updateRegisterStoreDTO
     */

    void updateStoreInfo(@RequestBody UpdateStoreBasicDTO updateRegisterStoreDTO);

    /**
     * 修改店铺入住信息
     *
     * @param updateRegisterStoreDTO 修改店铺参数
     * @param type                   创建类型
     */

    void updateRegisterStore(@RequestBody UpdateRegisterStoreDTO updateRegisterStoreDTO,
                             @RequestParam(required = false, value = "type") Integer type);

    /**
     * 店铺详情
     *
     * @param userId  用户ID
     * @param storeId 店铺Id
     * @return
     */

    RegisterStoreInfoDTO storeInfoByUserId(@RequestParam(value = "userId", required = false) Long userId,
                                           @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 修改店铺状态
     *
     * @param updateStoreStatusDTO
     */

    void updateStoreStatus(@RequestBody UpdateStoreStatusDTO updateStoreStatusDTO);

    /**
     * 根据店铺id查询详情（入住详情）
     *
     * @param storeId
     * @return
     */

    RegisterStoreInfoDTO adminInfo(@RequestParam Long storeId);

    /**
     * admin端创建店铺
     *
     * @param adminCreateStoreDTO
     */

    void adminCreateStore(@RequestBody AdminCreateStoreDTO adminCreateStoreDTO);


    /**
     * 修改店铺启用禁用状态
     *
     * @param storeId  店铺ID
     * @param isEnable 启用禁用 0 启用， 1 禁用
     */

    void updateStoreEnable(@RequestParam("storeId") Long storeId, @RequestParam("isEnable") Integer isEnable);

    /**
     * 根据用户id查询店铺列表
     *
     * @param userId
     * @return
     */

    PageData<StoreDTO> storeList(@RequestParam("userId") Long userId);

    /**
     * 查询出所有的店铺Id
     *
     * @return
     */

    List<Long> findStoreAll();

    /**
     * 首页>基础概况>店铺数据查询
     *
     * @param startDateStr 开始时间字符串
     * @param endDateStr   结束时间字符串
     * @return IndexStoreDataDTO
     * @date 2020/4/7/007 12:03
     * @author xuzhch
     */

    IndexStoreDataDTO indexStoreData(@RequestParam("startDateStr") String startDateStr, @RequestParam("endDateStr") String endDateStr);


    /**
     * 店铺首页
     *
     * @param sellerDetail
     * @return
     */

    IndexDTO index(@RequestBody SellerDetail sellerDetail);


    /**
     * 查询店铺信息
     *
     * @param storeId
     * @return
     */

    PageStoreDTO findStoreInfoById(@RequestParam("storeId") Long storeId);

}