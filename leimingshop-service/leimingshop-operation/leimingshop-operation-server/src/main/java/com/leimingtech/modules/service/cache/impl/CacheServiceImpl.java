/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.cache.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import com.leimingtech.modules.dto.goods.RecommendGoodsDTO;
import com.leimingtech.modules.dto.index.H5IndexDTO;
import com.leimingtech.modules.dto.index.H5StoreIndexDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.MobileIndexMenuDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreGoodsClassListDTO;
import com.leimingtech.modules.dto.storegoodsclass.StoreIndexGoodsClassDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import com.leimingtech.modules.enums.webfloor.IsShowEnum;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.cache.CacheService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.mobile.indexmenu.MobileIndexMenuService;
import com.leimingtech.modules.service.recommend.RecommendGoodsService;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.modules.service.webfloor.WebFloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * h5楼层设置
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Service

public class CacheServiceImpl implements CacheService {

    @Autowired

    private AdvService advService;

    @Autowired

    private WebFloorService webFloorService;

    @Autowired

    private MobileIndexMenuService mobileIndexMenuService;

    @Autowired
    private RecommendGoodsService recommendGoodsService;

    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Override

    public H5IndexDTO indexCache() {
        H5IndexDTO h5IndexDTO = new H5IndexDTO();
        // 移动首页菜单
        List<MobileIndexMenuDTO> mobileIndexMenuDTOList = mobileIndexMenuService.listIng();
        h5IndexDTO.setMobileIndexMenuDTOList(mobileIndexMenuDTOList);

        // h5轮播广告
        Map<String, Object> params = new HashMap<>(2);
        params.put("advKey", "h5index");

        List<AdvDTO> advDTOList = advService.listIngAdv(params);
        h5IndexDTO.setAdvDTOList(advDTOList);

        // h5楼层
        List<WebFloorAndLinkDTO> webFloorAndLinkDTOList = webFloorService.findFrontShopWebFloorList(new HashMap<>(10));
        List<WebFloorAndLinkDTO> collect = webFloorAndLinkDTOList.stream().filter(a -> a.getFloorType() == 1).collect(Collectors.toList());
        h5IndexDTO.setWebFloorAndLinkDTOList(collect);

        // 推荐商品
        List<GoodsRecommendDTO> goodsRecommendDTOList = recommendGoodsService.findRecommendList();
        if (null != goodsRecommendDTOList) {
            h5IndexDTO.setGoodsDTOList(ConvertUtils.sourceToTarget(goodsRecommendDTOList, RecommendGoodsDTO.class));
        }


        return h5IndexDTO;
    }


    /**
     * 店铺首页缓存
     *
     * @param storeId
     * @return
     */

    @Override
    public H5StoreIndexDTO storeIndexCache(@RequestParam(value = "storeId") Long storeId) {
        H5StoreIndexDTO h5IndexDTO = new H5StoreIndexDTO();

        // h5轮播广告
        Map<String, Object> params = new HashMap<>(2);
        params.put("advKey", "storeIndex");
        params.put("storeId", storeId);
        List<AdvDTO> advDTOList = advService.listIngAdv(params);
        h5IndexDTO.setAdvDTOList(advDTOList);

        // h5楼层
        List<WebFloorAndLinkDTO> floor = floor(storeId, IsShowEnum.H5_FLOOR.value());
        h5IndexDTO.setWebFloorAndLinkDTOList(floor);
        // 查询店铺商品分类
        List<StoreGoodsClassListDTO> frontStoreGoodsClass = storeGoodsClassService.findFrontStoreGoodsClass(storeId, null);
        if (CollectionUtils.isNotEmpty(frontStoreGoodsClass)) {
            h5IndexDTO.setStoreGoodsClass(ConvertUtils.sourceToTarget(frontStoreGoodsClass, StoreIndexGoodsClassDTO.class));
        }
        return h5IndexDTO;
    }

    /**
     * 查询楼层信息和楼层内商品信息
     *
     * @param storeId
     */
    @Override

    public List<WebFloorAndLinkDTO> floor(@RequestParam(value = "storeId", required = false) Long storeId,
                                          @RequestParam(value = "floorType", required = false) Integer floorType) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("storeId", storeId);
        List<WebFloorAndLinkDTO> webFloorList = webFloorService.findFrontShopWebFloorList(map);
        List<WebFloorAndLinkDTO> webFloorAndLinkDTOList = webFloorList.stream().filter(a -> a.getFloorType().equals(floorType)).collect(Collectors.toList());
        return webFloorAndLinkDTOList;
    }
}
