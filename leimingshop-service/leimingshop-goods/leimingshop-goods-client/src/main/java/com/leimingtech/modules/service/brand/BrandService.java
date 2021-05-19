/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.brand;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.dto.brand.BrandExcelDTO;
import com.leimingtech.modules.dto.brand.BrandSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 品牌管理BrandService
 * @Date :2019/5/20 14:05
 * @Version V1.0
 **/

public interface BrandService {

    /**
     * 保存时-根据品牌名称,品牌英文名称查询品牌信息
     *
     * @param brandName   品牌名称
     * @param brandNameEn 品牌英文名称
     * @return 返回品牌信息
     * @Author: weixianchun
     * @Description: 保存时-根据品牌名称,品牌英文名称查询品牌信息
     * @Date :2019/5/20 14:01
     * @Version V1.0
     **/
//
//    BrandDTO findByName(@RequestParam("brandName") String brandName, @RequestParam(value = "brandNameEn", required = false) String brandNameEn);

    /**
     * 修改时-查询品牌名称,品牌英文名称数量防止重复校验
     *
     * @param brandDTO 实体
     * @return 返回重复数量
     * @Author: weixianchun
     * @Description: 修改时-查询品牌名称,品牌英文名称数量防止重复校验
     * @Date :2019/5/20 14:0p7
     * @Version V1.0
     **/
//
//    int selectByBrandName(@RequestBody BrandDTO brandDTO);

    /**
     * 分页查询
     *
     * @param params 查询条件
     * @return 返回分页信息
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    PageData<BrandDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有品牌信息
     *
     * @param params 查询条件
     * @return 返回品牌信息
     * @Author: weixianchun
     * @Description: 查询所有品牌信息
     * @Date :2019/5/28 19:51
     * @Version V1.0
     **/

    List<BrandDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据品牌id查询信息
     *
     * @param id 品牌id
     * @return 返回品牌详情
     * @Author: weixianchun
     * @Description: 根据品牌id查询信息
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    BrandDTO get(Long id);

    /**
     * 功能描述:
     * 〈根据ids查询品牌集合〉
     *
     * @param brandIds 品牌id
     * @param storeId  店铺id
     * @return 返回品牌信息
     * @author : 刘远杰
     */

    List<BrandDTO> getByBrandIds(@RequestBody List<Long> brandIds,
                                 @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 保存品牌信息
     *
     * @param dto 实体
     * @Author: weixianchun
     * @Description: 保存品牌信息
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void save(@RequestBody BrandSaveDTO dto);

    /**
     * 修改品牌信息
     *
     * @param dto 实体
     * @Author: weixianchun
     * @Description: 修改品牌信息
     * @Date :2019/5/28 19:52
     * @Version V1.0
     **/

    void update(@RequestBody BrandDTO dto);

    /**
     * 根据品牌id删除品牌信息
     *
     * @param ids 品牌id
     * @return
     * @Author: weixianchun
     * @Description: 根据品牌id删除品牌信息
     * @Date :2019/5/28 19:53
     * @Version V1.0
     **/

    List<String> deleteByBrandId(@RequestBody Long[] ids);

    /**
     * 导出日志
     *
     * @param params 查询条件
     * @return 返回日志信息
     * @Author: weixianchun
     * @Description: 导出日志
     * @Date :2019/7/9 16:00
     * @Version V1.0
     **/

    List<BrandExcelDTO> exportList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈获得店铺关联所有品牌〉
     *
     * @param storeId 店铺id
     * @return 返回品牌信息
     * @author : 刘远杰
     */

    List<BrandDTO> storeBrandList(Long storeId);

    /**
     * 功能描述 查询出所有的品牌
     *
     * @param * @param
     * @return java.util.List<com.leimingtech.modules.dto.brand.BrandDTO>
     * @author lishuo
     * @date 15/7/2020
     */

    List<BrandDTO> findAllBrand();


    Integer selectCountByCondition(@RequestBody BrandDTO brandDTO);
}
