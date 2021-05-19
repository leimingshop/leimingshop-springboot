/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.spec.DefaultSpecPictureDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributePictureDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributePictureSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品规格属性值与图片关联
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */

public interface SpecAttributePictureService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<SpecAttributePictureDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询商品属性关联的图片信息
     *
     * @param id
     * @return
     */

    SpecAttributePictureDTO get(Long id);

    /**
     * 保存商品属性关联的图片信息
     *
     * @param dto
     */

    void save(@RequestBody List<SpecAttributePictureSaveDTO> dto);

    /**
     * 修改商品属性的图片信息
     *
     * @param dto
     */

    void update(@RequestBody SpecAttributePictureDTO dto);

    /**
     * 根据ID删除商品属性的图片信息
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出商品属性的图片信息
     *
     * @param params
     * @return
     */

    List<SpecAttributePictureDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据规格属性id删除图片
     *
     * @param ids
     */

    void deleteBySpecattrId(@RequestBody Long[] ids);

    /**
     * 根据goodsid删除图片
     *
     * @param goodsId
     */

    void deleteByGoodsId(Long goodsId);

    /**
     * 查询商品图片信息
     *
     * @param goodsId
     * @return
     */

    List<DefaultSpecPictureDTO> getDefaultPic(Long goodsId);

    /**
     * 保存
     *
     * @param specAttributePictureSaveDTOList 保存参数
     */

    void saveBatch(@RequestParam("specAttributePictureSaveDTOS") List<SpecAttributePictureSaveDTO> specAttributePictureSaveDTOList);

    /**
     * 根据商品ID，批量删除
     *
     * @param goodsIds 商品id数组
     * @author xuzhch
     * @date 2020年8月26日
     */

    void deleteBatchBygoodsIds(@RequestBody Long[] goodsIds);
}
