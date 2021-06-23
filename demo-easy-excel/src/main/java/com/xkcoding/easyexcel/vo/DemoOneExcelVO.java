package com.xkcoding.easyexcel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ContentRowHeight(10)
@HeadRowHeight(20)
@ColumnWidth(25)
public class DemoOneExcelVO implements Serializable {
    @ExcelProperty("序号")
    // 字符串的头背景设置成粉红 IndexedColors.PINK.getIndex()
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 43)
    // 字符串的头字体设置成20
    @HeadFontStyle(fontHeightInPoints = 15)
    // 字符串的内容的背景设置成天蓝 IndexedColors.SKY_BLUE.getIndex()
//    @ContentStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 45)
    // 字符串的内容字体设置成20
//    @ContentFontStyle(fontHeightInPoints = 15)
    private String no;

    @ColumnWidth(40)
    @ExcelProperty(value = "学生姓名")
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 43)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String studentName;

    @ExcelProperty(value = {"成绩","科目"})
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 53)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String name;

    @ExcelProperty(value = {"成绩","分数"})
    @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 53)
    @HeadFontStyle(fontHeightInPoints = 15)
    private String score;
}
