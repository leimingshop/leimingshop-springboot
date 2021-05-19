/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.custom;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.custom.*;
import com.leimingtech.modules.vo.goodsclasscustom.GoodsClassCustomImportVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 商品自定义分类
 *
 * @author xuzhch
 * @email 170518@163.com
 * @since 1.0.0 2019-05-22
 */

public interface GoodsClassCustomService {
    /**
     * 根据父ID查询子级分类
     *
     * @param id       展示类目父级id
     * @param showFlag 展示状态
     * @param showType 0 H5展示分类，1 pc展示分类
     * @return 返回展示分类信息
     * @author xuzhch
     */

    List<GoodsClassCustomDTO> selectListByParentId(Long id,
                                                   @RequestParam(value = "showFlag", required = false) Integer showFlag,
                                                   @RequestParam(value = "showType", required = false) Integer showType);

    /**
     * 首页楼层查询分类(返回树形结构)
     *
     * @param showType 0 H5展示分类 1 pc展示分类
     * @return java.util.List<com.leimingtech.modules.dto.custom.GoodsClassCustomTreeDTO>
     * @Author: weixianchun
     * @Description: 首页楼层查询分类(返回树形结构)
     * @Version 1.0
     */

    List<GoodsClassCustomTreeDTO> sellectCustomTree(@RequestParam(value = "showType", required = false) Integer showType);

    /**
     * 查询所有分类(分层级关系)
     *
     * @param showType 0 H5展示分类 1 pc展示分类
     * @return 返回展示分类信息
     * @author xuzhch
     */

    List<GoodsClassCustomListDTO> selectAllCustom(Integer showType);

    /**
     * 查询所有(h5)
     *
     * @param showType 0 H5展示分类，1 pc展示分类
     * @return 返回展示分类信息
     */

    List<GoodsClassCustomListDTO> selectAllCustomH5(Integer showType);

    /**
     * 重载保存商品类目
     *
     * @param goodsClassCustomSaveDTO
     * @author xuzhch
     */

    void save(@RequestBody GoodsClassCustomSaveDTO goodsClassCustomSaveDTO);

    /**
     * 重载修改商品类目
     *
     * @param goodsClassCustomDTO
     * @author xuzhch
     */

    void update(@RequestBody GoodsClassCustomUpdateDTO goodsClassCustomDTO);

    /**
     * 根据id查询展示类目，新增上级展示类目名称及ID根据id查询展示类目，新增上级展示类目名称及ID
     *
     * @param id 主键id
     * @return 返回真是分类详情
     */

    GoodsClassCustomDetailDTO selectDetailById(Long id);

    /**
     * 删除类目管理及其子级分类
     *
     * @param ids
     * @return
     * @author xuzhch
     */

    void deleteCorrelationIds(@RequestBody Long[] ids);

    /**
     * 根据父ID 获取父级名称
     *
     * @param id 展示分类父id
     * @return 返回分类名称
     */

    String selectNameById(Long id);

    /**
     * 根据ID删除商品展示类目
     *
     * @param id 主键id
     * @return 返回删除成功数量
     */

    Integer deleteCustomById(Long id);

    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<GoodsClassCustomDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出
     *
     * @param params
     * @return
     */

    List<GoodsClassCustomDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 启用/禁用展示类目
     *
     * @param params
     */

    void updateShowFlag(@RequestParam Map<String, Object> params);

    /**
     * 查询分类名称是否重复
     *
     * @param gcParentId 分类父iD
     * @param gcName     分类名称
     * @param showType   0 H5展示分类，1 pc展示分类
     * @return 返回分类名称重复数量
     */

    Integer findClassName(@RequestParam("gcParentId") Long gcParentId,
                          @RequestParam("gcName") String gcName,
                          @RequestParam(value = "showType", required = false) Integer showType);

    /**
     * 更新时验证分类名称是否重复
     *
     * @param gcParentId 分类父iD
     * @param gcName     分类名称
     * @param id         分类id
     * @param showType   0 H5展示分类，1 pc展示分类
     * @return 返回分类名称重复数量
     */

    Integer updateVerifyClassName(@RequestParam("gcParentId") Long gcParentId,
                                  @RequestParam("gcName") String gcName,
                                  @RequestParam("id") Long id,
                                  @RequestParam(value = "showType", required = false) Integer showType);

    /**
     * 校验子级名称是否和父级名称一样
     *
     * @param parentId 父级ID
     * @param gcName   子名称
     * @return
     */

    Integer findParentName(@RequestParam("parentId") Long parentId, @RequestParam("gcName") String gcName);

    /**
     * 根据分类id查询数量
     *
     * @param gcId
     * @return
     */

    Integer findCustomByGcId(Long gcId);

    /**
     * 功能描述 根据模板 从excel中导入
     *
     * @param files 导入的文件
     * @return void
     * @author lishuo
     * @date 2020/6/15
     */
    GoodsClassCustomImportVo importGoodsClassCustom(@RequestPart("files") MultipartFile files);

    /**
     * 查询当前分类下是否有子分类
     *
     * @param id 分类id
     * @return 子分类的集合
     */
    List<GoodsClassCustomDTO> findGcSon(Long id);
}