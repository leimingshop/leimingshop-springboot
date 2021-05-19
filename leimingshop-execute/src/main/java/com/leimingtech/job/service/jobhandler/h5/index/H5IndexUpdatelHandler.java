/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.job.service.jobhandler.h5.index;

import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.modules.service.mobile.indexmenu.MobileIndexMenuService;
import com.leimingtech.modules.service.recommend.RecommendGoodsService;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.modules.service.webfloor.WebFloorService;
import com.leimingtech.modules.task.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：
 * <h5首页缓存更新/h5分类缓存更新定时任务>
 *
 * @author 刘远杰
 * @Date 2019/5/23 10:59
 * Version 7.0
 **/
@Slf4j
@Component
public class H5IndexUpdatelHandler implements Job {

    /**
     * H5展示分类
     */
    private final int H5_SHOW_CLASS = 0;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private MobileIndexMenuService mobileIndexMenuService;

    @Autowired
    private AdvService advService;

    @Autowired
    private WebFloorService webFloorService;

    @Autowired
    private RecommendGoodsService recommendGoodsService;

    @Autowired
    private GoodsClassCustomService goodsClassCustomService;

    @Autowired
    private StoreGoodsClassService storeGoodsClassService;

    @Override
    public void run(String string) {

        // 移动首页菜单
        redisUtils.delete(RedisConstants.MOBILE_INDEX_MENU_PREFIX);
        mobileIndexMenuService.listIng();
        log.info("更新移动首页菜单缓存成功,{}", RedisConstants.MOBILE_INDEX_MENU_PREFIX);

        // h5轮播广告
        redisUtils.deleteByPattern(RedisConstants.ADV_PREFIX + "*");
        Map<String, Object> params = new HashMap<>(2);
        params.put("advKey", "h5index");
        advService.listIngAdv(params);
        // pc轮播广告
        params.clear();
        params.put("advKey", "pcIndex");
        advService.listIngAdv(params);
        // 更新店铺首页轮播
        params.clear();
        params.put("advKey", "storeIndex");
        advService.listIngAdv(params);
        log.info("更新h5轮播广告缓存成功,{}", RedisConstants.ADV_PREFIX + "h5index");

        // h5楼层
        redisUtils.deleteByPattern(RedisConstants.ADV_FLOOR_PREFIX + "*");
        redisUtils.delete(RedisConstants.STORE_WEB_FLOOR_PREFIX);
        redisUtils.delete(RedisConstants.WEB_FLOOR_PREFIX);
        webFloorService.findFrontShopWebFloorList(params);
        log.info("更新h5楼层缓存成功,{}", RedisConstants.WEB_FLOOR_PREFIX);

        // 推荐商品
        redisUtils.delete(RedisConstants.RECOMMEND_GOODS);
        recommendGoodsService.findRecommendList();
        log.info("更新推荐商品缓存成功,{}", RedisConstants.RECOMMEND_GOODS);

        // 展示分类
        redisUtils.deleteByPattern(RedisConstants.ADV_CLASS_PREFIX + "*");
        redisUtils.delete(RedisKeys.getH5CustomClass());
        goodsClassCustomService.selectAllCustomH5(H5_SHOW_CLASS);
        log.info("更新展示分类缓存成功,{}", RedisConstants.RECOMMEND_GOODS);
        // 定时更新店铺商品分类
        storeGoodsClassService.timeUpdate();

        log.info("执行h5首页缓存更新定时任务成功");

    }
}
