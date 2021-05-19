/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.enums.DelFlagEnum;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.message.MessageReceiverDao;
import com.leimingtech.dto.message.MessageReceiverDTO;
import com.leimingtech.dto.message.MessageReceiverDeleteDTO;
import com.leimingtech.dto.message.SellerMessageReceiverDTO;
import com.leimingtech.entity.message.MessageReceiverEntity;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.service.message.MessageReceiverService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 短消息接收人表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-16
 */
@Service
public class MessageReceiverServiceImpl extends CrudServiceImpl<MessageReceiverDao,
        MessageReceiverEntity, MessageReceiverDTO> implements MessageReceiverService {

    @Override
    public QueryWrapper<MessageReceiverEntity> getWrapper(Map<String, Object> params) {
        String messageId = (String) params.get("messageId");
        String receiveName = (String) params.get("receiveName");
        String strTime = (String) params.get("strTime");
        String endTime = (String) params.get("endTime");

        String statusStr = (String) params.get("status");
        Integer status = null;
        //当进行时间查询时,添加过滤条件
        if (StringUtils.isNotBlank(strTime)) {
            statusStr = String.valueOf(MessageEnum.MESSAGE_IS_READ.value());
        }
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }

        QueryWrapper<MessageReceiverEntity> wrapper = new QueryWrapper<>();
        wrapper.ge(StringUtils.isNotBlank(strTime), "update_date", strTime);
        wrapper.le(StringUtils.isNotBlank(endTime), "update_date", endTime + " 23:59:59");
        wrapper.eq(StringUtils.isNotBlank(messageId), "message_id", messageId);
        wrapper.like(StringUtils.isNotBlank(receiveName), "receive_id", receiveName);
        wrapper.like(StringUtils.isNotBlank(statusStr), "status", status);

        return wrapper;
    }

    /**
     * 查询接收人未读信息条数
     *
     * @param id
     * @return
     */
    @Override

    public Integer selectByReceiveId(Long id) {
        return baseDao.selectByReceiveId(id);
    }

    /**
     * 根据id查询接收人详情
     *
     * @param id
     * @return
     */
    @Override

    public MessageReceiverDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 保存
     *
     * @param dto
     */
    @Override

    public void save(@RequestBody MessageReceiverDTO dto) {
        super.save(dto);
    }

    /**
     * 接收人批量保存
     *
     * @param dtoList
     */

    @Override
    public void saveBatch(@RequestBody List<MessageReceiverDTO> dtoList) {
        List<MessageReceiverEntity> messageReceiverEntities = ConvertUtils.sourceToTarget(dtoList, MessageReceiverEntity.class);
        super.insertBatch(messageReceiverEntities);
    }

    @Override

    public void update(@RequestBody MessageReceiverDTO dto) {
        super.update(dto);
    }

    @Override

    public PageData<MessageReceiverDTO> pageMessag(@RequestParam Map<String, Object> params) {
        IPage<MessageReceiverEntity> page = getPage(params, "", false);

        List<MessageReceiverDTO> dtoList = baseDao.adminList(page, params);
        //未读状态 不显示修改时间
        for (MessageReceiverDTO dto : dtoList) {
            if (dto.getStatus() == MessageEnum.MESSAGE_IS_UNREAD.value()) {
                dto.setUpdateDate(null);
            }
        }
        return new PageData<>(dtoList, page.getTotal());
    }


    /**
     * 修改接收人状态为已读
     *
     * @param params
     */
    @Override

    public void updateStatus(@RequestParam Map<String, Object> params) {
        Long id = Long.valueOf(params.get("id").toString());
        Long messageId = Long.valueOf(params.get("messageId").toString());
        baseDao.updateStatus(id, messageId);
    }

    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据接收人id获取列表
     *
     * @param receiveId
     * @return
     */

    @Override
    public List<MessageReceiverDTO> selectListByReceiveId(Long receiveId) {
        QueryWrapper<MessageReceiverEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("receive_id", receiveId);
        wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());
        List<MessageReceiverEntity> messageReceiverEntities = baseDao.selectList(wrapper);
        return ConvertUtils.sourceToTarget(messageReceiverEntities, MessageReceiverDTO.class);
    }

    /**
     * 功能描述:
     * 〈用户删除站内信〉
     *
     * @param messageReceiverDeleteDTO
     * @author : 刘远杰
     */

    @Override
    public Boolean deleteBatch(@RequestBody MessageReceiverDeleteDTO messageReceiverDeleteDTO, @RequestParam("receiveId") Long receiveId) {
        Long[] ids = messageReceiverDeleteDTO.getIds();
        if (ids != null && ids.length > 0) {
            UpdateWrapper<MessageReceiverEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("message_id", Arrays.asList(ids)).eq("show_flag", MessageEnum.MESSAGE_SHOW_YES.value())
                    .eq(receiveId != null, "receive_id", receiveId);

            MessageReceiverEntity messageReceiverEntity = new MessageReceiverEntity();
            messageReceiverEntity.setShowFlag(MessageEnum.MESSAGE_SHOW_NO.value());

            baseDao.update(messageReceiverEntity, updateWrapper);
        }
        return true;
    }

    /**
     * seller端站内信列表
     *
     * @param params
     * @return
     */

    @Override
    public PageData<SellerMessageReceiverDTO> sellerMessagePage(@RequestParam Map<String, Object> params) {

        IPage<MessageReceiverEntity> page = getPage(params, null, false);

        List<SellerMessageReceiverDTO> list = baseDao.sellerMessagePage(page, params);

        return new PageData<>(list, page.getTotal());
    }

    /**
     * seller端用户删除站内信
     * 《修改展示状态，不修改删除表示》
     *
     * @param ids 接收人表ID
     */

    @Override
    public void deleteShow(@RequestBody Long[] ids) {
        UpdateWrapper<MessageReceiverEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        MessageReceiverEntity messageReceiverEntity = new MessageReceiverEntity();
        messageReceiverEntity.setShowFlag(MessageEnum.MESSAGE_SHOW_NO.value());

        baseDao.update(messageReceiverEntity, updateWrapper);
    }

    /**
     * 批量已读
     *
     * @param ids 主键id
     */

    @Override
    public void batchRead(@RequestBody Long[] ids) {
        UpdateWrapper<MessageReceiverEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id", ids);
        MessageReceiverEntity messageReceiverEntity = new MessageReceiverEntity();
        messageReceiverEntity.setStatus(MessageEnum.MESSAGE_IS_READ.value());
        baseDao.update(messageReceiverEntity, updateWrapper);

    }

    /**
     * 清除未读消息
     *
     * @param memberId
     */

    @Override
    public void read(@RequestParam("memberId") Long memberId) {

        baseDao.read(memberId);

    }
}
