/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.mobile.indexmenu.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.constants.mobile.indexmenu.MobileIndexMenuResultConstant;
import com.leimingtech.modules.dao.mobile.indexmenu.MobileIndexMenuDao;
import com.leimingtech.modules.dto.mobile.indexmenu.InsertMobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.MobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.UpdateMobileIndexMenuDTO;
import com.leimingtech.modules.dto.mobile.indexmenu.UpdateShowDTO;
import com.leimingtech.modules.entity.mobile.indexmenu.MobileIndexMenuEntity;
import com.leimingtech.modules.enums.mobile.indexmenu.ShowFlagEnum;
import com.leimingtech.modules.enums.webfloor.LinkTypeEnum;
import com.leimingtech.modules.service.mobile.indexmenu.MobileIndexMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 移动首页菜单管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019-06-11
 */
@Service

@Slf4j
public class MobileIndexMenuServiceImpl extends CrudServiceImpl<MobileIndexMenuDao, MobileIndexMenuEntity, MobileIndexMenuDTO>
        implements MobileIndexMenuService {

    private static final String CODE = "code";

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 功能描述:
     * 〈移动首页菜单分页查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public PageData<MobileIndexMenuDTO> page(@RequestParam Map<String, Object> params) {

        // 构造排序条件
        QueryWrapper<MobileIndexMenuEntity> wrapper = this.getWrapper(params);
        wrapper.orderByAsc("sort");
        wrapper.orderByDesc("create_date");

        IPage<MobileIndexMenuEntity> page = this.baseDao.selectPage(this.getPage(params, "sort",
                true), wrapper);
        return this.getPageData(page, this.currentDtoClass());
    }

    /**
     * 功能描述:
     * 〈移动首页菜单展示中集合〉
     *
     * @author : 刘远杰
     */

    @Override
    public List<MobileIndexMenuDTO> listIng() {

        List<MobileIndexMenuDTO> mobileIndexMenuDTOList = new ArrayList<>();

        // 查询缓存
        Object obj = redisUtils.get(RedisConstants.MOBILE_INDEX_MENU_PREFIX);

        if (obj == null) {
            List<MobileIndexMenuEntity> baseList = baseDao.selectList(new QueryWrapper<MobileIndexMenuEntity>()
                    .eq("show_flag", ShowFlagEnum.YES.value())
                    .orderByAsc("sort")
                    .last(", create_date desc"));
            if (!CollectionUtils.isEmpty(baseList)) {
                mobileIndexMenuDTOList = baseList.stream().map(mobileIndexMenuEntity -> {
                    MobileIndexMenuDTO mobileIndexMenuDTO = new MobileIndexMenuDTO();
                    BeanCopier.create(MobileIndexMenuEntity.class, MobileIndexMenuDTO.class, false).copy(mobileIndexMenuEntity, mobileIndexMenuDTO, null);
                    return mobileIndexMenuDTO;
                }).collect(Collectors.toList());
            }
            // 更新缓存
            redisUtils.set(RedisConstants.MOBILE_INDEX_MENU_PREFIX, mobileIndexMenuDTOList, RedisConstants.JEDIS_EXPIRE);
            log.debug("reids更新移动首页菜单缓存:{}", mobileIndexMenuDTOList);
        } else {
            mobileIndexMenuDTOList = (List<MobileIndexMenuDTO>) obj;
            log.debug("reids获取移动首页菜单缓存:{}", mobileIndexMenuDTOList);
        }
        return mobileIndexMenuDTOList;

    }

    /**
     * 功能描述:
     * 〈查询移动首页菜单详情〉
     *
     * @param id 移动首页菜单id
     * @author : 刘远杰
     */

    @Override
    public MobileIndexMenuDTO get(long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈保存移动首页菜单〉
     *
     * @param dto 保存移动首页菜单实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> saveMobileIndexMenu(@RequestBody InsertMobileIndexMenuDTO dto) {
        Map<String, Object> result = this.checkMenuNameWhenAdd(dto.getMenuName());
        if ((Integer) result.get(CODE) == MobileIndexMenuResultConstant.SUCCESS) {
            MobileIndexMenuEntity entity = ConvertUtils.sourceToTarget(dto, MobileIndexMenuEntity.class);
            setClassId(entity);
            baseDao.insert(entity);

            // 更新缓存
            redisUtils.delete(RedisConstants.MOBILE_INDEX_MENU_PREFIX);
            log.debug("删除移动首页菜单缓存");
            this.listIng();

            result.put("message", "保存成功");
        }

        return result;
    }

    /**
     * 功能描述:
     * 〈编辑移动首页菜单〉
     *
     * @param dto 编辑移动首页菜单实体
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> updateMobileIndexMenu(@RequestBody UpdateMobileIndexMenuDTO dto) {
        Map<String, Object> result = this.checkMenuNameWhenUpdate(dto.getMenuName(), dto.getId());
        if ((Integer) result.get(CODE) == MobileIndexMenuResultConstant.SUCCESS) {
            MobileIndexMenuEntity entity = ConvertUtils.sourceToTarget(dto, MobileIndexMenuEntity.class);
            setClassId(entity);
            baseDao.editMobileIndexMenu(entity);

            // 更新缓存
            redisUtils.delete(RedisConstants.MOBILE_INDEX_MENU_PREFIX);
            log.debug("删除移动首页菜单缓存");
            this.listIng();

            result.put("message", "修改成功");
        }

        return result;
    }

    private void setClassId(MobileIndexMenuEntity entity) {
        if (LinkTypeEnum.SEARCHGOODSBYCLASS.value().equals(entity.getLinkType())) {
            Long classId = baseDao.getClassIdByCustomId(entity.getUrl());
            entity.setUrl(String.valueOf(classId));
        }
    }

    /**
     * 启用禁用
     *
     * @param dto
     * @return
     */

    @Override
    public void isShow(@RequestBody UpdateShowDTO dto) {
        baseDao.updateById(ConvertUtils.sourceToTarget(dto, MobileIndexMenuEntity.class));
        // 删除缓存
        redisUtils.delete(RedisConstants.MOBILE_INDEX_MENU_PREFIX);
        this.listIng();

    }

    /**
     * 功能描述:
     * 〈逻辑删除移动首页菜单〉
     *
     * @param id 移动首页菜单id
     * @author : 刘远杰
     */

    @Override
    public int deleteById(Long id) {

        int count = baseDao.deleteById(id);
        // 更新缓存
        redisUtils.delete(RedisConstants.MOBILE_INDEX_MENU_PREFIX);
        log.debug("删除移动首页菜单缓存");
        this.listIng();

        return count;
    }

    /**
     * 功能描述:
     * 〈批量逻辑删除移动首页菜单〉
     *
     * @param ids 移动首页菜单id数组
     * @author : 刘远杰
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));
        // 更新缓存
        redisUtils.delete(RedisConstants.MOBILE_INDEX_MENU_PREFIX);
        log.debug("删除移动首页菜单缓存");
        this.listIng();
    }

    /**
     * 功能描述:
     * 〈新增移动首页菜单校验规格菜单名称重复性〉
     *
     * @param menuName 移动首页菜单名称
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkMenuNameWhenAdd(@RequestParam("menuName") String menuName) {
        Map<String, Object> map = new HashMap<>(10);
        if (StringUtils.isBlank(StringUtils.trim(menuName))) {
            map.put(CODE, MobileIndexMenuResultConstant.ERR_INV_PARAMS);
            map.put("message", "输入移动首页菜单名称不合法");
        } else {
            QueryWrapper<MobileIndexMenuEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("menu_name", menuName);
            List<MobileIndexMenuEntity> entities = baseDao.selectList(wrapper);
            if (CollectionUtils.isNotEmpty(entities)) {
                map.put(CODE, MobileIndexMenuResultConstant.ERR_REPEAT);
                map.put("message", "移动首页菜单名称已存在");
            } else {
                map.put(CODE, MobileIndexMenuResultConstant.SUCCESS);
                map.put("message", "移动首页菜单名称可用");
            }
        }
        return map;
    }

    /**
     * 功能描述:
     * 〈修改移动首页菜单校验规格菜单名称重复性〉
     *
     * @param menuName 移动首页菜单名称
     * @param id       移动首页菜单id
     * @author : 刘远杰
     */

    @Override
    public Map<String, Object> checkMenuNameWhenUpdate(@RequestParam("menuName") String menuName, @RequestParam("id") Long id) {
        Map<String, Object> map = new HashMap<>(10);
        MobileIndexMenuEntity entity = baseDao.selectById(id);
        if (entity == null) {
            // 传入id是否正确
            map.put(CODE, MobileIndexMenuResultConstant.ERR_INV_PARAMS);
            map.put("message", "移动首页菜单不存在");
        } else {
            if (StringUtils.isBlank(StringUtils.trim(menuName))) {
                map.put(CODE, MobileIndexMenuResultConstant.ERR_INV_PARAMS);
                map.put("message", "输入移动首页菜单名称不合法");
            } else {
                QueryWrapper<MobileIndexMenuEntity> wrapper = new QueryWrapper<>();
                wrapper.eq("menu_name", menuName);
                wrapper.ne("id", id);
                List<MobileIndexMenuEntity> entities = baseDao.selectList(wrapper);
                if (CollectionUtils.isNotEmpty(entities)) {
                    map.put(CODE, MobileIndexMenuResultConstant.ERR_REPEAT);
                    map.put("message", "移动首页菜单名称已存在");
                } else {
                    map.put(CODE, MobileIndexMenuResultConstant.SUCCESS);
                    map.put("message", "移动首页菜单名称可用");
                }
            }
        }
        return map;
    }

    @Override
    public QueryWrapper<MobileIndexMenuEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String menuName = (String) params.get("menuName");
        String showFlag = (String) params.get("showFlag");

        QueryWrapper<MobileIndexMenuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(menuName), "menu_name", menuName);
        wrapper.eq(StringUtils.isNotBlank(showFlag), "show_flag", showFlag);

        return wrapper;
    }

}
