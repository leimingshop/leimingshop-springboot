/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.attribute.AttributeValueDao;
import com.leimingtech.modules.dto.attribute.AttributeValueDTO;
import com.leimingtech.modules.entity.attribute.AttributeValueEntity;
import com.leimingtech.modules.service.attribute.AttributeValueService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 属性值管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */
@Service("attributeValueService")
@Transactional(rollbackFor = Exception.class)
public class AttributeValueServiceImpl extends CrudServiceImpl<AttributeValueDao, AttributeValueEntity, AttributeValueDTO>
        implements AttributeValueService {

    /**
     * @param attrId
     * @author 刘远杰
     * @Description 属性值物理删除
     * @Date 15:31
     */
    @Override
    public Integer deleteAttrValueByAttrId(Long attrId) {
        return baseDao.deleteAttrValueByAttrId(attrId);
    }

    /**
     * @param attrId
     * @author 刘远杰
     * @Description 属性值逻辑删除
     * @Date 2019/5/20 15:32
     */
    @Override
    public Integer logicDeleteAttrValueByAttrId(Long attrId) {
        UpdateWrapper<AttributeValueEntity> wrapper = new UpdateWrapper<>();
        wrapper.eq(attrId != null, "attr_id", attrId);
        return baseDao.delete(wrapper);
    }

    @Override
    public void insertBatch(List<AttributeValueDTO> list) {
        List<AttributeValueEntity> attributeValueEntities = ConvertUtils.sourceToTarget(list, AttributeValueEntity.class);
        super.insertBatch(attributeValueEntities);
    }

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 根据属性ID查询属性值集合
     * @Date: 2019/6/13 16:44
     * @Version: V1.0
     */
    @Override
    public List<AttributeValueDTO> findListByAttrId(Long id) {
        return baseDao.findListByAttrId(id);
    }

    @Override
    public QueryWrapper<AttributeValueEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<AttributeValueEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        if (StringUtils.isNotBlank((String) params.get(Constant.ORDER_FIELD))
                && StringUtils.isNotBlank((String) params.get(Constant.ORDER))) {
            if (StringUtils.equals((String) params.get(Constant.ORDER), Constant.ASC)) {
                wrapper.orderByAsc((String) params.get(Constant.ORDER_FIELD));
            } else {
                wrapper.orderByDesc((String) params.get(Constant.ORDER_FIELD));
            }
        }

        return wrapper;
    }


}
