/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.webfloor;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.webfloor.InsertWebFloorAndLinkDTO;
import com.leimingtech.modules.dto.webfloor.UpdateWebFloorAndLinkDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * h5楼层设置
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */

public interface WebFloorService {

    /**
     * 功能描述:
     * 〈楼层分页查询〉
     *
     * @param params 查询条件
     * @return 返回楼层分页信息
     * @author : 刘远杰
     */

    PageData<WebFloorDTO> findShopWebFloorPagerList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈楼层查询〉
     *
     * @param params 查询条件
     * @return 返回楼层信息
     * @author : 刘远杰
     */

    List<WebFloorDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据id查询楼层及关联图片跳转链接〉
     *
     * @param id 楼层id
     * @return 返回楼层配置信息
     * @author : 刘远杰
     */

    WebFloorAndLinkDTO findWebFloorLinkConfigById(Long id);

    /***
     * 修改楼层及图片跳转
     * @Author 刘远杰
     * @Description 修改楼层及图片跳转
     * @Date 2019/5/12 12:27
     * @param dto 楼层及图片链接实体
     */

    void saveWebFloorAndLink(@RequestBody InsertWebFloorAndLinkDTO dto);

    /***
     * 修改楼层及图片跳转
     *
     * @Author 刘远杰
     * @Description 修改楼层及图片跳转
     * @Date 2019/5/12 12:27
     * @param dto 楼层及图片链接实体
     */

    void updateWebFloorAndLink(@RequestBody UpdateWebFloorAndLinkDTO dto);

    /**
     * 删除h5楼层设置及关联图片链接（逻辑删除）
     *
     * @param ids     删除楼层索引
     * @param storeId storeId 店铺id
     * @Author 刘远杰
     * @Description 删除h5楼层设置及关联图片链接（逻辑删除）
     * @Date 2019/5/12 17:42
     */

    void deleteWebFloorAndLink(@RequestBody Long[] ids, @RequestParam(value = "storeId", required = false) Long storeId);

    /**
     * 功能描述:
     * 〈h5展示中楼层集合查询〉
     *
     * @param params 查询条件
     * @return 返回楼层配置信息
     * @author : 刘远杰
     */

    List<WebFloorAndLinkDTO> findFrontShopWebFloorList(@RequestParam Map<String, Object> params);

}
