package com.xzl.csdn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xzl.csdn.auth.entity.CsdnLoginLog;
import com.xzl.csdn.auth.model.CsdnLoginLogRequest;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto-generator
 * @since 2020-08-10
 */
public interface CsdnLoginLogMapper extends BaseMapper<CsdnLoginLog> {
	IPage queryList(Page page, CsdnLoginLogRequest request);
}
