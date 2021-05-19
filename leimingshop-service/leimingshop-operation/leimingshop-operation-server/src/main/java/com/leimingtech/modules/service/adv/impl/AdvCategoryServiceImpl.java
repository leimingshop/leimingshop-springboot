/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.adv.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.constants.adv.AdvDefaultConstants;
import com.leimingtech.modules.constants.adv.AdvResultCodeConstants;
import com.leimingtech.modules.dao.adv.AdvCategoryDao;
import com.leimingtech.modules.dto.adv.AdvCategoryDTO;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.entity.adv.AdvCategoryEntity;
import com.leimingtech.modules.enums.adv.SysFlagEnum;
import com.leimingtech.modules.service.adv.AdvCategoryService;
import com.leimingtech.modules.service.adv.AdvService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 广告类别管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
@Service
public class AdvCategoryServiceImpl extends CrudServiceImpl<AdvCategoryDao, AdvCategoryEntity, AdvCategoryDTO>
        implements AdvCategoryService {

    @Autowired
    private AdvService advService;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 功能描述:
     * 〈广告类别分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<AdvCategoryDTO> findAdvCategoryPageList(@RequestParam Map<String, Object> params) {
        //分页
        IPage<AdvCategoryEntity> page = getPage(params, Constant.UPDATE_DATE, false);
        //查询
        List<AdvCategoryEntity> list = baseDao.findAdvCategoryPage(params, page);
        return getPageData(list, page.getTotal(), AdvCategoryDTO.class);
    }

    /**
     * 功能描述:
     * 〈广告类别查询〉
     *
     * @param params
     * @author : 刘远杰
     */

    @Override
    public List<AdvCategoryDTO> findAdvCategoryList(@RequestParam Map<String, Object> params) {
        //查询
        List<AdvCategoryEntity> list = baseDao.findAdvCategoryList(params);
        return ConvertUtils.sourceToTarget(list, AdvCategoryDTO.class);
    }

    /**
     * 功能描述:
     * 〈根据id查询广告类别〉
     *
     * @param id 广告类别id
     * @author : 刘远杰
     */

    @Override
    public AdvCategoryDTO get(Long id) {
        AdvCategoryDTO advCategoryDTO = new AdvCategoryDTO();
        AdvCategoryEntity entity = this.baseDao.selectById(id);
        BeanCopier.create(AdvCategoryEntity.class, AdvCategoryDTO.class, false)
                .copy(entity, advCategoryDTO, null);
        return advCategoryDTO;
    }

    /**
     * 功能描述:
     * 〈保存广告类别〉
     *
     * @param dto 广告类别实体
     * @return : void
     * @author : 刘远杰
     */

    @Override
    public void save(@RequestBody AdvCategoryDTO dto) {
        AdvCategoryEntity entity = ConvertUtils.sourceToTarget(dto, this.currentModelClass());
        // 普通广告类别
        entity.setSysFlag(AdvDefaultConstants.SYS_FLAG);
        this.insert(entity);
    }

    /**
     * 功能描述:
     * 〈校验广告类别标识是否重复〉
     *
     * @param advKey 广告类别标识
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkAdvKey(@RequestParam("advKey") String advKey) {
        Map<String, Object> map = new HashMap<>(10);
        if (StringUtils.isBlank(StringUtils.trim(advKey))) {
            map.put("code", AdvResultCodeConstants.ERR_INV_PARAMS);
            map.put("message", "输入广告类别标识不合法");
        } else {
            Map<String, Object> params = new HashMap<>(10);
            params.put("advKey", advKey);
            List<AdvCategoryEntity> advCategoryEntityList = baseDao.findAdvCategoryList(params);
            if (!CollectionUtils.isEmpty(advCategoryEntityList)) {
                map.put("code", AdvResultCodeConstants.ERR_REPEAT);
                map.put("message", "广告类别标识已存在");
            } else {
                map.put("code", AdvResultCodeConstants.SUCCESS);
                map.put("message", "广告类别标识可用");
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈广告分类逻辑删除〉
     *
     * @param ids 广告分类ids
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> logicDeleteAdvCategory(@RequestBody Long[] ids) {
        Map<String, Object> map = new HashMap<>(10);
        // 系统定义广告分类不可删除
        Integer count = baseDao.countAdvCategory(ids);
        if (count > 0) {
            map.put("code", AdvResultCodeConstants.ERR_NO_PER);
            map.put("message", "系统定义广告分类不可删除");
            return map;
        }

        // 广告分类下有展示广告不可删除
        for (Long id : ids) {
            // 查询当前时间类别下正在展示的广告
            Map<String, Object> params = new HashMap(10);
            params.put("categoryId", id);
            params.put("currentTime", new Date());
            List<AdvDTO> advList = advService.findIngAdv(params);
            if (!CollectionUtils.isEmpty(advList)) {
                map.put("code", AdvResultCodeConstants.ERR_NO_PER);
                map.put("message", "广告分类下有展示广告，不可删除");
                return map;
            }
        }

        this.deleteBatchIds(Arrays.asList(ids));
        for (Long id : ids) {
            redisUtils.delete(RedisConstants.ADV_PREFIX + id);
            log.debug("清除广告缓存{}", RedisConstants.ADV_PREFIX + id);
        }
        map.put("code", AdvResultCodeConstants.SUCCESS);
        map.put("message", "删除广告分类成功");
        return map;
    }

    /**
     * 功能描述:
     * 〈更新非系统定义的广告分类〉
     *
     * @param dto 广告分类实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> updateAdvCategory(@RequestBody AdvCategoryDTO dto) {
        Map<String, Object> map = new HashMap<>(10);
        AdvCategoryEntity advCategoryEntity = baseDao.selectById(dto.getId());
        if (advCategoryEntity != null) {
            if (advCategoryEntity.getDelFlag() == DelFlagEnum.DEL.value()) {
                map.put("code", AdvResultCodeConstants.ERR_NO_RESULT);
                map.put("message", "该id对应的数据已删除");
                return map;
            }
            if (advCategoryEntity.getSysFlag() != SysFlagEnum.NOMARL.value()) {
                map.put("code", AdvResultCodeConstants.ERR_NO_PER);
                map.put("message", "楼层、分类广告分类不可修改");
                return map;
            }
            this.update(dto);
            redisUtils.delete(RedisConstants.ADV_PREFIX + dto.getId());
            redisUtils.delete(RedisConstants.ADV_PREFIX + dto.getId());
            log.debug("清除广告缓存{}", RedisConstants.ADV_PREFIX + dto.getId());
            map.put("code", AdvResultCodeConstants.SUCCESS);
            map.put("message", "修改广告类别成功");
            return map;
        } else {
            map.put("code", AdvResultCodeConstants.ERR_NO_RESULT);
            map.put("message", "该id对应的数据不存在");
            return map;
        }
    }

    /**
     * 功能描述:
     * 〈根据广告分类标识查询广告分类〉
     *
     * @param advKey 广告分类标识
     * @author : 刘远杰
     */

    @Override
    public AdvCategoryDTO findAdvCategoryByAdvKey(String advKey) {
        AdvCategoryEntity entity = baseDao.findAdvCategoryByAdvKey(advKey);
        return ConvertUtils.sourceToTarget(entity, AdvCategoryDTO.class);
    }

    /**
     * 功能描述:
     * 〈更新广告类别状态〉
     *
     * @param status 更改后状态
     * @param id     广告类别id
     * @return : int
     * @author : 刘远杰
     */

    @Override
    public int updateStatus(@RequestParam("status") Integer status, @RequestParam("id") Long id) {

        AdvCategoryEntity advCategoryEntity = new AdvCategoryEntity();
        advCategoryEntity.setId(id);
        advCategoryEntity.setStatus(status);
        return baseDao.updateById(advCategoryEntity);
    }

    @Override
    public QueryWrapper<AdvCategoryEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AdvCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        return wrapper;
    }

}
