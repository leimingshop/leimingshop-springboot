/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goodsclass.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


/**
 * 商品分类
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */

public interface GoodsClassService {

    /**
     * 通过商品分类id查询只返回商品的分类名称
     *
     * @param goodsClassId goodsClassId:商品分类id
     * @return 返回分类名称
     * @Description 通过商品分类id查询只返回商品的分类名称
     * @Author huangkeyuan
     * @Date 09:56 2019-05-23
     */

    String queryGoodsClassName(@RequestParam("goodsClassId") Long goodsClassId);

    /**
     * 根据classID查询IDPath
     *
     * @param goodsClassId
     * @return
     */

    String queryIdPathById(Long goodsClassId);

    /**
     * 保存商品分类
     *
     * @param dto 保存参数
     * @return void
     * @Description 保存商品分类
     * @Author huangkeyuan
     * @Date 13:59 2019-05-23
     */

    void saveGoodsClass(@RequestBody GoodsClassSaveDTO dto);

    /**
     * 修改商品分类
     *
     * @param dto 修改参数
     * @return void
     * @Description 修改商品分类
     * @Author huangkeyuan
     * @Date 13:59 2019-05-23
     */

    void updateGoodsClass(@RequestBody GoodsClassUpdateDTO dto);

    /**
     * 删除商品分类
     *
     * @param ids 主键id
     * @return 返回删除结果
     * @Description
     * @Author huangkeyuan
     * @Date 15:59 2019-05-23
     */

    Map<String, Object> deleteGoodsClassIds(@RequestBody Long[] ids);


    /**
     * 导入商品分类数据
     *
     * @param files 文件信息
     * @return void
     * @Description
     * @Author huangkeyuan
     * @Date 18:11 2019-05-23
     */
    void importGoodsClass(@RequestPart("files") MultipartFile files);

    /**
     * 查询所有分类(分层级关系)
     *
     * @param type type===3 查询出只有三级的分类
     * @return 返回分类信息
     * @author huangkeyuan
     */

    List<GoodsClassListDTO> selectAllGoodsClass(@RequestParam(value = "type", required = false) Integer type);

    /**
     * 查询分页信息
     *
     * @param params 查询条件
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goodsclass.GoodsClassDTO>
     * @Description
     * @Author huangkeyuan
     * @Date 16:55 2019-05-28
     */

    PageData<GoodsClassDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据分类id查询分类关联的规格、属性、品牌信息
     *
     * @param id 分类id
     * @return GoodsClassDetailDTO
     * @Description
     * @Author huangkeyuan
     * @Date 17:14 2019-05-28
     */

    GoodsClassDetailDTO getGoodsClassById(Long id);

    /**
     * 查询列表
     *
     * @param params 查询条件
     * @return java.util.List<com.leimingtech.modules.dto.goodsclass.GoodsClassDTO>
     * @Description 查询列表
     * @Author huangkeyuan
     * @Date 17:17 2019-05-28
     */

    List<GoodsClassDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据父ID查询子级分类
     *
     * @param id
     * @param storeId 店铺id
     * @return 返回分类信息
     * @author xuzhch
     */

    List<GoodsClassDTO> selectListByParentId(Long id,
                                             @RequestParam(value = "storeId", required = false) Long storeId);


    /**
     * 根据Id查询分类数据(店铺关联的分类)
     *
     * @param classIds 分类id
     * @return 返回分类信息
     * @author xuzhch
     */

    List<GoodsClassQueryNameDTO> selectListByClassId(@RequestBody List<Long> classIds);

    /**
     * 根据ID查询订单商品信息
     *
     * @param id: 商品分类ID
     * @return 返回分类信息
     * @date 2019/6/23 16:37
     * @author LX lixiangx@leimingtech.com
     **/

    GoodsClassDTO findById(Long id);

    /**
     * 查询分类名称数量(修改时)
     *
     * @param dto 查询参数
     * @return 返回分类名称数量
     * @Author: weixianchun
     * @Description: 查询分类名称数量(修改时)
     * @Date :2019/7/3 19:58
     * @Param dto 实体
     * @Version V1.0
     **/

    int gcNameConutCheck(@RequestBody GoodsClassUpdateDTO dto);

    /**
     * 查询分类名称数量(保存时)
     *
     * @param gcName     商品分类名称
     * @param gcParentId 父id
     * @return 返回分类名称数量
     * @Author: weixianchun
     * @Description: 查询分类名称数量(保存时)
     * @Date :2019/7/3 20:40
     * @Version V1.0
     **/

    Integer findByGcName(@RequestParam("gcName") String gcName, @RequestParam("gcParentId") Long gcParentId);

    /**
     * 根据分类id查询品牌信息, 并模糊查询品牌名称
     *
     * @param gcClassId 分类id
     * @param brandName 品牌名称
     * @return 返回分类品牌信息
     * @Author: weixianchun
     * @Description: 根据分类id查询品牌信息, 并模糊查询品牌名称
     * @Date :2019/7/4 11:55
     * @Version V1.0
     **/

    List<GoodsClassBrandDTO> brandByGcId(@RequestParam("gcClassId") Long gcClassId, @RequestParam(value = "brandName", required = false) String brandName);

    /**
     * 校验子分组名称不能和父分组名称不能一样
     *
     * @param parentId 父分组ID
     * @param gcName   子分组名称
     * @return 返回分组名称数量
     */

    Integer findParentName(@RequestParam("parentId") Long parentId, @RequestParam("gcName") String gcName);

    /**
     * 查询分类信息
     *
     * @param classLists 分类id
     * @return 返回分类信息
     */

    List<Map<String, Object>> selectNameByCustomIds(@RequestBody List<Long> classLists);

    /**
     * 功能描述:
     * 〈根据一级分类集合获取所有三级分类〉
     *
     * @param classIds 一级分类集合
     * @return 返回分类品牌信息
     * @author : 刘远杰
     */

    List<GoodsClassDTO> selectThreeByOneIds(@RequestBody List<Long> classIds);


    /**
     * 查询店铺关联的分类，分层级关系
     *
     * @param storeId 店铺Id
     * @return
     * @date 2020/2/26/026 17:56
     * @author xuzhch
     **/

    List<GoodsClassListDTO> selectGoodsClassByStoreId(Long storeId);

    /**
     * 功能描述 根据分类名称和父类id 查找id
     *
     * @param gcName   分类名称
     * @param parentId 父类id
     * @return Long 自己的id
     * @author lishuo
     * @date 2020/6/16
     */

    Long findByGcNameAndParentId(@RequestParam("gcName") String gcName, @RequestParam("parentId") long parentId);

    /**
     * 获取子分类
     *
     * @param id 主键id
     * @return 返回分类信息
     */

    List<GoodsClassDTO> selectClassByParentId(Long id);

    /**
     * 功能描述 查询出所有的分类
     *
     * @param * @param
     * @return java.util.List<com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO>
     * @author lishuo
     * @date 15/7/2020
     */

    List<GoodsClassListDTO> findAllGoodsClass();

    /**
     * 查询当前分类下是否有子分类
     *
     * @param id 分类id
     * @return 子分类的集合
     */
    List<GoodsClassDTO> findGcSon(Long id);
}
