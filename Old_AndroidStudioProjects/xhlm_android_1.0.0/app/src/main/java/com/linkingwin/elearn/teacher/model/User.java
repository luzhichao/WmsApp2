package com.linkingwin.elearn.teacher.model;


import java.util.List;

import lombok.Data;


/**
 * @author wangchaoyu
 */
@Data
public class User {

    private String userId; //用户ID

    private String username;//用户名(账号)

    private String password;//密码

    private String nickName;//昵称

    private String mobile;//手机

    private String email;//邮件

    private String address;//省市县地址

    private String street;//街道地址

    private Integer sex;//0女 1男 2保密

    private String passStrength;//密码强度

    private String avatar = "https://s1.ax1x.com/2018/05/19/CcdVQP.png";
}
