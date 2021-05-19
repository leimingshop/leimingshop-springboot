/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.storegoodsclass.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.dao.storegoodsclass.StoreGoodsClassDao;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.storegoodsclass.*;
import com.leimingtech.modules.entity.storegoodsclass.StoreGoodsClassEntity;
import com.leimingtech.modules.enums.adv.AdvTypeEnum;
import com.leimingtech.modules.enums.goodsclass.GoodsClassErrorCodeEnum;
import com.leimingtech.modules.excel.goods.StoreGoodsClassTemplateExcel;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.storegoodsclass.StoreGoodsClassService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 店铺商品分类表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-05-12
 */
@Service
@Transactional(rollbackFor = Exception.class)

@Slf4j
public class StoreGoodsClassServiceImpl extends BaseServiceImpl<StoreGoodsClassDao, StoreGoodsClassEntity> implements StoreGoodsClassService {

    private static final String SUFFIX = ".xlsx";


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private AdvService advService;
    /**
     * 店铺id
     */
    private Long storeId;
    /**
     * 一级店铺商品分类名称
     */
    private String firstStoreGoodsClassName = null;

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<StoreGoodsClassDTO> page(@RequestParam Map<String, Object> params) {
        IPage<StoreGoodsClassEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, StoreGoodsClassDTO.class);
    }

    /**
     * 条件查询
     *
     * @param params
     * @return
     */
    @Override

    public List<StoreGoodsClassListDTO> list(@RequestParam Map<String, Object> params) {
        //查询所有分类
        String storeId = (String) params.get("storeId");
        String type = (String) params.get("type");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag", DelFlagEnum.NORMAL.value());
        queryWrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        queryWrapper.orderByAsc("sort");
        queryWrapper.orderByDesc("create_date");
        //除列表外的其他地方只查展示的数据
        if (StringUtils.isNotBlank(type)) {
            queryWrapper.eq("show_flag", 1);
        }
        List<StoreGoodsClassEntity> list = baseDao.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        List<StoreGoodsClassListDTO> classListDTOList = ConvertUtils.sourceToTarget(list, StoreGoodsClassListDTO.class);
        if (StringUtils.isNotBlank(type)) {
            List<StoreGoodsClassListDTO> customListDTOList = TreeUtils.build(classListDTOList, 0L);
            List<StoreGoodsClassListDTO> collect = customListDTOList.stream().filter(a -> CollectionUtils.isNotEmpty(a.getChildren())).collect(Collectors.toList());
            return collect;
        }
        //获取所有分类ID
        List<Long> classLists = classListDTOList.stream().filter(a -> a.getGcParentId() != 0).collect(Collectors.toList()).stream().map(StoreGoodsClassListDTO::getId).collect(Collectors.toList());
        //根据分类ID查询商品数量
        List<Map<String, Object>> classCount = goodsService.findClassGoodsCount(classLists);
        for (StoreGoodsClassListDTO storeGoodsClassDTO : classListDTOList) {
            for (Map<String, Object> map : classCount) {
                Object firstStoreGoodsGcId = map.get("storeGoodsGcId");
                Object num = map.get("num");
                if (firstStoreGoodsGcId == null) {
                    break;
                }

                if (storeGoodsClassDTO.getId().compareTo(Long.valueOf(firstStoreGoodsGcId.toString())) == 0) {
                    storeGoodsClassDTO.setNum(Integer.valueOf(num.toString()) == null ? 0 : Integer.valueOf(num.toString()));

                }
            }

            if (storeGoodsClassDTO.getNum() == null) {
                storeGoodsClassDTO.setNum(0);
            }
        }


        List<StoreGoodsClassListDTO> customListDTOList = TreeUtils.build(classListDTOList, 0L);
        //遍历一级分类
        for (StoreGoodsClassListDTO customListDTO : customListDTOList) {
            List<StoreGoodsClassListDTO> childrens2 = customListDTO.getChildren();
            int oneCount = 0;
            if (CollectionUtils.isNotEmpty(childrens2)) {
                //遍历二级分类
                for (StoreGoodsClassListDTO children2 : childrens2) {
                    oneCount = oneCount + children2.getNum();
                }
            }
            customListDTO.setNum(oneCount);
        }

        return customListDTOList;
    }

    /**
     * 条件构造器
     *
     * @param params
     * @return
     */
    private QueryWrapper<StoreGoodsClassEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<StoreGoodsClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 获取分类详情
     *
     * @param id
     * @return
     */
    @Override

    public StoreGoodsClassDTO get(Long id) {
        StoreGoodsClassEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, StoreGoodsClassDTO.class);
    }

    /**
     * 保存分类
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody AddStoreGoodsClassDTO dto) {
        StoreGoodsClassEntity entity = ConvertUtils.sourceToTarget(dto, StoreGoodsClassEntity.class);

        insert(entity);
        // 更新缓存
        redisUtils.hDel(RedisConstants.STORE_GOODS_CLASS, entity.getStoreId().toString());
        this.findFrontStoreGoodsClass(entity.getStoreId(), null);
    }

    /**
     * 更新分类
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody UpdateStoreGoodsClassDTO dto) {
        StoreGoodsClassEntity entity = ConvertUtils.sourceToTarget(dto, StoreGoodsClassEntity.class);
        updateById(entity);

        //如果分类是一级分类则修改子分类一同被禁用
        if (entity.getShowFlag() == 0) {
            StoreGoodsClassEntity classEntity = baseDao.selectById(entity.getId());
            if (classEntity.getGcParentId() == 0) {
                List<StoreGoodsClassEntity> list = baseDao.selectList(Wrappers.<StoreGoodsClassEntity>lambdaQuery()
                        .eq(StoreGoodsClassEntity::getGcParentId, entity.getId())
                        .eq(StoreGoodsClassEntity::getDelFlag, DelFlagEnum.NORMAL.values()));
                for (int i = 0; i < list.size(); i++) {
                    StoreGoodsClassEntity info = list.get(i);
                    info.setShowFlag(0);
                    updateById(info);
                }
            }
        }

        // 更新缓存
        redisUtils.hDel(RedisConstants.STORE_GOODS_CLASS, entity.getStoreId().toString());
        this.findFrontStoreGoodsClass(entity.getStoreId(), null);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Map<String, Object> delete(@RequestBody Long[] ids, @RequestParam("storeId") Long storeId) {
        Map<String, Object> res = new HashMap<>(10);
        //校验是否有子分类
        for (Long id : ids) {
            //查询此分类下是否存在子分类
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("gc_parent_id", id);
            wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
            Integer childNum = baseDao.selectCount(wrapper);


            if (childNum != null && childNum > 0) {
                StoreGoodsClassEntity storeGoodsClassEntity = baseDao.selectById(id);
                res.put("code", GoodsClassErrorCodeEnum.DELETE_GOODCLASS_FAIL.value());
                res.put("msg", "存在子分类: " + storeGoodsClassEntity.getGcName() + ",请先删除子分类");
                return res;
            }
        }
        // 校验店铺分类下面是否关联商品
        List<String> nameList = goodsService.findClassName(ids);
        if (CollectionUtils.isEmpty(nameList)) {
            baseDao.deleteBatchIds(Arrays.asList(ids));
            // 更新缓存
            redisUtils.hDel(RedisConstants.STORE_GOODS_CLASS, storeId.toString());
            this.findFrontStoreGoodsClass(storeId, null);

        } else {
            res.put("code", GoodsClassErrorCodeEnum.GOODS_CLASS_RELATION_GOODS.value());
            res.put("msg", "改分类下关联商品:" + nameList.get(0) + "不可做删除操作");
        }

        return res;
    }

    /**
     * 查询分类名称是否重复
     *
     * @param gcName  分类名称
     * @param storeId 店铺ID
     * @param type    1 校验同一级别下分类名称不能重复，2 校验子分类不能和父分类名称重复
     * @return
     */

    @Override
    public Integer classNameCount(@RequestParam("gcName") String gcName,
                                  @RequestParam("storeId") Long storeId,
                                  @RequestParam(value = "gcParentId", required = false) Long gcParentId,
                                  @RequestParam(value = "type", required = false) Integer type,
                                  @RequestParam(value = "id", required = false) Long id) {

        return baseDao.classNameCount(gcName, storeId, gcParentId, type, id);
    }

    /**
     * 查询店铺商品分类缓存
     *
     * @param storeId
     * @param type    1 H5查询
     * @return
     */

    @Override
    public List<StoreGoodsClassListDTO> findFrontStoreGoodsClass(@RequestParam(value = "storeId") Long storeId,
                                                                 @RequestParam(value = "type", required = false) Integer type) {

        Object obj = redisUtils.hGet(RedisConstants.STORE_GOODS_CLASS, String.valueOf(storeId));
        List<StoreGoodsClassListDTO> classListDTOList;
        if (obj == null) {
            // 应该查询展示类目关联的分类ID便于前端进行商品搜索跳转
            List<StoreGoodsClassListDTO> storeGoodsClassList = baseDao.findStoreGoodsClass(storeId);
            List<StoreGoodsClassListDTO> build = TreeUtils.build(storeGoodsClassList, 0L);
            classListDTOList = build.stream().filter(c -> c.getChildren() != null).collect(Collectors.toList());
            redisUtils.hSet(RedisConstants.STORE_GOODS_CLASS, String.valueOf(storeId), classListDTOList, RedisConstants.JEDIS_EXPIRE);
        } else {
            classListDTOList = (List<StoreGoodsClassListDTO>) obj;
        }
        // 查询广告
        if (type != null) {
            classListDTOList.stream().forEach(s -> {
                Map<String, Object> params = new HashMap<>(10);
                params.put("advType", AdvTypeEnum.CLASS.value() + "");
                params.put("relationId", s.getId() + "");
                params.put("storeId", storeId.toString());
                List<AdvDTO> advDTOList = advService.listIngAdv(params);
                if (CollectionUtils.isNotEmpty(advDTOList)) {
                    s.setList(ConvertUtils.sourceToTarget(advDTOList, AdvRelatedGoodsDTO.class));
                } else {
                    s.setList(new ArrayList<>());
                }
            });

        }
        return classListDTOList;
    }

    /**
     * 定时更新店铺商品分类
     */

    @Override
    public void timeUpdate() {
        List<StoreGoodsClassListDTO> storeGoodsClassList = baseDao.findStoreGoodsClass(null);
        if (CollectionUtils.isEmpty(storeGoodsClassList)) {
            return;
        }
        // 更新分类广告
        storeGoodsClassList.forEach(s -> {
            Map<String, Object> params = new HashMap<>(3);
            redisUtils.delete(RedisConstants.ADV_CLASS_PREFIX + s.getId());
            params.put("advType", AdvTypeEnum.CLASS.value() + "");
            params.put("relationId", s.getId().toString());
            advService.listIngAdv(params);
        });
    }

    /**
     * 查询所有一级分类
     *
     * @return
     */

    @Override
    public List<StoreGoodsClassListDTO> firstClass(@RequestParam("storeId") Long storeId,
                                                   @RequestParam(value = "type", required = false) Integer type) {
        List<StoreGoodsClassListDTO> storeGoodsClassListDTO = baseDao.firstClass(storeId);
        if (type != null && type == 1) {
            List<StoreGoodsClassListDTO> build = TreeUtils.build(storeGoodsClassListDTO, 0L);
            List<StoreGoodsClassListDTO> collect = build.stream().filter(a -> CollectionUtils.isNotEmpty(a.getChildren())).collect(Collectors.toList());
            return collect;
        }
        return storeGoodsClassListDTO;
    }

    /**
     * 功能描述 从excel中导入店铺的商品分类
     *
     * @param file     excel
     * @param storeIds 店铺的id
     * @return void
     * @author lishuo
     * @date 2020/6/17
     */
    @Override
    public void importFromExcel(@RequestPart("file") MultipartFile file, @RequestParam("storeId") Long storeIds) {
        try {
            String suffix = StringUtils.substring(file.getOriginalFilename(), file.getOriginalFilename().lastIndexOf("."));
            if (!SUFFIX.equals(suffix)) {
                //如果不是.xls结尾的文件的话需要返回异常提示
                throw new ServiceException(GoodsStatusCode.GOODS_CLASS_IMPORT_ERROR);
            } else {
                storeId = storeIds;
                AnalysisEventListener<StoreGoodsClassTemplateExcel> listener = EasyExcelUtils.getListener(batchInsert(), 10);
                EasyExcel.read(file.getInputStream(), StoreGoodsClassTemplateExcel.class, listener).sheet().doRead();
            }
        } catch (Exception e) {
            log.error("导入店铺商品分类信息错误", e);
        }
    }

    /**
     * 功能描述 根据店铺商品分类的名称 查询id
     *
     * @param storeGcName 店铺商品分类名称
     * @param parentId    商品分类的父类Id
     * @param storeId     店铺的id
     * @return Long  查询到的商品分类的id
     * @author lishuo
     * @date 24/6/2020
     */
    @Override

    public Long findStoreGcId(String storeGcName, long parentId, Long storeId) {
        return baseDao.findStoreGoodsClassByGcName(storeGcName, parentId, storeId);

    }

    /**
     * 功能描述 根据店铺id 下面的所有的分类
     *
     * @param * @param storeId
     * @return java.util.List<com.leimingtech.modules.dto.storeclass.StoreGoodsClassDTO>
     * @author lishuo
     * @date 15/7/2020
     */

    @Override
    public List<StoreGoodsClassDTO> findAllStoreGoodsClass(Long storeId) {
        QueryWrapper<StoreGoodsClassEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("store_id", storeId).eq("show_flag", 1).eq("del_flag", 0);
        List<StoreGoodsClassEntity> storeGoodsClassEntities = baseDao.selectList(queryWrapper);

        return ConvertUtils.sourceToTarget(storeGoodsClassEntities, StoreGoodsClassDTO.class);
    }

    private Consumer<List<StoreGoodsClassTemplateExcel>> batchInsert() {
        return storeGoodsClassTemplateExcels -> this.saveFromExcel(storeGoodsClassTemplateExcels);
    }

    private void saveFromExcel(List<StoreGoodsClassTemplateExcel> storeGoodsClassTemplateExcels) {
        //循环表中的数据 保存到数据库
        for (StoreGoodsClassTemplateExcel storeGoodsClassTemplateExcel : storeGoodsClassTemplateExcels) {
            if (storeGoodsClassTemplateExcel == null) {
                continue;
            }
            Long parentId = null;
            if (StringUtils.isEmpty(storeGoodsClassTemplateExcel.getFirstStoreGoodsClassName())) {
                storeGoodsClassTemplateExcel.setFirstStoreGoodsClassName(firstStoreGoodsClassName);
            } else {
                firstStoreGoodsClassName = storeGoodsClassTemplateExcel.getFirstStoreGoodsClassName();
            }
            //判断一级分类
            firstStoreGoodsClassName = storeGoodsClassTemplateExcel.getFirstStoreGoodsClassName();
            Long id = baseDao.findStoreGoodsClassByGcName(storeGoodsClassTemplateExcel.getFirstStoreGoodsClassName(), 0L, storeId);
            if (id == null) {
                //没有一级分类
                StoreGoodsClassEntity firstStoreGoodsClassEntity = new StoreGoodsClassEntity();
                firstStoreGoodsClassEntity.setGcName(storeGoodsClassTemplateExcel.getFirstStoreGoodsClassName());
                firstStoreGoodsClassEntity.setGcParentId(0L);
                firstStoreGoodsClassEntity.setSort(storeGoodsClassTemplateExcel.getSort());
                firstStoreGoodsClassEntity.setShowFlag(1);
                firstStoreGoodsClassEntity.setStoreId(storeId);
                baseDao.insert(firstStoreGoodsClassEntity);
                parentId = firstStoreGoodsClassEntity.getId();
            } else {
                parentId = id;
            }
            // 判断二级分类
            if (StringUtils.isEmpty(storeGoodsClassTemplateExcel.getSecondStoreGoodsClassName())) {
                continue;
            }
            String secondStoreGoodsClassName = storeGoodsClassTemplateExcel.getSecondStoreGoodsClassName();
            Long sid = baseDao.findStoreGoodsClassByGcName(secondStoreGoodsClassName, parentId, storeId);
            if (sid == null) {
                // 没有二级分类
                StoreGoodsClassEntity secondGoodsClassEntity = new StoreGoodsClassEntity();
                secondGoodsClassEntity.setGcParentId(parentId);
                secondGoodsClassEntity.setGcName(secondStoreGoodsClassName);
                secondGoodsClassEntity.setStoreId(storeId);
                secondGoodsClassEntity.setShowFlag(1);
                secondGoodsClassEntity.setSort(storeGoodsClassTemplateExcel.getSort());
                baseDao.insert(secondGoodsClassEntity);
            }
        }
    }

    /**
     * 根据分类id查询分类信息
     *
     * @param classId 分类id
     * @return 返回分类信息
     */
    @Override

    public List<StoreGoodsClassDTO> getClassById(@RequestBody List<Long> classId) {
        QueryWrapper<StoreGoodsClassEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", classId).eq("del_flag", DelFlagEnum.NORMAL.value());
        List<StoreGoodsClassEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, StoreGoodsClassDTO.class);
    }
}
