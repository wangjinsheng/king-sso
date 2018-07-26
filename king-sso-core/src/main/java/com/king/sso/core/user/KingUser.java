package com.king.sso.core.user;

import java.io.Serializable;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 15:50
 * @Description:
 */
public class KingUser implements Serializable {
    private static final long serialVersionUID = 42L;

    private int userid;
    private String username;
    private String other;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
