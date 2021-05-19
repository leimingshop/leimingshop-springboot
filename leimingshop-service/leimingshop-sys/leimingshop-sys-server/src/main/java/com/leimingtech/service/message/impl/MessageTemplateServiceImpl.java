/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.message.MessageTemplateDao;
import com.leimingtech.dto.message.FindMessageTemplateDTO;
import com.leimingtech.dto.message.ShopMessageListVO;
import com.leimingtech.dto.message.ShopMessageTemplateVO;
import com.leimingtech.entity.message.MessageTemplateEntity;
import com.leimingtech.service.message.MessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 消息模板配置表
 *
 * @author tyl@leimingtech.com
 * @since v1.0.0 2019-08-22
 */
@Service
@Transactional

public class MessageTemplateServiceImpl extends BaseServiceImpl<MessageTemplateDao, MessageTemplateEntity> implements MessageTemplateService {

    @Autowired
    private MessageTemplateDao messageTemplateDao;

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     */
    @Override

    public PageData<ShopMessageTemplateVO> page(@RequestParam Map<String, Object> params) {
        IPage<MessageTemplateEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ShopMessageTemplateVO.class);
    }

    /**
     * 获取结果集
     *
     * @param params
     * @return
     */
    @Override

    public List<ShopMessageTemplateVO> list(Map<String, Object> params) {
        List<MessageTemplateEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ShopMessageTemplateVO.class);
    }

    /**
     * 无分页消息列表
     *
     * @return
     */
    @Override

    public List<ShopMessageListVO> listNoPage() {
        return messageTemplateDao.listNoPage();
    }

    private QueryWrapper<MessageTemplateEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<MessageTemplateEntity> wrapper = new QueryWrapper<>();
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

    public ShopMessageTemplateVO get(Long id) {
        MessageTemplateEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ShopMessageTemplateVO.class);
    }

    @Override

    public Map<String, Object> getTemplate(Long id) {
        List<String> labelList = messageTemplateDao.selectLabel();
        Map<String, Object> resultMap = new HashMap<>(2);

        resultMap.put("labelList", labelList);
        MessageTemplateEntity shopMessageTemplateVO = baseDao.selectOne(new QueryWrapper<MessageTemplateEntity>().eq("message_type_id", id));
        if (null != shopMessageTemplateVO) {
            resultMap.put("messageTypeName", shopMessageTemplateVO.getTempTitle());
            resultMap.put("hasTemplate", "true");
            resultMap.put("messAgeTemplate", shopMessageTemplateVO);
        } else {
            resultMap.put("hasTemplate", "false");
        }
        return resultMap;
    }


    /**
     * 保存
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody ShopMessageTemplateVO dto) {

        insert(ConvertUtils.sourceToTarget(dto, MessageTemplateEntity.class));
    }

    /**
     * 模板新增或更新
     *
     * @param params
     */
    @Override

    public void saveOrUpdate(@RequestParam Map<String, Object> params) {
        //模板类型id判断存不存在模板
        Long messageId = Long.parseLong(params.get("messageId").toString());
        int count = messageTemplateDao.selectCount(messageId);
        if (count > 0) {
            //有这个模板，更新 0 站内信  1 APP推送 2 短信
            messageTemplateDao.updateByMessageId(params);
        } else {
            //新增  0 站内信  1 APP推送 2 短信
            params.put("id", IdWorker.getId());
            String templateType = params.get("templateType").toString();
            if ("0".equals(templateType)) {
                messageTemplateDao.save(params);
            } else if ("1".equals(templateType)) {
                messageTemplateDao.saveApp(params);
            } else {
                messageTemplateDao.saveSms(params);
            }
        }

    }

    /**
     * 修改
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody ShopMessageTemplateVO dto) {
        updateById(ConvertUtils.sourceToTarget(dto, MessageTemplateEntity.class));
    }

    /**
     * 修改发送短信
     *
     * @param map
     */
    @Override

    public void updateIsSend(@RequestParam Map<String, Object> map) {
        messageTemplateDao.updateIsSend(map);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, ShopMessageTemplateEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }


    /**
     * 根据code获取消息模板
     *
     * @param code: 消息code
     * @return 消息模板
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public FindMessageTemplateDTO getMessageByCode(@RequestParam("code") String code) {
        MessageTemplateEntity messageTemplateEntity = Optional.ofNullable(baseDao.selectOne(Wrappers.<MessageTemplateEntity>lambdaQuery()
                .eq(MessageTemplateEntity::getMessageTypeId, code)))
                .orElse(new MessageTemplateEntity());

        FindMessageTemplateDTO findMessageTemplateDTO = new FindMessageTemplateDTO();
        BeanCopier.create(MessageTemplateEntity.class, FindMessageTemplateDTO.class, false)
                .copy(messageTemplateEntity, findMessageTemplateDTO, null);
        return findMessageTemplateDTO;
    }

}
