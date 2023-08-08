package com.xzl.csdn.domain.query;

import com.xzl.csdn.constant.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiqh
 * @date 2023-07-19 11:20
 **/
@ApiModel("看板查询对象")
@Data
public class CsdnBoardQuery extends BaseRequest {

    /**
     * 区域编码（默认富阳）
     */
    @ApiModelProperty(hidden=true)
    private String townCode = Constants.FUYANG_TOWN_CODE;

    @ApiModelProperty("告警编号")
    private String errorNo;

    @ApiModelProperty("注册代码")
    private String registerCode;

    /**
     * 故障类型
     */
    @ApiModelProperty(hidden=true)
    private String errorType;

    @ApiModelProperty("查询类型(困人月度：1，故障月度：2，梯阻月度：3，不文明月度：4)")
    private Integer type;

    @ApiModelProperty("场所名称")
    private String locationName;

    @ApiModelProperty("场所类型")
    private String locationType;

    @ApiModelProperty("梯领(0~6:6,6~10:10,10~15:15)")
    private Integer liftTime;

    @ApiModelProperty("街道")
    private String streetCode;

    @ApiModelProperty("故障查询事件状态(今年:1,当月:2,当日:3)")
    private Integer alarmFlag;

    @ApiModelProperty("电梯点位类型(普通电梯:0,物联网电梯:1,困人电梯:2,烟雾:1000016，治安事件：1000019，煤气罐：1000108，遗留物：1000095，大件物品：1000050)")
    private Integer liftPositType;

    /**
     * 场所名称集合
     */
    @ApiModelProperty(hidden=true)
    private List<String> locationNameList;

    /**
     * 告警类型集合
     */
    @ApiModelProperty(hidden=true)
    private List<String> alarmTypeList;

    /**
     * 注册代码集合
     */
    @ApiModelProperty(hidden=true)
    private List<String> registerCodeList;

    /**
     * 物联网标志(0:未接入, 1:已接入)
     */
    @ApiModelProperty(hidden=true)
    private Integer connectNetFlag;


    /**
     * ture:开启 false:关闭 （开启时使用registerCodeList字段固定电梯值查询,关闭时使用动态查询富阳区电梯）
     */
    @ApiModelProperty(hidden=true)
    private boolean enable = true;


    /**
     * 第一版试点电梯
     */
    public List<String> getRegisterCodeList() {
        List<String> list = new ArrayList<>();
        list.add("30103301832004120010");
        list.add("30103301832004120011");
        list.add("30103301832004120012");
        list.add("30103301832004120013");
        list.add("30103301832004120014");
        list.add("30103301832004120015");
        list.add("30103301832004120007");
        list.add("30103301832004120008");
        list.add("30103301832004120009");
        list.add("30103301832004030004");
        list.add("30103301832004030003");
        list.add("30103301832003120016");
        list.add("30103301832004030002");
        list.add("30103301832004120020");
        list.add("30103301832004030001");
        list.add("30103301832003120015");
        list.add("30103301832004120021");
        list.add("30103301832003120014");
        list.add("30103301832003120013");
        list.add("30103301832004120016");
        list.add("30103301832004120017");
        list.add("30103301832004120018");
        list.add("30103301832004120019");
        list.add("30103301832006040001");
        list.add("30103301832006040002");
        list.add("31303301832013090007");
        list.add("31303301832013090008");
        list.add("31303301832013090009");
        list.add("31303301832011090008");
        list.add("31303301832011090009");
        list.add("31103301832011090008");
        list.add("31303301832011090006");
        list.add("31103301832013090030");
        list.add("31103301832011090009");
        list.add("31303301832011090007");
        list.add("31303301832011090004");
        list.add("31303301832011090005");
        list.add("31303301832011090002");
        list.add("31303301832011090003");
        list.add("31103301832011090007");
        list.add("31103301832013090029");
        list.add("31103301832011090010");
        list.add("31103301832013090028");
        list.add("31303301832011090011");
        list.add("31103301832013090025");
        list.add("31103301832013090024");
        list.add("31103301832013090027");
        list.add("31303301832011090010");
        list.add("31103301832013090026");
        list.add("31103301832013090032");
        list.add("31103301832013090031");
        list.add("31103301832013090023");
        list.add("31103301832011070007");
        list.add("31103301832011080002");
        list.add("31103301832011080008");
        list.add("31103301832011080007");
        list.add("31103301832011070002");
        list.add("31103301832011080009");
        list.add("31103301832011080004");
        list.add("31103301832011070004");
        list.add("31103301832011090003");
        list.add("31103301832011080003");
        list.add("31103301832011070003");
        list.add("31103301832011080006");
        list.add("31103301832011070006");
        list.add("31103301832011080005");
        list.add("31103301832011070005");
        list.add("31103301832011090004");
        list.add("31103301832008090007");
        list.add("31103301832008090006");
        list.add("31103301832008090005");
        list.add("31103301832008090004");
        list.add("31103301832008090003");
        list.add("31103301832015085032");
        list.add("31103301832015085034");
        list.add("31703301832014045010");
        list.add("31103301832015085033");
        list.add("31703301832014045011");
        list.add("31703301832014045012");
        list.add("31303301832013080012");
        list.add("31303301832013080013");
        list.add("31303301832013080014");
        list.add("31303301832013080015");
        list.add("31303301832013080016");
        list.add("31303301832014075003");
        list.add("31703301832014075001");
        list.add("31303301832014075002");
        list.add("31303301832014075004");
        list.add("31303301832014075001");
        list.add("31703301832014075008");
        list.add("31703301832014075009");
        list.add("31703301832014075006");
        list.add("31703301832014075007");
        list.add("31703301832014075004");
        list.add("31703301832014075005");
        list.add("31703301832014075002");
        list.add("31703301832014075003");
        list.add("31703301832014045001");
        list.add("31703301832014045002");
        list.add("31703301832014045003");
        list.add("31703301832014045004");
        list.add("31703301832014045005");
        list.add("31703301832014045006");
        list.add("31703301832014045007");
        list.add("31703301832014045008");
        list.add("31703301832014045009");
        list.add("31703301832014075010");
        list.add("31103301832011110009");
        list.add("31103301832011090002");
        list.add("31103301832011110005");
        list.add("31103301832011090001");
        list.add("31103301832011090017");
        list.add("31103301832011110004");
        list.add("31103301832011110001");
        list.add("31103301832011110002");
        list.add("31103301832011090016");
        return list;
    }

}
