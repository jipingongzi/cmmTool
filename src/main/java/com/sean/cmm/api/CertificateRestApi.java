package com.sean.cmm.api;

import com.alibaba.fastjson2.JSONObject;
import com.sean.cmm.dto.*;
import com.sean.cmm.jpa.entity.CertificateEntity;
import com.sean.cmm.service.CertificateQueryService;
import com.sean.cmm.service.CertificateService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author seanx
 * 证书服务api
 */
@RestController
@RequestMapping("/certificate")
public class CertificateRestApi {

    private final CertificateService certificateService;
    private final CertificateQueryService certificateQueryService;
    public CertificateRestApi(CertificateService certificateService, CertificateQueryService certificateQueryService) {
        this.certificateService = certificateService;
        this.certificateQueryService = certificateQueryService;
    }

    /**
     * 上传文件
     * @param files 上传的文件，支持多文件，
     *              但是目前只有一个文件并且包含四个sheet，基准数据以第三个sheet为准
     */
    @PostMapping("/excel")
    public JSONObject insert(@RequestParam("files") MultipartFile[] files) {
        JSONObject response = new JSONObject();
        String message;
        int apiStatus = 0;
        try {
            MultipartFile file = files[0];
            List<CertificateResultDto> certificateResultDtoList = convertFile2Dto(file);
            certificateService.saveCertificate(certificateResultDtoList);
            message = "文件解析成功";
        }catch (Exception e){
            e.printStackTrace();
            apiStatus = -1;
            message = "文件解析失败：" + e.getMessage();
        }
        response.put("info",message);
        response.put("apiStatus",apiStatus);
        return response;
    }

    @GetMapping("/search")
    public JSONObject search(@RequestParam(value = "userIdNo")String userIdNo){
        int id = certificateQueryService.getCertificateId(userIdNo);
        JSONObject response = new JSONObject();
        response.put("id",id);
        return response;
    }

    @GetMapping("/query")
    public JSONObject search(@RequestParam(value = "certificateId")Integer certificateId){
        Optional<CertificateEntity> certificateEntityOptional = certificateQueryService.get(certificateId);
        if(!certificateEntityOptional.isPresent()){
            return new JSONObject();
        }
        return JSONObject.parseObject(JSONObject.toJSONString(certificateEntityOptional.get()));
    }

    /**
     * 上传文件合并
     * @param files list,bank
     */
    @PostMapping("/merge")
    public JSONObject merge(@RequestParam("files") MultipartFile[] files) throws IOException {
        int apiStatus = 0;
        if (files.length != 2) {
           throw new RuntimeException("文件数量错误");
        } else {
            MultipartFile listFile = null;
            MultipartFile bankFile = null;
            for (MultipartFile file : files) {
                if (file.getOriginalFilename().contains("list")) {
                    listFile = file;
                } else if (file.getOriginalFilename().contains("bank")) {
                    bankFile = file;
                } else {
                    throw new RuntimeException("文件命名错误");
                }
            }
            Workbook bankBook = getWorkbook(bankFile);
            Workbook listBook = getWorkbook(listFile);
            Sheet bankSheet = bankBook.getSheetAt(0);
            Sheet listSheet = listBook.getSheetAt(0);
            Map<String,StudentUserInfo> studentUserInfos = convertSheet2UserInfo(bankSheet);
            Map<String,StudentStudyInfo> studentStudyInfos = convertSheet2StudyInfo(listSheet);
            List<StudentMergeInfo> mergeInfos = mergeStudent(studentUserInfos, studentStudyInfos);
            HSSFWorkbook mergeWorkBook = createFromStudentMergeInfo(mergeInfos);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mergeWorkBook.write(bos);
            byte[] bytes = bos.toByteArray();
            InputStream in = new ByteArrayInputStream(bytes);
            String fileName = "study-merge.xlsx";
//            OssUtil.uploadFile("tool-file",fileName,in);

            JSONObject r = new JSONObject();
            r.put("dir","https://tool-file.oss-cn-chengdu.aliyuncs.com/" + fileName);
            r.put("apiStatus",apiStatus);
            return r;
        }
    }

    private List<CertificateResultDto> convertFile2Dto(MultipartFile file) throws IOException {
        Workbook wb;
        // 当excel是2007时,创建excel2007
        if (file.getOriginalFilename().contains("xlsx")) {
            wb = new XSSFWorkbook(file.getInputStream());
        } else {
            // 当excel是2003时,创建excel2003
            wb = new HSSFWorkbook(file.getInputStream());
        }
        if(wb.getNumberOfSheets() != 4){
            throw new RuntimeException("excel sheet异常");
        }
        List<CertificateTheoryTestDto> theoryTestDtoList = convertSheet2CertificateTheoryTestDtoList(wb.getSheetAt(2));
        Map<String,BigDecimal> completeRateMap = convertSheet2CertificateCompleteRateMap(wb.getSheetAt(0));
        Map<String,BigDecimal> practiceTestMap = convertSheet2CertificatePracticeTestMap(wb.getSheetAt(1));
        Map<String,BigDecimal> manuallyTestMap = convertSheet2CertificateManuallyTestMap(wb.getSheetAt(3));
        List<CertificateResultDto> resultDtoList = new ArrayList<>();

        for (CertificateTheoryTestDto theoryTestDto : theoryTestDtoList) {
            String userIdNo = theoryTestDto.getUserIdNo();
            if (completeRateMap.containsKey(userIdNo) && practiceTestMap.containsKey(userIdNo) && manuallyTestMap.containsKey(userIdNo)) {
                CertificateResultDto resultDto = new CertificateResultDto(userIdNo,
                        theoryTestDto.getUserName(), theoryTestDto.getBankInfo(), theoryTestDto.getArea(),
                        theoryTestDto.getExamStartTime(), theoryTestDto.getExamSuccessTime(),
                        completeRateMap.get(userIdNo), practiceTestMap.get(userIdNo), manuallyTestMap.get(userIdNo),
                        theoryTestDto.getScore());
                resultDtoList.add(resultDto);
            }
        }
       return resultDtoList;
    }
    private Map<String,BigDecimal> convertSheet2CertificateCompleteRateMap(Sheet sheet){
        Map<String,BigDecimal> map = new HashMap<>();
        for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
            Row row = sheet.getRow(i);
            map.put(
                    row.getCell(2).getStringCellValue(),
                    BigDecimal.valueOf(row.getCell(5).getNumericCellValue()).setScale(2, RoundingMode.DOWN));
        }
        return map;
    }
    private Map<String,BigDecimal> convertSheet2CertificatePracticeTestMap(Sheet sheet){
        Map<String,BigDecimal> map = new HashMap<>();
        for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
            Row row = sheet.getRow(i);
            map.put(
                    row.getCell(2).getStringCellValue(),
                    BigDecimal.valueOf(row.getCell(4).getNumericCellValue()).setScale(2, RoundingMode.DOWN));
        }
        return map;
    }
    private List<CertificateTheoryTestDto> convertSheet2CertificateTheoryTestDtoList(Sheet sheet){
        List<CertificateTheoryTestDto> certificateTheoryTestDtoArrayList = new ArrayList<>();
        for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
            Row row = sheet.getRow(i);
            CertificateTheoryTestDto certificateTheoryTestDto = new CertificateTheoryTestDto(
                    row.getCell(1).getStringCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue(),
                    row.getCell(5).getStringCellValue(),
                    row.getCell(7).getStringCellValue(),
              Double.valueOf(row.getCell(8).getNumericCellValue()).intValue() + "",
                    BigDecimal.valueOf(row.getCell(6).getNumericCellValue()).setScale(2, RoundingMode.DOWN));
            certificateTheoryTestDtoArrayList.add(certificateTheoryTestDto);
        }
        return certificateTheoryTestDtoArrayList;
    }
    private Map<String, BigDecimal> convertSheet2CertificateManuallyTestMap(Sheet sheet){
        Map<String,BigDecimal> map = new HashMap<>();
        for (int i = 1; i <= sheet.getLastRowNum() ; i++) {
            Row row = sheet.getRow(i);
            map.put(
                    row.getCell(2).getStringCellValue(),
                    BigDecimal.valueOf(row.getCell(6).getNumericCellValue()).setScale(2, RoundingMode.DOWN));
        }
        return map;
    }

    private Map<String, StudentUserInfo> convertSheet2UserInfo(Sheet sheet){
        Map<String,StudentUserInfo> map = new LinkedHashMap<>(sheet.getLastRowNum());
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String name = row.getCell(0).getStringCellValue();
            String sex = row.getCell(1).getStringCellValue();
            String email = row.getCell(2).getStringCellValue();
            String phone = row.getCell(3).getNumericCellValue()+"";
            String idCard = row.getCell(4).getStringCellValue();
            String pwd = row.getCell(5).getStringCellValue();
            String stuff = row.getCell(6).getStringCellValue();
            String bank = row.getCell(7).getStringCellValue();
            String bankLow = row.getCell(8).getStringCellValue();
            String area = row.getCell(9).getStringCellValue();
            String testType = row.getCell(10).getStringCellValue();
            String serNo = row.getCell(11) == null ? "" : row.getCell(11).getStringCellValue();
            String content = row.getCell(12)== null ? "" : row.getCell(12).getStringCellValue();
            String site = row.getCell(13)== null ? "" : row.getCell(13).getStringCellValue();
            String group = row.getCell(14).getStringCellValue();
            map.put(group+ "_" + name,new StudentUserInfo(name,sex,email,phone,idCard,pwd,stuff,bank,bankLow,area,
                    testType, serNo, content,site,group));
        }
        return map;
    }
    private Map<String, StudentStudyInfo> convertSheet2StudyInfo(Sheet sheet){
        Map<String,StudentStudyInfo> map = new LinkedHashMap<>(sheet.getLastRowNum());
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            String name = row.getCell(1).getStringCellValue();
            String group = row.getCell(2).getStringCellValue();
            String studyProcess = row.getCell(3).getNumericCellValue()+"";

            double value = row.getCell(4).getNumericCellValue();
            String studyTime = convertSecondsToHMmSs((BigDecimal.valueOf(value * 24 * 60 * 60).longValue()));
            System.out.println();

            String studyCourse = row.getCell(5).getStringCellValue();
            String studyTest = row.getCell(6).getStringCellValue();
            map.put(group+ "_" + name,new StudentStudyInfo(name,group,studyProcess,studyTime,studyCourse,studyTest));
        }
        return map;
    }

    private String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60));
        return String.format("%d:%02d:%02d", h,m,s);}

    private List<StudentMergeInfo> mergeStudent(Map<String,StudentUserInfo> userInfoMap, Map<String,
                StudentStudyInfo> studyInfoMap){
        List<StudentMergeInfo> result = new ArrayList<>();
        userInfoMap.keySet().forEach(k -> {
            result.add(new StudentMergeInfo(userInfoMap.get(k),studyInfoMap.get(k)));
        });
        return result;
    }

    private Workbook getWorkbook(MultipartFile file) throws IOException {
        Workbook book;
        if (file.getOriginalFilename().contains("xlsx")) {
            book = new XSSFWorkbook(file.getInputStream());
        } else {
            // 当excel是2003时,创建excel2003
            book = new HSSFWorkbook(file.getInputStream());
        }
        return book;
    }

    private static HSSFWorkbook createFromStudentMergeInfo(List<StudentMergeInfo> infos){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("merge");
        HSSFRow columns = sheet.createRow(0);
        HSSFCell c0 = columns.createCell(0);
        c0.setCellValue("姓名");
        HSSFCell c1 = columns.createCell(1);
        c1.setCellValue("性别");
        HSSFCell c2 = columns.createCell(2);
        c2.setCellValue("邮箱");
        HSSFCell c3 = columns.createCell(3);
        c3.setCellValue("手机");
        HSSFCell c4 = columns.createCell(4);
        c4.setCellValue("身份证");
        HSSFCell c5 = columns.createCell(5);
        c5.setCellValue("密码");
        HSSFCell c6 = columns.createCell(6);
        c6.setCellValue("职位");
        HSSFCell c7 = columns.createCell(7);
        c7.setCellValue("行别");
        HSSFCell c8 = columns.createCell(8);
        c8.setCellValue("所属支行");
        HSSFCell c9 = columns.createCell(9);
        c9.setCellValue("地区");
        HSSFCell c10 = columns.createCell(10);
        c10.setCellValue("考试类型");
        HSSFCell c11 = columns.createCell(11);
        c11.setCellValue("序号");
        HSSFCell c12 = columns.createCell(12);
        c12.setCellValue("备注");
        HSSFCell c13 = columns.createCell(13);
        c13.setCellValue("网点");
        HSSFCell c14 = columns.createCell(14);
        c14.setCellValue("组");
        HSSFCell c15 = columns.createCell(15);
        c15.setCellValue("学习进度");
        HSSFCell c16 = columns.createCell(16);
        c16.setCellValue("学习总时长");
        HSSFCell c17 = columns.createCell(17);
        c17.setCellValue("完成节数/总小节");
        HSSFCell c18 = columns.createCell(18);
        c18.setCellValue("完成测试/总测试");

        for (int i = 0; i < infos.size(); i++) {
            StudentMergeInfo item = infos.get(i);
            HSSFRow row = sheet.createRow(i + 1);
            HSSFCell r0 = row.createCell(0);
            r0.setCellValue(item.getName());
            HSSFCell r1 = row.createCell(1);
            r1.setCellValue(item.getSex());
            HSSFCell r2 = row.createCell(2);
            r2.setCellValue(item.getEmail());
            HSSFCell r3 = row.createCell(3);
            r3.setCellValue(item.getPhone());
            HSSFCell r4 = row.createCell(4);
            r4.setCellValue(item.getIdCard());
            HSSFCell r5 = row.createCell(5);
            r5.setCellValue(item.getPwd());
            HSSFCell r6 = row.createCell(6);
            r6.setCellValue(item.getStuff());
            HSSFCell r7 = row.createCell(7);
            r7.setCellValue(item.getBank());
            HSSFCell r8 = row.createCell(8);
            r8.setCellValue(item.getBankLow());
            HSSFCell r9 = row.createCell(9);
            r9.setCellValue(item.getArea());
            HSSFCell r10 = row.createCell(10);
            r10.setCellValue(item.getTestType());
            HSSFCell r11 = row.createCell(11);
            r11.setCellValue(item.getSerNo());
            HSSFCell r12 = row.createCell(12);
            r12.setCellValue(item.getContent());
            HSSFCell r13 = row.createCell(13);
            r13.setCellValue(item.getSite());
            HSSFCell r14 = row.createCell(14);
            r14.setCellValue(item.getGroup());
            HSSFCell r15 = row.createCell(15);
            if(item.getStudyProcess()!=null) {
                r15.setCellValue(new BigDecimal(item.getStudyProcess()).multiply(new BigDecimal("100")) + "%");
            }
            HSSFCell r16 = row.createCell(16);
            r16.setCellValue(item.getStudyTime());
            HSSFCell r17 = row.createCell(17);
            r17.setCellValue(item.getStudyCourse());
            HSSFCell r18 = row.createCell(18);
            r18.setCellValue(item.getStudyTest());
        }
        return workbook;
    }
}
