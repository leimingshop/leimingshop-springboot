/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.aftersale.dao.AftersaleLogDao;
import com.leimingtech.modules.aftersale.dto.*;
import com.leimingtech.modules.aftersale.entity.AftersaleLogEntity;
import com.leimingtech.modules.aftersale.service.AftersaleGoodsService;
import com.leimingtech.modules.aftersale.service.AftersaleLogService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 售后日志
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AftersaleLogServiceImpl extends CrudServiceImpl<AftersaleLogDao, AftersaleLogEntity, AftersaleLogDTO> implements AftersaleLogService {

    private static final String SORT_ASC = "ASC";

    @Resource
    private AftersaleLogDao aftersaleLogDao;

    @Autowired
    private AftersaleGoodsService aftersaleGoodsService;

    @Override
    public QueryWrapper<AftersaleLogEntity> getWrapper(Map<String, Object> params) {
        String aftersaleSn = params.get("aftersaleSn").toString();

        QueryWrapper<AftersaleLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(aftersaleSn), "aftersale_sn", aftersaleSn);
        wrapper.orderByDesc("create_date");
        return wrapper;
    }

    /**
     * 保存售后日志
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody AftersaleLogSaveDTO dto) {
        AftersaleLogEntity reasonDescriptionEntity = ConvertUtils.sourceToTarget(dto, AftersaleLogEntity.class);
        aftersaleLogDao.insert(reasonDescriptionEntity);
    }

    /**
     * 修改售后日志
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody AftersaleLogDTO dto) {
        super.update(dto);
    }

    /**
     * 删除售后日志
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID查询售后日志
     *
     * @param serviceSn
     * @return
     */
    @Override

    public AftersaleLogDTO get(Long serviceSn) {
        return super.get(serviceSn);
    }


    @Override

    public List<AftersaleLogDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleLogDTO>
     * @Description 查询所有的售后日志列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 18:13 2019-06-10
     */
    @Override

    public List<AftersaleLogListDTO> listLog(Long aftersaleSn) {
        return aftersaleLogDao.findLog(aftersaleSn);
    }

    /**
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleLogDTO>
     * @Description 分页查询所有的售后日志列表
     * @Param * @param params:
     * @Author huangkeyuan
     * @Date 13:40 2019-06-11
     */
    @Override

    public PageData<AftersaleLogDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * @param aftersaleSn:售后单号
     * @Author: SWH ab4856812@163.com
     * @Description:H5端售后进度
     * @Date: 2019/6/20 14:25
     * @Version: V1.0
     */
    @Override

    public AfterSaleProcessDTO findProcess(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("sort") String sort) {
        AfterSaleProcessDTO afterSaleProcessDTO = new AfterSaleProcessDTO();
        Map<String, Object> logMap = new HashMap<>(1);
        logMap.put("aftersaleSn", aftersaleSn);
        List<AftersaleLogDTO> logList = aftersaleLogDao.findAfterFrontLog(aftersaleSn, sort);
        if (CollectionUtils.isNotEmpty(logList)) {
            afterSaleProcessDTO.setLogList(logList);
            afterSaleProcessDTO.setAftersaleSn(aftersaleSn);
            if (SORT_ASC.equals(sort)) {
                afterSaleProcessDTO.setLastLog(logList.get(logList.size() - 1).getMessage());
                afterSaleProcessDTO.setCreateDate(logList.get(logList.size() - 1).getCreateDate());
            } else {
                afterSaleProcessDTO.setLastLog(logList.get(0).getMessage());
                afterSaleProcessDTO.setCreateDate(logList.get(0).getCreateDate());
            }
        }
        // 查询售后商品数量和图片
        Map<String, Object> queryMap = new HashMap<>(2);
        queryMap.put("id", aftersaleSn);
        queryMap.put("is_gift", 0L);
        AftersaleGoodsSaveDTO afterGoods = aftersaleGoodsService.getAfterGoods(queryMap);
        if (null != afterGoods) {
            afterSaleProcessDTO.setGoodsImage(afterGoods.getSpecMainPicture());
            afterSaleProcessDTO.setGoodsNum(afterGoods.getGoodsNum());
        }
        return afterSaleProcessDTO;
    }

    /**
     * @param aftersaleSn:售后单号
     * @Author: SWH ab4856812@163.com
     * @Description:查询日志状态
     * @Date: 2019/6/20 14:25
     * @Version: V1.0
     */
    @Override

    public AftersaleLogDTO findlogByStatus(@RequestParam("aftersaleSn") Long aftersaleSn, @RequestParam("value") Integer value) {
        return aftersaleLogDao.findlogByStatus(aftersaleSn, value);
    }

}
