/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.audit.impl;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.modules.dao.article.CmsArticleDao;
import com.leimingtech.modules.dao.audit.CmsAuditDao;
import com.leimingtech.modules.dto.audit.CmsAuditDTO;
import com.leimingtech.modules.dto.audit.CmsAuditSaveDTO;
import com.leimingtech.modules.dto.remind.CmsRemindMqDTO;
import com.leimingtech.modules.entity.article.CmsArticleEntity;
import com.leimingtech.modules.entity.audit.CmsAuditEntity;
import com.leimingtech.modules.enums.remind.RemindTypeEnum;
import com.leimingtech.modules.service.audit.CmsAuditService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 审核记录管理 CmsAuditServiceImpl
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2020-03-24
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsAuditServiceImpl extends BaseServiceImpl<CmsAuditDao, CmsAuditEntity> implements CmsAuditService {

    @Resource
    private CmsArticleDao cmsArticleDao;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    /**
     * 审核结果详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsAuditDTO get(Long id) {
        CmsAuditDTO auditList = baseDao.selectAuditInfo(id);
        return auditList;
    }

    /**
     * 新增审核结果
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CmsAuditSaveDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity articleEntity = new CmsArticleEntity();
        articleEntity.setId(dto.getArticleId());
        articleEntity.setAudit(dto.getAudit());
        articleEntity.setAuditDate(date);
        cmsArticleDao.updateById(articleEntity);
        CmsAuditEntity auditEntity = new CmsAuditEntity();
        auditEntity.setArticleId(dto.getArticleId());
        auditEntity.setAuditResult(dto.getAuditResult());
        auditEntity.setCreateDate(date);
        auditEntity.setUpdateDate(date);
        auditEntity.setCreator(userName);
        auditEntity.setUpdater(userName);
        insert(auditEntity);

        //文章审核通过，才会向用户发送艾特的消息
        if (1 == dto.getAudit()) {
            CmsArticleEntity article = cmsArticleDao.selectById(dto.getArticleId());
            if (StringUtils.isNotBlank(article.getRemind())) {
                CmsRemindMqDTO cmsRemindMqDTO = new CmsRemindMqDTO();
                cmsRemindMqDTO.setRemindType(RemindTypeEnum.ARTICLE_CODE.value());
                cmsRemindMqDTO.setRemindTypeId(article.getId());
                cmsRemindMqDTO.setMemberIdList(article.getRemind());
                cmsRemindMqDTO.setCreatorId(article.getArticleCreaterId());
                String message = JSONObject.toJSONString(cmsRemindMqDTO);
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CMS_REMIND_MESSAGE_QUEUE, message);
            }
        }
    }

}
