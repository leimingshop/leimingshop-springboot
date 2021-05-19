/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.time;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.goods.time.GoodsTimeDao;
import com.leimingtech.modules.dto.goods.time.GoodsTimeDTO;
import com.leimingtech.modules.entity.goods.time.GoodsTimeEntity;
import com.leimingtech.modules.enums.goods.GoodsTimeStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 定时上架商品
 * @Date :2019/6/4 18:47
 * @Version V1.0
 **/
@Service
public class GoodsTimeServiceImpl extends CrudServiceImpl<GoodsTimeDao, GoodsTimeEntity, GoodsTimeDTO> implements GoodsTimeService {

    /**
     * @Author: weixianchun
     * @Description: 条件构造器
     * @Date :2019/6/4 18:49
     * @Param params 可变参数
     * @Version V1.0
     **/
    @Override
    public QueryWrapper<GoodsTimeEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<GoodsTimeEntity> wrapper = new QueryWrapper<>();
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

    public PageData<GoodsTimeDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 查询所有定时上架商品
     * @Date :2019/5/28 19:51
     * @Param params 可变参数
     * @Version V1.0
     **/
    @Override

    public List<GoodsTimeDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据id查询信息
     * @Date :2019/5/28 19:52
     * @Param id 定时上架商品id
     * @Version V1.0
     **/
    @Override

    public GoodsTimeDTO get(Long id) {
        return super.get(id);
    }

    /**
     * @Author: weixianchun
     * @Description: 保存定时上架商品
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/
    @Override

    public void save(@RequestBody GoodsTimeDTO dto) {

        super.save(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 修改定时上架商品
     * @Date :2019/5/28 19:52
     * @Param dto 实体
     * @Version V1.0
     **/
    @Override

    public void update(@RequestBody GoodsTimeDTO dto) {
        super.update(dto);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据ids删除定时上架商品
     * @Date :2019/5/28 19:53
     * @Param ids 定时上架商品id
     * @Version V1.0
     **/
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据规格id批量删除定时规格
     * @Date :2019/6/13 21:02
     * @Param specIds 规格id
     * @Version V1.0
     **/

    @Override
    public void deleteBySpecIds(@RequestBody List<Long> specIds) {
        baseDao.deleteBySpecIds(specIds);
    }

    /**
     * @Author: weixianchun
     * @Description: 根据商品id批量删除定时规格
     * @Date :2019/6/13 21:02
     * @Param goodsIds 商品id
     * @Version V1.0
     **/

    @Override
    public void deleteByGoodsId(@RequestBody List<Long> goodsIds) {
        baseDao.deleteByGoodsId(goodsIds);
    }

    /**
     * 查询所有可执行的定时任务
     *
     * @return
     */

    @Override
    public List<GoodsTimeDTO> selectList(@RequestParam("nowDate") Date nowDate, @RequestParam("type") Integer type) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        wrapper.le("shelf_time", nowDate);
        if (type == GoodsTimeStatusEnum.GOODS.value()) {
            wrapper.isNull("spec_id");
        } else {
            wrapper.isNotNull("spec_id");
        }
        List list = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(list, GoodsTimeDTO.class);
    }

    /**
     * 根据商品id查询定时任务
     *
     * @param goodsId
     * @return
     */

    @Override
    public GoodsTimeDTO selectByGoodsId(Long goodsId) {
        GoodsTimeEntity goodsTimeEntity = this.baseDao.selectOne(Wrappers.<GoodsTimeEntity>lambdaQuery()
                .eq(GoodsTimeEntity::getGoodsId, goodsId)
                .isNull(GoodsTimeEntity::getSpecId));

        return ConvertUtils.sourceToTarget(goodsTimeEntity, GoodsTimeDTO.class);
    }

    /**
     * 根据规格id查询定时任务
     *
     * @param specId
     * @return
     */

    @Override
    public GoodsTimeDTO selectBySpecId(Long specId) {
        return baseDao.selectByspecId(specId);
    }

    /**
     * 删除商品定时上下架
     */

    @Override
    public void deleteBySpecId(Long specId) {
        QueryWrapper<GoodsTimeEntity> wrapper = new QueryWrapper();
        wrapper.eq("spec_id", specId);
        wrapper.eq("operational_status", GoodsTimeStatusEnum.GOODS_TIME_STATUS_SHOW.value());
        baseDao.delete(wrapper);
    }

    /**
     * 修改上下架状态与实践
     *
     * @param goodsTimeDTO: 封装实体
     * @date 2020/1/6 11:56
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public void updateStatusAndTime(@RequestBody GoodsTimeDTO goodsTimeDTO) {
        this.update(Wrappers.<GoodsTimeEntity>lambdaUpdate()
                .set(GoodsTimeEntity::getOperationalStatus, goodsTimeDTO.getOperationalStatus())
                .set(GoodsTimeEntity::getShowStatus, goodsTimeDTO.getShowStatus())
                .set(GoodsTimeEntity::getShelfTime, goodsTimeDTO.getShelfTime())
                .eq(GoodsTimeEntity::getGoodsId, goodsTimeDTO.getGoodsId())
                .eq(null != goodsTimeDTO.getSpecId(), GoodsTimeEntity::getSpecId, goodsTimeDTO.getSpecId()));
    }
}