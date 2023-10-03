function paperDetailInit() {
    var user = JSON.parse(window.localStorage.getItem("user"));
    var paper = {}

    var paperId = getUrlParam("paperId");
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/user/exam/paper/" + paperId,
        dataType: "json",
        beforeSend: function (request) {
            request.setRequestHeader("XC_USER_TOKEN", user.currentToken);
        },
        success: function (data) {
            paper = data.data;
            buildPaperHtml(paper);
        }
    })
}

function buildPaperHtml(paper){
    $("#theQuestionList").html("");
    $("#batchTitle").text(paper.batchTitle);
    $("#paperTitle").text(paper.title);
    $("#paperTitle2").text(paper.title);
    $("#questionNumber").text(paper.questionNumber);
    $("#point").text(paper.point);
    $("#description").text(paper.description);

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
}

function addFile(obj){
    var fileHtml = "<p><label style=\"color: #3c8b3c\"><strong> * 上传附件：</strong></label>" +
         "<input class=\"easyui-filebox\" style=\"width:30%;margin-right: 10px\"/>&nbsp;" +
         "<input class=\"easyui-textbox\" type=\"text\" style=\"width:15%\"" +
          " data-options=\"prompt:'附件描述'\"/></p>";
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
    var paperId = getUrlParam("paperId");
    var paperInstance = {};
    paperInstance.paperId = paperId;
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
        type: "POST",
        contentType: "application/json",
        url: "/user/exam/paper/instance",
        data: JSON.stringify(paperInstance),
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("XC_USER_TOKEN",user.currentToken);
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

// function formatter1(date){
//     if (!date){
//         return '';
//     }
//     var y = date.getFullYear();
//     var m = date.getMonth() + 1;
//     var d = date.getDate();
//
//     var h = date.getHours();
//     var min = date.getMinutes();
//     var s = date.getSeconds();
//     return formatNumber(y) + "-" + formatNumber(m) + "-" + formatNumber(d)
//     + " " + formatNumber(h) + ":" + formatNumber(min) + ":" + formatNumber(s);
// }
// function formatNumber(num) {
//     if(num < 10){
//         return 0 + "" + num;
//     }
//     return num;
// }