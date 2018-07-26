package com.king.sso.core.exception;

/**
 * @Auther: wangjinsheng
 * @Date: 2018/7/25 15:46
 * @Description:
 */
public class KingSsoException extends RuntimeException {
    private static final long serialVersionUID = 42L;

    public KingSsoException(String msg){
        super(msg);
    }
}
