/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.store;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.store.*;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.entity.store.StoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Mapper
public interface StoreDao extends BaseDao<StoreEntity> {
    /**
     * 分页查询店铺信息
     *
     * @param params
     * @return
     */
    List<PageStoreDTO> findPage(Map<String, Object> params);

    /**
     * 查询店铺信息
     *
     * @param userId 用户id
     * @return
     */
    List<UserAllStoreDTO> findByUserId(Long userId);

    /**
     * 验证店铺名称是否重复
     *
     * @param storeName 店铺名称
     * @param storeId   店铺ID
     * @return
     */
    Integer findStoreName(@Param("storeName") String storeName, @Param("storeId") Long storeId);

    /**
     * 查询店铺等级下面关联的店铺数量
     *
     * @param gradeId
     * @return
     */
    Integer findGradeCount(Long gradeId);

    /**
     * 导出商户列表
     *
     * @param params
     * @return
     */
    List<ExportStoreDTO> exportList(Map<String, Object> params);

    /**
     * 根据ID 查询店铺状态
     *
     * @param storeId 店铺id
     * @return 返回店铺状态信息
     */
    Integer findStoreStatus(Long storeId);

    /**
     * 店铺详情
     *
     * @param userId  用户ID
     * @param storeId 店铺ID
     * @return
     */
    RegisterStoreInfoDTO storeInfoByUserId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    /**
     * 查询店铺待审核列表
     *
     * @param params
     * @return
     */
    List<PageStoreDTO> findAuditPage(Map<String, Object> params);

    /**
     * 查询店铺审核详情
     *
     * @param storeId 店铺id
     * @return 返回店铺审核详情
     */
    RegisterStoreInfoDTO adminInfo(Long storeId);

    /**
     * 获取待审核店铺分类
     *
     * @param id
     * @return
     */
    List<StoreClassDTO> getAuditStoreClass(Long id);

    /**
     * 查询店铺列表
     *
     * @param params
     * @return
     */
    List<StoreDTO> storeList(Map<String, Object> params);

    /**
     * 查询出所有的店铺
     *
     * @return
     */
    List<Long> findStoreAll();

    /**
     * 查询店铺首页数据
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 返回店铺信息
     */
    IndexStoreDataDTO selectIndexStoreData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * 获取店铺信息
     *
     * @param storeId 店铺id
     * @param userId  用户id
     * @return 返回店铺分页
     */
    PageStoreDTO storeInfo(@Param("storeId") Long storeId, @Param("userId") Long userId);

    /**
     * 查询店铺信息
     *
     * @param storeId
     * @return
     */
    PageStoreDTO findStoreInfoById(Long storeId);

}