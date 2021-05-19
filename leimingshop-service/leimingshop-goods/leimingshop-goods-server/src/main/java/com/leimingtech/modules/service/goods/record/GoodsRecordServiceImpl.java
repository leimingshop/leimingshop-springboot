/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.record;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dao.goods.record.GoodsRecordDao;
import com.leimingtech.modules.dto.goods.record.GoodsRecordDTO;
import com.leimingtech.modules.entity.goods.record.GoodsRecordEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品上下架状态记录
 * @Date :2019/6/4 18:34
 * @Version V1.0
 **/
@Service

public class GoodsRecordServiceImpl extends CrudServiceImpl<GoodsRecordDao, GoodsRecordEntity, GoodsRecordDTO> implements GoodsRecordService {

    /**
     * @Author: weixianchun
     * @Description: 条件构造器
     * @Date :2019/6/5 9:20
     * @Param params 可变参数
     * @Version V1.0
     **/
    @Override
    public QueryWrapper<GoodsRecordEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/28 19:51
     * @Param params 可变参数
     * @Version V1.0
     **/

    @Override
    public PageData<GoodsRecordDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有商品上下架状态记录
     * @Date :2019/5/28 19:51
     * @Param params 可变参数
     * @Version V1.0
     **/

    @Override
    public List<GoodsRecordDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品上下架状态记录id查询信息
     * @Date :2019/5/28 19:52
     * @Param id 商品上下架状态记录id
     * @Version V1.0
     **/

    @Override
    public GoodsRecordDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存商品上下架状态记录
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void save(@RequestBody GoodsRecordDTO dto) {
        super.save(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 修改商品上下架状态记录
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/

    @Override
    public void update(@RequestBody GoodsRecordDTO dto) {
        super.update(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品上下架状态记录id批量删除
     * @Date :2019/5/28 19:53
     * @Param ids 商品上下架状态记录id
     * @Version V1.0
     **/

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }


}