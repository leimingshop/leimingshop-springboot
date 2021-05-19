//
//package com.leimingtech.service.impl;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.baomidou.mybatisplus.core.toolkit.Wrappers;
//import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
//import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;

//import com.leimingtech.commons.tools.constant.Constant;
//import com.leimingtech.commons.tools.page.PageData;
//import com.leimingtech.commons.tools.utils.ConvertUtils;
//import com.leimingtech.dao.MessageUnsubscribeDao;
//import com.leimingtech.entity.MessageUnsubscribeEntity;
//import com.leimingtech.enums.message.MessageEnum;
//import com.leimingtech.message.dto.AliSmsUpInfoDTO;
//import com.leimingtech.message.dto.MessageUnsubscribeDTO;
//import com.leimingtech.message.service.MessageUnsubscribeService;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * 短信消息退订表
// *
// * @author kuangweiguo xuzhch@leimingtech.com
// * @since v1.0.0 2020-07-28
// */
//@Service
//@Transactional
//
//@Slf4j
//public class MessageUnsubscribeServiceImpl extends BaseServiceImpl<MessageUnsubscribeDao, MessageUnsubscribeEntity> implements MessageUnsubscribeService {
//
//    @Override
//
//    public PageData<MessageUnsubscribeDTO> page(@RequestParam Map<String, Object> params) {
//        IPage<MessageUnsubscribeEntity> page = baseDao.selectPage(
//                getPage(params, Constant.UPDATE_DATE, false),
//                getWrapper(params)
//        );
//
//        return getPageData(page, MessageUnsubscribeDTO.class);
//    }
//
//    @Override
//
//    public List<MessageUnsubscribeDTO> list(Map<String, Object> params) {
//        List<MessageUnsubscribeEntity> entityList = baseDao.selectList(getWrapper(params));
//
//        return ConvertUtils.sourceToTarget(entityList, MessageUnsubscribeDTO.class);
//    }
//
//    private QueryWrapper<MessageUnsubscribeEntity> getWrapper(Map<String, Object> params) {
//        String id = (String) params.get("id");
//
//        QueryWrapper<MessageUnsubscribeEntity> wrapper = new QueryWrapper<>();
//        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
//        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
//
//        return wrapper;
//    }
//
//    @Override
//
//    public MessageUnsubscribeDTO get( Long id) {
//        MessageUnsubscribeEntity entity = baseDao.selectById(id);
//
//        return ConvertUtils.sourceToTarget(entity, MessageUnsubscribeDTO.class);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//
//    public void save(@RequestBody MessageUnsubscribeDTO dto) {
//        MessageUnsubscribeEntity entity = ConvertUtils.sourceToTarget(dto, MessageUnsubscribeEntity.class);
//
//        insert(entity);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//
//    public void update(@RequestBody MessageUnsubscribeDTO dto) {
//        MessageUnsubscribeEntity entity = ConvertUtils.sourceToTarget(dto, MessageUnsubscribeEntity.class);
//
//        updateById(entity);
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//
//    public void delete(@RequestBody Long[] ids) {
//        //逻辑删除
//        //logicDelete(ids, MessageUnsubscribeEntity.class);
//
//        //物理删除
//        baseDao.deleteBatchIds(Arrays.asList(ids));
//    }
//
//    /**
//     * 获取SmsUp（上行短信消息）
//     * 上行短信指用户发送给通信服务提供商的短信，用于定制某种服务、完成某种查询、或是办理某种业务等。
//     * 通过订阅SmsReport短信下行状态报告，可以获知每条短信的发送情况，了解短信是否达到终端用户的状态与相关信息。
//     * 采用通过HTTP批量推送方式可以拉取上行短信消息（SmsUp）
//     */
//
//    @Override
//    public void smsUpInfoGet(@RequestBody List<AliSmsUpInfoDTO> aliSmsUpInfoDTOS) {
//        log.info("接收到上行短信消息，短信内容为{}", aliSmsUpInfoDTOS);
//        if (CollectionUtils.isEmpty(aliSmsUpInfoDTOS)) {
//            return;
//        }
//        //先根据手机号查库。获取数据库黑名单数据
//        Set<String> mobileSet = aliSmsUpInfoDTOS.stream().map(AliSmsUpInfoDTO::getPhoneNumber).collect(Collectors.toSet());
//        List<MessageUnsubscribeEntity> entityList = baseDao.selectList(Wrappers.<MessageUnsubscribeEntity>lambdaQuery()
//                .in(MessageUnsubscribeEntity::getMemberMobile, mobileSet)
//                .eq(MessageUnsubscribeEntity::getUnsubscribeFlag, MessageEnum.UNSUBSCRIBE.value())
//                .eq(MessageUnsubscribeEntity::getDelFlag, DelFlagEnum.NORMAL.value()));
//        Map<String, Integer> mobileMap = entityList.stream().collect(Collectors.toMap(MessageUnsubscribeEntity::getMemberMobile, MessageUnsubscribeEntity::getUnsubscribeFlag));
//        List<MessageUnsubscribeEntity> entities = new ArrayList<>();
//        for (AliSmsUpInfoDTO aliSmsUpInfoDTO : aliSmsUpInfoDTOS) {
//            String phoneNumber = aliSmsUpInfoDTO.getPhoneNumber();
//            String content = aliSmsUpInfoDTO.getContent();
//            if (content.equals("TD") && null == mobileMap.get(phoneNumber)) {
//                MessageUnsubscribeEntity entity = new MessageUnsubscribeEntity();
//                entity.setMemberMobile(phoneNumber);
//                entity.setUnsubscribeFlag(MessageEnum.UNSUBSCRIBE.value());
//                entities.add(entity);
//            }
//        }
//        if (CollectionUtils.isNotEmpty(entities)) {
//            super.insertBatch(entities);
//        }
//    }
//
//    /**
//     * 查询黑名单
//     *
//     * @param mobile
//     * @param flag
//     * @return
//     */
//
//    @Override
//    public MessageUnsubscribeDTO selectBlacklistByMobile(@RequestParam("mobile") String mobile,
//                                                         @RequestParam(required = false, name = "messageTypeId") Long messageTypeId,
//                                                         @RequestParam(required = false, name = "flag") Integer flag) {
//        MessageUnsubscribeEntity messageUnsubscribeEntity = baseDao.selectOne(Wrappers.<MessageUnsubscribeEntity>lambdaQuery()
//                .eq(MessageUnsubscribeEntity::getMemberMobile, mobile)
//                .eq(MessageUnsubscribeEntity::getDelFlag, DelFlagEnum.NORMAL.value())
//                .eq(messageTypeId != null, MessageUnsubscribeEntity::getUnsubscribeFlag, messageTypeId)
//                .eq(flag != null, MessageUnsubscribeEntity::getUnsubscribeFlag, flag));
//        return BeanUtil.isEmpty(messageUnsubscribeEntity) ? null : ConvertUtils.sourceToTarget(messageUnsubscribeEntity, MessageUnsubscribeDTO.class);
//
//
//    }
//}
