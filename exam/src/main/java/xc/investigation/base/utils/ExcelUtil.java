package xc.investigation.base.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author seanx
 */
public class ExcelUtil {
    public static void excelResponse(Workbook wb, HttpServletResponse response, String fileName) throws IOException {
        // 为什么使用 ISO8859-1 编码，这个主要是由于http协议，http header头要求其内容必须为iso8859-1编码,所以我们最终要把其编码为 ISO8859-1 编码的字符串.
        // 但是前面为什么不直接使用 "中文文件名".getBytes("ISO8859-1"); 这样的代码呢?因为ISO8859-1编码的编码表中，根本就没有包含汉字字符，
        // 当然也就无法通过"中文文件名".getBytes("ISO8859-1");来得到正确的“中文文件名”在ISO8859-1中的编码值了，
        // 所以再通过new String()来还原就无从谈起了。
        // 所以先通过 "中文文件名".getBytes("utf-8") 获取其 byte[] 字节，让其按照字节来编码，
        // 即在使用 new String("中文文件名".getBytes("utf-8"), "ISO8859-1") 将其重新组成一个字符串，传送给浏览器。
        String contentDisposition = "attachment;filename=" + new String(
                fileName.getBytes(StandardCharsets.UTF_8),
                StandardCharsets.ISO_8859_1);
        if(wb instanceof HSSFWorkbook){
            contentDisposition += ".xls";
        }else {
            contentDisposition += ".xlsx";
        }
        response.setHeader("Content-Disposition", contentDisposition);
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        OutputStream output = response.getOutputStream();
        BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
        wb.write(bufferedOutPut);
        bufferedOutPut.flush();
        bufferedOutPut.close();
    }

    public static HSSFCellStyle headerCellStyle(HSSFWorkbook workbook){
        HSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setAlignment(HorizontalAlignment.CENTER);
        titleCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleCellStyle.setFont(titleFont);
        return titleCellStyle;
    }
}
