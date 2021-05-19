/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.webfloor.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.webfloor.WebFloorLinkConfigDao;
import com.leimingtech.modules.dto.webfloor.WebFloorLinkConfigDTO;
import com.leimingtech.modules.entity.webfloor.WebFloorLinkConfigEntity;
import com.leimingtech.modules.service.webfloor.WebFloorLinkConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * h5楼层图片链接
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WebFloorLinkConfigServiceImpl extends CrudServiceImpl<WebFloorLinkConfigDao,
        WebFloorLinkConfigEntity, WebFloorLinkConfigDTO> implements WebFloorLinkConfigService {

    @Autowired
    private WebFloorLinkConfigDao webFloorLinkConfigDao;

    /**
     * 功能描述:
     * 〈楼层图片跳转链接条件查询〉
     *
     * @param params 查询条件
     * @author : 刘远杰
     */

    @Override
    public List<WebFloorLinkConfigDTO> list(@RequestParam Map<String, Object> params) {
        List<WebFloorLinkConfigEntity> entityList = this.baseDao.selectList(this.getWrapper(params));
        return ConvertUtils.sourceToTarget(entityList, this.currentDtoClass());
    }

    /**
     * 功能描述:
     * 〈批量保存楼层图片链接〉
     *
     * @param webFloorLinkConfigDTOList 楼层图片链接集合
     * @return : boolean
     * @author : 刘远杰
     */

    @Override
    public boolean insertBatch(@RequestBody List<WebFloorLinkConfigDTO> webFloorLinkConfigDTOList) {
        List<WebFloorLinkConfigEntity> entityList = ConvertUtils.sourceToTarget(webFloorLinkConfigDTOList, WebFloorLinkConfigEntity.class);
        return this.insertBatch(entityList, 100);
    }

    /**
     * 功能描述:
     * 〈根据条件删除图片链接〉
     *
     * @param params 删除条件
     * @return : int 删除数量
     * @author : 刘远杰
     */

    @Override
    public Integer deleteWebFloorLinkConfig(@RequestParam Map<String, Object> params) {
        return baseDao.deleteWebFloorLinkConfig(params);
    }

    /**
     * 功能描述:
     * 〈根据条件逻辑删除图片链接〉
     *
     * @param map 删除条件
     * @return : int 删除数量
     * @author : 刘远杰
     */

    @Override
    public Integer logicDeletewebFloorLinkConfig(@RequestParam Map<String, Object> map) {
        return baseDao.logicDeleteWebFloorLinkConfig(map);
    }

    /**
     * 根据专题页id查询关联数量
     *
     * @param id 专题页id
     * @return
     */

    @Override
    public Integer findFloorNumByTopicId(@RequestParam("id") Long id) {
        return baseDao.findFloorNumByTopicId(id);
    }


    /**
     * 查询楼层配置根据楼层ID
     *
     * @param floorId: 楼层ID
     * @return: 楼层配置集合
     * @date 2019/8/16 18:50
     * @author lixiang
     **/
    @Override

    public List<WebFloorLinkConfigDTO> findConfigListByFloorId(Long floorId,
                                                               @RequestParam(value = "storeId", required = false) Long storeId) {
        return webFloorLinkConfigDao.findConfigListByFloorId(floorId, storeId);
    }

    @Override
    public QueryWrapper<WebFloorLinkConfigEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        Long floorId = (Long) params.get("floorId");

        QueryWrapper<WebFloorLinkConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(floorId != null, "floor_id", floorId);

        return wrapper;
    }
}
