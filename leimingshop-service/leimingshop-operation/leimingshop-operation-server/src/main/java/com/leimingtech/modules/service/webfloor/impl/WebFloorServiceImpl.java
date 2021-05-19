/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.webfloor.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.constants.webfloor.WebFloorDefaultConstant;
import com.leimingtech.modules.dao.webfloor.WebFloorDao;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.webfloor.*;
import com.leimingtech.modules.entity.webfloor.WebFloorEntity;
import com.leimingtech.modules.enums.adv.AdvTypeEnum;
import com.leimingtech.modules.enums.webfloor.IsShowEnum;
import com.leimingtech.modules.enums.webfloor.LinkTypeEnum;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.webfloor.WebFloorLinkConfigService;
import com.leimingtech.modules.service.webfloor.WebFloorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * h5楼层设置表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Service

@Slf4j
public class WebFloorServiceImpl extends CrudServiceImpl<WebFloorDao, WebFloorEntity, WebFloorDTO> implements WebFloorService {


    private static final String TIME = "time";

    @Autowired
    private WebFloorLinkConfigService webFloorLinkConfigService;

    @Autowired
    private AdvService advService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private GoodsService goodsService;

    /**
     * 功能描述:
     * 〈楼层分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<WebFloorDTO> findShopWebFloorPagerList(@RequestParam Map<String, Object> params) {
        //分页
        IPage<WebFloorEntity> page = getPage(params, "sort", true);
        //查询
        List<WebFloorEntity> list = baseDao.findShopWebFloorPage(params, page);
        return getPageData(list, page.getTotal(), WebFloorDTO.class);
    }

    /**
     * 功能描述:
     * 〈楼层查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<WebFloorDTO> list(@RequestParam Map<String, Object> params) {
        //查询
        List<WebFloorEntity> list = baseDao.findShopWebFloorPagerList(params);
        return ConvertUtils.sourceToTarget(list, WebFloorDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据id查询楼层及关联图片跳转链接〉
     *
     * @param id 楼层id
     * @author : 刘远杰
     */

    @Override
    public WebFloorAndLinkDTO findWebFloorLinkConfigById(Long id) {
        WebFloorAndLinkDTO webFloorAndLinkDTO = null;

        // 查询楼层主表
        WebFloorDTO webFloorDTO = this.get(id);

        if (webFloorDTO != null) {
            webFloorAndLinkDTO = ConvertUtils.sourceToTarget(webFloorDTO, WebFloorAndLinkDTO.class);
            Map<String, Object> params = new HashMap<>(10);
            // 查询图片链接集合
            params.put("floorId", id);
            List<WebFloorLinkConfigDTO> webFloorLinkConfigDTOList = webFloorLinkConfigService.list(params);

            webFloorAndLinkDTO.setWebFloorLinkConfigDTOList(webFloorLinkConfigDTOList);
        }
        return webFloorAndLinkDTO;
    }

    /**
     * 功能描述:
     * 〈保存楼层及图片链接〉
     *
     * @param dto 楼层及图片链接实体
     * @author : 刘远杰
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWebFloorAndLink(@RequestBody InsertWebFloorAndLinkDTO dto) {

        // 保存楼层主表
        WebFloorEntity entity = ConvertUtils.sourceToTarget(dto, WebFloorEntity.class);

        // 默认按钮名称
        if (StringUtils.isBlank(entity.getActionName())) {
            entity.setActionName(WebFloorDefaultConstant.ACTION_NAME);
        }
        // 默认展示风格
        if (StringUtils.isBlank(entity.getShowType())) {
            entity.setShowType(WebFloorDefaultConstant.SHOW_TYPE);
        }

        baseDao.insert(entity);

        // 批量保存楼层图片跳转表
        batchInsertWebFloorLinkConfig(entity, dto.getWebFloorLinkConfigDTOList());

        // 更新缓存
        if (dto.getStoreId() != null) {
            redisUtils.hDel(RedisConstants.STORE_WEB_FLOOR_PREFIX, dto.getStoreId().toString());
        } else {
            redisUtils.delete(RedisConstants.WEB_FLOOR_PREFIX);
        }
        log.debug("删除h5楼层缓存");
        Map<String, Object> map = new HashMap<>(2);
        map.put("storeId", dto.getStoreId());
        this.findFrontShopWebFloorList(map);

    }

    /**
     * 功能描述:
     * 〈修改楼层及图片跳转〉
     *
     * @param dto 楼层及图片链接实体
     * @author : 刘远杰
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWebFloorAndLink(@RequestBody UpdateWebFloorAndLinkDTO dto) {

        // 删除原关联内容，重新插入
        WebFloorEntity webFloorEntity = baseDao.selectById(dto.getId());
        if (webFloorEntity != null) {
            Map<String, Object> params = new HashMap<>(10);
            params.put("floorId", webFloorEntity.getId());
            webFloorLinkConfigService.deleteWebFloorLinkConfig(params);
            // 修改主表
            WebFloorEntity entity = ConvertUtils.sourceToTarget(dto, WebFloorEntity.class);
            baseDao.editShopWebFloorPagerList(entity);

            // 批量保存楼层图片跳转表
            entity.setShowType(webFloorEntity.getShowType());
            batchInsertWebFloorLinkConfig(entity, dto.getWebFloorLinkConfigDTOList());
        }

        // 更新缓存
        if (dto.getStoreId() != null) {
            redisUtils.hDel(RedisConstants.STORE_WEB_FLOOR_PREFIX, dto.getStoreId().toString());
        } else {
            redisUtils.delete(RedisConstants.WEB_FLOOR_PREFIX);
        }
        log.debug("删除h5楼层缓存");
        Map<String, Object> map = new HashMap<>(2);
        map.put("storeId", dto.getStoreId());
        this.findFrontShopWebFloorList(map);

    }

    /**
     * 功能描述:
     * 〈删除h5楼层设置及关联图片链接（逻辑删除）〉
     *
     * @param ids 删除楼层索引
     * @author : 刘远杰
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWebFloorAndLink(@RequestBody Long[] ids, @RequestParam(value = "storeId", required = false) Long storeId) {
        for (Long id : ids) {
            Map<String, Object> map = new HashMap<>(10);
            map.put("floorId", id);
            webFloorLinkConfigService.logicDeletewebFloorLinkConfig(map);
        }
        baseDao.deleteBatchIds(Arrays.asList(ids));
        // 更新缓存
        if (storeId != null) {
            redisUtils.hDel(RedisConstants.STORE_WEB_FLOOR_PREFIX, storeId.toString());
        } else {
            redisUtils.delete(RedisConstants.WEB_FLOOR_PREFIX);
        }
        log.debug("删除h5楼层缓存");
        Map<String, Object> map = new HashMap<>(2);
        map.put("storeId", storeId);
        this.findFrontShopWebFloorList(map);
    }

    /**
     * 功能描述:
     * 〈批量保存楼层托按链接，插入楼层标识、样式〉
     *
     * @param entity 楼层实体
     * @param list   图片链接集合
     * @author : 刘远杰mo'd
     */
    private void batchInsertWebFloorLinkConfig(WebFloorEntity entity, List<InsertWebFloorLinkConfigDTO> list) {
        List<WebFloorLinkConfigDTO> webFloorLinkConfigDTOList = new ArrayList<>();
        for (InsertWebFloorLinkConfigDTO insertWebFloorLinkConfigDTO : list) {
            WebFloorLinkConfigDTO webFloorLinkConfigDTO = ConvertUtils.sourceToTarget(insertWebFloorLinkConfigDTO,
                    WebFloorLinkConfigDTO.class);
            // 插入楼层id、样式
            webFloorLinkConfigDTO.setFloorId(entity.getId());
            webFloorLinkConfigDTO.setTypeShowMarking(entity.getShowType());
            webFloorLinkConfigDTOList.add(webFloorLinkConfigDTO);
        }
        // 批量保存楼层主表
        webFloorLinkConfigService.insertBatch(webFloorLinkConfigDTOList);
    }

    @Override
    public QueryWrapper<WebFloorEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        Integer isShow = (Integer) params.get("isShow");

        QueryWrapper<WebFloorEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        if (isShow != null) {
            wrapper.eq("is_show", isShow);
        }

        return wrapper;
    }

    /**
     * 功能描述:
     * 〈h5展示中楼层集合查询，获取楼层广告〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<WebFloorAndLinkDTO> findFrontShopWebFloorList(@RequestParam Map<String, Object> params) {
        Long storeId = null;
        Object object = params.get("storeId");
        Object obj = null;
        if (object != null) {
            storeId = Long.valueOf(object.toString());
            obj = redisUtils.hGet(RedisConstants.STORE_WEB_FLOOR_PREFIX, String.valueOf(storeId));
        } else {
            obj = redisUtils.get(RedisConstants.WEB_FLOOR_PREFIX);
        }
        if (obj == null) {

            // 应该查询展示类目关联的分类ID便于前端进行商品搜索跳转
            List<WebFloorEntity> webFloorEntityList = baseDao.findH5WebFloor(storeId);

            List<WebFloorAndLinkDTO> webFloorAndLinkDTOList = new ArrayList<>();

            if (CollectionUtils.isNotEmpty(webFloorEntityList)) {
                webFloorAndLinkDTOList = webFloorEntityList.stream().map(webFloorEntity -> {
                    WebFloorAndLinkDTO webFloorAndLinkDTO = new WebFloorAndLinkDTO();
                    BeanCopier.create(WebFloorEntity.class, WebFloorAndLinkDTO.class, false).copy(webFloorEntity, webFloorAndLinkDTO, null);
                    params.put("floorId", webFloorAndLinkDTO.getId());
                    // 应该查询展示类目关联的分类ID便于前端进行商品搜索跳转
                    List<WebFloorLinkConfigDTO> webFloorLinkConfigDTOList = webFloorLinkConfigService.findConfigListByFloorId(webFloorAndLinkDTO.getId(), webFloorEntity.getStoreId());
                    if (webFloorEntity.getStoreId() == null && webFloorEntity.getFloorType() == IsShowEnum.H5_FLOOR.value()) {
                        webFloorAndLinkDTO.setWebFloorLinkConfigDTOList(webFloorLinkConfigDTOList);
                    } else {
                        for (WebFloorLinkConfigDTO webFloorLinkConfigDTO : webFloorLinkConfigDTOList) {
                            Boolean flag = webFloorLinkConfigDTO.getConfigStyle() != null && webFloorLinkConfigDTO.getConfigStyle() == IsShowEnum.GOODS_STYLE.value();
                            if (flag || LinkTypeEnum.GOODSDETAIL.value().equals(webFloorLinkConfigDTO.getLinkType())) {
                                setGoodsInfo(webFloorLinkConfigDTO);
                            }
                        }
                        webFloorAndLinkDTO.setWebFloorLinkConfigDTOList(webFloorLinkConfigDTOList);
                    }
                    // 查询楼层广告
                    params.clear();
                    params.put("advType", AdvTypeEnum.FLOOR.value() + "");
                    params.put("relationId", webFloorAndLinkDTO.getId() + "");
                    params.put("storeId", webFloorAndLinkDTO.getStoreId());
                    List<AdvDTO> advDTOList = advService.listIngAdv(params);
                    webFloorAndLinkDTO.setAdvDTOList(advDTOList);
                    return webFloorAndLinkDTO;
                }).collect(Collectors.toList());
            }
            if (storeId != null) {
                redisUtils.hSet(RedisConstants.STORE_WEB_FLOOR_PREFIX, String.valueOf(storeId), webFloorAndLinkDTOList, RedisConstants.JEDIS_EXPIRE);
            } else {
                timeUpdate(webFloorAndLinkDTOList, params);
                redisUtils.set(RedisConstants.WEB_FLOOR_PREFIX, webFloorAndLinkDTOList, RedisConstants.JEDIS_EXPIRE);
            }

            log.debug("reids更新h5楼层缓存:{}", webFloorAndLinkDTOList);
            return webFloorAndLinkDTOList;
        } else {
            List<WebFloorAndLinkDTO> webFloorAndLinkDTOList = (List<WebFloorAndLinkDTO>) obj;
            log.debug("reids获取h5楼层缓存:{}", webFloorAndLinkDTOList);
            return webFloorAndLinkDTOList;
        }

    }

    /**
     * 增加商品信息
     * 商品规格ID和商品ID还有商品价格都拼接在typeKeyword字段里面
     * 现在前端调用一次接口，后台代码会执行两次方法，在执行第二次方法的时候数据会有问题，现在还没找出哪里有问题
     * 所以在查询商品信息的时候做一个判断，如果typekeyWord的长度大于20就不去查询商品信息了
     *
     * @param webFloorLinkConfigDTO
     */
    private void setGoodsInfo(WebFloorLinkConfigDTO webFloorLinkConfigDTO) {
        //TODO 暂定写法；
        if (webFloorLinkConfigDTO.getTypeKeyWord().length() <= 19) {
            GoodsDTO goodsDTO = goodsService.get(Long.valueOf(webFloorLinkConfigDTO.getTypeKeyWord()));
            if (goodsDTO != null) {
                webFloorLinkConfigDTO.setTypeKeyWord(goodsDTO.getSpecId() + "," + goodsDTO.getId() + "," + goodsDTO.getSpecSellPrice());
                if (webFloorLinkConfigDTO.getConfigStyle() != null && webFloorLinkConfigDTO.getConfigStyle() == IsShowEnum.GOODS_STYLE.value()) {
                    webFloorLinkConfigDTO.setSubTitle(goodsDTO.getGoodsSubTitle());
                    webFloorLinkConfigDTO.setGoodsName(goodsDTO.getGoodsName());
                }
            }
        }

    }

    /**
     * 定时更新店铺楼层
     *
     * @param webFloorAndLinkDTOList
     * @param params
     */
    private void timeUpdate(List<WebFloorAndLinkDTO> webFloorAndLinkDTOList, Map<String, Object> params) {
        if (StringUtils.isNotBlank((String) params.get(TIME))) {
            List<WebFloorAndLinkDTO> collect = webFloorAndLinkDTOList.stream().filter(a -> (a.getStoreId() != null)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)) {
                return;
            }
            Map<Long, List<WebFloorAndLinkDTO>> listMap = collect.stream().collect(Collectors.groupingBy(WebFloorAndLinkDTO::getStoreId));
            Set<Long> keySet = listMap.keySet();
            for (Long key : keySet) {
                List<WebFloorAndLinkDTO> value = listMap.get(key);
                redisUtils.hSet(RedisConstants.STORE_WEB_FLOOR_PREFIX, String.valueOf(key), value, RedisConstants.JEDIS_EXPIRE);
            }
        }

    }

}
