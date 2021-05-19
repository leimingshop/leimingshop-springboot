/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.goods.spec;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goods.spec.DefaultSpecPictureDTO;
import com.leimingtech.modules.dto.goods.spec.SpecAttributePictureSaveDTO;
import com.leimingtech.modules.entity.goods.spec.GoodsSpecAttrPicEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品规格属性值与图片关联表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Mapper
public interface SpecAttributePictureDao extends BaseDao<GoodsSpecAttrPicEntity> {
    /**
     * 获取默认图片
     *
     * @param goodsId 商品id
     * @return 返回商品默认图片
     */
    List<DefaultSpecPictureDTO> getDefaultPic(Long goodsId);

    /**
     * 保存信息
     *
     * @param goodsSpecAttrPicEntities 保存参数
     */
    void saveBatch(@Param("goodsSpecAttrPicEntities") List<SpecAttributePictureSaveDTO> goodsSpecAttrPicEntities);
}