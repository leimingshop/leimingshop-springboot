/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.adv.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.adv.AdvLinkConfigDao;
import com.leimingtech.modules.dto.adv.AdvLinkConfigDTO;
import com.leimingtech.modules.entity.adv.AdvLinkConfigEntity;
import com.leimingtech.modules.service.adv.AdvLinkConfigService;
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
public class AdvLinkConfigServiceImpl extends CrudServiceImpl<AdvLinkConfigDao,
        AdvLinkConfigEntity, AdvLinkConfigDTO> implements AdvLinkConfigService {


    @Override
    public QueryWrapper<AdvLinkConfigEntity> getWrapper(Map<String, Object> params) {
        return null;
    }

    /**
     * 批量保存广告配置
     *
     * @param advLinkConfigDTOList
     */

    @Override
    public void saveBatch(@RequestBody List<AdvLinkConfigDTO> advLinkConfigDTOList) {
        insertBatch(ConvertUtils.sourceToTarget(advLinkConfigDTOList, AdvLinkConfigEntity.class));
    }

    /**
     * 删除广告配置
     *
     * @param advId 广告ID
     */

    @Override
    public void delete(@RequestBody List<Long> advId) {
        baseDao.logicDelete(advId);
    }

    /**
     * 查询广告配置
     *
     * @param id
     * @return
     */

    @Override
    public List<AdvLinkConfigDTO> getLinkList(@RequestParam("id") Long id) {

        return baseDao.getLinkList(id);
    }
}
