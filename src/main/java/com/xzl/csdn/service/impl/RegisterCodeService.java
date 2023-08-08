package com.xzl.csdn.service.impl;

import com.xzl.csdn.domain.entity.ZhptLiftInfo;
import com.xzl.csdn.domain.query.CsdnBoardQuery;
import com.xzl.csdn.mapper.CsdnBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author shiqh
 * @date 2023-07-28 18:05
 **/
@Service
public class RegisterCodeService {

    @Autowired
    private CsdnBoardMapper csdnBoardMapper;

    /**
     * 获取注册代码
     */
    public List<String> getRegisterCodeList() {
        CsdnBoardQuery query = new CsdnBoardQuery();

        List<String> registerCodeList;
        if (query.isEnable()) {
            registerCodeList = query.getRegisterCodeList();
        } else {
            List<ZhptLiftInfo> liftList = csdnBoardMapper.queryLiftInfo(query);
            registerCodeList = liftList.stream().map(ZhptLiftInfo::getRegisterCode).collect(Collectors.toList());
        }
        return registerCodeList;
    }

}
