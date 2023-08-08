package com.xzl.csdn.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author xinzailing
 */
public class BussinessNoUtil {

    /**
     * 获取业务编码
     *
     * @param code      业务编码
     * @param increment 自增码
     * @return
     */
    public static String getBusinessNo(String sysNoPre, String code, Integer increment) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String pre = sysNoPre + sdf.format(now) + code;
        return pre + String.format("%06d", increment);
    }

    /**
     * 获取业务编码
     *
     * @param code      系统前缀
     * @param increment 业务编码
     * @return
     */
    public static String getBusinessNo(String code, Integer increment) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String pre = sdf.format(now) + code;
        return pre + String.format("%06d", increment);
    }

    public static synchronized String getConsulNo(String code) {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String pre = sdf.format(now) + code;
        Random r = new Random();
        return pre + String.format("%02d", r.nextInt(10));
    }

    public static final String SYS_CODE = "STARWB_";   //编号前缀
    public static final String CSDN_SYS_CODE = "CSDN";   //城市大脑编号前缀

    public static final String CODE_CHECK_SUBJECT = "01";//主题表
    public static final String CODE_CHECK_LEVEL_ONE = "02";//一级指标表
    public static final String CODE_CHECK_LEVEL_TWO = "03";//二级指标表
    public static final String CODE_CHECK_REQUIREMENT = "04";//指标要求表
    public static final String CODE_CHECK_BATCH = "05";//双随机任务
    public static final String CODE_CHECK_REPORT = "06";//信用报告
    public static final String CODE_CHECK_REPORT_SCORE_DEAL = "06";//信用报告加减分
    public static final String CODE_CHECK_LIFT_REPORT = "08";//检查报告
    public static final String CODE_LIFT_APPLY = "09";//按需维保
    public static final String CODE_LIFT_DELAY = "10";//延期申请
    public static final String CODE_LIFT_FIX = "11";//维修
    public static final String CODE_LIFT_STOP = "12";//报停
    public static final String CODE_LIFT_INSURANCE = "13";//保险申报
    public static final String CODE_MAINT_APPLY = "20";//维保单位资质申报
    public static final String CODE_EXPERIMENT_APPLY = "21";//按需维保试点申报
    public static final String CODE_REPORT = "23";//报告
    public static final String CODE_REPORT_DETAIL = "24";//报告详情
    public static final String CODE_PUNISH_PUBLICITY = "25";//处罚公示
    public static final String CODE_CHECK_APPLY = "26";//检测单位资质申报
    public static final String CODE_CHECK_PROJECT_APPLY = "27";//检测项目申报
    public static final String CODE_EVENT = "28";//重大事件处置
    public static final String CONSULT_APPEAL_CODE_EVENT = "29";//咨询投诉处置


    public static final String CODE_LIFT_PROBLEM_DEAL = "14";//电梯现场处置单

    public static final String CODE_WLW_IMPORT = "09";  //物联网电梯导入

    /*生成主题编号*/
    public static String createCheckSubjectNo(String sysNoPre, Integer id) {
        return BussinessNoUtil.getBusinessNo(sysNoPre, CODE_CHECK_SUBJECT, id);
    }

    /*一级指标表*/
    public static String createCheckLevelOneNo(String sysNoPre, Integer id) {
        return BussinessNoUtil.getBusinessNo(sysNoPre, CODE_CHECK_LEVEL_ONE, id);
    }

    /*二级指标表*/
    public static String createCheckLevelTwoNo(String sysNoPre, Integer id) {
        return BussinessNoUtil.getBusinessNo(sysNoPre, CODE_CHECK_LEVEL_TWO, id);
    }

    /*指标要求*/
    public static String createCheckRequirementNo(String sysNoPre, Integer id) {
        return BussinessNoUtil.getBusinessNo(sysNoPre, CODE_CHECK_REQUIREMENT, id);
    }

    /*双随机任务*/
    public static String createCheckBatchNo(Integer id) {
        return BussinessNoUtil.getBusinessNo(CODE_CHECK_BATCH, id);
    }

    /*信用报告编号*/
    public static String createCheckReportNo(Integer id) {
        return BussinessNoUtil.getBusinessNo(CODE_CHECK_REPORT, id);
    }

    /*信用报告加减分编号*/
    public static String createReportScoerDealNo(Integer id) {
        return BussinessNoUtil.getBusinessNo(CODE_CHECK_REPORT_SCORE_DEAL, id);
    }

    /*一级指标表*/
    public static String createCheckResultNo(String sysNoPre, Integer id) {
        return BussinessNoUtil.getBusinessNo(sysNoPre, CODE_CHECK_LIFT_REPORT, id);
    }

    /*电梯现场处置单*/
    public static String createLiftProblemDealNo(Integer id) {
        return BussinessNoUtil.getBusinessNo(CODE_LIFT_PROBLEM_DEAL, id);
    }

    /*重大事件编号*/
    public static String createEventNo(Integer id) {
        return BussinessNoUtil.getBusinessNo(CODE_EVENT, id);
    }

    /*咨询投诉事件编号*/
    public static String createConsultAppealEventNo(Integer id) {
        return BussinessNoUtil.getBusinessNo(CONSULT_APPEAL_CODE_EVENT, id);
    }

}
