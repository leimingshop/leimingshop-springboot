/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.favorites;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dao.favorites.FavoritesDao;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.cart.CartDTO;
import com.leimingtech.modules.dto.cart.FavoriteCartDTO;
import com.leimingtech.modules.dto.favorites.FavoritesDTO;
import com.leimingtech.modules.dto.favorites.FavoritesPageDTO;
import com.leimingtech.modules.dto.favorites.FavoritesQueryDTO;
import com.leimingtech.modules.dto.favorites.StoreFavoritesDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.entity.favorites.FavoritesEntity;
import com.leimingtech.modules.enums.point.MemberPointLogEnum;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.modules.vo.FavResulltVO;
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
 * 用户收藏
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-15
 */
@Service

public class FavoritesServiceImpl extends CrudServiceImpl<FavoritesDao, FavoritesEntity, FavoritesQueryDTO> implements FavoritesService {

    @Resource
    private FavoritesDao favoritesDao;

    @Autowired
    private CartService cartService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private StoreFavoritesService storeFavoritesService;

    @Override
    public QueryWrapper<FavoritesEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        String memberId = (String) params.get("memberId");

        QueryWrapper<FavoritesEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(memberId), "member_id", memberId);

        return wrapper;
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override

    public FavoritesQueryDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 查询商品是否收藏
     *
     * @param specId 商品规格ID
     * @return
     */

    @Override
    public Integer findIsFavorites(@RequestParam("specId") Long specId, @RequestParam("memberId") Long memberId) {
        return baseDao.findIsFavorites(specId, memberId);
    }

    /**
     * @param memberId
     * @return void
     * @Description 购物车批量移入收藏
     * @Param * @param :
     * @Author huangkeyuan
     * @Date 15:49 2019-07-24
     */
    @Override

    public void cartFavoriteGoods(@RequestParam("memberId") Long memberId) {
        // 从购物车服务cartService中获取到当前选中的商品的id
        List<FavoriteCartDTO> list = cartService.findGoodsIdsByMemberId(memberId);
        for (FavoriteCartDTO dto : list) {
            FavoritesDTO favoritesDTO = new FavoritesDTO();
            favoritesDTO.setSpecId(dto.getSpecId());
            favoritesDTO.setFavPrice(dto.getSpecSellPrice());
            favoritesDTO.setMemberId(memberId);
            favoritesDTO.setStoreId(dto.getStoreId());
            this.saveGoodsFav(favoritesDTO);
        }
        // 移除当前购物车中已选中的商品
        cartService.deleteIsSelectCart(memberId);
    }

    /**
     * 根据用户id获取商品收藏数量
     *
     * @param id
     * @return
     */

    @Override
    public Integer countByMemberId(Long id) {

        return favoritesDao.countByMemberId(id);
    }

    /**
     * @return void
     * @Description 保存用户商品收藏记录
     * @Author huangkeyuan
     * @Date 17:35 2019-05-15
     * @Param [dto]
     */
    @Override

    public Map<String, Object> saveGoodsFav(@RequestBody FavoritesDTO dto) {
        Map<String, Object> result = new HashMap<>(10);
        //保存之前先检验是否已收藏
        int isFav = this.findIsFavorites(dto.getSpecId(), dto.getMemberId());

        if (isFav == 0) {
            FavoritesEntity favEntity = ConvertUtils.sourceToTarget(dto, FavoritesEntity.class);
            // 查询店铺类型
            StoreDTO storeDTO = storeService.get(favEntity.getStoreId());
            favEntity.setStoreType(storeDTO.getStoreType());
            favoritesDao.insert(favEntity);

            // todo 收藏商品

            Map<String, Object> memberParams = new HashMap<>(10);
            memberParams.put("memberId", dto.getMemberId());
            memberParams.put("memberName", dto.getMemberName());
            memberParams.put("sourceType", MemberPointLogEnum.FAVORITES_GOODS_SOURCE_TYPE.code());
            memberParams.put("pointDesc", MemberPointLogEnum.FAVORITES_GOODS_SOURCE_TYPE.value());
            memberService.savePoint(memberParams);

            result.put("code", ResultCodeEnum.SUCCESS.value());
            result.put("msg", "收藏成功");
            return result;

        } else {
            result.put("code", ResultCodeEnum.WARN.value());
            result.put("msg", "你已经收藏过了");
            return result;
        }

    }

    /**
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goodsclass.GoodsClassDTO>
     * @Description 查询分页信息
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 16:55 2019-05-28
     */

    @Override
    public PageData<FavoritesPageDTO> favPage(@RequestParam Map<String, Object> params) {

        //分页
        IPage<FavoritesEntity> page = getPage(params, "create_date", false);
        //查询
        List<FavoritesPageDTO> list = favoritesDao.favPage(params);

        return new PageData<>(list, page.getTotal());
    }

    /**
     * @return com.leimingtech.commons.tools.utils.Result
     * @Description 删除商品收藏
     * @Author huangkeyuan
     * @Date 15:59 2019-05-23
     */

    @Override
    public void delete(@RequestBody Long[] ids, @RequestParam("memberId") Long memberId) {
        for (Long specId : ids) {
            QueryWrapper<FavoritesEntity> wrapper = new QueryWrapper<>();
            wrapper.eq(StringUtils.isNotBlank(memberId.toString()), "member_id", memberId);
            wrapper.eq(StringUtils.isNotBlank(specId.toString()), "spec_id", specId);
            baseDao.delete(wrapper);
        }
    }

    /**
     * 用户收藏商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */
    @Override

    public List<Long> getFavoriteGoodIds(@RequestParam("memberId") Long memberId) {
        List<Long> goodIds = baseDao.getFavoriteGoodIds(memberId);
        return goodIds;
    }

    /**
     * 购物车单个商品移入收藏夹
     *
     * @param cartId
     */

    @Override
    public void cartOneFavorites(@RequestParam("cartId") Long cartId) {
        // 查询购物车信息
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("id", cartId);
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        CartDTO cartDTO = JSONObject.parseObject(pageModelDTO.getJsonRsList().get(0), CartDTO.class);
        // 封装收藏数据
        FavoritesDTO favoritesDTO = new FavoritesDTO();
        favoritesDTO.setFavPrice(cartDTO.getSpecSellPrice());
        favoritesDTO.setMemberId(cartDTO.getMemberId());
        favoritesDTO.setSpecId(cartDTO.getSpecId());
        favoritesDTO.setStoreId(cartDTO.getStoreId());
        //保存收藏商品
        this.saveGoodsFav(favoritesDTO);
        //删除购物车
        esDataUtils.deleteByQuery(pageModelDTO, ElasticSearchConstant.CART_INDEX);
    }

    /**
     * 查询用户是否收藏店铺和商品ID
     *
     * @param memberId 用户ID
     * @param storeId  店铺ID
     * @param specId   规格ID
     * @return
     */

    @Override
    public FavResulltVO isFavGoodsStore(@RequestParam(value = "memberId") Long memberId,
                                        @RequestParam(value = "storeId", required = false) Long storeId,
                                        @RequestParam(value = "specId", required = false) Long specId) {
        FavResulltVO favResulltDTO = new FavResulltVO();
        if (storeId != null) {
            StoreFavoritesDTO favorite = storeFavoritesService.isFavorite(memberId, storeId);
            favResulltDTO.setStoreFav(favorite == null ? 0 : 1);
        }

        if (specId != null) {
            Integer isFavorites = baseDao.findIsFavorites(specId, memberId);
            favResulltDTO.setGoodsFav(isFavorites);
            //获取当前商品的收藏数量
            Integer num = baseDao.goodsTotalFav(specId);
            favResulltDTO.setNum(num);
        }

        return favResulltDTO;
    }

    /**
     * 查询当前商品收藏数量
     *
     * @param goodsId 规格ID
     * @return
     */

    @Override
    public Integer goodsTotalFav(@RequestParam("goodsId") Long goodsId) {
        return baseDao.goodsTotalFav(goodsId);
    }
}
