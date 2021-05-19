/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.favorites.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.favorites.StoreFavoritesDao;
import com.leimingtech.modules.dto.evaluate.FindEvaluateStoreDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesByStoreIdDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesPageDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesSaveDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.entity.favorites.StoreFavoritesEntity;
import com.leimingtech.modules.enums.FavoriteEnum;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.evaluate.EvaluateStoreService;
import com.leimingtech.modules.service.favorites.StoreFavoritesService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户店铺收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Service

public class StoreFavoritesServiceImpl extends CrudServiceImpl<StoreFavoritesDao, StoreFavoritesEntity, StoreFavoritesDTO> implements StoreFavoritesService {

    @Resource
    private StoreFavoritesDao storeFavoritesDao;
    @Resource
    private EvaluateStoreService evaluateStoreService;
    @Resource
    private StoreService storeService;
    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private MemberService memberService;

    @Override
    public QueryWrapper<StoreFavoritesEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String memberId = (String) params.get("memberId");

        //获取用户信息 todo
        QueryWrapper<StoreFavoritesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq("member_id", memberId);

        return wrapper;
    }

    /**
     * @return void
     * @Description 保存店铺收藏
     * @Author huangkeyuan
     * @Date 18:54 2019-05-15
     * @Param [dto]
     */
    @Override

    public void saveStoreFav(@RequestBody StoreFavoritesSaveDTO dto, @RequestParam("memberId") Long memberId, @RequestParam("memberName") String memberName) {

        StoreFavoritesEntity favEntity = ConvertUtils.sourceToTarget(dto, StoreFavoritesEntity.class);
        favEntity.setMemberId(memberId);
        StoreFavoritesDTO favorite = isFavorite(memberId, dto.getStoreId());
        if (favorite == null) {
            storeFavoritesDao.insert(favEntity);

            Map<String, Object> memberParams = new HashMap<>(10);
            memberParams.put("memberId", memberId);
            memberParams.put("memberName", memberName);
            memberParams.put("sourceType", MemberPointLogEnum.ATTENTION_STORE_SOURCE_TYPE.code());
            memberParams.put("pointDesc", MemberPointLogEnum.ATTENTION_STORE_SOURCE_TYPE.value());

            memberService.savePoint(memberParams);

        }

        // todo 关注店铺

        // 更新店铺索引
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(dto.getStoreId()));
    }

    /**
     * @return void
     * @Description 删除
     * @Param * @param ids:
     * @Author huangkeyuan
     * @Date 11:48 2019-05-29
     */

    @Override
    public void delete(@RequestBody Long[] ids, @RequestParam("memberId") Long memberId) {
        for (Long storeId : ids) {
            QueryWrapper<StoreFavoritesEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(memberId.toString()), "member_id", memberId);
            wrapper.eq(StringUtils.isNotBlank(storeId.toString()), "store_id", storeId);
            baseDao.delete(wrapper);
            // 更新店铺索引
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE, String.valueOf(storeId));
        }
    }

    /**
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.favorites.StoreFavoritesDTO>
     * @Description 查询分页
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 11:53 2019-05-29
     */

    @Override
    public PageData<StoreFavoritesPageDTO> storePage(@RequestParam Map<String, Object> params) {

        //分页
        IPage<StoreFavoritesEntity> page = getPage(params, "create_date", false);
        //查询
        List<StoreFavoritesPageDTO> list = storeFavoritesDao.favPage(params);

        return new PageData<>(list, page.getTotal());
    }

    /**
     * 校验用户是否关联该店铺
     *
     * @param userId
     * @param storeId
     * @return
     */

    @Override
    public StoreFavoritesDTO isFavorite(@RequestParam("userId") Long userId, @RequestParam("storeId") Long storeId) {
        StoreFavoritesEntity storeFavoritesEntity = baseDao.selectOne(new QueryWrapper<StoreFavoritesEntity>()
                .eq("member_id", userId).eq("store_id", storeId));
        return ConvertUtils.sourceToTarget(storeFavoritesEntity, StoreFavoritesDTO.class);
    }

    /**
     * 根据店铺Id查询关注人数
     *
     * @param storeId
     * @return
     */

    @Override
    public Integer selectFavoriteCount(Long storeId) {
        return baseDao.selectCount(new QueryWrapper<StoreFavoritesEntity>().eq("store_id", storeId));
    }

    /**
     * @Author: weixianchun
     * @Description: 根据店铺id查询信息
     * @Date :2019/7/29 16:03
     * @Param storeId 店铺id
     * @Version V1.0
     **/

    @Override
    public StoreFavoritesByStoreIdDTO selectByStoreId(@RequestParam("storeId") Long storeId, @RequestParam("userId") Long userId) {

        StoreFavoritesByStoreIdDTO storeFavoritesByStoreIdDTO = new StoreFavoritesByStoreIdDTO();
        //关注人数
        Integer count = selectFavoriteCount(storeId);
        //是否关注店铺
        StoreFavoritesDTO favoritesDTO = isFavorite(userId, storeId);
        //综合评分
        FindEvaluateStoreDTO avgPoint = evaluateStoreService.findAvgPoint(storeId);
        if (null != avgPoint) {
            Double sevalDeliverycredit = avgPoint.getSevalDeliverycredit();
            Double sevalDesccredit = avgPoint.getSevalDesccredit();
            Double sevalServicecredit = avgPoint.getSevalServicecredit();
            double sum = sevalDesccredit + sevalDeliverycredit + sevalServicecredit;
            double sevalAvg = sum / 3;
            storeFavoritesByStoreIdDTO.setSevalAvg(sevalAvg);
        }

        //根据店铺id查询信息
        StoreDTO storeDTO = storeService.get(storeId);
        storeFavoritesByStoreIdDTO.setCount(count);
        if (null == favoritesDTO) {
            storeFavoritesByStoreIdDTO.setIsFavorites(FavoriteEnum.NOT_FAVORITE.value());
        } else {
            storeFavoritesByStoreIdDTO.setIsFavorites(FavoriteEnum.ALREADY_FAVORITE.value());
        }
        storeFavoritesByStoreIdDTO.setStoreDTO(storeDTO);
        return storeFavoritesByStoreIdDTO;
    }
}
