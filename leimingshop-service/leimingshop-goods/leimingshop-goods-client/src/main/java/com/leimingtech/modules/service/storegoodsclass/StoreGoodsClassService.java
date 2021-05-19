/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.storegoodsclass;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.storegoodsclass.AddStoreGoodsClassDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassListDTO;
import com.leimingtech.modules.dto.storegoodsclass.UpdateStoreGoodsClassDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */

public interface StoreGoodsClassService {
    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    PageData<StoreGoodsClassDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 条件查询
     *
     * @param params
     * @return
     */

    List<StoreGoodsClassListDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 获取详情
     *
     * @param id
     * @return
     */

    StoreGoodsClassDTO get(Long id);

    /**
     * 保存分类
     *
     * @param dto
     */

    void save(@RequestBody AddStoreGoodsClassDTO dto);

    /**
     * 更新分类
     *
     * @param dto
     */

    void update(@RequestBody UpdateStoreGoodsClassDTO dto);

    /**
     * 删除分类
     *
     * @param ids
     * @param storeId 店铺id
     * @return 返回删除结果
     */

    Map<String, Object> delete(@RequestBody Long[] ids, @RequestParam("storeId") Long storeId);

    /**
     * 查询分类名称是否重复
     *
     * @param gcName     分类名称
     * @param storeId    店铺ID
     * @param gcParentId 父ID
     * @param type       1 校验同一级别下分类名称不能重复，2 校验子分类不能和父分类名称重复 3 更新分类校验同一级别下分类名称不能重复
     * @param id         分类ID 更新时使用
     * @return
     */

    Integer classNameCount(@RequestParam("gcName") String gcName,
                           @RequestParam("storeId") Long storeId,
                           @RequestParam(value = "gcParentId", required = false) Long gcParentId,
                           @RequestParam(value = "type", required = false) Integer type,
                           @RequestParam(value = "id", required = false) Long id);

    /**
     * 查询店铺商品分类缓存
     *
     * @param storeId
     * @param type
     * @return
     */

    List<StoreGoodsClassListDTO> findFrontStoreGoodsClass(@RequestParam(value = "storeId") Long storeId,
                                                          @RequestParam(value = "type", required = false) Integer type);

    /**
     * 定时更新店铺商品分类
     */

    void timeUpdate();

    /**
     * 查询所有一级分类
     *
     * @param storeId 店铺id、
     * @param type    类型
     * @return 返回店铺分类id
     */

    List<StoreGoodsClassListDTO> firstClass(@RequestParam("storeId") Long storeId,
                                            @RequestParam("type") Integer type);

    /**
     * 功能描述 根据excel导入店铺商品分类信息
     *
     * @param file    excel
     * @param storeId 店铺id
     * @return void
     * @author lishuo
     * @date 2020/6/17
     */
    void importFromExcel(@RequestPart("file") MultipartFile file, @RequestParam("storeId") Long storeId);

    /**
     * 功能描述 根据店铺商品分类的名称 查询id
     *
     * @param storeGcName 店铺商品分类名称
     * @param parentId    商品分类的父类Id
     * @param storeId     店铺的id
     * @return Long  查询到的商品分类的id
     * @author lishuo
     * @date 24/6/2020
     */

    Long findStoreGcId(@RequestParam("storeGcName") String storeGcName, @RequestParam("parentId") long parentId, @RequestParam("storeId") Long storeId);

    /**
     * 功能描述 根据店铺id 下面的所有的分类
     *
     * @param * @param storeId
     * @return java.util.List<com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO>
     * @author lishuo
     * @date 15/7/2020
     */

    List<StoreGoodsClassDTO> findAllStoreGoodsClass(Long storeId);

    /**
     * 根据分类id查询分类信息
     *
     * @param classId 分类id
     * @return 返回分类信息
     */

    List<StoreGoodsClassDTO> getClassById(@RequestBody List<Long> classId);
}