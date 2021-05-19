/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.message.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.message.MessageTextDao;
import com.leimingtech.dto.message.*;
import com.leimingtech.entity.message.MessageTextEntity;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.modules.aftersale.service.AftersaleReturnService;
import com.leimingtech.modules.constants.mongodb.CollectionName;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.member.MessageMemberReceiverDTO;
import com.leimingtech.modules.dto.store.MessageStoreReceiverDTO;
import com.leimingtech.modules.dto.warning.StorageWarningDTO;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.warning.StorageWarningService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.message.MessageTextService;
import com.leimingtech.statuscode.MessageStatusCode;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * 站内信内容表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Slf4j
@Service

public class MessageTextServiceImpl extends CrudServiceImpl<MessageTextDao, MessageTextEntity, MessageTextDTO> implements MessageTextService {

    @Autowired
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AftersaleReturnService aftersaleReturnService;
    @Autowired
    private StorageWarningService storageWarningService;
    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private NosqlService nosqlService;

    @Override
    public QueryWrapper<MessageTextEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<MessageTextEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        if (params.get("messageTitle") != null) {
            String messageTitle = params.get("messageTitle").toString();
            wrapper.like(true, "message_title", messageTitle);
        }
        if (params.get("messageType") != null) {
            String messageType = params.get("messageType").toString();
            wrapper.eq(true, "message_type", messageType);
        }
        if (params.get("sendMode") != null) {
            String sendMode = params.get("sendMode").toString();
            wrapper.eq(true, "send_mode", sendMode);
        }

        return wrapper;
    }


    /**
     * 保存站内信
     *
     * @param messageTextDTO
     * @return
     */
    @Override

    public void saveMessage(@RequestBody MessageTextDTO messageTextDTO) {
        //数据校验，如果是私信，并且用户信息与店铺信息为空则抛异常
        List<MessageMemberReceiverDTO> memberList = messageTextDTO.getMemberList();
        List<MessageStoreReceiverDTO> storeList = messageTextDTO.getStoreList();
        if (MessageCodeEnum.MESSAGE_TYPE_PRVT.value().equals(messageTextDTO.getMessageType()) && CollectionUtils.isEmpty(memberList) && CollectionUtils.isNotEmpty(storeList)) {
            throw new ServiceException(MessageStatusCode.SEND_NO_SELECT_FAILED);
        }

        // 替换占位符参数，保存站内信
        if (messageTextDTO.getParamsMap() != null) {
            messageTextDTO.setMessageContent(replaceParams(messageTextDTO.getMessageContent(), messageTextDTO.getParamsMap()));
        }
        MessageTextEntity messageTextEntity = ConvertUtils.sourceToTarget(messageTextDTO, MessageTextEntity.class);
        messageTextEntity.setSendTime(new Date());
        messageTextEntity.setCreator(messageTextDTO.getUserName());
        baseDao.insert(messageTextEntity);
//        MessageMongoDTO messageMongoDTO = new MessageMongoDTO();
//        messageMongoDTO.setId(IdGenerator.defaultSnowflakeId());
//        messageMongoDTO.setMessageCode(messageTextEntity.getMessageType() + "");
//        messageMongoDTO.setMessageSendType("0");
//        messageMongoDTO.setSendName(messageTextEntity.getCreator());
//        messageMongoDTO.setSendTime(messageTextEntity.getCreateDate());
//        messageMongoDTO.setMessageSource(0);
//        messageMongoDTO.setParamJson(JSON.toJSONString(messageTextDTO.getParamsMap()));
//        nosqlService.saveObj(messageMongoDTO,CollectionName.MESSAGE_RECORD);
        //封装数据，保存接收人信息
        Map<String, Object> map = new HashMap();
        map.put("userId", messageTextDTO.getUserId());
        map.put("userName", messageTextDTO.getUserName());
        map.put("receiverType", messageTextDTO.getReceiverType());
        map.put("messageTextEntity", messageTextEntity);
        map.put("memberList", memberList);
        map.put("storeList", storeList);
        String message = JSONObject.toJSONString(map);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_MESSAGE_QUEUE, message);

    }

    /**
     * 分页查询站内信列表
     *
     * @param params
     * @return
     */
    @Override

    public PageData<MessageTextListDTO> pageMessage(@RequestParam Map<String, Object> params) {
        IPage<MessageTextEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getMessageWrapper(params)
        );
        return getPageData(page, MessageTextListDTO.class);
    }

    /**
     * 分页方法
     *
     * @param params
     * @return
     */
    @Override

    public PageData<MessageMongoDTO> pageList(@RequestParam Map<String, Object> params) {

        Integer page = Integer.valueOf(String.valueOf(params.get("page")));
        Integer limit = Integer.valueOf(String.valueOf(params.get("limit")));
        Object messageSendType = params.get("messageSendType");
        Object messageCode = params.get("messageCode");
        Object messageTitle = params.get("messageTitle");
        Query query = new Query();
        query.skip(page);
        query.limit(limit);
        Criteria sendModeCriteria = messageSendType != null ? Criteria.where("messageSendType").is(String.valueOf(messageSendType)) : null;
        Criteria messageTypeCriteria = messageCode != null ? Criteria.where("messageCode").is(String.valueOf(messageCode)) : null;
        Criteria messageTitleCriteria = messageTitle != null ? Criteria.where("messageTitle").is(String.valueOf(messageTitle)) : null;
        if (null != sendModeCriteria) {
            query.addCriteria(sendModeCriteria);
        }
        if (null != messageTypeCriteria) {
            query.addCriteria(messageTypeCriteria);
        }
        if (null != messageTitleCriteria) {
            query.addCriteria(messageTitleCriteria);
        }
        query.addCriteria(Criteria.where("delFlag").ne(1));
        query.with(Sort.by("sendTime").descending());
        long count = mongoTemplate.count(query, MessageMongoDTO.class, CollectionName.MESSAGE_RECORD);
        List<MessageMongoDTO> mongoDTOList = mongoTemplate.find(query, MessageMongoDTO.class, CollectionName.MESSAGE_RECORD);
        return new PageData<MessageMongoDTO>(mongoDTOList, count);

    }

    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    @Override

    public MessageTextDTO get(Long id) {
        MessageTextEntity messageTextEntity = baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(messageTextEntity, MessageTextDTO.class);
    }

    /**
     * 修改站内信信息
     *
     * @param dto
     */
    @Override

    public void update(@RequestBody MessageTextDTO dto) {
        super.update(dto);
    }


    /**
     * 删除
     *
     * @param ids
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        UpdateResult updateResult = mongoTemplate.updateMulti(Query.query(Criteria.where("_id").in(ids)),
                Update.update("delFlag", 1), CollectionName.MESSAGE_RECORD);

        Long removeCount = updateResult.getModifiedCount();
        log.info("删除消息{}条", removeCount);
//        super.delete(ids);
    }

    /**
     * 根据接收人id获取站内信列表
     *
     * @param params
     * @return
     */

    @Override
    public PageData<MessageTextListDTO> pageByReceiveId(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<MessageTextListDTO> dtoPage = new Page<>(pageNum, limit);
        List<MessageTextListDTO> page = baseDao.selectMessageList(
                dtoPage,
                params
        );
        return new PageData(page, dtoPage.getTotal());
    }

    /**
     * 封装参数
     *
     * @param params
     * @return
     */
    private QueryWrapper<MessageTextEntity> getMessageWrapper(Map<String, Object> params) {
        String messageTitle = (String) params.get("messageTitle");
        String strTime = (String) params.get("strTime");
        String endTime = (String) params.get("endTime");
        String messageTypeStr = (String) params.get("messageType");
        String creator = (String) params.get("creator");
        Integer messageType = null;
        if (StringUtils.isNotBlank(messageTypeStr)) {
            messageType = Integer.valueOf(messageTypeStr);
        }

        QueryWrapper<MessageTextEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(messageTitle), "message_title", messageTitle);
        wrapper.ge(StringUtils.isNotBlank(strTime), "send_time", strTime);
        wrapper.le(StringUtils.isNotBlank(endTime), "send_time", endTime + " 23:59:59");
        wrapper.eq(StringUtils.isNotBlank(messageTypeStr), "message_type", messageType);
        wrapper.eq(StringUtils.isNotBlank(creator), "creator", creator);

        return wrapper;
    }


    /**
     * 数据处理,封装100手机号为一个数组,每个数组放入list中
     *
     * @param memberEntityList
     * @return
     */
    private List<ArrayList<String>> dateProcessing(List<MemberDTO> memberEntityList) {
        List<ArrayList<String>> phoneList = new ArrayList<>();
        int num = memberEntityList.size() / 100;
        for (int i = 0; i <= num + 1; i++) {
            ArrayList<String> tel = new ArrayList<>(100);
            int size = (memberEntityList.size() - i * 100) >= 100 ? i + 100 : memberEntityList.size() - 1;
            for (int j = i * 100; j <= size; j++) {
                if (StringUtils.isNoneBlank(memberEntityList.get(j).getMemberMobile())) {
                    String memberMobile = memberEntityList.get(j).getMemberMobile();
                    tel.add(memberMobile);
                }
            }
            //防止数据不够100,多保存空值
            if (!tel.isEmpty()) {
                phoneList.add(tel);
            }
        }
        return phoneList;
    }

    /**
     * 获取短信开关
     *
     * @param type 类型  0 站内信 1 短信
     * @param code 消息类型
     * @return
     */

    @Override
    public FindMessageTemplateDTO getMessageNo(@RequestParam("type") Integer type, @RequestParam("code") String code) {

        FindMessageTemplateDTO messageNo = baseDao.getMessageNo(type, code);
        return messageNo;
    }


    /**
     * 根据ID获取信息内容
     */

    @Override
    public MessageMongoDTO getMessageInfo(@RequestParam("id") Long id) {
        MessageMongoDTO messageTextDTO = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)), MessageMongoDTO.class, CollectionName.MESSAGE_RECORD);
//        QueryWrapper<MessageTextEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", id);
//        MessageTextEntity messageTextEntity = baseDao.selectOne(queryWrapper);
        return messageTextDTO;
    }


    /**
     * @param params:    待替换文本
     * @param paramsMap: 替换数据的Map
     * @return 替换后的数据
     * @date 2020/4/9 17:53
     * @author lixiangx@leimingtech.com
     **/
    private String replaceParams(String params, Map<String, String> paramsMap) {
        if (paramsMap.get("sellerName") != null) {
            params = params.replace("$sellerName$", paramsMap.get("sellerName"));
        }
        if (paramsMap.get("orderSn") != null) {
            params = params.replace("$orderSn$", paramsMap.get("orderSn"));
        }
        if (paramsMap.get("Username") != null) {
            params = params.replace("$Username$", paramsMap.get("Username"));
        }
        if (paramsMap.get("goodsName") != null) {
            params = params.replace("$goodsName$", paramsMap.get("goodsName"));
        }
        if (paramsMap.get("number") != null) {
            params = params.replace("$number$", paramsMap.get("number"));
        }
        if (paramsMap.get("code") != null) {
            params = params.replace("$code$", paramsMap.get("code"));
        }
        if (paramsMap.get("cause") != null) {
            params = params.replace("$cause$", paramsMap.get("cause"));
        }
        if (paramsMap.get("goodsId") != null) {
            params = params.replace("$goodsId$", paramsMap.get("goodsId"));
        }
        if (paramsMap.get("storeName") != null) {
            params = params.replace("$storeName$", paramsMap.get("storeName"));
        }
        return params;
    }

    /**
     * 查询seller端站内信详情
     *
     * @param receiveInfoId 站内信接收人表ID
     * @return
     */

    @Override
    public SellerMessageTextDTO sellerMessageInfo(@RequestParam("receiveInfoId") Long receiveInfoId) {

        SellerMessageTextDTO sellerMessageTextDTO = baseDao.sellerMessageInfo(receiveInfoId);
        // 更新已读
        baseDao.updateStatus(receiveInfoId);

        return sellerMessageTextDTO;
    }

    /**
     * 获取消息提醒列表
     *
     * @param storeId
     */

    @Override
    public MessageRemindDTO getRemingList(@RequestParam("storeId") Long storeId) {

        StorageWarningDTO storageWarningDTO = storageWarningService.get(storeId);

        MessageRemindDTO messageRemindDTO = new MessageRemindDTO();

        messageRemindDTO.setGoodsAuditRepulseCount(goodsService.findGoodsCount(storeId, 1));

        messageRemindDTO.setGoodsAuditCount(goodsService.findGoodsCount(storeId, 2));

        messageRemindDTO.setSaleBarterCount(aftersaleReturnService.findCount(storeId, 1));

        messageRemindDTO.setSaleReturnCount(aftersaleReturnService.findCount(storeId, 0));

        messageRemindDTO.setSysMessageCount(baseDao.findCount(storeId, 1));

        messageRemindDTO.setPrivateMessageCount(baseDao.findCount(storeId, 0));
        if (storageWarningDTO != null && storageWarningDTO.getIsEnable() == 0) {
            messageRemindDTO.setStorageWarnCount(goodsSpecService.findCount(storeId, storageWarningDTO.getStorage()));
        }


        return messageRemindDTO;
    }


    @Override
    public void deleteInnerMessage(@RequestBody Long[] ids) {
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }
}
