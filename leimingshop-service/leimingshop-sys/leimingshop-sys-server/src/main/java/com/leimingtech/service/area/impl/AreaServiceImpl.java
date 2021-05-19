/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.area.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.TreeUtils;
import com.leimingtech.constants.SysRedisConstants;
import com.leimingtech.dao.area.AreaDao;
import com.leimingtech.dto.area.*;
import com.leimingtech.entity.area.AreaEntity;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.address.WechatAddressDTO;
import com.leimingtech.service.area.AreaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: weixianchun
 * @Description: 地区管理impl
 * @Date :2019/5/28 8:59
 * @Version V1.0
 **/
@Service

@Slf4j
public class AreaServiceImpl extends CrudServiceImpl<AreaDao, AreaEntity, AreaDTO> implements AreaService {


    @Resource
    private AreaDao areaDao;
    @Autowired
    private RedisUtils redisUtils;


    @Override
    public QueryWrapper<AreaEntity> getWrapper(Map<String, Object> params) {
        String areaDeep = (String) params.get("areaDeep");
        String areaName = (String) params.get("areaName");
        String parentId = (String) params.get("parentId");


        QueryWrapper<AreaEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(areaDeep), "area_deep", areaDeep);
        wrapper.like(StringUtils.isNotBlank(areaName), "area_name", areaName);
        wrapper.eq(StringUtils.isNotBlank(parentId), "area_parent_id", parentId);
        if (StringUtils.isBlank(parentId) && areaDeep == null && StringUtils.isBlank(areaName)) {
            wrapper.eq("area_deep", 1);
        }


        return wrapper;
    }


    /**
     * @Author: weixianchun
     * @Description: 根据父id查询地区
     * @Date :2019/5/27 17:07
     * @Param parentId 地区父id
     * @Version V1.0
     **/
    @Override

    public List<AreaDTO> getAreasByParentId(Long parentId) {
        return areaDao.getAreasListByParentId(parentId);
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 查询全部一级地址信息
     * @Date: 2019/5/28 16:19
     * @Version: V1.0
     */
    @Override

    public List<AreaDTO> findFirstList() {
        QueryWrapper<AreaEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("area_parent_id", 0);
        return ConvertUtils.sourceToTarget(baseDao.selectList(wrapper), AreaDTO.class);
    }


    @Override
    public String getAreaByAeraDeep(Integer areaDeep) {
        List<AreaJSDTO> areaJSDTOList = baseDao.getAreaByAeraDeep(areaDeep);
        return JSONArray.toJSONString(areaJSDTOList);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 获取全量地址
     * @Date: 2019/7/19 12:13
     * @Version: V1.0
     */

    @Override
    public AreaAllDTO findAreaAll(@RequestParam("paramVersion") String paramVersion) {
        Integer version = null;
        if (StringUtils.isNotEmpty(paramVersion)) {
            version = Integer.valueOf(paramVersion);
        }
        AreaAllDTO areaAllDTO = new AreaAllDTO();
        String areaVersionKey = SysRedisConstants.AREA_VERSION;
        Object redisVersionObj = redisUtils.get(areaVersionKey);
        if (null != redisVersionObj && null != version) {
            Integer redisVersion = (Integer) redisVersionObj;
            if (redisVersion.equals(version)) {
                areaAllDTO.setVersion(version);
                return areaAllDTO;
            }
        }

        //判断redis中是否有缓存数据
        String areaKey = SysRedisConstants.AREA;
        Object obj = redisUtils.get(areaKey);
        if (obj == null) {
            // redis中不存在
            String first = this.getAreaByAeraDeep(1);
            String second = this.getAreaByAeraDeep(2);
            String third = this.getAreaByAeraDeep(3);
            String fourth = this.getAreaByAeraDeep(4);
            areaAllDTO.setFirstArea(first);
            areaAllDTO.setSecondArea(second);
            areaAllDTO.setThirdArea(third);
            areaAllDTO.setFourthArea(fourth);
            areaAllDTO.setVersion(0);
            redisUtils.set(areaKey, areaAllDTO, SysRedisConstants.JEDIS_EXPIRE);
            redisUtils.set(areaVersionKey, 0, SysRedisConstants.JEDIS_EXPIRE);
        } else {
            areaAllDTO = (AreaAllDTO) obj;
            if (null != redisVersionObj) {
                areaAllDTO.setVersion((Integer) redisVersionObj);
            }
        }
        return areaAllDTO;
    }

    /**
     * @ Author: SWH ab4856812@163.com
     * @ Description: 压缩地址信息
     * @ Version: V1.0
     */

    @Override
    public AreaAllMiniDTO findAreaMiniAll(@RequestParam("paramVersion") String paramVersion) {
        Integer version = null;
        if (StringUtils.isNotEmpty(paramVersion)) {
            version = Integer.valueOf(paramVersion);
        }
        AreaAllMiniDTO areaAllMiniDTO = new AreaAllMiniDTO();
        String areaMiniVersionKey = SysRedisConstants.AREA_MINI_VERSION;
        Object redisVersionObj = redisUtils.get(areaMiniVersionKey);
        if (null != redisVersionObj && null != version) {
            Integer redisVersion = (Integer) redisVersionObj;
            if (redisVersion.equals(version)) {
                areaAllMiniDTO.setVersion(version);
                return areaAllMiniDTO;
            }
        }

        //判断redis中是否有缓存数据
        String areaKey = SysRedisConstants.AREA_MINI;
        Object obj = redisUtils.get(areaKey);
        if (obj == null) {
            // redis中不存在 则查询全部信息
            List<AreaMiniDTO> returnList = new ArrayList<>();
            List<AreaJSDTO> baseList = baseDao.getAreaAll();
            baseList.forEach(areaJsonDTO -> {
                if (areaJsonDTO.getP().equals(0L)) {
                    List<AreaMiniDTO> childList = this.getChild(areaJsonDTO.getI(), baseList);
                    AreaMiniDTO areaMiniDTO = new AreaMiniDTO();
                    areaMiniDTO.setI(areaJsonDTO.getI());
                    areaMiniDTO.setN(areaJsonDTO.getN());
                    areaMiniDTO.setC(childList);
                    returnList.add(areaMiniDTO);
                }
            });
            areaAllMiniDTO.setData(returnList);
            areaAllMiniDTO.setVersion(0);
            redisUtils.set(areaKey, areaAllMiniDTO, SysRedisConstants.JEDIS_EXPIRE);
            redisUtils.set(areaMiniVersionKey, 0, SysRedisConstants.JEDIS_EXPIRE);
        } else {
            areaAllMiniDTO = (AreaAllMiniDTO) obj;
            if (null != redisVersionObj) {
                areaAllMiniDTO.setVersion((Integer) redisVersionObj);
            }
        }
        return areaAllMiniDTO;
    }

    private List<AreaMiniDTO> getChild(Long parentCode, List<AreaJSDTO> baseList) {
        List<AreaMiniDTO> childList = new ArrayList<>();
        baseList.forEach(areaJsonDTO -> {
            if (areaJsonDTO.getP().equals(parentCode)) {
                AreaMiniDTO areaMiniDTO = new AreaMiniDTO();
                areaMiniDTO.setI(areaJsonDTO.getI());
                areaMiniDTO.setN(areaJsonDTO.getN());
                areaMiniDTO.setC(this.getChild(areaJsonDTO.getI(), baseList));
                childList.add(areaMiniDTO);
            }
        });
        return childList;
    }


    /**
     * 根据微信返回的地址信息查询对应的省市区ID
     *
     * @param wechatAddressDTO: 微信地址名称
     * @date 2020/3/31 13:49
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public MemberAddressDTO findAreaByCondition(@RequestBody WechatAddressDTO wechatAddressDTO) {

        // lixiang 2020.3.31 微信依旧使用三级地址，而且直辖市地址信息为天津市-天津市-东丽区，与本系统地址库(天津-东丽区)不符合，因此直辖市需要过滤二级地址
        List<String> list = Arrays.asList("北京市", "上海市", "天津市", "重庆市");

        // 根据省名称查询出数据库中对应的地址信息 （微信一级地址存在 **市 与本系统一级地址名称不符）
        String provinceName = StringUtils.remove(StringUtils.remove(wechatAddressDTO.getProvinceName(), "省"), "市");

        // 一级地址名称切换
        provinceName = provinceName.replaceAll("内蒙古自治区", "内蒙古");
        provinceName = provinceName.replaceAll("广西壮族自治区", "广西");
        provinceName = provinceName.replaceAll("西藏自治区", "西藏");
        provinceName = provinceName.replaceAll("宁夏回族自治区", "宁夏");
        provinceName = provinceName.replaceAll("新疆维吾尔自治区", "新疆");
        provinceName = provinceName.replaceAll("香港特别行政区", "港澳");
        provinceName = provinceName.replaceAll("澳门特别行政区", "港澳");
        AreaEntity areaProvinceEntity = baseDao.selectOne(Wrappers.<AreaEntity>lambdaQuery()
                .eq(AreaEntity::getAreaName, provinceName)
                .eq(AreaEntity::getAreaDeep, 1)
                .select(AreaEntity::getId));

        // 查询市信息  以市名称和省级id作为查询条件
        LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = Wrappers.<AreaEntity>lambdaQuery()
                .eq(AreaEntity::getAreaParentId, areaProvinceEntity.getId())
                .select(AreaEntity::getId);

        // 判断该城市是不是直辖市
        AreaEntity areaCityEntity;
        AreaEntity countryArea = null;
        if (list.contains(wechatAddressDTO.getCityName())) {
            // 是直辖市 直接获取第三级区地址作为本系统第二级地址查询
            lambdaQueryWrapper.eq(AreaEntity::getAreaName, StringUtils.remove(wechatAddressDTO.getCountryName(), "市"));
            // 查询市信息  以市名称和省级id作为查询条件
            areaCityEntity = Optional.ofNullable(baseDao.selectOne(lambdaQueryWrapper)).orElse(new AreaEntity());
        } else {
            lambdaQueryWrapper.eq(AreaEntity::getAreaName, wechatAddressDTO.getCityName());
            // 查询市信息  以市名称和省级id作为查询条件
            areaCityEntity = Optional.ofNullable(baseDao.selectOne(lambdaQueryWrapper)).orElse(new AreaEntity());

            // 查询三级地址
            countryArea = baseDao.selectOne(Wrappers.<AreaEntity>lambdaQuery()
                    .eq(AreaEntity::getAreaName, wechatAddressDTO.getCountryName())
                    .eq(AreaEntity::getAreaParentId, areaCityEntity.getId())
                    .select(AreaEntity::getId));
        }

        countryArea = Optional.ofNullable(countryArea).orElse(new AreaEntity());
        // 创建用户地址信息
        MemberAddressDTO memberAddressDTO = new MemberAddressDTO();
        memberAddressDTO.setTrueName(wechatAddressDTO.getUserName());
        memberAddressDTO.setMobPhone(wechatAddressDTO.getTelNumber());
        memberAddressDTO.setAddress(wechatAddressDTO.getDetailInfo());
        memberAddressDTO.setZipCode(wechatAddressDTO.getPostalCode());
        memberAddressDTO.setProvinceId(areaProvinceEntity.getId());
        memberAddressDTO.setCityId(areaCityEntity.getId());
        memberAddressDTO.setAreaId(countryArea.getId());
        return memberAddressDTO;
    }

    /**
     * 分页查询地区
     *
     * @param params
     * @return
     */

    @Override
    public PageData<AreaPageDTO> page(@RequestParam Map params) {
        // 分页
        IPage<AreaEntity> page = getPage(params, "id", true);
        // 查询
        List<AreaPageDTO> list = baseDao.findPage(params);

        return new PageData(list, page.getTotal());
    }

    /**
     * 删除地区
     *
     * @param id 主键ID
     */

    @Override
    public void delete(Long id) {
        // 查询地区下是否有子地区
        Integer count = baseDao.selectArea(id);
        if (count > 0) {
            throw new ServiceException("请删除子地区");
        }
        baseDao.deleteById(id);
    }

    /**
     * 新增地区
     *
     * @param areaAddDTO
     */

    @Override
    public void save(@RequestBody AreaAddDTO areaAddDTO) {
        AreaEntity areaEntity = ConvertUtils.sourceToTarget(areaAddDTO, AreaEntity.class);
        areaEntity.setId(IdWorker.getId());
        areaEntity.setSeqNum(areaEntity.getId().toString());
        areaEntity.setAreaOpen(0);
        if (areaEntity.getAreaParentId() == null) {
            areaEntity.setAreaParentId(0L);
        }
        baseDao.insert(areaEntity);
    }

    /**
     * 修改地区
     *
     * @param areaUpdateDTO
     */

    @Override
    public void update(@RequestBody AreaUpdateDTO areaUpdateDTO) {
        AreaEntity areaDTO = ConvertUtils.sourceToTarget(areaUpdateDTO, AreaEntity.class);
        baseDao.updateById(areaDTO);
    }

    /**
     * 地区详情
     *
     * @param id
     * @return
     */

    @Override
    public AreaDTO info(@RequestParam("id") Long id) {
        AreaEntity areaEntity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(areaEntity, AreaDTO.class);
    }

    /**
     * 查询所有地区
     *
     * @return
     */

    @Override
    public List<AreaTreeDTO> treeList() {
        List<AreaTreeDTO> list = baseDao.selectAll();

        List<AreaTreeDTO> build1 = TreeUtils.build(list, 0L);
        return build1;
    }

    /**
     * 查询父ID 的父ID
     *
     * @param parentId
     * @return
     */

    @Override
    public AreaDTO parentArea(@RequestParam("parentId") Long parentId) {
        return baseDao.selectParnetId(parentId);
    }

    /**
     * 查询所有大区
     *
     * @return
     */

    @Override
    public List<RegionDTO> regionList() {
        return baseDao.regionList();
    }

    /**
     * 功能描述：
     * <地区去除四级及大区分层>
     *
     * @param
     * @return
     * @date 2020/4/22 13:28
     * @author 刘远杰
     **/

    @Override
    public List<AreaAndRegionTreeDTO> regionAndAreaTree() {
        String regionAreaKey = SysRedisConstants.REGIONANDAREA;

        Object obj = redisUtils.get(regionAreaKey);
        if (obj == null) {
            List<AreaAndRegionTreeDTO> areaTreeList = baseDao.threeAreaAsAreaList();
            List<AreaAndRegionTreeDTO> regionTreeList = baseDao.regionAsAreaList();
            areaTreeList.addAll(regionTreeList);

            // 按照地址层级进行分组
            Map<Integer, List<AreaAndRegionTreeDTO>> collectMap = areaTreeList.stream().collect(Collectors.groupingBy(AreaAndRegionTreeDTO::getAreaDeep));
            List<AreaAndRegionTreeDTO> list1 = collectMap.get(-1);
            List<AreaAndRegionTreeDTO> list2 = collectMap.get(1);
            List<AreaAndRegionTreeDTO> list3 = collectMap.get(2);
            List<AreaAndRegionTreeDTO> list4 = collectMap.get(3);
//            List<AreaAndRegionTreeDTO> list5 = collectMap.get(4);

//            List<AreaAndRegionTreeDTO> collect = list4.stream().map(map -> list5.stream()
//                    .filter(m -> map.getId().equals(m.getAreaParentId())).findFirst().map(m -> {
//                        if (map.getChildren() == null) {
//                            map.setChildren(new ArrayList<>());
//                        }
//                        map.getChildren().add(m);
//                        return map;
//                    }).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());

//            for (AreaAndRegionTreeDTO menu1 : list5) {
//                menu1.setChildren(new ArrayList<>());
//                for (AreaAndRegionTreeDTO menu2 : list4) {
//                    if (menu1.getAreaParentId().equals(menu2.getId())) {
//                        if (menu2.getChildren() == null) {
//                            menu2.setChildren(new ArrayList<>());
//                        }
//                        menu2.getChildren().add(menu1);
//                        break;
//                    }
//                }
//            }

            for (AreaAndRegionTreeDTO menu1 : list4) {
                for (AreaAndRegionTreeDTO menu2 : list3) {
                    if (menu1.getAreaParentId().equals(menu2.getId())) {
                        if (menu2.getChildren() == null) {
                            menu2.setChildren(new ArrayList<>());
                        }
                        menu2.getChildren().add(menu1);
                        break;
                    }
                }
            }
            for (AreaAndRegionTreeDTO menu1 : list3) {
                for (AreaAndRegionTreeDTO menu2 : list2) {
                    if (menu1.getAreaParentId().equals(menu2.getId())) {
                        if (menu2.getChildren() == null) {
                            menu2.setChildren(new ArrayList<>());
                        }
                        menu2.getChildren().add(menu1);
                        break;
                    }
                }
            }
            for (AreaAndRegionTreeDTO menu1 : list2) {
                for (AreaAndRegionTreeDTO menu2 : list1) {
                    if (menu1.getAreaParentId() != null && menu1.getAreaParentId().equals(menu2.getId())) {
                        if (menu2.getChildren() == null) {
                            menu2.setChildren(new ArrayList<>());
                        }
                        menu2.getChildren().add(menu1);
                        break;
                    }
                }
            }

            redisUtils.set(regionAreaKey, list1, SysRedisConstants.JEDIS_EXPIRE);
            return list1;
        } else {
            List<AreaAndRegionTreeDTO> tree = JSONArray.parseArray(obj.toString(), AreaAndRegionTreeDTO.class);
            return tree;
        }

    }


    @Override
    public List<AreaTreeDTO> getAreasTree() {
        List<AreaTreeDTO> dtoArrayList = new ArrayList<AreaTreeDTO>();
        if (redisUtils.get(SysRedisConstants.AREA_TREE) == null) {
            redisUtils.delete(SysRedisConstants.AREA_TREE);
            List<AreaTreeDTO> areaTreeDTOList = baseDao.selectAll();
            dtoArrayList = TreeUtils.build(areaTreeDTOList);
            redisUtils.set(SysRedisConstants.AREA_TREE, dtoArrayList, SysRedisConstants.JEDIS_EXPIRE);
        }
        dtoArrayList = (List<AreaTreeDTO>) redisUtils.get(SysRedisConstants.AREA_TREE);
        return dtoArrayList;
    }


    @Override
    public List<AreaPcDTO> getCurrentAndChildren(Long parentId) {
        List<AreaPcDTO> areaPcDTOList = baseDao.selectCurrentAndChildren(parentId);
        return areaPcDTOList;
    }
}
