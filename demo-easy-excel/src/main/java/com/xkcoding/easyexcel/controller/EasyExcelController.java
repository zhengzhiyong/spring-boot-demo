package com.xkcoding.easyexcel.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.xkcoding.easyexcel.vo.DemoTwoExcelVO;
import com.xkcoding.easyexcel.vo.DemoOneExcelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.excel.EasyExcel;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RestController
@RequestMapping(value = "easyExcel")
public class EasyExcelController {

    private List<DemoOneExcelVO> getDemoOneList() {
        List<DemoOneExcelVO> list = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            final String s = String.valueOf(i);
            list.add(new DemoOneExcelVO(s, "张三" + s, "科目" + s, "分数" + s));
        });
        return list;
    }

    private List<DemoTwoExcelVO> getTotalList() {
        final DemoTwoExcelVO demoTwoExcelVO = DemoTwoExcelVO.builder()
            .count("10")
            .high("100")
            .total("789")
            .avg("78.9")
            .low("75")
            .build();
        List<DemoTwoExcelVO> totalList = new ArrayList<>();
        totalList.add(demoTwoExcelVO);
        return totalList;
    }

    /**
     * 到处普通表头
     * @param response
     */
    @GetMapping(value = "demoOne")
    public void exportDemoOne(HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(os, DemoOneExcelVO.class).sheet("模板").doWrite(getDemoOneList());
        } catch (Exception e) {
            log.error("call method exportDemoOne has an error msg {} {}", e, e.getMessage());
        }
    }


    /**
     * 导出多个表头的excel
     *
     * @param response
     */
    @GetMapping(value = "demoTwo")
    public void exportdemoTwo(HttpServletResponse response) {
        try (OutputStream os = response.getOutputStream()) {
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            ExcelWriter excelWriter = EasyExcel.write(os, DemoOneExcelVO.class).build();
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").needHead(Boolean.FALSE).build();
            WriteTable writeTable0 = EasyExcel.writerTable(0).needHead(Boolean.TRUE).build();
            WriteTable writeTable1 = EasyExcel.writerTable(1).needHead(Boolean.TRUE).build();
            writeTable0.setClazz(DemoTwoExcelVO.class);
            excelWriter.write(getTotalList(), writeSheet, writeTable0);
            excelWriter.write(getDemoOneList(), writeSheet, writeTable1);
            excelWriter.finish();
            os.flush();
        } catch (Exception e) {
            log.error("call method exportdemoTwo has an error msg {} {}", e, e.getMessage());
        }
    }
}
