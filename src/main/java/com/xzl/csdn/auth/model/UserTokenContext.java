package com.xzl.csdn.auth.model;

/**
 * @author: liupu
 * @description: 获取用户信息
 * @date: 2021/8/9
 */
public class UserTokenContext {

   private UserTokenContext(){}
   static ThreadLocal<JwtUser> userInfo = new ThreadLocal<>();

   public static void setUserInfo(JwtUser jwtUser){
      userInfo.set(jwtUser);
   }

   public static JwtUser getUserInfo(){
      return userInfo.get();
   }

   public static void clear(){
      userInfo.remove();
   }

}
