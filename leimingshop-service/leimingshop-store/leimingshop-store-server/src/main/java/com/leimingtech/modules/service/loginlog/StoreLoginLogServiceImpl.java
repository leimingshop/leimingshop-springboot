/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.loginlog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.loginlog.StoreLoginLogDao;
import com.leimingtech.modules.dto.loginlog.StoreLoginLogDTO;
import com.leimingtech.modules.entity.loginlog.StoreLoginLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 店铺登录日志
 * @Date :2019/6/28 10:24
 * @Version V1.0
 **/
@Service
@Transactional(rollbackFor = Exception.class)

public class StoreLoginLogServiceImpl extends BaseServiceImpl<StoreLoginLogDao, StoreLoginLogEntity> implements StoreLoginLogService {
    /**
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/6/28 10:27
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override

    public PageData<StoreLoginLogDTO> page(@RequestParam Map<String, Object> params) {

        IPage<StoreLoginLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, StoreLoginLogDTO.class);
    }

    /**
     * @Author: weixianchun
     * @Description: 导出日志信息
     * @Date :2019/6/28 10:33
     * @Param params 查询条件
     * @Version V1.0
     **/
    @Override

    public List<StoreLoginLogDTO> list(@RequestParam Map<String, Object> params) {
        List<StoreLoginLogEntity> entityList = this.baseDao.selectList(this.getWrapper(params));
        return ConvertUtils.sourceToTarget(entityList, StoreLoginLogDTO.class);
    }

    /**
     * 功能描述:
     * <构造器>
     *
     * @param params
     * @return
     * @date 2020/1/14 16:07
     * @author weixianchun
     **/
    private QueryWrapper<StoreLoginLogEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        //状态  0：失败    1：成功    2：账号已锁定
        String status = (String) params.get("status");
        //用户操作
        String operation = (String) params.get("operation");
        //店铺id
        String storeId = (String) params.get("storeId");
        //用户名
        String creator = (String) params.get("creator");
        //开始时间
        String startDate = (String) params.get("startDate");
        //结束时间
        String endDate = (String) params.get("endDate");

        QueryWrapper<StoreLoginLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);
        wrapper.eq(StringUtils.isNotBlank(storeId), "store_id", storeId);
        wrapper.like(StringUtils.isNotBlank(operation), "operation", operation);
        wrapper.like(StringUtils.isNotBlank(creator), "creator", creator);
        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            startDate = startDate + " 00:00:00";
            endDate = endDate + " 23:59:59";
            params.put("startDate", startDate);
            params.put("endTime", endDate);
        }
        return wrapper;
    }
}
