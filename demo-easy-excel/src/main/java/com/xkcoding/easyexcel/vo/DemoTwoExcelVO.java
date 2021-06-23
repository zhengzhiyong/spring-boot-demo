package com.xkcoding.easyexcel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.Serializable;

@Data
@Builder
public class DemoTwoExcelVO implements Serializable {
    @ExcelProperty("学生数量")
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 51)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String count;

    @ExcelProperty(value = {"成绩","总分数"})
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 51)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String total;

    @ExcelProperty(value = {"成绩","平均分"})
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 51)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String avg;

    @ExcelProperty(value = {"成绩","最高分"})
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 51)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String high;

    @ExcelProperty(value = {"成绩","最低分"})
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 51)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String low;
}
