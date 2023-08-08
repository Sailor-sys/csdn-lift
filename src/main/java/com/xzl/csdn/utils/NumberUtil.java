package com.xzl.csdn.utils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author tfdeng
 * @Description
 * @Date 2023/06/02/15:17
 * @Version 1.0
 */
public class NumberUtil {
    /**
     * 保留两位小数
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    scale
     * @return BigDecimal
     */
    private static BigDecimal getRate2(Integer dividend, Integer divisor, Integer scale) {
        if (Objects.equals(0, divisor) || Objects.isNull(divisor)) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(scale)) {
            scale = 4;
        }
        return new BigDecimal(dividend).divide(new BigDecimal(divisor), scale, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))
                .stripTrailingZeros();
    }
}
