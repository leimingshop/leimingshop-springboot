/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dao.SysMailLogDao;
import com.leimingtech.entity.SysMailLogEntity;
import com.leimingtech.message.dto.SysMailLogDTO;
import com.leimingtech.message.service.SysMailLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.Map;

/**
 * 邮件发送记录方法实现类
 *
 * @author xuzhch
 * @date 2020年9月16日
 */
@Service

public class SysMailLogServiceImpl extends BaseServiceImpl<SysMailLogDao, SysMailLogEntity> implements SysMailLogService {


    @Override
    public PageData<SysMailLogDTO> page(Map<String, Object> params) {
        IPage<SysMailLogEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        return getPageData(page, SysMailLogDTO.class);
    }

    private QueryWrapper<SysMailLogEntity> getWrapper(Map<String, Object> params) {
        String templateId = (String) params.get("templateId");
        String mailTo = (String) params.get("mailTo");
        String status = (String) params.get("status");

        QueryWrapper<SysMailLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(templateId), "template_id", templateId);
        wrapper.like(StringUtils.isNotBlank(mailTo), "mail_to", mailTo);
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);

        return wrapper;
    }

    public void save(Long templateId, String from, String[] to, String[] cc, String subject, String content, Integer status) {
        SysMailLogEntity log = new SysMailLogEntity();
        log.setTemplateId(templateId);
        log.setMailFrom(from);
        log.setMailTo(JSON.toJSONString(to));
        if (cc != null) {
            log.setMailCc(JSON.toJSONString(cc));
        }
        log.setSubject(subject);
        log.setContent(content);
        log.setStatus(status);
        this.insert(log);
    }


    @Override
    public Boolean deleteBatchIds(@RequestBody Long[] ids) {

        deleteBatchIds(Arrays.asList(ids));
        return true;
    }
}
