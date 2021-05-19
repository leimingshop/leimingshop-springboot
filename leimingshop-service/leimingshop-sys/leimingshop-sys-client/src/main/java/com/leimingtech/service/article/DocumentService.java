/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.article;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.article.DocumentDTO;
import com.leimingtech.dto.article.DocumentSaveDTO;
import com.leimingtech.dto.article.DocumentUpdateDTO;
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

public interface DocumentService {
    /**
     * 保存系统文章
     *
     * @param dto
     */

    void save(@RequestBody DocumentSaveDTO dto);

    /**
     * 修改系统文章
     *
     * @param dto
     */

    void update(@RequestBody DocumentUpdateDTO dto);

    /**
     * 删除系统文章
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 导出系统文章
     *
     * @param params
     * @return
     */

    List<DocumentDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询系统文章
     *
     * @param id
     * @return
     */

    DocumentDTO get(Long id);

    /**
     * 分页查询
     *
     * @param params
     * @return
     */

    PageData<DocumentDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 分页查询
     *
     * @param docCode
     * @return
     */

    DocumentDTO getDetailByCode(String docCode);
}