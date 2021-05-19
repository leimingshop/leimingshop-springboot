/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.reason.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.reason.ReasonDescriptionDao;
import com.leimingtech.dto.reason.ReasonDescriptionDTO;
import com.leimingtech.dto.reason.ReasonDescriptionSaveDTO;
import com.leimingtech.entity.reason.ReasonDescriptionEntity;
import com.leimingtech.redis.SysSettingRedis;
import com.leimingtech.service.reason.ReasonDescriptionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 原因描述
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.com
 * @since 1.0.0 2019-06-10
 */

@Service

public class ReasonDescriptionServiceImpl extends
        CrudServiceImpl<ReasonDescriptionDao, ReasonDescriptionEntity, ReasonDescriptionDTO>
        implements ReasonDescriptionService {

    @Resource
    private ReasonDescriptionDao reasonDescriptionDao;

    @Autowired
    private SysSettingRedis sysSettingRedis;

    @Override
    public QueryWrapper<ReasonDescriptionEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String type = (String) params.get("type");
        String role = (String) params.get("role");
        String content = (String) params.get("content");

        QueryWrapper<ReasonDescriptionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(type), "type", type);
        wrapper.like(StringUtils.isNotBlank(role), "role", role);
        wrapper.like(StringUtils.isNotBlank(content), "content", content);

        return wrapper;
    }

    /**
     * 保存原因描述
     *
     * @param dto
     */

    @Override
    public void save(@RequestBody ReasonDescriptionSaveDTO dto) {
        ReasonDescriptionEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, ReasonDescriptionEntity.class);
        reasonDescriptionDao.insert(reasonDescriptionEntity);
        sysSettingRedis.setAfter("afterInfo", reasonDescriptionEntity);
    }

    /**
     * 修改原因描述
     *
     * @param dto
     */

    @Override
    public void update(@RequestBody ReasonDescriptionDTO dto) {
        super.update(dto);
        sysSettingRedis.setAfter("afterInfo", ConvertUtils.sourceToTarget(dto, ReasonDescriptionEntity.class));
    }

    /**
     * 删除原因描述
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
        sysSettingRedis.delete("afterInfo", ids);
    }

    /**
     * 根据ID查询原因描述
     *
     * @param id
     * @return
     */

    @Override
    public ReasonDescriptionDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @return java.util.List<com.leimingtech.dto.reason.ReasonDescriptionDTO>
     * @Description 查询所有的原因描述列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */

    @Override
    public List<ReasonDescriptionDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.dto.reason.ReasonDescriptionDTO>
     * @Description 分页查询所有的原因描述列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */

    @Override
    public PageData<ReasonDescriptionDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

}
