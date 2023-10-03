function paperDetailInit() {
    var admin = JSON.parse(window.localStorage.getItem("admin"));
    var paper = {}

    if(previewFlag === 1){
        paper = previewPaper;
        buildPaperHtml(paper);
    }else {
        var paperId = getUrlParam("paperId");
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/admin/exam/paper/" + paperId,
            dataType: "json",
            beforeSend: function (request) {
                request.setRequestHeader("XC_ADMIN_TOKEN", admin.currentToken);
            },
            success: function (data) {
                paper = data.data;
                buildPaperHtml(paper);
            }
        })
    }
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
        var questionHtml = "<div id="+question.questionTreeId+">\n" +
            "                    <strong><p>";
        questionHtml = questionHtml + question.title +" (" + question.point + "分)[" + question.type + "]</p></strong>";

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
        }else if(question.type === "UN_SETTLED_CHOICE"){
            var optionHtml = "";
            var optionAddVoList = question.optionAddVoList;
            for (let j = 0; j < optionAddVoList.length; j++) {
                var option = optionAddVoList[j];
                optionHtml = optionHtml + "<div id="+option.optionTreeId+" style=\"margin-bottom:20px\">\n" +
                    "                        <input labelWidth=250 class=\"easyui-checkbox\" name=\"fruit\"" +
                     " value=\"Apple\" label=\""+ option.title +"("+ option.point +"分)\">\n" +
                    "                    </div>";
            }
            questionHtml = questionHtml + optionHtml + "<div>\n" +
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
                if(option.correctFlag == 'true' || (option.correctFlag && option.correctFlag != 'false')){
                    optionHtml = optionHtml + "<div id="+option.optionTreeId+" style=\"margin-bottom:20px\">\n" +
                        "                            <input width=250 labelWidth=180 class=\"easyui-radiobutton\"" +
                         " name=\"fruit\" value=\"Apple\" label=\"" + option.title + "\" >\n" +
                        "<label style='color: #3c8b3c'>正确选项</label></div>";

                    console.log(optionHtml)
                }else {
                    optionHtml = optionHtml + "<div id="+option.optionTreeId+" style=\"margin-bottom:20px\">\n" +
                        "                            <input width=250 labelWidth=180 class=\"easyui-radiobutton\"" +
                         " name=\"fruit\" value=\"Apple\" label=\"" + option.title + "\" >\n" +
                        "                        </div>";
                }
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
        html = html + questionHtml;
    }
    $("#theQuestionList").append(html);
    $.parser.parse($("#theQuestionList"));
}

function addFile(obj){
    var fileHtml = "<p>\n" +
        "                        <label style=\"color: #3c8b3c\"><strong> * 上传附件：</strong></label>\n" +
        "                        <input class=\"easyui-filebox\" style=\"width:30%\"" +
         " data-options=\"buttonText:'选择附件'\">\n" +
        "                        <input class=\"easyui-textbox\" type=\"text\" style=\"width:15%;\"" +
         " data-options=\"prompt:'附件描述'\">\n" +
        "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\"" +
         "  style=\"width:23%\" iconCls=\"icon-cancel\" onclick=\"removeFile(this)\">删除附件</a>\n" +
        "                    </p>";
    $(obj).parent().next("div.fileRegion").append(fileHtml);
    $.parser.parse($(obj).parent().next("div.fileRegion").children("p").last());
}
function removeFile(obj){
    $(obj).parents("p").remove();
}