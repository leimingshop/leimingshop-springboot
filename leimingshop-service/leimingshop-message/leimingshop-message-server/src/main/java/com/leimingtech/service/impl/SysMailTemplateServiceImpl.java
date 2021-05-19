/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.impl;

import cn.hutool.http.HtmlUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dao.SysMailTemplateDao;
import com.leimingtech.email.EmailUtils;
import com.leimingtech.entity.SysMailTemplateEntity;
import com.leimingtech.message.dto.SysMailTemplateDTO;
import com.leimingtech.message.service.SysMailTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Map;

/**
 * 邮件发送方法实现类
 *
 * @author xuzhch
 * @date 2020年9月16日
 */
@Service

public class SysMailTemplateServiceImpl extends CrudServiceImpl<SysMailTemplateDao, SysMailTemplateEntity, SysMailTemplateDTO> implements SysMailTemplateService {
    @Autowired
    private EmailUtils emailUtils;


    @Override
    public PageData<SysMailTemplateDTO> page(@RequestParam Map<String, Object> params) {
        PageData<SysMailTemplateDTO> page = super.page(params);
        return page;
    }


    @Override
    public SysMailTemplateDTO info(Long id) {
        SysMailTemplateDTO sysMailTemplateDTO = get(id);
        sysMailTemplateDTO.setContent(HtmlUtil.escape(sysMailTemplateDTO.getContent()));
        return get(id);
    }


    @Override
    public boolean saveMailTemplate(@RequestBody SysMailTemplateDTO dto) {
        //校验类型
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);
        dto.setContent(HtmlUtil.unescape(dto.getContent()));
        save(dto);
        return true;

    }


    @Override
    public boolean updateMailTemplate(@RequestBody SysMailTemplateDTO dto) {
        //校验类型
        ValidatorUtils.validateEntity(dto, UpdateGroup.class, DefaultGroup.class);
        dto.setContent(HtmlUtil.unescape(dto.getContent()));
        update(dto);
        return true;
    }


    @Override
    public boolean deleteMailTemplates(@RequestBody Long[] ids) {
        deleteBatchIds(Arrays.asList(ids));
        return true;
    }

    @Override
    public QueryWrapper<SysMailTemplateEntity> getWrapper(Map<String, Object> params) {
        String name = (String) params.get("name");

        QueryWrapper<SysMailTemplateEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), "name", name);

        return wrapper;
    }

    @Override

    public boolean sendMail(@RequestParam("id") Long id, @RequestParam("mailTo") String mailTo, @RequestParam("mailCc") String mailCc, @RequestParam("params") String params) {
        Map<String, Object> map = null;
        try {
            if (StringUtils.isNotEmpty(params)) {
                map = JSON.parseObject(params, Map.class);
            }
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_FORMAT_ERROR);
        }
        String[] to = new String[]{mailTo};
        String[] cc = StringUtils.isBlank(mailCc) ? null : new String[]{mailCc};

        return emailUtils.sendMail(id, to, cc, map);
    }
}
