/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.netease;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@ToString
@ApiModel(description = "NeteaseUserInfoDTO")
public class NeteaseUserInfoDTO {

    /**
     * 网易云通信ID，最大长度32字符，必须保证一个
     * APP内唯一（只允许字母、数字、半角下划线_、
     *
     * @、半角点以及半角-组成，不区分大小写， 会统一小写处理，请注意以此接口返回结果中的accid为准）。
     */
    @NotNull
    @Length(max = 30)
    private String accid;
    /**
     * 否	网易云通信ID昵称，最大长度64字符。
     */
    @Length(max = 64)
    private String name;

    /**
     * json属性，开发者可选填，最大长度1024字符。该参数已不建议使用。
     */
    @Length(max = 1024)
    private String props;
    /**
     * icon网易云通信ID头像URL，开发者可选填，最大长度1024
     */
    @Length(max = 1024)
    private String icon;
    /**
     * 网易云通信ID可以指定登录token值，最大长度128字符，并更新，如果未指定，会自动生成token，并在
     * 创建成功后返回
     */
    @Length(max = 128)
    private String token;

    /**
     * 用户签名，最大长度256字符
     */
    @Length(max = 256)
    private String sign;

    /**
     * 否	用户email，最大长度64字符
     */
    @Length(max = 64)
    private String email;
    /**
     * 否	用户生日，最大长度16字符
     */
    @Length(max = 16)
    private String birth;

    /**
     * 用户mobile 最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)
     */
    @Length(max = 32)
    private String mobile;

    /**
     * 用户性别，0表示未知，1表示男，2女表示女，其它会报参数错误
     */
    private int gender;

    /**
     * 用户名片扩展字段，最大长度1024字符，用户可自行扩展，建议封装成JSON字符串
     */
    @Length(max = 1024)
    private String ex;
}
