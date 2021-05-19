/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.custom.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.ExcelCheckUtil;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.dto.setting.SettingDefaultAvatarsDTO;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.constants.RedisConstants;
import com.leimingtech.modules.constants.custom.ResultCode;
import com.leimingtech.modules.dao.custom.GoodsClassCustomDao;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.custom.*;
import com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO;
import com.leimingtech.modules.entity.custom.GoodsClassCustomEntity;
import com.leimingtech.modules.enums.adv.AdvTypeEnum;
import com.leimingtech.modules.enums.custom.ShowClassEnum;
import com.leimingtech.modules.enums.custom.ShowFlagEnum;
import com.leimingtech.modules.excel.GoodsClassCustomTemplateExcel;
import com.leimingtech.modules.listener.GoodsClassCustomListener;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
import com.leimingtech.modules.vo.goodsclasscustom.GoodsClassCustomImportVo;
import com.leimingtech.service.setting.SettingService;
import com.leimingtech.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品自定义分类表.
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-22
 */
@Service
@Slf4j
public class GoodsClassCustomServiceImpl extends CrudServiceImpl<GoodsClassCustomDao, GoodsClassCustomEntity, GoodsClassCustomDTO> implements GoodsClassCustomService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private GoodsClassCustomServiceImpl goodsClassCustomService;

    @Resource
    private GoodsClassCustomDao goodsClassCustomDao;

    @Autowired
    private GoodsClassService goodsClassService;

    @Autowired
    private AdvService advService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private SettingService settingService;
    @Autowired
    private UploadService uploadService;


    /**
     * 条件构造器
     *
     * @param params 查询条件
     * @return
     */
    @Override
    public QueryWrapper<GoodsClassCustomEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String gcParentId = (String) params.get("gcParentId");

        QueryWrapper<GoodsClassCustomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(gcParentId), "gc_parent_id", gcParentId);

        return wrapper;
    }

    /**
     * @param showType 0 H5展示分类 1 pc展示分类
     * @Author: weixianchun
     * @Description: 首页楼层查询分类(返回树形结构)
     * @Return: java.util.List<com.leimingtech.modules.dto.custom.GoodsClassCustomTreeDTO>
     * @Date: 2019/8/15 10:08
     * @Version 1.0
     */

    @Override
    public List<GoodsClassCustomTreeDTO> sellectCustomTree(@RequestParam(value = "showType", required = false) Integer showType) {

        List<GoodsClassCustomTreeDTO> treeDTOList = baseDao.sellectCustomTree(showType);
        List<GoodsClassCustomTreeDTO> build = TreeUtils.build(treeDTOList, 0L);
        Iterator<GoodsClassCustomTreeDTO> iterator = build.iterator();
        while (iterator.hasNext()) {
            GoodsClassCustomTreeDTO goodsClassCustomTreeDTO = iterator.next();
            if (CollectionUtils.isEmpty(goodsClassCustomTreeDTO.getChildren())) {
                iterator.remove();
            } else {
                List<GoodsClassCustomTreeDTO> children = goodsClassCustomTreeDTO.getChildren();
                children.removeIf(goodsClassCustomLevel2 -> CollectionUtils.isEmpty(goodsClassCustomLevel2.getChildren()));
            }
        }
        return build;
    }


    /**
     * 查询所有分类(分层级关系)
     *
     * @param showType 0 H5展示分类 1 pc展示分类
     * @author xuzhch
     */

    @Override
    public List<GoodsClassCustomListDTO> selectAllCustom(Integer showType) {

        //查询所有分类
        List<GoodsClassCustomListDTO> goodsClassCustomListResultDTO = baseDao.selectAllCustom(null, showType);
        //获取后台分类ID
        List<Long> classLists = new ArrayList<>();
        for (GoodsClassCustomListDTO goodsClassCustomListDTO : goodsClassCustomListResultDTO) {
            if (ObjectUtil.isNotEmpty(goodsClassCustomListDTO)) {
                Long classId = goodsClassCustomListDTO.getClassId();
                classLists.add(classId);
            }
        }
        //根据后台分类ID查询商品数量
        List<Map<String, Object>> classCount = goodsService.selectCountByClassIds(classLists);
        //查询后台分类名称
        List<Map<String, Object>> result = goodsClassService.selectNameByCustomIds(classLists);
        //设置三级分类商品数量和后台分类名称
        for (GoodsClassCustomListDTO goodsClassCustomListDTO : goodsClassCustomListResultDTO) {
            for (Map<String, Object> mapCount : classCount) {
                String gcId = mapCount.get("gcId").toString();
                String num = mapCount.get("num").toString();
                if (goodsClassCustomListDTO.getClassId().compareTo(Long.valueOf(gcId)) == 0) {
                    goodsClassCustomListDTO.setGoodsNum(Integer.valueOf(num) == null ? 0 : Integer.valueOf(num));
                }

            }
            for (Map<String, Object> map : result) {
                String id = map.get("id").toString();
                String gcName = (String) map.get("gcName");
                if (goodsClassCustomListDTO.getClassId().compareTo(Long.parseLong(id)) == 0) {
                    goodsClassCustomListDTO.setClassName(gcName);
                }
            }
        }
        //分层级结构
        List<GoodsClassCustomListDTO> customListDTOList = TreeUtils.build(goodsClassCustomListResultDTO, 0L);
        //遍历一级分类
        for (GoodsClassCustomListDTO customListDTO : customListDTOList) {
            List<GoodsClassCustomListDTO> childrens2 = customListDTO.getChildren();
            //如果一级分类不为空则取二级分类
            int oneCount = 0;
            if (CollectionUtils.isNotEmpty(childrens2)) {
                //遍历二级分类
                for (GoodsClassCustomListDTO children2 : childrens2) {
                    List<GoodsClassCustomListDTO> childrens3 = children2.getChildren();
                    if (CollectionUtils.isNotEmpty(childrens3)) {
                        int twoCount = 0;
                        for (GoodsClassCustomListDTO children3 : childrens3) {
                            if (children3.getGoodsNum() != null) {
                                twoCount = twoCount + children3.getGoodsNum();
                            }
                        }
                        children2.setGoodsNum(twoCount);
                        oneCount = oneCount + children2.getGoodsNum();
                    }
                }
            }
            customListDTO.setGoodsNum(oneCount);
        }
        return customListDTOList;
    }

    /**
     * 查询所有(h5)并缓存
     *
     * @return
     */

    @Override
    public List<GoodsClassCustomListDTO> selectAllCustomH5(Integer showType) {
        List<GoodsClassCustomListDTO> goodsClassCustomListDTOList = new ArrayList<>();

        Object allCustomCache = null;
        if (showType.equals(ShowClassEnum.SHOW_CLASS_TYPE_H5.value())) {
            allCustomCache = redisUtils.get(RedisKeys.getH5CustomClass());
        } else {
            allCustomCache = redisUtils.get(RedisKeys.getPcCustomClass());
        }
        if (null != allCustomCache) {
            goodsClassCustomListDTOList = (List<GoodsClassCustomListDTO>) allCustomCache;
            for (GoodsClassCustomListDTO goodsClassCustomListDTO : goodsClassCustomListDTOList) {
                Map<String, Object> params = new HashMap<>(3);
                params.put("advType", AdvTypeEnum.CLASS.value() + "");
                params.put("relationId", goodsClassCustomListDTO.getId().toString());
                List<AdvDTO> advDTOList = advService.listIngAdv(params);
                if (showType.equals(ShowClassEnum.SHOW_CLASS_TYPE_PC.value()) && CollectionUtils.isNotEmpty(advDTOList)) {
                    goodsClassCustomListDTO.setAdvDTO(advDTOList.get(0));
                }
                goodsClassCustomListDTO.setAdvDTOList(advDTOList);
            }
            return goodsClassCustomListDTOList;
        }

        goodsClassCustomListDTOList = selectAllH5Custom(showType);
        if (showType.equals(ShowClassEnum.SHOW_CLASS_TYPE_H5.value())) {
            redisUtils.set(RedisKeys.getH5CustomClass(), goodsClassCustomListDTOList, RedisConstants.JEDIS_EXPIRE);
        } else {
            redisUtils.set(RedisKeys.getPcCustomClass(), goodsClassCustomListDTOList, RedisConstants.JEDIS_EXPIRE);

        }

        return goodsClassCustomListDTOList;
    }

    /**
     * 查询所有(h5)
     *
     * @return
     */
    private List<GoodsClassCustomListDTO> selectAllH5Custom(Integer showType) {
        //查询所有分类
        List<GoodsClassCustomListDTO> goodsClassCustomListResultDTO = baseDao.selectAllCustom(ShowFlagEnum.YES.value(), showType);
        List<GoodsClassCustomListDTO> result = TreeUtils.build((List) goodsClassCustomListResultDTO, 0L);
        if (CollectionUtils.isEmpty(result)) {
            return result;
        }
        Iterator<GoodsClassCustomListDTO> iterator = result.iterator();
        while (iterator.hasNext()) {
            GoodsClassCustomListDTO goodsClassCustomListDTO = iterator.next();
            if (!CollectionUtils.isEmpty(goodsClassCustomListDTO.getChildren())) {
                Iterator<GoodsClassCustomListDTO> level2 = goodsClassCustomListDTO.getChildren().iterator();
                while (level2.hasNext()) {
                    GoodsClassCustomListDTO goodsClassCustomLevel2 = level2.next();
                    if (CollectionUtils.isEmpty(goodsClassCustomLevel2.getChildren())) {
                        level2.remove();
                        continue;
                    }
                }
            }
            if (CollectionUtils.isEmpty(goodsClassCustomListDTO.getChildren())) {
                iterator.remove();
                continue;
            }
            Map<String, Object> params = new HashMap<>(3);
            params.put("advType", AdvTypeEnum.CLASS.value() + "");
            params.put("relationId", goodsClassCustomListDTO.getId().toString());
            List<AdvDTO> advDTOList = advService.listIngAdv(params);
            if (showType.equals(ShowClassEnum.SHOW_CLASS_TYPE_PC.value()) && CollectionUtils.isNotEmpty(advDTOList)) {
                goodsClassCustomListDTO.setAdvDTO(advDTOList.get(0));
            }
            goodsClassCustomListDTO.setAdvDTOList(advDTOList);
        }
        return result;
    }

    /**
     * 根据父ID查询子级分类
     *
     * @param id
     * @param showFlag 展示状态
     * @return
     * @author xuzhch
     */

    @Override
    public List<GoodsClassCustomDTO> selectListByParentId(Long id,
                                                          @RequestParam(value = "showFlag", required = false) Integer showFlag,
                                                          @RequestParam(value = "showType", required = false) Integer showType) {

        // 构造查询条件
        QueryWrapper<GoodsClassCustomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(true, "gc_parent_id", id);
        wrapper.eq(showType != null, "show_type", showType);
        wrapper.eq(showFlag != null, "show_flag", showFlag);
        wrapper.orderByAsc("sort");
        wrapper.orderByDesc("create_date");

        List<GoodsClassCustomEntity> goodsClassCustomEntities = goodsClassCustomDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(goodsClassCustomEntities, GoodsClassCustomDTO.class);
    }

    /**
     * 根据ID查询展示类目详情
     *
     * @param id 主键id
     * @return 返回展示分类详情
     */

    @Override
    public GoodsClassCustomDetailDTO selectDetailById(Long id) {
        GoodsClassCustomDetailDTO detailDTO = goodsClassCustomDao.selectDetailById(id);
        Long goodsClassId = detailDTO.getClassId();
        String gcIdPath = goodsClassService.queryIdPathById(goodsClassId);
        detailDTO.setGcIdPath(gcIdPath);
        return detailDTO;
    }

    /**
     * 重载保存商品类目
     *
     * @param goodsClassCustomSaveDTO
     * @author xuzhch
     */

    @Override
    public void save(@RequestBody GoodsClassCustomSaveDTO goodsClassCustomSaveDTO) {
        GoodsClassCustomEntity goodsClassCustomEntity = ConvertUtils.sourceToTarget(goodsClassCustomSaveDTO, GoodsClassCustomEntity.class);
        //保存类目时设置为启用
        goodsClassCustomEntity.setShowFlag(ShowFlagEnum.YES.value());
        goodsClassCustomDao.insert(goodsClassCustomEntity);
        //给idPath字段赋值
        setIdPath(goodsClassCustomEntity);
        operationRedis();
    }

    /**
     * 清空缓存并重新缓存
     */
    private void operationRedis() {
        //由于PC展示分类与H5展示分类使用的为一个 所以取消缓存预热 只做清空操作
        redisUtils.delete(RedisKeys.getH5CustomClass());
        redisUtils.delete(RedisKeys.getPcCustomClass());
    }

    /**
     * 重载修改商品类目
     *
     * @param goodsClassCustomDTO
     * @author xuzhch
     */

    @Override
    public void update(@RequestBody GoodsClassCustomUpdateDTO goodsClassCustomDTO) {

        GoodsClassCustomEntity goodsClassCustomEntity = ConvertUtils.sourceToTarget(goodsClassCustomDTO,
                GoodsClassCustomEntity.class);
        //给idPath字段赋值 并返回结果
        setIdPath(goodsClassCustomEntity);
        operationRedis();
    }

    /**
     * 删除类目管理及其子级分类
     *
     * @param ids
     * @return
     */

    @Override
    public void deleteCorrelationIds(@RequestBody Long[] ids) {

        for (Long id : ids) {
            //删除类目管理子级分类
            goodsClassCustomDao.delete(new QueryWrapper<GoodsClassCustomEntity>().eq(true, "gc_parent_id", id));
        }
        //删除类目管理
        goodsClassCustomDao.deleteBatchIds(Arrays.asList(ids));
        operationRedis();
    }

    /**
     * 根据父ID 获取父级名称
     *
     * @param id 展示分类父id
     * @return 返回分类名称
     */

    @Override
    public String selectNameById(Long id) {
        GoodsClassCustomEntity goodsClassCustomEntity = goodsClassCustomDao.selectById(id);

        if (null != goodsClassCustomEntity && StringUtils.isNotBlank(goodsClassCustomEntity.getGcName())) {
            return goodsClassCustomEntity.getGcName();
        }
        return null;
    }

    /**
     * 根据id删除展示类目
     *
     * @param id 主键id
     * @return 返回删除成功数量
     */

    @Override
    public Integer deleteCustomById(Long id) {
        List<GoodsClassCustomEntity> goodsClassCustomEntities = goodsClassCustomDao.selectList(
                new QueryWrapper<GoodsClassCustomEntity>().eq(true, "gc_parent_id", id));
        if (CollectionUtils.isNotEmpty(goodsClassCustomEntities)) {
            return ResultCode.ERR_PARAMS;
        }
        goodsClassCustomDao.deleteById(id);
        operationRedis();
        return ResultCode.SUCCESS;
    }

    /**
     * 给IDPath字段赋值
     *
     * @param goodsClassCustomEntity
     * @author: xuzhch
     */
    private Integer setIdPath(GoodsClassCustomEntity goodsClassCustomEntity) {
        Long entityId = goodsClassCustomEntity.getId();
        Long gcParentId = goodsClassCustomEntity.getGcParentId();
        if (null == gcParentId || gcParentId == 0) {
            goodsClassCustomEntity.setIdPath(entityId + "");
        } else {
            GoodsClassCustomEntity goodsParentEntity = goodsClassCustomDao.selectById(gcParentId);
            goodsClassCustomEntity.setIdPath(goodsParentEntity.getIdPath() + "," + entityId);
        }
        return goodsClassCustomDao.updateById(goodsClassCustomEntity);
    }

    /**
     * 分页
     *
     * @param params
     * @return
     */

    @Override
    public PageData<GoodsClassCustomDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 导出
     *
     * @param params
     * @return
     */

    @Override
    public List<GoodsClassCustomDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 启用/禁用展示类目
     *
     * @param params
     */

    @Override
    public void updateShowFlag(@RequestParam Map<String, Object> params) {
        baseDao.updateShowFlag(params);
        operationRedis();
    }

    /**
     * 查询分类名称是否重复
     *
     * @param gcParentId 分类父iD
     * @param gcName     分类名称
     * @return
     */
    @Override

    public Integer findClassName(@RequestParam("gcParentId") Long gcParentId,
                                 @RequestParam("gcName") String gcName,
                                 @RequestParam(value = "showType", required = false) Integer showType) {
        return baseDao.findClassName(gcParentId, gcName, showType);
    }

    /**
     * 更新时验证分类名称是否重复
     *
     * @param gcParentId 分类父iD
     * @param gcName     分类名称
     * @param id         分类id
     * @return
     */

    @Override
    public Integer updateVerifyClassName(@RequestParam("gcParentId") Long gcParentId,
                                         @RequestParam("gcName") String gcName,
                                         @RequestParam("id") Long id,
                                         @RequestParam(value = "showType", required = false) Integer showType) {
        return baseDao.updateVerifyClassName(gcParentId, gcName, id, showType);
    }

    /**
     * 校验子级名称是否和父级名称一样
     *
     * @param parentId 父级ID
     * @param gcName   子名称
     * @return
     */

    @Override
    public Integer findParentName(@RequestParam("parentId") Long parentId, @RequestParam("gcName") String gcName) {
        return baseDao.findParentName(parentId, gcName);
    }


    /**
     * 根据分类id查询数量
     *
     * @param gcId
     * @return
     */

    @Override
    public Integer findCustomByGcId(Long gcId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.eq("class_id", gcId);
        return baseDao.selectCount(wrapper);
    }

    /**
     * 功能描述 从excel中导入展示分类
     *
     * @param files 要导入的excel文件
     * @return void
     * @author lishuo
     * @date 2020/6/16
     */
    @Override
    public GoodsClassCustomImportVo importGoodsClassCustom(@RequestPart("files") MultipartFile files) {
        // 查询出所有的后台关联分类
        List<GoodsClassListDTO> allGoodsClass = goodsClassService.findAllGoodsClass();
        List<GoodsClassListDTO> firstGoodsClassList = new ArrayList<>();
        List<GoodsClassListDTO> secondGoodsClassList = new ArrayList<>();
        List<GoodsClassListDTO> thirdGoodsClassList = new ArrayList<>();

        // 处理 分出分类级别
        for (GoodsClassListDTO goodsClass : allGoodsClass) {
            if (StringUtils.split(goodsClass.getGcIdpath(), ",").length == 1) {
                firstGoodsClassList.add(goodsClass);
            } else if (StringUtils.split(goodsClass.getGcIdpath(), ",").length == 2) {
                secondGoodsClassList.add(goodsClass);
            } else if (StringUtils.split(goodsClass.getGcIdpath(), ",").length == 3) {
                thirdGoodsClassList.add(goodsClass);
            }
        }
        Map<String, List<GoodsClassListDTO>> firstGoodsClassMap = firstGoodsClassList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcName));
        Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap = secondGoodsClassList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcParentId));
        Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap = thirdGoodsClassList.stream().collect(Collectors.groupingBy(GoodsClassListDTO::getGcParentId));
        GoodsClassCustomListener goodsClassCustomListener = new GoodsClassCustomListener(firstGoodsClassMap, secondGoodsClassMap, thirdGoodsClassMap);

        try {
            if (ExcelCheckUtil.isExcel(files.getInputStream())) {

                EasyExcel.read(files.getInputStream(), GoodsClassCustomTemplateExcel.class, goodsClassCustomListener).sheet().doRead();
                List<String> errorMessage = goodsClassCustomListener.getErrorMessage();
                GoodsClassCustomImportVo goodsClassCustomImportVo = new GoodsClassCustomImportVo();
                goodsClassCustomImportVo.setErrorMessage(errorMessage);
                goodsClassCustomImportVo.setTotalNum(goodsClassCustomListener.getList().size());
                if (CollectionUtils.isNotEmpty(errorMessage)) {
                    return goodsClassCustomImportVo;
                } else {
                    saveFromExcel(goodsClassCustomListener.getList(), firstGoodsClassMap, secondGoodsClassMap, thirdGoodsClassMap);
                    return goodsClassCustomImportVo;
                }
            } else {
                //如果不是.xls结尾的文件的话需要返回异常提示
                throw new ServiceException(GoodsStatusCode.GOODS_CLASS_IMPORT_ERROR);
            }
        } catch (Exception e) {
            logger.error("IO异常" + e);
        }
        return null;
    }

    /**
     * 查询当前分类下是否有子分类
     *
     * @param id 分类id
     * @return 子分类集合
     */
    @Override
    public List<GoodsClassCustomDTO> findGcSon(Long id) {
        QueryWrapper<GoodsClassCustomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("gc_parent_id", id);
        List<GoodsClassCustomEntity> list = goodsClassCustomDao.selectList(wrapper);
        List<GoodsClassCustomDTO> dtoList = ConvertUtils.sourceToTarget(list, GoodsClassCustomDTO.class);
        return dtoList;
    }

//    private Consumer<List<GoodsClassCustomTemplateExcel>> batchInsert() {
//        return goodsClassCustomTemplateExcels -> this.saveFromExcel(goodsClassCustomTemplateExcels, firstGoodsClassMap, secondGoodsClassMap, thirdGoodsClassMap);
//    }

    /**
     * 功能描述 保存分类信息
     *
     * @param *                   @param goodsClassCustomTemplateExcels 从excel中读取的list
     * @param firstGoodsClassMap
     * @param secondGoodsClassMap
     * @param thirdGoodsClassMap
     * @return void
     * @author lishuo
     * @date 19/6/2020
     */
    public void saveFromExcel(List<GoodsClassCustomTemplateExcel> goodsClassCustomTemplates, Map<String, List<GoodsClassListDTO>> firstGoodsClassMap, Map<Long, List<GoodsClassListDTO>> secondGoodsClassMap, Map<Long, List<GoodsClassListDTO>> thirdGoodsClassMap) {
        /**
         *  字段名称  excel导入时使用
         */
        String firstGcName = null;
        String secondGcName = null;
        String firstShowGcName = null;
        String secondShowGcName = null;
        SettingDefaultAvatarsDTO defaultAvatarsSet = settingService.getDefaultAvatarsSet();
        String picUrl = defaultAvatarsSet.getAvatar();
        // 循环list 判断保存数据
        for (GoodsClassCustomTemplateExcel goodsClassCustomTemplateExcel : goodsClassCustomTemplates) {
            Long gcId = null;
            if (goodsClassCustomTemplateExcel == null) {
                continue;
            }
            // 图片的判断
            if (goodsClassCustomTemplateExcel.getPicUrl() != null && !goodsClassCustomTemplateExcel.getPicUrl().startsWith("/group1")) {
                String s = uploadService.downloadToUrl(goodsClassCustomTemplateExcel.getPicUrl());
                goodsClassCustomTemplateExcel.setPicUrl(s);
            }
            StringBuilder idPath = new StringBuilder();
            Long parentId = null;
            // 查找后台关联分类的id
            if (StringUtils.isEmpty(goodsClassCustomTemplateExcel.getFirstGcName())) {
                goodsClassCustomTemplateExcel.setFirstGcName(firstGcName);
            } else {
                firstGcName = goodsClassCustomTemplateExcel.getFirstGcName();
            }
            if (StringUtils.isNotEmpty(firstGcName)) {
                List<GoodsClassListDTO> goodsClassListDTOList = firstGoodsClassMap.get(firstGcName);
                gcId = goodsClassListDTOList.get(0).getId();

            }
            if (StringUtils.isEmpty(goodsClassCustomTemplateExcel.getSecondGcName())) {
                goodsClassCustomTemplateExcel.setSecondGcName(secondGcName);
            } else {
                secondGcName = goodsClassCustomTemplateExcel.getSecondGcName();
            }
            if (StringUtils.isNotEmpty(secondGcName)) {
                List<GoodsClassListDTO> goodsClassListDTOList = secondGoodsClassMap.get(gcId);
                for (GoodsClassListDTO goodsClassListDTO : goodsClassListDTOList) {
                    if (goodsClassListDTO.getGcName().equals(secondGcName)) {
                        gcId = goodsClassListDTO.getId();

                    }
                }
            }
            String thirdGcName = goodsClassCustomTemplateExcel.getThirdGcName();
            if (StringUtils.isNotEmpty(thirdGcName)) {
                List<GoodsClassListDTO> goodsClassListDTOList = thirdGoodsClassMap.get(gcId);
                for (GoodsClassListDTO goodsClassListDTO : goodsClassListDTOList) {
                    if (goodsClassListDTO.getGcName().equals(thirdGcName)) {
                        gcId = goodsClassListDTO.getId();
                    }
                }
            }
            // 判断一级分类有没有
            if (StringUtils.isEmpty(goodsClassCustomTemplateExcel.getFirstShowGcName())) {
                goodsClassCustomTemplateExcel.setFirstShowGcName(firstShowGcName);
            } else {
                firstShowGcName = goodsClassCustomTemplateExcel.getFirstShowGcName();
            }
            GoodsClassCustomEntity classCustomEntity = baseDao.findByClassName(0L, goodsClassCustomTemplateExcel.getFirstShowGcName(), 0);
            // 一级分类没有
            if (classCustomEntity == null) {
                long id = IdWorker.getId();
                GoodsClassCustomEntity firstGoodsClassCustom = new GoodsClassCustomEntity();
                firstGoodsClassCustom.setId(id);
                firstGoodsClassCustom.setDelFlag(goodsClassCustomTemplateExcel.getShowFlag());
                firstGoodsClassCustom.setGcParentId(0L);
                firstGoodsClassCustom.setGcName(firstShowGcName);
                firstGoodsClassCustom.setSort(goodsClassCustomTemplateExcel.getSort());
                if (goodsClassCustomTemplateExcel.getPicUrl() == null) {
                    firstGoodsClassCustom.setGcPic(picUrl);
                } else {
                    firstGoodsClassCustom.setGcPic(goodsClassCustomTemplateExcel.getPicUrl());
                }
                firstGoodsClassCustom.setClassId(gcId);
                firstGoodsClassCustom.setShowType(goodsClassCustomTemplateExcel.getShowType());
                firstGoodsClassCustom.setIdPath(idPath.append(id).toString());
                baseDao.insert(firstGoodsClassCustom);
                parentId = id;
            } else {
                // 一级分类有
                Long id = classCustomEntity.getId();
                idPath.append(id);
                parentId = id;
            }
            // 判断二级分类
            if (StringUtils.isEmpty(goodsClassCustomTemplateExcel.getSecondShowGcName())) {
                goodsClassCustomTemplateExcel.setSecondShowGcName(secondShowGcName);
            } else {
                secondShowGcName = goodsClassCustomTemplateExcel.getSecondShowGcName();
            }
            if (StringUtils.isEmpty(secondShowGcName)) {
                continue;
            }
            GoodsClassCustomEntity goodsClassCustomEntity = baseDao.findByClassName(parentId, goodsClassCustomTemplateExcel.getSecondShowGcName(), 0);
            if (goodsClassCustomEntity == null) {
                // 二级分类没有
                Long id = IdWorker.getId();
                GoodsClassCustomEntity secondGoodsCustom = new GoodsClassCustomEntity();
                secondGoodsCustom.setId(id);
                secondGoodsCustom.setDelFlag(goodsClassCustomTemplateExcel.getShowFlag());
                secondGoodsCustom.setGcParentId(parentId);
                secondGoodsCustom.setGcName(secondShowGcName);
                secondGoodsCustom.setSort(goodsClassCustomTemplateExcel.getSort());
                if (goodsClassCustomTemplateExcel.getPicUrl() == null) {
                    secondGoodsCustom.setGcPic(picUrl);
                } else {
                    secondGoodsCustom.setGcPic(goodsClassCustomTemplateExcel.getPicUrl());
                }
                secondGoodsCustom.setClassId(gcId);
                secondGoodsCustom.setShowType(goodsClassCustomTemplateExcel.getShowType());
                secondGoodsCustom.setIdPath(idPath.append(",").append(id).toString());
                baseDao.insert(secondGoodsCustom);
                // 更新id path
                parentId = id;
            } else {
                // 二级分类有
                Long id = goodsClassCustomEntity.getId();
                idPath.append(id);
                parentId = id;
            }
            // 三级分类相关
            String thirdShowGcName = goodsClassCustomTemplateExcel.getThirdShowGcName();
            if (StringUtils.isEmpty(thirdShowGcName)) {
                continue;
            }
            GoodsClassCustomEntity thirdClassCustom = baseDao.findByClassName(parentId, thirdShowGcName, 0);
            if (thirdClassCustom == null) {
                // 三级分类没有
                long id = IdWorker.getId();
                GoodsClassCustomEntity thirdGoodsCustom = new GoodsClassCustomEntity();
                thirdGoodsCustom.setId(id);
                thirdGoodsCustom.setDelFlag(goodsClassCustomTemplateExcel.getShowFlag());
                thirdGoodsCustom.setGcParentId(parentId);
                thirdGoodsCustom.setGcName(thirdShowGcName);
                thirdGoodsCustom.setSort(goodsClassCustomTemplateExcel.getSort());
                if (goodsClassCustomTemplateExcel.getPicUrl() == null) {
                    thirdGoodsCustom.setGcPic(picUrl);
                } else {
                    thirdGoodsCustom.setGcPic(goodsClassCustomTemplateExcel.getPicUrl());
                }
                thirdGoodsCustom.setClassId(gcId);
                thirdGoodsCustom.setShowType(goodsClassCustomTemplateExcel.getShowType());
                thirdGoodsCustom.setIdPath(idPath.append(",").append(id).toString());
                baseDao.insert(thirdGoodsCustom);
                parentId = thirdGoodsCustom.getId();
            }
        }
    }
}

