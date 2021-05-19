/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.netease.impl;

public interface NeteaseAPI {

    /**
     * 1.第三方帐号导入到网易云通信平台。注册成功后务必在自身的应用服务器上维护好accid与token。
     * 2.注意accid，name长度以及考虑管理token。
     * 3.云信应用内的accid若涉及字母，请一律为小写，并确保服务端与所有客户端均保持小写。
     */
    String CREATE_USER_TOKEN = "https://api.netease.im/nimserver/user/create.action";

    /**
     * 1.更新网易云通信token。通过该接口，可以对accid更新到指定的token，更新后请开发者务必做好本地的维护。更新后，需要确保客户端SDK再次登录时携带的token保持最新。
     */
    String UPDARE_USER_TOKEN = "https://api.netease.im/nimserver/user/update.action";

    /**
     * 1.由云信webserver随机重置网易云通信ID的token，同时将新的token返回，更新后请开发者务必做好本地的维护。
     * 2.此接口与网易云通信token更新接口最大的区别在于：前者的token是由云信服务器指定，后者的token是由开发者自己指定。
     */
    String REFRESH_USER_TOKEN = "https://api.netease.im/nimserver/user/refreshToken.action";

    /**
     * 1.封禁网易云通信ID后，此ID将不能再次登录。若封禁时，该id处于登录状态，则当前登录不受影响，仍然可以收发消息。封禁效果会在下次登录时生效。因此建议，将needkick设置为true，让该账号同时被踢出登录。
     * 2.出于安全目的，账号创建后只能封禁，不能删除；封禁后账号仍计入应用内账号总数。
     */
    String BLOCK_USER = "https://api.netease.im/nimserver/user/block.action";

    /**
     * 解禁被封禁的网易云通信ID
     */
    String UNBLOCK_USER = "https://api.netease.im/nimserver/user/unblock.action";

    /**
     * 更新用户名片。用户名片中包含的用户信息，在群组、聊天室等场景下，会暴露给群组、聊天室内的其他用户。这些字段里mobile，email，birth，gender等字段属于非必填、可能涉及隐私的信息，如果您的业务下，这些信息为敏感信息，建议在通过扩展字段ex填写相关资料并事先加密。
     */
    String UPDATE_USER_INFO = "https://api.netease.im/nimserver/user/updateUinfo.action";


    String GET_USER_INFOS = "https://api.netease.im/nimserver/user/getUinfos.action";
    /**
     * 设置桌面端在线时，移动端是否需要推送
     */
    String USER_SET = "https://api.netease.im/nimserver/user/setDonnop.action";


}
