/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.transport.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dto.setting.SettingSeniorExpressDTO;
import com.leimingtech.enums.SettingsEnum;
import com.leimingtech.modules.dao.transport.TransportMessageDao;
import com.leimingtech.modules.dto.logistic.kuaidi100.RealTimeQueryDTO;
import com.leimingtech.modules.dto.logistic.kuaidi100.RealTimeQueryResponseDTO;
import com.leimingtech.modules.dto.transport.TransportFollowDTO;
import com.leimingtech.modules.dto.transport.TransportMessageDTO;
import com.leimingtech.modules.entity.transport.TransportMessageEntity;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.logistic.kuaidi100.Kuaidi100Service;
import com.leimingtech.modules.service.transport.TransportMessageService;
import com.leimingtech.service.setting.SettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物流信息表
 *
 * @author kuangweiguo kuangweiguo@leimingtech.com
 * @since v1.0.0 2019-08-13
 */
@SuppressWarnings("ALL")
@Service
@Transactional(rollbackFor = Exception.class)

public class TransportMessageServiceImpl extends BaseServiceImpl<TransportMessageDao, TransportMessageEntity> implements TransportMessageService {

    @Autowired
    private SettingService settingService;

    @Autowired
    private Kuaidi100Service kuaidi100Service;

    @Autowired
    private NosqlService nosqlService;

    @Override

    public PageData<TransportMessageDTO> page(@RequestParam Map<String, Object> params) {
        IPage<TransportMessageEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, TransportMessageDTO.class);
    }

    @Override

    public List<TransportMessageDTO> list(Map<String, Object> params) {
        List<TransportMessageEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, TransportMessageDTO.class);
    }

    private QueryWrapper<TransportMessageEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String nu = (String) params.get("nu");

        QueryWrapper<TransportMessageEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(nu), "nu", nu);

        return wrapper;
    }

    @Override

    public TransportMessageDTO get(Long id) {

        TransportMessageEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, TransportMessageDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody TransportMessageDTO dto) {
        TransportMessageEntity entity = new TransportMessageEntity();
        BeanCopier.create(TransportMessageDTO.class, TransportMessageEntity.class, false).copy(dto, entity, null);
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody TransportMessageDTO dto) {
        TransportMessageEntity entity = new TransportMessageEntity();
        BeanCopier.create(TransportMessageDTO.class, TransportMessageEntity.class, false).copy(dto, entity, null);

        updateById(entity);
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 查询正在投递中的物流单号
     * @Date: 2019/8/13 22:10
     * @Version: V1.0
     */
    @Override

    public List<TransportMessageDTO> progressList() {
        return baseDao.getProgressList();
    }

    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 批量修改数据
     * @Date: 2019/8/14 9:50
     * @Version: V1.0
     */
    @Override

    public Boolean updateData(@RequestBody List<TransportMessageDTO> transportMessageDTOList) {
        Boolean result = true;

        String queryRedisByName = settingService.queryRedisByName(SettingsEnum.EXPRESS.value());
        SettingSeniorExpressDTO expressSet = JSON.parseObject(queryRedisByName, SettingSeniorExpressDTO.class);
        if (null == expressSet) {
            return false;
        }
        for (TransportMessageDTO transportMessageDTO : transportMessageDTOList) {
            // 调用快递一百接口查询当前快递单数据
            RealTimeQueryDTO realTimeQueryDTO = new RealTimeQueryDTO();
            realTimeQueryDTO.setCom(transportMessageDTO.getCom());
            realTimeQueryDTO.setNum(transportMessageDTO.getNu());
            realTimeQueryDTO.setCustomer(expressSet.getExpressCustomer());
            realTimeQueryDTO.setKey(expressSet.getExpressKey());
            RealTimeQueryResponseDTO realtime = kuaidi100Service.realtime(realTimeQueryDTO);
            if (null == realtime) {
                continue;
            }

            // 修改数据库物流信息表
            TransportMessageEntity entity = new TransportMessageEntity();
            entity.setId(transportMessageDTO.getId());
            entity.setIscheck(realtime.getIscheck());
            entity.setState(realtime.getState());
            int updateResult = baseDao.updateById(entity);
            if (updateResult == 0) {
                continue;
            }

            // 保存mongodb信息
            Map<String, Object> map = new HashMap<>(7);
            map.put("_id", realtime.getNu());
            map.put("nu", realtime.getNu());
            map.put("companyName", transportMessageDTO.getComanyName());
            map.put("companyPhone", transportMessageDTO.getComanyPhone());
            map.put("state", realtime.getState());
            map.put("isCheck", realtime.getIscheck());
            map.put("data", realtime.getData());
            nosqlService.saveData("logistics_message", map);
        }

        return result;
    }

    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    @Override

    public TransportFollowDTO findMongoLogisticsByNu(String logisticsNo) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id", logisticsNo);
        List<String> logisticsMessage = nosqlService.queryData("logistics_message", map);
        TransportFollowDTO transportFollowDTO = new TransportFollowDTO();
        if (CollectionUtils.isNotEmpty(logisticsMessage)) {
            transportFollowDTO = JSONObject.parseObject(logisticsMessage.get(0), TransportFollowDTO.class);
        }
        return transportFollowDTO;
    }

}
