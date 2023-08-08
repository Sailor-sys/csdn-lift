package com.xzl.csdn.auth.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.xzl.csdn.auth.model.JwtUser;
import com.xzl.csdn.auth.model.LoginUserBo;
import com.xzl.csdn.auth.model.UserTokenContext;
import com.xzl.csdn.common.exception.BusinessException;
import com.xzl.csdn.service.CsdnUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * @author：lianp
 * @description：
 * @date：9:18 2018/6/28
 */
@Slf4j
public class SessionUtil {
    public static Integer getUserId(HttpSession session){
        LoginUserBo user = getUser(session);
        return user==null?null:user.getUserId();
    }

    public static String getNickname(HttpSession session){
        LoginUserBo user = getUser(session);
        return user==null?null:user.getNickname();
    }

    public static String getUserName(HttpSession session){
        LoginUserBo user = getUser(session);
        return user==null?null:user.getUsername();
    }

    public static String getPhone(HttpSession session){
        LoginUserBo user = getUser(session);
        return user==null?null:user.getPhone();
    }

    public static String getUserArea(HttpSession session){
        LoginUserBo user = getUser(session);
        return user==null?null:user.getTownCode();
    }

    public static String getUserUseUnitName(HttpSession session) {
        LoginUserBo user = getUser(session);
        if (Objects.isNull(user)) {
            throw new BusinessException("获取不到当前用户登录信息");
        }
        CsdnUserService csdnUserService = SpringUtil.getBean(CsdnUserService.class);
        return csdnUserService.getUseUnitName(user.getUserId());
    }

    public static LoginUserBo getUser(HttpSession session){
		JwtUser jwtUser = UserTokenContext.getUserInfo();

		if(jwtUser == null){
			return null;
		}
		LoginUserBo loginUserBo = new LoginUserBo();
		loginUserBo.setUserId(jwtUser.getId());
		loginUserBo.setUsername(jwtUser.getUsername());
		loginUserBo.setNickname(jwtUser.getNickName());
		loginUserBo.setPhone(jwtUser.getMobilePhone());
		loginUserBo.setTownCode(jwtUser.getTownCode());
		loginUserBo.setUnitId(jwtUser.getUnitId());
		return loginUserBo;
	}

    public static void main(String[] args) {
        String s = "SELECT DISTINCT aep.register_code from auth_employee e JOIN \n" +
                "auth_employee_permission aep ON e.id = aep.employee_id\n" +
                "where e.`status`= 1 and e.user_phone = '17689223989';";

        System.out.println(Base64Utils.decode(s.getBytes()));
    }
}
