/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.area;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.area.*;
import com.leimingtech.modules.dto.address.MemberAddressDTO;
import com.leimingtech.modules.dto.address.WechatAddressDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 地区管理service
 * @Date :2019/5/28 8:58
 * @Version V1.0
 **/

public interface AreaService {

    /**
     * @Author: weixianchun
     * @Description: 根据父id查询地区
     * @Date :2019/5/27 17:07
     * @Param parentId 地区父id
     * @Version V1.0
     **/

    List<AreaDTO> getAreasByParentId(Long parentId);


    /**
     * @Author: LX 17839193044@162.com
     * @Description: 查询全部一级地区
     * @Date: 2019/5/28 16:21
     * @Version: V1.0
     */

    List<AreaDTO> findFirstList();

    /**
     * 功能描述:
     * 〈获得某层级所有地区数据〉
     *
     * @param areaDeep 地区层级
     * @author : 刘远杰
     */

    String getAreaByAeraDeep(Integer areaDeep);

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 获取全量地址
     * @Date: 2019/7/19 12:13
     * @Version: V1.0
     */

    AreaAllDTO findAreaAll(@RequestParam("paramVersion") String paramVersion);

    /**
     * 压缩版地址查询
     *
     * @author songwenhao
     * @email ab4856812@163.com
     */

    AreaAllMiniDTO findAreaMiniAll(@RequestParam("paramVersion") String version);

    /**
     * 根据微信返回的地址信息查询对应的省市区ID
     *
     * @param wechatAddressDTO: 微信地址名称
     * @date 2020/3/31 13:49
     * @author lixiangx@leimingtech.com
     **/

    MemberAddressDTO findAreaByCondition(@RequestBody WechatAddressDTO wechatAddressDTO);

    /**
     * 分页查询地区
     *
     * @param params
     * @return
     */

    PageData<AreaPageDTO> page(@RequestParam Map params);

    /**
     * 删除地区
     *
     * @param id 主键ID
     */

    void delete(Long id);

    /**
     * 新增地区
     *
     * @param areaAddDTO
     */

    void save(@RequestBody AreaAddDTO areaAddDTO);

    /**
     * 修改地区
     *
     * @param areaUpdateDTO
     */

    void update(@RequestBody AreaUpdateDTO areaUpdateDTO);

    /**
     * 地区详情
     *
     * @param id
     * @return
     */

    AreaDTO info(@RequestParam("id") Long id);

    /**
     * 查询所有地区
     *
     * @return
     */

    List<AreaTreeDTO> treeList();

    /**
     * 查询父ID 的父ID
     *
     * @param parentId
     * @return
     */

    AreaDTO parentArea(@RequestParam("parentId") Long parentId);

    /**
     * 查询所有大区
     *
     * @return
     */

    List<RegionDTO> regionList();

    /**
     * 功能描述：
     * <地区及大区分层>
     *
     * @param
     * @return
     * @date 2020/4/22 13:28
     * @author 刘远杰
     **/

    List<AreaAndRegionTreeDTO> regionAndAreaTree();


    List<AreaTreeDTO> getAreasTree();


    List<AreaPcDTO> getCurrentAndChildren(Long parentId);
}
