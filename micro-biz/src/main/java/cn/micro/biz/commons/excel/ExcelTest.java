package cn.micro.biz.commons.excel;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ExcelTest {

    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/lry/Downloads/课表-上传模板-星期竖版2.xlsx"));
        List<List<List<ExcelCell>>> data = ExcelImportUtils.readSheet2007(false, "\n", "/", fileInputStream);
        System.out.println(JSON.toJSONString(data));
    }

}
