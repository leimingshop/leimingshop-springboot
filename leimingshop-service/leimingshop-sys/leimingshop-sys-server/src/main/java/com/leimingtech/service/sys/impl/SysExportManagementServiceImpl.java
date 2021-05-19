/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.sys.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.sys.SysExportManagementDao;
import com.leimingtech.dto.excel.ImportErrorExcel;
import com.leimingtech.dto.sys.SysExportManagementDTO;
import com.leimingtech.dto.sys.SysExportManagementVO;
import com.leimingtech.entity.sys.SysExportManagementPO;
import com.leimingtech.service.sys.SysExportManagementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 导入导出管理
 *
 * @author 刘远杰
 * @since v1.0.0 2019-11-14
 */
@Service
@Transactional

@Slf4j
public class SysExportManagementServiceImpl extends BaseServiceImpl<SysExportManagementDao, SysExportManagementPO> implements SysExportManagementService {

    @Autowired

    private SysUserServiceImpl sysUserService;

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */
    @Override

    public PageData<SysExportManagementVO> page(@RequestParam Map<String, Object> params) {

        IPage<SysExportManagementPO> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, SysExportManagementVO.class);
    }

    /**
     * 获取结果集
     *
     * @param params
     * @return
     */
    @Override

    public List<SysExportManagementVO> list(@RequestParam Map<String, Object> params) {
        List<SysExportManagementPO> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, SysExportManagementVO.class);
    }

    /**
     * 构造查询条件
     *
     * @param params
     * @return
     */
    private QueryWrapper<SysExportManagementPO> getWrapper(Map<String, Object> params) {
        QueryWrapper<SysExportManagementPO> wrapper = new QueryWrapper<>();
        String assignmentName = (String) params.get("assignmentName");
        String operationStatus = (String) params.get("operationStatus");
        if (params.get("storeId") != null && !"".equals(params.get("storeId").toString())) {
            wrapper.eq(true, "store_id", params.get("storeId").toString());
        }
        if (params.get("creator") != null && !"".equals(params.get("creator").toString())) {
            wrapper.eq(true, "creator", params.get("creator").toString());
        }

        wrapper.like(StringUtils.isNotBlank(assignmentName), "assignment_name", assignmentName);
        wrapper.eq(StringUtils.isNotBlank(operationStatus), "operation_status", operationStatus);
        if (params.get("id") != null) {
            Long id = Long.parseLong(params.get("id").toString());
            wrapper.eq(true, "id", id);
        }
        return wrapper;
    }

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     */
    @Override

    public SysExportManagementVO get(Long id) {
        SysExportManagementPO po = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(po, SysExportManagementVO.class);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)

    public SysExportManagementVO save(@RequestBody SysExportManagementDTO dto) {
        SysExportManagementPO po = ConvertUtils.sourceToTarget(dto, SysExportManagementPO.class);
        po.setCreateDate(new Date());
        po.setUpdateDate(new Date());
        insert(po);
        return ConvertUtils.sourceToTarget(po, SysExportManagementVO.class);
    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public Boolean update(@RequestBody SysExportManagementDTO dto) {
        SysExportManagementPO po = ConvertUtils.sourceToTarget(dto, SysExportManagementPO.class);
        po.setUpdateDate(new Date());

        return updateById(po);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 查询导入失败数据
     *
     * @param sysManagerId 导入导出管理id
     * @return
     */

    @Override
    public List<ImportErrorExcel> errorExportList(@RequestParam(value = "sysManagerId") Long sysManagerId) {
        return baseDao.errorExportListById(sysManagerId);
    }


    /**
     * 批量插入
     *
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void saveBach(@RequestBody List<SysExportManagementDTO> list) {
        List<SysExportManagementPO> dto = ConvertUtils.sourceToTarget(list, SysExportManagementPO.class);
        //分批处理
        if (null != dto && dto.size() > 0) {
            int pointsDataLimit = 1000;//限制条数
            Integer size = dto.size();
            //判断是否有必要分批
            if (pointsDataLimit < size) {
                //获取分批数是多少
                int part = size / pointsDataLimit;
                log.info("批量保存数据{}共有 ： " + size + "条，！" + " 分为 ：" + part + "批");
                for (int i = 0; i < part; i++) {
                    List<SysExportManagementPO> listPage = dto.subList(0, pointsDataLimit);
                    //1000条数据入库
                    baseDao.saveBach(listPage);
                    //剔除已入库的数据
                    dto.subList(0, pointsDataLimit).clear();
                }
            }
            if (!dto.isEmpty()) {
                baseDao.saveBach(dto);
            }
        }
    }

    /**
     * 批量修改实体
     *
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void updateBach(@RequestBody List<SysExportManagementDTO> list) {
        List<SysExportManagementPO> dto = ConvertUtils.sourceToTarget(list, SysExportManagementPO.class);
        //分批处理
        if (null != dto && dto.size() > 0) {
            int pointsDataLimit = 1000;//限制条数
            Integer size = dto.size();
            //判断是否有必要分批
            if (pointsDataLimit < size) {
                //获取分批数是多少
                int part = size / pointsDataLimit;
                log.info("批量修改数据{}共有 ： " + size + "条，！" + " 分为 ：" + part + "批");
                for (int i = 0; i < part; i++) {
                    List<SysExportManagementPO> listPage = dto.subList(0, pointsDataLimit);
                    //1000条数据入库
                    baseDao.updateBach(listPage);
                    //剔除已入库的数据
                    dto.subList(0, pointsDataLimit).clear();
                }
            }
            if (!dto.isEmpty()) {
                baseDao.updateBach(dto);
            }
        }
    }
}
