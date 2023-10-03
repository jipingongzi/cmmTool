function paperDetailInstanceInit() {
    var admin = JSON.parse(window.localStorage.getItem("admin"));
    var paperInstance = {}

    var paperInstanceId = getUrlParam("paperInstanceId");
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/exam/paper/instance/" + paperInstanceId,
        dataType: "json",
        beforeSend: function (request) {
            request.setRequestHeader("XC_ADMIN_TOKEN", admin.currentToken);
        },
        success: function (data) {
            paperInstance = data.data;
            buildPaperHtml(paperInstance);
        }
    })
}

function buildPaperHtml(vo){
    var paperInstance = vo.instanceDto;
    var editable = vo.editable;
    var paper = vo.paperVo;

    if(editable.editable === false){
        $("#editable").html(editable.info);
    }

    $("#theQuestionList").html("");

    $("#batchTitle").text(paper.batchTitle);
    $("#paperTitle").text(paper.title);
    $("#paperTitle2").text(paper.title);
    $("#questionNumber").text(paper.questionNumber);
    $("#point").text(paperInstance.point);
    $("#description").text(paper.description);

    $('#banks').combobox('select', paperInstance.bankCode);
    $('#startTime').timespinner('setValue', paperInstance.startTime);
    $('#endTime').timespinner('setValue', paperInstance.endTime);
    if(editable.editable === false){
        $('#banks').combobox('disable');
        $('#startTime').timespinner('disable');
        $('#endTime').timespinner('disable');
        $('#paperFile').filebox('disable');
    }
    for (let i = 0; i < paperInstance.fileList.length; i++) {
        var file = paperInstance.fileList[i];
        var fileHtml = "<p key='"+ file.description +"' url='"+file.url +"' style='margin-left: 30px;font-size:" +
            " smaller'>附件" + (i + 1) + "-" + fileAlign(file.description);

        if(editable.editable === false) {
            fileHtml +="<span class='fileSpan' onclick=previewQuestionFile('" + file.url + "','" + file.url + "')" +
                " style='margin-left:20px;color:#0099FF'>[预览附件]</span></p>";
        }else{
            fileHtml += "<span class='fileSpan'" +
                " onclick=removeQuestionFile(this,'" + file.url + "') style='margin-left:20px;color:" +
                " #CC2222'>[删除附件]</span>" +
                "<span class='fileSpan' onclick=previewQuestionFile('" + file.url + "','" + file.url + "')" +
                " style='margin-left:20px;color:#0099FF'>[预览附件]</span></p>";
        }
        $("#divFiles").append(fileHtml);
    }
    var html = "";
    var questionAddVoList = paper.questionAddVoList;
    for (i = 0; i < questionAddVoList.length; i++) {
        var question = questionAddVoList[i];
        var questionHtml = "<div type=" + question.type +" id="+question.questionTreeId+">\n" +
            "                    <strong><p>";
        questionHtml = questionHtml + question.title +" (" + question.point + "分)[" + getQtype(question.type) + "]</p></strong>";

        if(question.type === "INPUT"){
            questionHtml += "<div style='margin-bottom:20px'><input class=\"easyui-textbox\" type=\"text\" style=\"width:100%;\"></div>\n" +
                "<div>\n" +
                "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\"\n" +
                "                           iconCls=\"icon-add\" onclick=\"addFile(this)\">添加附件描述\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"fileRegion\">\n" +
                "                    </div>\n" +
                "                    <hr/>"
        }else if(question.type === "MULTIPLE_CHOICE"){
            questionHtml = questionHtml + "<form>";
            var optionHtml = "";
            var optionAddVoList = question.optionAddVoList;
            for (let j = 0; j < optionAddVoList.length; j++) {
                var option = optionAddVoList[j];
                optionHtml = optionHtml + "<div id="+option.optionTreeId+" style=\"margin-bottom:20px\">\n" +
                    "                        <input labelWidth=250 class=\"easyui-checkbox\" name=\"fruit\"" +
                    " value=\""+option.optionTreeId+"\" label=\""+ option.title +"\">\n" +
                    "                    </div>";
            }
            questionHtml = questionHtml + optionHtml + "</form><div>\n" +
                "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\"\n" +
                "                           iconCls=\"icon-add\" onclick=\"addFile(this)\">添加附件描述\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"fileRegion\">\n" +
                "                    </div>\n" +
                "                    <hr/>";
        }else {
            questionHtml = questionHtml + "<form>";
            var optionHtml = "";
            var optionAddVoList = question.optionAddVoList;
            for (let j = 0; j < optionAddVoList.length; j++) {
                var option = optionAddVoList[j];
                optionHtml = optionHtml + "<div id="+option.optionTreeId+" style=\"margin-bottom:20px\">\n" +
                    "                            <input width=250 labelWidth=180 class=\"easyui-radiobutton\"" +
                    " name=\"fruit\" value=\""+option.optionTreeId+"\" label=\"" + option.title + "\" >\n" +
                    "                        </div>";
            }
            questionHtml = questionHtml + optionHtml + "</form><div>\n" +
                "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\"\n" +
                "                           iconCls=\"icon-add\" onclick=\"addFile(this)\">添加附件描述\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"fileRegion\">\n" +
                "                    </div>\n" +
                "                    <hr/>";
        }
        questionHtml += "</div>"
        html = html + questionHtml;
    }

    $("#theQuestionList").append(html);
    $.parser.parse($("#theQuestionList"));
    fillAnswer(paperInstance.answerList,editable);
    $.parser.parse($("#theQuestionList"));
    if(editable.editable === false){
        $("#createBtn").linkbutton("disable");
        var btns = $("a.easyui-linkbutton");
        for (let i = 0; i < btns.length; i++) {
            $(btns[i]).linkbutton("disable")
        }

    }
}

function fillAnswer(answerList,editable) {
    var questionList = $("#theQuestionList").children("div");
    for (let i = 0; i < questionList.length; i++) {
        var questionDom = questionList[i];
        var questionType = $(questionDom).attr("type");
        var questionTreeId = $(questionDom).attr("id");
        var myAnswer = '';
        var answerFiles = [];
        for (let j = 0; j < answerList.length; j++) {
            var answer = answerList[j];
            if(answer.questionTreeId === questionTreeId){
                myAnswer = answer.answer;
                answerFiles = answer.fileList;
            }
        }
        if(questionType === "INPUT"){
            $(questionDom).children("div").children("input").textbox("setValue",myAnswer);
            if(editable.editable === false) {
                $(questionDom).children("div").children("input").textbox("disable");
            }
        }else if(questionType === "MULTIPLE_CHOICE"){
            var optionDomList = $(questionDom).children("form").children("div");
            for (let j = 0; j < optionDomList.length; j++) {
                if(myAnswer.includes($(optionDomList[j]).children("input").attr("value"))){
                    $(optionDomList[j]).children("input").checkbox("check");
                }
                if(editable.editable === false) {
                    $(optionDomList[j]).children("input").checkbox("disable");
                }
            }
        }else {
            var optionDomList = $(questionDom).children("form").children("div");
            for (let j = 0; j < optionDomList.length; j++) {
                if($(optionDomList[j]).children("input").attr("value") === myAnswer){
                    $(optionDomList[j]).children("input").radiobutton("check");
                }
                if(editable.editable === false) {
                    $(optionDomList[j]).children("input").radiobutton("disable");
                }
            }
        }
        //add file
        for (let j = 0; j < answerFiles.length; j++) {
            var answerFile = answerFiles[j];
            var fileHtml = "<p key='"+ answerFile.url +"' url='"+answerFile.url +"' style='margin-left:" +
                " 30px;font-size: smaller'>附件" + (j + 1) + "-" + fileAlign(answerFile.url) ;

            if(editable.editable === false) {
                fileHtml += "&nbsp;<input class=\"easyui-textbox\" type=\"text\" disabled value='" + answerFile.description + "' style=\"width:15%\"" +
                    " data-options=\"prompt:'附件描述'\"/>";;
                fileHtml +="<span class='fileSpan' onclick=previewQuestionFile('" + answerFile.url + "','" + answerFile.url + "')" +
                    " style='margin-left:20px;color:#0099FF'>[预览附件]</span></p>";
            }else{
                fileHtml += "&nbsp;<input class=\"easyui-textbox\" type=\"text\" value='" + answerFile.description + "' style=\"width:15%\"" +
                    " data-options=\"prompt:'附件描述'\"/>";
                fileHtml += "<span class='fileSpan'" +
                    " onclick=removeQuestionFile(this,'" + answerFile.url + "') style='margin-left:20px;color:" +
                    " #CC2222'>[删除附件]</span>" +
                    "<span class='fileSpan' onclick=previewQuestionFile('" + answerFile.url + "','" + answerFile.url + "')" +
                    " style='margin-left:20px;color:#0099FF'>[预览附件]</span></p>";
            }
            $(questionDom).children("div.fileRegion").append(fileHtml)
        }
    }
}

function addFile(obj){
    var fileHtml = "<p><label style=\"color: #3c8b3c\"><strong> * 上传附件：</strong></label>" +
        "<input class=\"easyui-filebox\" style=\"width:30%;margin-right: 10px\"/>&nbsp;" +
        "<input class=\"easyui-textbox\" type=\"text\" style=\"width:15%\"" +
        " data-options=\"prompt:'附件描述'\"/></p>";
    console.log(fileHtml)
    var fileDom = $(obj).parent().next("div.fileRegion").append(fileHtml);
    $.parser.parse($(obj).parent().next("div.fileRegion").children("p").last());

    $(obj).parent().next("div.fileRegion").children("p").last().children("input").first().filebox({
        buttonText:"选择文件",
        onChange:async function (e) {
            var file = $(this).filebox("files")[0];
            var fileType = fileAlign(file.name);
            if(fileType === '未知类型'){
                messageError("请上传图片或视频！");
                return ;
            }
            var resp = await uploadFile(file);
            $(this).parent().attr("url",resp.url);
            $(this).parent().attr("key",resp.name);

            $(this).parent().children("span.fileSpan").remove();
            $(this).parent().append("<span class='fileSpan'" +
                " onclick=removeQuestionFile(this,'"+resp.name+"') style='margin-left:20px;color:" +
                " #CC2222'>[删除附件]</span>" +
                "<span class='fileSpan' onclick=previewQuestionFile('" + resp.url + "','" + resp.name +"')" +
                " style='margin-left:20px;color:#0099FF'>[预览附件]</span>")
        }
    })
}

function removeQuestionFile(obj,key){
    $(obj).parent().remove();
}

function previewQuestionFile(url,key) {
    $("#previewW").window("open");
    if('图片' === fileAlign(key)){
        $("#video").hide();
        $("#picture").show();
        $("#picture").attr("src",url)
    }else {
        $("#picture").hide();
        $("#video").show();
        $("#video").attr("src",url)
    }
}

function getQtype(type) {
    if(type === 'INPUT'){
        return '问答题';
    }else if(type === 'SINGLE_CHOICE'){
        return '单选题';
    }else if(type === 'MULTIPLE_CHOICE'){
        return '多选题';
    }else if(type === 'JUDGMENT'){
        return '判断题';
    }else if(type == 'UN_SETTLED_CHOICE'){
        return '不定项选择题';
    }
    return '';
}

function create() {
    // $('#createBtn').linkbutton('disable');
    var paperInstanceId = getUrlParam("paperInstanceId");
    var paperInstance = {};
    paperInstance.id = paperInstanceId;
    paperInstance.bankCode = $('#banks').combobox('getValues').toString();
    paperInstance.startTime = $("#startTime").timespinner('getValue');
    paperInstance.endTime = $("#endTime").timespinner('getValue');

    if(paperInstance.bankCode === undefined || "" === paperInstance.bankCode) {
        messageError("请选择调查银行");
        return;
    }
    if(paperInstance.startTime === undefined || "" === paperInstance.startTime){
        messageError("请填写开始时间");
        return;
    }

    var fileDomList = $("#divFiles").children("p");
    var fileList = [];
    for (let i = 0; i < fileDomList.length; i++) {
        var fileDom = fileDomList[i];
        var file = {};
        file.url = $(fileDom).attr("url");
        file.description = $(fileDom).attr("key");
        file.type = getFileType(file.description);
        fileList.push(file);
    }
    paperInstance.fileList = fileList;

    var questionDomList = $("#theQuestionList").children("div");
    var answerList = [];
    for (let i = 0; i < questionDomList.length; i++) {
        var questionDom = questionDomList[i];
        var answer = {};
        answer.questionTreeId = $(questionDom).attr("id");
        var questionType = $(questionDom).attr("type");
        if(questionType === 'INPUT'){
            answer.answer = $(questionDom).children("div").children("input").textbox("getValue");
        }else if( questionType === 'MULTIPLE_CHOICE'){
            var optionDomList = $(questionDom).children("form").children("div");
            for (let j = 0; j < optionDomList.length; j++) {
                if($(optionDomList[j]).children("input").checkbox("options")["checked"]){
                    if(answer.answer) {
                        answer.answer += "," + $(optionDomList[j]).children("input").checkbox("options")["value"];
                    }else {
                        answer.answer = $(optionDomList[j]).children("input").checkbox("options")["value"];
                    }
                }
            }
        }else{
            var optionDomList = $(questionDom).children("form").children("div");
            for (let j = 0; j < optionDomList.length; j++) {
                if($(optionDomList[j]).children("input").radiobutton("options")["checked"]){
                    answer.answer = $(optionDomList[j]).children("input").radiobutton("options")["value"];
                    break;
                }
            }
        }
        if(answer.answer === undefined || "" === answer.answer){
            messageError("请完成所有题目答案");
            $('#createBtn').linkbutton('enable');
            return;
        }
        //get file
        var questionFileDoms = $(questionDom).children("div.fileRegion").children("p");
        var questionFileList = [];
        for (let j = 0; j < questionFileDoms.length; j++) {
            var questionFileDom = questionFileDoms[j];
            var questionFile = {};
            questionFile.url = $(questionFileDom).attr("url");
            questionFile.type = getFileType($(questionFileDom).attr("key"));
            questionFile.description = $(questionFileDom).children("input").last().textbox("getValue");
            questionFileList.push(questionFile);
        }
        answer.fileList = questionFileList;
        answerList.push(answer);
    }
    paperInstance.answerList = answerList;
    //submit
    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: "/admin/exam/paper/instance",
        data: JSON.stringify(paperInstance),
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                message(data.info)
            }else {
                $('#createBtn').linkbutton('enable');
                messageError(data.info)
            }
        }
    });
}

function getFileType(fileName) {
    if('图片' === fileAlign(fileName)){
        return 'PICTURE';
    }else if('视频' === fileAlign(fileName)){
        return 'VIDEO';
    }else {
        return "UNKNOW";
    }
}

function formatter1(date){
    if (!date){
        return '';
    }
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();

    var h = date.getHours();
    var min = date.getMinutes();
    var s = date.getSeconds();
    return formatNumber(y) + "-" + formatNumber(m) + "-" + formatNumber(d)
        + " " + formatNumber(h) + ":" + formatNumber(min) + ":" + formatNumber(s);
}
function formatNumber(num) {
    if(num < 10){
        return 0 + "" + num;
    }
    return num;
}