/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodsclass.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dao.goodsclass.GoodsClassDao;
import com.leimingtech.modules.dto.goodsclass.*;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.entity.goodsclass.GoodsClassEntity;
import com.leimingtech.modules.enums.goodsclass.GoodsClassErrorCodeEnum;
import com.leimingtech.modules.excel.goods.GoodsClassTemplateExcel;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goodsclass.GoodsClassAttrService;
import com.leimingtech.modules.service.goodsclass.GoodsClassBrandService;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.goodsclass.GoodsClassSpecService;
import com.leimingtech.modules.service.storeclass.StoreClassService;
import com.leimingtech.modules.statuscode.GoodsStatusCode;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 商品分类表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-05-22
 */
@Slf4j
@Service

public class GoodsClassServiceImpl extends CrudServiceImpl<GoodsClassDao, GoodsClassEntity, GoodsClassDTO> implements GoodsClassService {

    private static final String SUFFIX = ".xlsx";
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Resource
    private GoodsClassDao goodsClassDao;
    @Resource
    private GoodsClassServiceImpl goodsClassService;
    @Autowired
    private StoreClassService storeClassService;

    @Autowired
    private GoodsClassSpecService goodsClassSpecService;

    @Autowired
    private GoodsClassAttrService goodsClassAttrService;

    @Autowired
    private GoodsClassBrandService goodsClassBrandService;

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsClassCustomService goodsClassCustomService;


    @Override
    public QueryWrapper<GoodsClassEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String gcParentId = (String) params.get("gcParentId");

        QueryWrapper<GoodsClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(gcParentId), "gc_parent_id", gcParentId);

        return wrapper;
    }

    /**
     * @return com.leimingtech.modules.goodsclass.entity.GoodsClassEntity
     * @Description 通过商品分类id查询只返回商品的分类名称
     * @Param * @param goodsClassId:
     * @Author huangkeyuan
     * @Date 10:19 2019-05-23
     */
    @Override

    public String queryGoodsClassName(@RequestParam("goodsClassId") Long goodsClassId) {

        GoodsClassEntity entity = goodsClassDao.selectById(goodsClassId);

        return entity.getGcName();
    }

    /**
     * 根据classID查询IDPath
     *
     * @param goodsClassId
     * @return
     */

    @Override
    public String queryIdPathById(Long goodsClassId) {
        return goodsClassDao.queryIdPathById(goodsClassId);
    }

    /**
     * @return void
     * @Description 保存商品分类
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 12:01 2019-05-23
     */
    @Override

    public void saveGoodsClass(@RequestBody GoodsClassSaveDTO dto) {
        GoodsClassEntity goodsClassEntity = ConvertUtils.sourceToTarget(dto, GoodsClassEntity.class);
        goodsClassDao.insert(goodsClassEntity);
        Long goodsClassId = goodsClassEntity.getId();
        //封装规格Id 并保存关联品牌数据
        if (!CollectionUtils.isEmpty(dto.getGoodsClassBrandSaveDTOList())) {
            goodsClassBrandService.saveBatch(dto.getGoodsClassBrandSaveDTOList(), goodsClassId);
        }

        //构造保存对象
        GoodsClassUpdateDTO goodsClassUpdateDTO = new GoodsClassUpdateDTO();
        goodsClassUpdateDTO.setId(goodsClassId);
        goodsClassUpdateDTO.setSpecIds(dto.getSpecIds());
        goodsClassUpdateDTO.setAttrIds(dto.getAttrIds());
        //保存商品分类关联规格
        goodsClassSpecService.insertBatch(goodsClassUpdateDTO);
        //保存商品分类关联属性
        goodsClassAttrService.insertBatch(goodsClassUpdateDTO);
        setGcIdpath(goodsClassEntity);


    }

    /**
     * @return com.leimingtech.commons.tools.utils.Result
     * @Description 修改商品分类信息
     * @Param * @param dto:
     * @Author huangkeyuan
     * @Date 14:56 2019-05-23
     */
    @Override

    public void updateGoodsClass(@RequestBody GoodsClassUpdateDTO dto) {

        GoodsClassEntity goodsClassEntity = ConvertUtils.sourceToTarget(dto, GoodsClassEntity.class);
        setGcIdpath(goodsClassEntity);
        Long goodsClassId = goodsClassEntity.getId();
        //修改商品分类关联品牌
//        if (!CollectionUtils.isEmpty(dto.getGoodsClassBrandSaveDTOS())) {
        goodsClassBrandService.saveBatch(dto.getGoodsClassBrandSaveDTO(), goodsClassId);
//        }
        //修改商品分类关联规格
        goodsClassSpecService.updateGoodsClassSpec(dto);
        //修改商品分类关联属性
        goodsClassAttrService.updateGoodsClassAttr(dto);

    }

    /**
     * @return com.leimingtech.commons.tools.utils.Result
     * @Description 删除商品分类
     * @Param * @param ids:
     * @Author huangkeyuan
     * @Date 15:12 2019-05-23
     */
    @Override

    public Map<String, Object> deleteGoodsClassIds(@RequestBody Long[] ids) {
        Map<String, Object> res = new HashMap<>(10);

        for (Long id : ids) {
            //查询此分类下是否存在子分类
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("gc_parent_id", id);
            wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
            Integer childNum = baseDao.selectCount(wrapper);
            GoodsClassEntity goodsClassEntity = baseDao.selectById(id);
            if (childNum != null && childNum > 0) {
                res.put("code", GoodsClassErrorCodeEnum.DELETE_GOODCLASS_FAIL.value());
                res.put("msg", "存在关联商品分类: " + goodsClassEntity.getGcName() + ",不可删除");
                return res;
            }
            //关联名牌
            Integer brandNum = goodsClassBrandService.findBrandByGcId(id);
            if (brandNum != null && brandNum > 0) {
                res.put("code", GoodsClassErrorCodeEnum.DELETE_GOODCLASS_FAIL.value());
                res.put("msg", "存在关联品牌: " + goodsClassEntity.getGcName() + ",不可删除");
                return res;
            }
            //关联规格
            Integer specNum = goodsClassSpecService.findSpecByGcId(id);
            if (specNum != null && specNum > 0) {
                res.put("code", GoodsClassErrorCodeEnum.DELETE_GOODCLASS_FAIL.value());
                res.put("msg", "存在关联规格: " + goodsClassEntity.getGcName() + ",不可删除");
                return res;
            }
            //关联属性
            Integer attrNum = goodsClassAttrService.findAttrByGcId(id);
            if (attrNum != null && attrNum > 0) {
                res.put("code", GoodsClassErrorCodeEnum.DELETE_GOODCLASS_FAIL.value());
                res.put("msg", "存在关联属性: " + goodsClassEntity.getGcName() + ",不可删除");
                return res;
            }
            //关联展示分类
            Integer customNum = goodsClassCustomService.findCustomByGcId(id);
            if (customNum != null && customNum > 0) {
                res.put("code", GoodsClassErrorCodeEnum.DELETE_GOODCLASS_FAIL.value());
                res.put("msg", "存在关联展示分类: " + goodsClassEntity.getGcName() + ",不可删除");
                return res;
            }
        }
        //关联商品
        //根据分类id查询分类名称
        List<String> list = goodsService.findCountByClassId(ids);
        //为空删除
        if (CollectionUtils.isEmpty(list)) {
            //批量删除分类
            goodsClassDao.deleteBatchIds(Arrays.asList(ids));
            // 删除当前分类关联的店铺分类
            storeClassService.deleteByClassId(ids);
        } else {
            res.put("code", GoodsClassErrorCodeEnum.GOODS_CLASS_RELATION_GOODS.value());
            res.put("msg", "存在关联商品分类:" + list.get(0) + "不可做删除操作");
        }
        return res;
    }


    /**
     * 功能描述 从excel中导入商品的分类
     *
     * @param files excel文件
     * @return void
     * @author lishuo
     * @date 2020/6/16
     */
    @Override
    public void importGoodsClass(@RequestPart("files") MultipartFile files) {
        AnalysisEventListener<GoodsClassTemplateExcel> listener = EasyExcelUtils.getListener(this.batchInsert(), 10);
        try {
            String suffix = StringUtils.substring(files.getOriginalFilename(), files.getOriginalFilename().lastIndexOf("."));
            if (!SUFFIX.equals(suffix)) {
                //如果不是.xlsx 结尾的文件的话需要返回异常提示
                throw new ServiceException(GoodsStatusCode.GOODS_CLASS_IMPORT_ERROR);
            } else {
                EasyExcel.read(files.getInputStream(), GoodsClassTemplateExcel.class, listener).sheet().doRead();
            }
        } catch (Exception e) {
            logger.error("Excel导入异常" + e);
        }
    }

    private Consumer<List<GoodsClassTemplateExcel>> batchInsert() {
        return users -> this.saveGoodsClassFromExcel(users);
    }

    /**
     * @return void
     * @Description 根据gcId的值设置gc_id_path的值
     * @Param * @param goodsClassEntity:
     * @Author huangkeyuan
     * @Date 14:38 2019-05-23
     */
    private void setGcIdpath(GoodsClassEntity goodsClassEntity) {
        Long entityId = goodsClassEntity.getId();
        Long gcParentId = goodsClassEntity.getGcParentId();
        if (null == gcParentId || gcParentId == 0) {
            goodsClassEntity.setGcIdpath(Long.toString(entityId));

        } else {
            GoodsClassEntity goodsParentEntity = goodsClassDao.selectById(gcParentId);
            goodsClassEntity.setGcIdpath(goodsParentEntity.getGcIdpath() + "," + entityId);

        }
        goodsClassDao.updateById(goodsClassEntity);
    }


    /**
     * 查询所有分类(分层级关系)
     *
     * @param
     * @author huangkeyuan
     */
    @Override

    public List<GoodsClassListDTO> selectAllGoodsClass(@RequestParam(value = "type", required = false) Integer type) {
        //查询所有分类
        List<GoodsClassListDTO> goodsClassListResultDTO = goodsClassDao.selectAllGoodClass();
        for (GoodsClassListDTO classListDTO : goodsClassListResultDTO) {
            classListDTO.setLowerLevel(true);
            String gcIdpath = classListDTO.getGcIdpath();
            String[] idSize = gcIdpath.split(",");
            if (idSize.length == 3) {
                classListDTO.setLowerLevel(false);
            }
        }
        List<GoodsClassListDTO> classListDTOList = TreeUtils.build(goodsClassListResultDTO, 0L);
        if (CollectionUtils.isEmpty(classListDTOList)) {
            return classListDTOList;
        }
        if (type != null && type == GoodsClassErrorCodeEnum.GOODS_CLASS_THREE.value()) {
            Iterator<GoodsClassListDTO> iterator = classListDTOList.iterator();
            while (iterator.hasNext()) {
                GoodsClassListDTO goodsClassListDTO = iterator.next();
                if (!CollectionUtils.isEmpty(goodsClassListDTO.getChildren())) {
                    Iterator<GoodsClassListDTO> level2 = goodsClassListDTO.getChildren().iterator();
                    while (level2.hasNext()) {
                        GoodsClassListDTO goodsClassLevel = level2.next();
                        if (CollectionUtils.isEmpty(goodsClassLevel.getChildren())) {
                            level2.remove();
                            continue;
                        }
                    }
                }
                if (CollectionUtils.isEmpty(goodsClassListDTO.getChildren())) {
                    iterator.remove();
                    continue;
                }
            }
        }
        return classListDTOList;
    }

    /**
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goodsclass.GoodsClassDTO>
     * @Description 查询分页信息
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 16:55 2019-05-28
     */

    @Override
    public PageData<GoodsClassDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @return GoodsClassDetailDTO
     * @Description 根据分类id查询分类关联的规格、属性、品牌信息
     * @Param * @param id:
     * @Author huangkeyuan
     * @Date 17:14 2019-05-28
     */

    @Override
    public GoodsClassDetailDTO getGoodsClassById(Long id) {
        return baseDao.selectDetailByGcId(id);
    }

    /**
     * @return java.util.List<com.leimingtech.modules.dto.goodsclass.GoodsClassDTO>
     * @Description 查询列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 17:17 2019-05-28
     */

    @Override
    public List<GoodsClassDTO> list(@RequestParam Map<String, Object> params) {

        // 构造查询条件
        QueryWrapper<GoodsClassEntity> wrapper = getWrapper(params);
        wrapper.orderByAsc("gc_sort");
        wrapper.orderByAsc("create_date");

        List<GoodsClassEntity> entityList = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(entityList, currentDtoClass());

    }

    /**
     * 根据父ID查询子级分类
     *
     * @param id
     * @return
     * @author xuzhch
     */

    @Override
    public List<GoodsClassDTO> selectListByParentId(Long id,
                                                    @RequestParam(value = "storeId", required = false) Long storeId) {

        if (id == 0 && storeId != null) {
            List<StoreClassDTO> byStoreId = storeClassService.findByStoreId(storeId);
            List<Long> ids = new ArrayList<>();
            for (StoreClassDTO storeClassDTO : byStoreId) {
                ids.add(storeClassDTO.getClassId());
            }
            return null;
        }
        if (id == 0) {
            List<GoodsClassDTO> goodsClassDTOList = goodsClassDao.selectListByParentId(id);
            List<Long> classIds = goodsClassDTOList.stream().map(GoodsClassDTO::getId).collect(Collectors.toList());
            List<GoodsClassEntity> goodsClassEntities = goodsClassDao.selectList(new QueryWrapper<GoodsClassEntity>().in("gc_parent_id", classIds).eq("del_flag", 0));
            Set<Long> idList = goodsClassEntities.stream().map(GoodsClassEntity::getGcParentId).collect(Collectors.toSet());
            Iterator<GoodsClassDTO> iterator = goodsClassDTOList.iterator();
            while (iterator.hasNext()) {
                GoodsClassDTO goodsClassDTO = iterator.next();
                if (!idList.contains(goodsClassDTO.getId())) {
                    iterator.remove();
                }
            }
            return goodsClassDTOList;
        }
        return goodsClassDao.selectListByParentId(id);

    }


    /**
     * 根据Id查询分类数据(店铺关联的分类)
     *
     * @param classIds
     * @return
     * @author xuzhch
     */

    @Override
    public List<GoodsClassQueryNameDTO> selectListByClassId(@RequestBody List<Long> classIds) {
        if (CollectionUtils.isEmpty(classIds)) {
            return Collections.emptyList();
        }
        return baseDao.selectListByClassIds(classIds);
    }


    /**
     * 根据ID查询订单商品信息
     *
     * @param id: 商品分类ID
     * @return 商品分类DTO
     * @date 2019/6/23 16:37
     * @author LX lixiangx@leimingtech.com
     **/
    @Override

    public GoodsClassDTO findById(Long id) {
        return super.get(id);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询分类名称数量(修改时)
     * @Date :2019/7/3 19:58
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public int gcNameConutCheck(@RequestBody GoodsClassUpdateDTO dto) {
        return baseDao.gcNameConutCheck(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询分类名称数量(保存时)
     * @Date :2019/7/3 20:40
     * @Param gcName 商品分类名称
     * @Version V1.0
     **/

    @Override
    public Integer findByGcName(@RequestParam("gcName") String gcName, @RequestParam("gcParentId") Long gcParentId) {
        return baseDao.findByGcName(gcName, gcParentId);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据分类id查询品牌信息, 并模糊查询品牌名称
     * @Date :2019/7/4 11:55
     * @Param gcClassId 分类id
     * @Param brandName 品牌名称
     * @Version V1.0
     **/

    @Override
    public List<GoodsClassBrandDTO> brandByGcId(@RequestParam("gcClassId") Long gcClassId, @RequestParam(value = "brandName", required = false) String brandName) {
        GoodsClassEntity goodsClassEntity = baseDao.selectById(gcClassId);
        String idpath = goodsClassEntity.getGcIdpath();
        String[] ids = idpath.split(",");
        return baseDao.brandByGcId(ids, brandName);
    }

    /**
     * 校验子分组名称不能和父分组名称不能一样
     *
     * @param parentId 父分组ID
     * @param gcName   子分组名称
     * @return
     */

    @Override
    public Integer findParentName(@RequestParam("parentId") Long parentId, @RequestParam("gcName") String gcName) {
        return baseDao.findParentName(parentId, gcName);
    }


    @Override

    public List<Map<String, Object>> selectNameByCustomIds(@RequestBody List<Long> classLists) {
        return baseDao.selectNameByCustomIds(classLists);
    }

    /**
     * 功能描述:
     * 〈根据一级分类集合获取所有三级分类〉
     *
     * @param classIds 一级分类集合
     * @author : 刘远杰
     */

    @Override
    public List<GoodsClassDTO> selectThreeByOneIds(@RequestBody List<Long> classIds) {
        return baseDao.selectThreeByOneIds(classIds);
    }

    /**
     * 查询店铺关联的分类，分层级关系
     *
     * @param storeId 店铺Id
     * @return
     * @date 2020/2/26/026 17:56
     * @author xuzhch
     **/
    @Override

    public List<GoodsClassListDTO> selectGoodsClassByStoreId(Long storeId) {
        List<StoreClassDTO> storeClassList = storeClassService.findByStoreId(storeId);
        List<Long> ids = storeClassList.stream().map(StoreClassDTO::getClassId).collect(Collectors.toList());
        List<GoodsClassListDTO> goodsClassDTOList = baseDao.selectListByStoreClass(ids);
        List<GoodsClassListDTO> build = TreeUtils.build(goodsClassDTOList, 0L);
        Iterator<GoodsClassListDTO> iterator = build.iterator();
        while (iterator.hasNext()) {
            GoodsClassListDTO level1List = iterator.next();
            if (CollectionUtils.isEmpty(level1List.getChildren())) {
                iterator.remove();
            } else {
                List<GoodsClassListDTO> level2List = level1List.getChildren();
                level2List.removeIf(goodsClassCustomLevel2 -> CollectionUtils.isEmpty(goodsClassCustomLevel2.getChildren()));
            }
        }
        return build;
    }

    /**
     * 功能描述 根据分类名称和父类id 查找id
     *
     * @param gcName   分类名称
     * @param parentId 父类id
     * @return Integer 自己的id
     * @author lishuo
     * @date 2020/6/16
     */
    @Override

    public Long findByGcNameAndParentId(@RequestParam("gcName") String gcName, @RequestParam("parentId") long parentId) {
        return baseDao.findByGcNameAndParentId(gcName, parentId);
    }


    /**
     * 功能描述 读取list保存分类到数据库
     *
     * @param goodsClassTemplateExcelList 要保存的分类集合
     * @return void
     * @author lishuo
     * @date 2020/6/12
     */
    public void saveGoodsClassFromExcel(List<GoodsClassTemplateExcel> goodsClassTemplateExcelList) {
        /**
         * 一级分类名称 excel导入数据时使用
         */
        String firstGcName = null;
        /**
         * 二级分类名称 excel导入数据时使用
         */
        String secondGcName = null;
        /**
         * 三级分类名称 excel导入数据时使用
         */
        String thirdGcName = null;
        for (GoodsClassTemplateExcel goodsClassTemplateExcel : goodsClassTemplateExcelList) {
            if (goodsClassTemplateExcel == null) {
                continue;
            }
            Long parentId = null;
            StringBuilder idPath = new StringBuilder();
            // 一级类别判断重复
            if (StringUtils.isEmpty(goodsClassTemplateExcel.getFirstGcName())) {
                goodsClassTemplateExcel.setFirstGcName(firstGcName);
            } else {
                firstGcName = goodsClassTemplateExcel.getFirstGcName();
            }
            Long firstId = baseDao.findByGcNameAndParentId(goodsClassTemplateExcel.getFirstGcName(), 0L);
            if (firstId == null) {
                //  没有重复保存一级分类
                long id = IdWorker.getId();
                GoodsClassEntity goodsClassEntity = new GoodsClassEntity();
                goodsClassEntity.setGcSort(goodsClassTemplateExcel.getGcSort());
                goodsClassEntity.setGcName(goodsClassTemplateExcel.getFirstGcName());
                goodsClassEntity.setGcParentId(0L);
                goodsClassEntity.setId(id);
                goodsClassEntity.setGcIdpath(idPath.append(id).toString());
                baseDao.insert(goodsClassEntity);
                parentId = id;
            } else {
                idPath.append(firstId);
                parentId = firstId;
            }
            // 二级类别判断重复
            if (StringUtils.isEmpty(goodsClassTemplateExcel.getSecondGcName())) {
                goodsClassTemplateExcel.setSecondGcName(secondGcName);
            } else {
                secondGcName = goodsClassTemplateExcel.getSecondGcName();
            }
            Long secondId = baseDao.findByGcNameAndParentId(goodsClassTemplateExcel.getSecondGcName(), parentId);
            if (secondId == null) {
                // 没有重复 保存二级分类
                long id = IdWorker.getId();
                GoodsClassEntity goodsClassEntity = new GoodsClassEntity();
                goodsClassEntity.setId(id);
                goodsClassEntity.setGcSort(goodsClassTemplateExcel.getGcSort());
                goodsClassEntity.setGcName(goodsClassTemplateExcel.getSecondGcName());
                goodsClassEntity.setGcParentId(parentId);
                goodsClassEntity.setGcIdpath(idPath.append(",").append(id).toString());
                goodsClassEntity.setClassType(goodsClassTemplateExcel.getClassType());
                parentId = id;
                baseDao.insert(goodsClassEntity);
            } else {
                idPath.append(",").append(secondId);
                parentId = secondId;
            }
            // 三级分类重复判断
            if (StringUtils.isEmpty(goodsClassTemplateExcel.getThirdGcName())) {
                continue;
            }
            Long thirdId = baseDao.findByGcNameAndParentId(thirdGcName, parentId);
            if (thirdId == null) {
                // 保存三級分类
                long id = IdWorker.getId();
                GoodsClassEntity goodsClassEntity = new GoodsClassEntity();
                goodsClassEntity.setId(id);
                goodsClassEntity.setGcParentId(parentId);
                goodsClassEntity.setClassType(goodsClassTemplateExcel.getClassType());
                goodsClassEntity.setGcSort(goodsClassTemplateExcel.getGcSort());
                goodsClassEntity.setGcName(goodsClassTemplateExcel.getThirdGcName());
                goodsClassEntity.setGcIdpath(idPath.append(",").append(id).toString());
                baseDao.insert(goodsClassEntity);
            }
        }
    }


    @Override
    public List<GoodsClassDTO> selectClassByParentId(Long id) {
        List<GoodsClassEntity> goodsClassEntities = baseDao.selectList(Wrappers.<GoodsClassEntity>lambdaQuery()
                .eq(GoodsClassEntity::getGcParentId, id)
                .eq(GoodsClassEntity::getDelFlag, 0));
        return Optional.ofNullable(ConvertUtils.sourceToTarget(goodsClassEntities, GoodsClassDTO.class)).orElse(new ArrayList<GoodsClassDTO>());
    }

    /**
     * 功能描述 查询出所有的分类
     *
     * @return java.util.List<com.leimingtech.modules.dto.goodsclass.GoodsClassListDTO>
     * @author lishuo
     * @date 15/7/2020
     */

    @Override
    public List<GoodsClassListDTO> findAllGoodsClass() {
        List<GoodsClassEntity> goodsClassEntities = baseDao.selectList(
                Wrappers.<GoodsClassEntity>lambdaQuery()
                        .eq(GoodsClassEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
        return ConvertUtils.sourceToTarget(goodsClassEntities, GoodsClassListDTO.class);
    }

    /**
     * 查询当前分类下是否有子分类
     *
     * @param id 分类id
     * @return 子分类的集合
     */
    @Override
    public List<GoodsClassDTO> findGcSon(Long id) {
        QueryWrapper<GoodsClassEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("gc_parent_id", id);
        List<GoodsClassEntity> list = goodsClassDao.selectList(wrapper);
        List<GoodsClassDTO> dtoList = ConvertUtils.sourceToTarget(list, GoodsClassDTO.class);
        return dtoList;
    }
}
