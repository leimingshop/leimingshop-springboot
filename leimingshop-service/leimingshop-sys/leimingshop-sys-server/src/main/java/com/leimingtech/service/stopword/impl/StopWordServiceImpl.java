/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.stopword.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.EasyExcelUtils;
import com.leimingtech.commons.tools.utils.SensitiveWordUtil;
import com.leimingtech.dao.stopword.StopWordDao;
import com.leimingtech.dto.excel.UploadExcelDTO;
import com.leimingtech.dto.stopword.AddStopWordDTO;
import com.leimingtech.dto.stopword.StopWordDTO;
import com.leimingtech.dto.stopword.UpdateStopWordDTO;
import com.leimingtech.entity.stopword.StopWordEntity;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.service.stopword.StopWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 广告禁语管理
 *
 * @author chengqian
 * @since v1.0.0 2019-08-21
 */
@Service
@Slf4j

public class StopWordServiceImpl extends BaseServiceImpl<StopWordDao, StopWordEntity> implements StopWordService {

    /**
     * 获取分页信息
     *
     * @param params
     * @return
     * @author chengqian
     * @date 2019-08-21
     */
    @Override

    public PageData<StopWordDTO> page(@RequestParam Map<String, Object> params) {
        IPage<StopWordEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );
        return getPageData(page, StopWordDTO.class);
    }

    /**
     * 获取结果集
     *
     * @param params
     * @return
     * @author chengqian
     * @date 2019-08-21
     */
    @Override

    public List<StopWordDTO> list(@RequestParam Map<String, Object> params) {
        List<StopWordEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, StopWordDTO.class);
    }

    private QueryWrapper<StopWordEntity> getWrapper(Map<String, Object> params) {
        QueryWrapper<StopWordEntity> wrapper = new QueryWrapper<>();
        String name = (String) params.get("name");
        wrapper.like(StringUtils.isNotBlank(name), "name", name);

        return wrapper;
    }

    /**
     * 获取实体信息
     *
     * @param id
     * @return
     * @author chengqian
     * @date 2019-08-21
     */
    @Override

    public StopWordDTO get(Long id) {
        StopWordEntity entity = baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, StopWordDTO.class);
    }

    /**
     * 保存
     *
     * @param dto
     * @author chengqian
     * @date 2019-08-21
     */
    @Override

    public void save(@RequestBody AddStopWordDTO dto) {
        baseDao.insert(ConvertUtils.sourceToTarget(dto, StopWordEntity.class));
        SensitiveWordUtil.sensitiveWordMap = null;

    }


    /**
     * 修改
     *
     * @param dto
     * @author chengqian
     * @date 2019-08-21
     */
    @Override

    public void update(@RequestBody UpdateStopWordDTO dto) {
        StopWordEntity entity = ConvertUtils.sourceToTarget(dto, StopWordEntity.class);
        baseDao.updateById(entity);
        SensitiveWordUtil.sensitiveWordMap = null;
    }

    /**
     * 删除
     *
     * @param ids
     * @author chengqian
     * @date 2019-08-21
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, StopWordEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
        //异步调用mq同步敏感词到redis
//        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_WORD_TO_REDIS, "");
        SensitiveWordUtil.sensitiveWordMap = null;
        log.info("删除" + ids);
    }

    /**
     * 导入禁用词
     */
    @Override
    public Boolean importForbiddenInfo(@RequestPart("file") MultipartFile files) {
        AnalysisEventListener<UploadExcelDTO> listener = EasyExcelUtils.getListener(this.batchInsert(), 10);

        try {
            String suffix = StringUtils.substring(files.getOriginalFilename(), files.getOriginalFilename().lastIndexOf("."));
            if (!".xlsx".equals(suffix)) {
                //如果不是.xls结尾的文件的话需要返回异常提示
                throw new ServiceException("导入禁用词异常");
            } else {
                EasyExcel.read(files.getInputStream(), UploadExcelDTO.class, listener).sheet().doRead();
            }
        } catch (Exception e) {
            log.error("Excel导入异常" + e);
        }
        return true;
    }

    public Consumer<List<UploadExcelDTO>> batchInsert() {
        return this::saveStopList;
    }

    private void saveStopList(List<UploadExcelDTO> excelDTOList) {
        if (CollectionUtil.isEmpty(excelDTOList)) {
            log.info("excel信息为空，导入完毕");
            return;
        }

        log.info("excel信息不为空，开始更新禁用词");

        //查询所有禁用词
        Set<String> total = baseDao.findAllName();
        Set<StopWordEntity> newNames = new HashSet<>();

        //第一行为标题，取出第一行
        if (CollectionUtil.isNotEmpty(excelDTOList)) {
            for (UploadExcelDTO forbiddenInfoDTO : excelDTOList) {
                //不存在则新增
                if (StringUtils.isNotBlank(forbiddenInfoDTO.getName()) && !total.contains(forbiddenInfoDTO.getName())) {
                    newNames.add(new StopWordEntity(forbiddenInfoDTO.getName()));
                }
            }

            log.info("excel总数量：{}，实际新增数：{}", excelDTOList.size(), newNames.size());
            //批量新增
            if (CollectionUtil.isNotEmpty(newNames)) {
                insertBatch(newNames);
            }
        }
    }


    /**
     * 禁用词处理
     *
     * @param text: 待处理文本
     * @return 处理完文本
     * @date 2020/5/7 14:29
     * @author lixiangx@leimingtech.com
     **/
    @Override

    public String replaceStopWord(@RequestParam(value = "text") String text) {
        if (MapUtils.isEmpty(SensitiveWordUtil.sensitiveWordMap)) {
            // 查询禁用词列表
            List<StopWordEntity> lists = baseDao.selectList(Wrappers.<StopWordEntity>lambdaQuery());
            Set<String> collect = lists.stream().map(StopWordEntity::getName).collect(Collectors.toSet());

            // 初始化禁用词map
            SensitiveWordUtil.initKeyWord(collect);
        }
        return SensitiveWordUtil.replaceSensitiveWord(text, SensitiveWordUtil.maxMatchType, SensitiveWordUtil.replaceCharStar);
    }
}
