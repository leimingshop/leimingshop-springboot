/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.adv;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.adv.AdvLinkConfigDTO;
import com.leimingtech.modules.entity.adv.AdvEntity;
import com.leimingtech.modules.vo.adv.AdvEntityVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 广告管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Mapper
public interface AdvDao extends BaseDao<AdvEntity> {

    /**
     * admin查询广告列表
     *
     * @param params 查询条件
     * @param page   分页信息
     * @return 返回广告信息
     * @Author 刘远杰
     * @Description admin查询广告列表
     * @Date 2019/5/14 15:39
     */
    List<AdvEntityVo> findAdvShowList(@Param("params") Map<String, Object> params, IPage page);


    /**
     * 根据id查询广告详情
     *
     * @param id 广告索引
     * @return 返回广告信息
     * @Author 刘远杰
     * @Description 根据id查询广告详情
     * @Date 2019/5/14 16:36
     */
    AdvEntityVo findAdvById(Long id);

    /**
     * 根据类别id查询展示中的广告
     *
     * @param params 查询参数
     * @return 返回广告信息
     * @Author 刘远杰
     * @Description 根据类别id查询展示中的广告
     * @Date 2019/5/15 13:46
     */
    List<AdvDTO> findIngAdv(Map<String, Object> params);

    /**
     * 功能描述:
     * 〈广告编辑〉
     *
     * @param advEntity 广告实体
     * @return 返回更新成功数量
     * @author : 刘远杰
     */
    int editAdv(AdvEntity advEntity);

    /**
     * 查询所有店铺分类广告
     *
     * @return
     */
    List<AdvDTO> stoeGoodsClassAdv();

    /**
     * 查询店铺分类广告
     *
     * @param params
     * @param page
     * @return
     */
    List<AdvEntityVo> findStoreAdvShowList(@Param("params") Map<String, Object> params, IPage page);

    /**
     * 查询已关联的pc展示分类Id
     *
     * @return
     */
    List<Long> relevanceClass();

    /**
     * 查询分类广告配置
     *
     * @param advId
     * @return
     */
    List<AdvLinkConfigDTO> findAdvLink(Long advId);

    /**
     * 查询当前电批logo广告数量
     *
     * @param storeId 店铺id
     * @param advId   广告id
     * @return logo数量
     */
    Integer getLogoCount(@Param("storeId") Long storeId, @Param("advId") Long advId);

    /**
     * 查询展示分类关联商品id
     *
     * @param relationTarget 展示分类id
     * @return
     */
    Long getClassIdByCustomId(String relationTarget);
}
