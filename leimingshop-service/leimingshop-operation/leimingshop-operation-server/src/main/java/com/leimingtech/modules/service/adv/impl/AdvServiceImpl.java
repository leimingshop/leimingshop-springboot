/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.adv.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.constants.adv.AdvResultCodeConstants;
import com.leimingtech.modules.dao.adv.AdvDao;
import com.leimingtech.modules.dto.adv.AdvCategoryDTO;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.adv.AdvLinkConfigDTO;
import com.leimingtech.modules.entity.adv.AdvEntity;
import com.leimingtech.modules.enums.adv.AdvTypeEnum;
import com.leimingtech.modules.enums.webfloor.LinkTypeEnum;
import com.leimingtech.modules.service.adv.AdvCategoryService;
import com.leimingtech.modules.service.adv.AdvLinkConfigService;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.webfloor.WebFloorService;
import com.leimingtech.modules.vo.adv.AdvEntityVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 广告管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Slf4j
@Service
public class AdvServiceImpl extends CrudServiceImpl<AdvDao, AdvEntity, AdvDTO> implements AdvService {

    private static final String STORE_ID = "storeId";

    private static final String CATEGORY_ID = "categoryId";

    private static final String ADV_KEY = "advKey";

    private static final String ADV_TYPE = "advType";

    private static final String PC_STORE_LOGO = "pcStoreLogo";

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AdvCategoryService advCategoryService;

    @Autowired
    private WebFloorService webFloorService;

    @Autowired
    private AdvLinkConfigService advLinkConfigService;

    /**
     * 功能描述:
     * 〈admin广告分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<AdvDTO> findAdvShowList(@RequestParam Map<String, Object> params) {
        //分页
        IPage<AdvEntity> page = getPage(params, "la.update_date", false);
        //查询
        List<AdvEntityVo> list = null;
        if (params.get(STORE_ID) == null) {
            list = baseDao.findAdvShowList(params, page);
        } else {
            list = baseDao.findStoreAdvShowList(params, page);
        }

        return getPageData(list, page.getTotal(), AdvDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据id查询广告〉
     *
     * @param id 广告id
     * @author : 刘远杰
     */

    @Override
    public AdvDTO findAdvById(Long id) {
        AdvEntityVo advEntityVo = baseDao.findAdvById(id);
        AdvDTO advDTO = ConvertUtils.sourceToTarget(advEntityVo, AdvDTO.class);
        List<AdvLinkConfigDTO> advLinkConfigDTOList = advLinkConfigService.getLinkList(id);
        advDTO.setAdvLinkConfigDTOList(advLinkConfigDTOList);
        return advDTO;
    }

    /**
     * 功能描述:
     * 〈查询当前时间正在展示中的广告〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<AdvDTO> findIngAdv(@RequestParam Map<String, Object> params) {
        List<AdvDTO> ingAdv = baseDao.findIngAdv(params);
        return ingAdv;
    }

    @Override
    public QueryWrapper<AdvEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AdvEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 功能描述:
     * 〈前端展示广告查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<AdvDTO> listIngAdv(@RequestParam Map<String, Object> params) {
        // redis中查询广告
        String key = "";
        List<AdvDTO> advDTOList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        // 获取广告缓存key
        if (StringUtils.isNotBlank((String) params.get(CATEGORY_ID))) {
            // 一般广告缓存key值拼接
            AdvCategoryDTO dto = advCategoryService.get(Long.parseLong((String) params.get(CATEGORY_ID)));
            key = stringBuilder.append(RedisConstants.ADV_PREFIX).append(dto.getAdvKey()).toString();
        } else if (StringUtils.isNotBlank((String) params.get(ADV_KEY))) {
            key = stringBuilder.append(RedisConstants.ADV_PREFIX).append(params.get(ADV_KEY)).toString();
        } else if (StringUtils.isNotBlank((String) params.get(ADV_TYPE))) {
            Integer advType = Integer.parseInt((String) params.get(ADV_TYPE));
            if (advType.equals(AdvTypeEnum.FLOOR.value())) {
                // 楼层广告缓存key值拼接
                key = stringBuilder.append(RedisConstants.ADV_FLOOR_PREFIX).append(params.get("relationId")).toString();
            } else if (advType.equals(AdvTypeEnum.CLASS.value())) {
                // 分类广告缓存key值拼接
                key = stringBuilder.append(RedisConstants.ADV_CLASS_PREFIX).append(params.get("relationId")).toString();
            }
        }
        Object obj = redisUtils.get(key);

        if (obj == null) {
            // redis中不存在，查询数据库当前时间展示中的广告
            advDTOList = baseDao.findIngAdv(params);
            // 更新缓存中的广告
            redisUtils.set(key, advDTOList, RedisConstants.JEDIS_EXPIRE);
        } else {
            // redis获取到广告数据
            advDTOList = (List<AdvDTO>) obj;
        }
        Object storeId = params.get("storeId");
        if (storeId != null) {
            advDTOList = advDTOList.stream().filter(a -> a.getStoreId() != null && a.getStoreId().compareTo(Long.valueOf(storeId.toString())) == 0).collect(Collectors.toList());
        }
        return advDTOList;
    }

    /**
     * 功能描述:
     * 〈保存广告〉
     *
     * @param dto 广告实体
     * @author : 刘远杰
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveAdv(@RequestBody AdvDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        if (dto.getRelationType().equals(LinkTypeEnum.SEARCHGOODSBYCLASS.value())) {
            Long classId = baseDao.getClassIdByCustomId(dto.getRelationTarget());
            dto.setRelationTarget(String.valueOf(classId));
        }

        AdvEntity advEntity = ConvertUtils.sourceToTarget(dto, AdvEntity.class);
        if (dto.getCategoryId() != null && dto.getAdvType() == AdvTypeEnum.NORMAL.value()) {
            // 一般广告保存,储存广告类别信息
            AdvCategoryDTO advCategoryDTO = advCategoryService.get(dto.getCategoryId());
            if (advCategoryDTO != null) {
                advEntity.setCategoryId(advCategoryDTO.getId());
                advEntity.setAdvKey(advCategoryDTO.getAdvKey());
            } else {
                map.put("code", AdvResultCodeConstants.ERR_NO_RESULT);
                map.put("message", "广告分类id不存在");
                return map;
            }
        }
        // 校验是否是pc店铺首页logo，店铺首页轮播只能保存一个
        Integer logoCount = 0;
        if (StringUtils.isNotBlank(advEntity.getAdvKey()) && PC_STORE_LOGO.equals(advEntity.getAdvKey())) {
            logoCount = baseDao.getLogoCount(advEntity.getStoreId(), null);
        }

        if (logoCount > 0) {
            map.put("code", AdvResultCodeConstants.ERR_REPEAT);
            map.put("message", "pc店铺首页Logo只能添加一个");
            return map;
        }

        // 保存
        baseDao.insert(advEntity);
        if (CollectionUtils.isNotEmpty(dto.getAdvLinkConfigDTOList())) {
            saveAdvLink(advEntity, dto);
        }
        // 更新缓存
        updateRedis(advEntity);
        map.put("code", AdvResultCodeConstants.SUCCESS);
        map.put("message", "保存广告成功");

        return map;
    }

    /**
     * 保存pc展示分类广告配置
     *
     * @param advEntity
     * @param dto
     */
    private void saveAdvLink(AdvEntity advEntity, AdvDTO dto) {
        advLinkConfigService.delete(Collections.singletonList(advEntity.getId()));
        List<AdvLinkConfigDTO> advLinkConfigDTO = dto.getAdvLinkConfigDTOList();
        advLinkConfigDTO.stream().forEach(a -> {
            a.setAdvId(advEntity.getId());
        });
        advLinkConfigService.saveBatch(advLinkConfigDTO);

    }

    /**
     * 功能描述:
     * 〈更新广告〉
     *
     * @param dto 广告实体
     * @return : void
     * @author : 刘远杰
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAdv(@RequestBody AdvDTO dto) {

        if (dto.getRelationType() != null && dto.getRelationType().equals(LinkTypeEnum.SEARCHGOODSBYCLASS.value())) {
            Long classId = baseDao.getClassIdByCustomId(dto.getRelationTarget());
            dto.setRelationTarget(String.valueOf(classId));
        }

        AdvEntity entity = ConvertUtils.sourceToTarget(dto, AdvEntity.class);
        baseDao.editAdv(entity);
        if (CollectionUtils.isNotEmpty(dto.getAdvLinkConfigDTOList())) {
            saveAdvLink(entity, dto);
        }
        // 更新缓存
        updateRedis(baseDao.selectById(entity.getId()));
    }

    /**
     * 功能描述:
     * 〈广告逻辑删除〉
     *
     * @param ids
     * @author : 刘远杰
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer logicDeleteAdv(@RequestBody Long[] ids) {

        List<AdvEntity> advEntities = baseDao.selectBatchIds(Arrays.asList(ids));
        Integer row = baseDao.deleteBatchIds(Arrays.asList(ids));
        advLinkConfigService.delete(Arrays.asList(ids));
        for (AdvEntity advEntity : advEntities) {
            // 更新缓存
            updateRedis(advEntity);
        }
        return row;

    }


    /**
     * 根据展示类目ID查询广告信息
     *
     * @param gcId: 展示类目ID
     * @return 广告位集合
     * @date 2019/7/15 21:14
     * @author lixiang
     **/
    @Override
    public List<AdvDTO> findByGcId(Long gcId) {
        AssertUtils.isNull("展示类目ID不能为空");

        // 缓存中获取
        String key = RedisConstants.ADV_CLASS_PREFIX + gcId;
        Object obj = redisUtils.get(key);
        if (obj != null) {
            return (List<AdvDTO>) obj;
        }

        // 从数据库中获取
        QueryWrapper<AdvEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(gcId != null, "relation_id", gcId);
        List<AdvEntity> advEntityList = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(advEntityList, AdvDTO.class);

    }

    /**
     * 功能描述:
     * 〈更新缓存〉
     *
     * @param advEntity 广告实体
     * @author : 刘远杰
     */
    private void updateRedis(AdvEntity advEntity) {

        // 清空对应广告缓存
        deleteRedis(advEntity);
        // 更新缓存
        Map<String, Object> params = new HashMap<>(10);
        if (StringUtils.isNotBlank(advEntity.getAdvKey()) && advEntity.getAdvType() == AdvTypeEnum.NORMAL.value()) {
            params.put(ADV_KEY, advEntity.getAdvKey());
        } else if (advEntity.getAdvType() == AdvTypeEnum.FLOOR.value()) {
            // 楼层广告
            params.put(ADV_TYPE, advEntity.getAdvType() + "");
            params.put("relationId", advEntity.getRelationId() + "");
        } else if (advEntity.getAdvType() == AdvTypeEnum.CLASS.value()) {
            // 分类广告
            params.put(ADV_TYPE, advEntity.getAdvType() + "");
            params.put("relationId", advEntity.getRelationId() + "");
        }
        params.put("storeId", advEntity.getStoreId());
        // 调用查询广告，更新缓存方法
        this.listIngAdv(params);
        // 删除楼层缓存
        if (advEntity.getStoreId() != null) {
            redisUtils.hDel(RedisConstants.STORE_WEB_FLOOR_PREFIX, advEntity.getStoreId().toString());
        } else {
            redisUtils.delete(RedisConstants.WEB_FLOOR_PREFIX);
        }
        // 重新缓存
        Map<String, Object> map = new HashMap<>(2);
        map.put("storeId", advEntity.getStoreId());
        webFloorService.findFrontShopWebFloorList(map);
    }

    /**
     * 功能描述:
     * 〈删除指定广告缓存〉
     *
     * @param advEntity 广告实体
     * @return : void
     * @author : 刘远杰
     */
    private void deleteRedis(AdvEntity advEntity) {
        String key = "";
        if (advEntity.getAdvType() == AdvTypeEnum.FLOOR.value()) {
            key = RedisConstants.ADV_FLOOR_PREFIX + advEntity.getRelationId();
        } else if (advEntity.getAdvType() == AdvTypeEnum.CLASS.value()) {
            key = RedisConstants.ADV_CLASS_PREFIX + advEntity.getRelationId();
        } else if (advEntity.getAdvType() == AdvTypeEnum.NORMAL.value() && StringUtils.isNotBlank(advEntity.getAdvKey())) {
            key = RedisConstants.ADV_PREFIX + advEntity.getAdvKey();
        }

        redisUtils.delete(key);
        log.debug("清除广告缓存{}", key);
    }

    /**
     * 查询店铺商品分类广告
     *
     * @return
     */

    @Override
    public void stoeGoodsClassAdv() {
        List<AdvDTO> advDTOList = baseDao.stoeGoodsClassAdv();
        StringBuilder stringBuilder = new StringBuilder();
        for (AdvDTO advDTO : advDTOList) {
            String key = stringBuilder.append(RedisConstants.ADV_CLASS_PREFIX).append(advDTO.getId().toString()).toString();
            redisUtils.hSet(key, String.valueOf(advDTO.getStoreId()), advDTO, RedisConstants.JEDIS_EXPIRE);
        }
    }

    /**
     * 查询已关联的pc展示分类
     *
     * @return
     */

    @Override
    public List<Long> relevanceClass() {
        return baseDao.relevanceClass();
    }
}
