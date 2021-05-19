/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.article.DocumentDao;
import com.leimingtech.dto.article.DocumentDTO;
import com.leimingtech.dto.article.DocumentSaveDTO;
import com.leimingtech.dto.article.DocumentUpdateDTO;
import com.leimingtech.entity.article.DocumentEntity;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.service.article.DocumentService;
import com.leimingtech.service.article.code.ArticleStatusCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Service
public class DocumentServiceImpl extends CrudServiceImpl<DocumentDao, DocumentEntity, DocumentDTO> implements DocumentService {
    /**
     * 条件构造器
     *
     * @param params
     * @author xuzhch
     */
    @Override
    public QueryWrapper<DocumentEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");
        String docTitle = (String) params.get("docTitle");
        String docCode = (String) params.get("docCode");

        QueryWrapper<DocumentEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.eq(StringUtils.isNotBlank(docTitle), "doc_title", docTitle);
        wrapper.eq(StringUtils.isNotBlank(docCode), "doc_code", docCode);

        return wrapper;
    }

    /**
     * 系统文章保存
     *
     * @param dto
     * @author xuzhch
     */

    @Override

    public void save(@RequestBody DocumentSaveDTO dto) {
        DocumentEntity entity = baseDao.selectOne(Wrappers.<DocumentEntity>lambdaQuery()
                .eq(true, DocumentEntity::getDocCode, dto.getDocCode())
                .eq(true, DocumentEntity::getDelFlag, 0).or()
                .eq(true, DocumentEntity::getDelFlag, 1));
        if (entity != null) {
            throw new ServiceException(ArticleStatusCode.DOCUMENT_CODE_REPEA);
        }
        DocumentEntity documentEntity = ConvertUtils.sourceToTarget(dto, DocumentEntity.class);
        insert(documentEntity);
    }

    /**
     * 系统文章修改
     *
     * @param dto
     * @author xuzhch
     */
    @Override

    public void update(@RequestBody DocumentUpdateDTO dto) {
        DocumentEntity documentEntity = ConvertUtils.sourceToTarget(dto, DocumentEntity.class);
        updateById(documentEntity);
    }

    /**
     * 删除系统文章
     *
     * @param ids
     */

    @Override
    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 导出系统文章
     *
     * @param params
     * @return
     */

    @Override
    public List<DocumentDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /**
     * 根据ID查询系统文章
     *
     * @param id
     * @return
     */

    @Override
    public DocumentDTO get(Long id) {
        return super.get(id);
    }

    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    @Override
    public PageData<DocumentDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }


    @Override
    public DocumentDTO getDetailByCode(String docCode) {
        DocumentEntity documentEntity = baseDao.selectOne(Wrappers.<DocumentEntity>lambdaQuery()
                .eq(DocumentEntity::getDocCode, docCode));
        // 通过工具类转换数据格式
        DocumentDTO documentDTO = ConvertUtils.sourceToTarget(documentEntity, DocumentDTO.class);
        return documentDTO;
    }
}
