/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.recommend.impl;

import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import com.leimingtech.modules.service.recommend.CartRecommendService;
import com.leimingtech.modules.service.recommend.RecommendGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/7/12 17:55
 * @Version: V1.0
 */
@Service
@Slf4j

public class RecommendGoodsServiceImpl implements RecommendGoodsService {


    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    private CartRecommendService cartRecommendService;


    /**
     * 功能描述:
     * 〈首页推荐商品〉
     *
     * @author : songwenhao
     */
    @Override

    public List<GoodsRecommendDTO> findRecommendList() {
        String key = RedisConstants.RECOMMEND_GOODS;
        Object obj = redisUtils.get(key);
        List<GoodsRecommendDTO> recommendDTOList;
        if (obj == null) {
            // redis中不存在
            recommendDTOList = cartRecommendService.findList();
            if (null == recommendDTOList) {
                return Collections.emptyList();
            }

            redisUtils.set(key, recommendDTOList, RedisConstants.JEDIS_EXPIRE);
        } else {
            recommendDTOList = (List<GoodsRecommendDTO>) obj;
        }
        return recommendDTOList;
    }
}
