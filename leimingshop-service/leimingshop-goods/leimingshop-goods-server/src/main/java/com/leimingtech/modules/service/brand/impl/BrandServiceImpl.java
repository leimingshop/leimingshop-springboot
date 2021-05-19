/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.brand.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.brand.BrandDao;
import com.leimingtech.modules.dto.brand.BrandDTO;
import com.leimingtech.modules.dto.brand.BrandExcelDTO;
import com.leimingtech.modules.dto.brand.BrandSaveDTO;
import com.leimingtech.modules.dto.goodsclass.GoodsClassDTO;
import com.leimingtech.modules.dto.storeclass.StoreClassDTO;
import com.leimingtech.modules.entity.brand.BrandEntity;
import com.leimingtech.modules.enums.brand.BrandErrorCodeEnum;
import com.leimingtech.modules.service.brand.BrandService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goodsclass.GoodsClassService;
import com.leimingtech.modules.service.storeclass.StoreClassService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: weixianchun
 * @Description: 品牌管理 BrandServiceImpl
 * @Date :2019/5/20 14:03
 * @Version V1.0
 **/
@Service
public class BrandServiceImpl extends CrudServiceImpl<BrandDao, BrandEntity, BrandDTO> implements BrandService {

    @Resource
    private GoodsService goodsService;
    @Autowired
    private StoreClassService storeClassService;
    @Autowired
    private GoodsClassService goodsClassService;

    /**
     * @Author: weixianchun
     * @Description: 条件构造器
     * @Date :2019/6/24 14:58
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override
    public QueryWrapper<BrandEntity> getWrapper(Map<String, Object> params) {

        String id = (String) params.get("id");
        //获取品牌名称
        String brandName = (String) params.get("brandName");
        //获取品牌英文名称
        String brandNameEn = (String) params.get("brandNameEn");
        //获取品牌首字母
        String brandInitials = (String) params.get("brandInitials");
        //审核状态
        String brandApply = (String) params.get("brandApply");

        QueryWrapper<BrandEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //拼接,模糊查询条件
        wrapper.like(StringUtils.isNotBlank(brandName), "brand_name", brandName);
        wrapper.like(StringUtils.isNotBlank(brandNameEn), "brand_name_en", brandNameEn);
        wrapper.like(StringUtils.isNotBlank(brandInitials), "brand_initials", brandInitials);
        wrapper.like(StringUtils.isNotBlank(brandApply), "brand_apply", brandApply);

        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 保存时-根据品牌名称,品牌英文名称查询品牌信息
     * @Date :2019/5/20 14:01
     * @Param :[brandName] 品牌名称
     * @Param :[brandNameEn] 品牌英文名称
     * @Version V1.0
     **/
//    @Override
//
//    public BrandDTO findByName(@RequestParam("brandName") String brandName, @RequestParam(value = "brandNameEn", required = false) String brandNameEn) {
//
//        return baseDao.findByName(brandName, brandNameEn);
//    }

    /**
     * @Author: weixianchun
     * @Description: 修改时-查询品牌名称数量防止重复校验
     * @Date :2019/5/20 14:07
     * @Param :[brandDTO] 实体
     * @Version V1.0
     **/
//    @Override
//
//    public int selectByBrandName(@RequestBody BrandDTO brandDTO) {
////        return brandDao.selectByBrandName(brandDTO);
//    }

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override

    public PageData<BrandDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有品牌信息
     * @Date :2019/5/28 19:51
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override

    public List<BrandDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询信息
     * @Date :2019/5/28 19:52
     * @Param id 品牌id
     * @Version V1.0
     **/
    @Override

    public BrandDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 功能描述:
     * 〈根据ids查询品牌集合〉
     *
     * @param brandIds 品牌id
     * @param storeId  店铺id
     * @author : 刘远杰
     */

    @Override
    public List<BrandDTO> getByBrandIds(@RequestBody List<Long> brandIds,
                                        @RequestParam(value = "storeId", required = false) Long storeId) {
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(storeId != null, "store_id", storeId)
                .in("id", brandIds).eq("del_flag", DelFlagEnum.NORMAL.value());
        List<BrandEntity> entityList = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(entityList, BrandDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存品牌信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/
    @Override

    public void save(@RequestBody BrandSaveDTO dto) {
        BrandEntity brandEntity = ConvertUtils.sourceToTarget(dto, BrandEntity.class);
        baseDao.insert(brandEntity);
    }

    /**
     * @Author: weixianchun
     * @Description: 修改品牌信息
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/
    @Override

    public void update(@RequestBody BrandDTO dto) {
        super.update(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id批量删除品牌信息
     * @Date :2019/5/28 19:53
     * @Param ids 品牌id
     * @Version V1.0
     **/
    @Override

    public List<String> deleteByBrandId(@RequestBody Long[] ids) {

        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            Long id = ids[i];
            //根据品牌id查询品牌数量
            int count = goodsService.findCountByBrandId(id);
            //数量不为空,品牌关联着商品
            if (count != 0) {
                BrandDTO brandDTO = super.get(id);
                //获取品牌名称
                String brandName = brandDTO.getBrandName();
                list.add(brandName);
            }
        }
        if (CollectionUtils.isEmpty(list)) {
            super.delete(ids);
        }
        return list;
    }

    /**
     * @Author: weixianchun
     * @Description: 导出日志
     * @Date :2019/7/9 16:00
     * @Param params 查询条件
     * @Version V1.0
     **/

    @Override
    public List<BrandExcelDTO> exportList(@RequestParam Map<String, Object> params) {
        params.put(Constant.ORDER_FIELD, Constant.UPDATE_DATE);
        params.put(Constant.ORDER, "desc");
        List<BrandEntity> list = baseDao.selectList(getWrapper(params));
        ArrayList<BrandExcelDTO> excelList = new ArrayList<>();
        for (BrandEntity brandEntity : list) {
            //转换
            BrandExcelDTO excelDTO = ConvertUtils.sourceToTarget(brandEntity, BrandExcelDTO.class);
            //申请状态
            if (brandEntity.getBrandApply() == BrandErrorCodeEnum.BRAND_APPLY_PASS.value()) {
                excelDTO.setBrandApply("通过");
            } else if (brandEntity.getBrandApply() == BrandErrorCodeEnum.BRAND_APPLYING.value()) {
                excelDTO.setBrandApply("申请中");
            }
            excelList.add(excelDTO);
        }

        return excelList;
    }

    /**
     * 功能描述:
     * 〈获得店铺关联所有品牌〉
     *
     * @param storeId 店铺id
     * @author : 刘远杰
     */

    @Override
    public List<BrandDTO> storeBrandList(Long storeId) {
        // 获得店铺分类（一级）
        List<StoreClassDTO> storeClassDTOList = storeClassService.findByStoreId(storeId);
        if (CollectionUtils.isEmpty(storeClassDTOList)) {
            return Collections.emptyList();
        }
        // 获得三级店铺分类
        List<Long> classIds = storeClassDTOList.stream().map(StoreClassDTO::getClassId).collect(Collectors.toList());
        List<GoodsClassDTO> goodsClassDTOList = goodsClassService.selectThreeByOneIds(classIds);
        if (CollectionUtils.isEmpty(goodsClassDTOList)) {
            return Collections.emptyList();
        }
        // 根据三级分类查询品牌集合
        classIds = goodsClassDTOList.stream().map(GoodsClassDTO::getId).collect(Collectors.toList());
        return baseDao.selectByGcId(classIds);
    }

    /**
     * 功能描述 查询出所有的品牌
     *
     * @param * @param
     * @return java.util.List<com.leimingtech.modules.dto.brand.BrandDTO>
     * @author lishuo
     * @date 15/7/2020
     */

    @Override
    public List<BrandDTO> findAllBrand() {
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        List<BrandEntity> brandEntities = baseDao.selectList(queryWrapper);
        return ConvertUtils.sourceToTarget(brandEntities, BrandDTO.class);
    }


    @Override
    public Integer selectCountByCondition(@RequestBody BrandDTO brandDTO) {
        return baseDao.selectCountByCondition(brandDTO);
    }
}
