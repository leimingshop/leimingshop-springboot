/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec;


import com.leimingtech.modules.dto.spec.SpecValueDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 规格值管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */

public interface SpecValueService {

    /**
     * 功能描述:
     * 〈规格值条件查询〉
     *
     * @param params 查询条件
     * @return 返回规格值
     * @author : 刘远杰
     */

    List<SpecValueDTO> findSpecValueList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈规格物理删除〉
     *
     * @param specId 规格id
     * @return : int 删除数量
     * @author : 刘远杰
     */

    int deleteSpecValueBySpecId(Long specId);

    /**
     * 功能描述:
     * 〈逻辑删除规格、规格值〉
     *
     * @param specId 规格id
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    int logicDeleteSpecValueBySpecId(Long specId);

    /**
     * 功能描述:
     * 〈规格值批量保存〉
     *
     * @param list id集合
     * @author : 刘远杰
     */

    void insertBatch(@RequestBody List<SpecValueDTO> list);

}
