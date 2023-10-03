var admin = JSON.parse(window.localStorage.getItem("admin"));
var paperId = 0;

var previewPaper = {};
var previewFlag = 0;

paperEditorInit();

function paperEditorInit() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/exam/batch/list",
        dataType: "json",
        async: false,
        beforeSend: function (request) {
            request.setRequestHeader("XC_ADMIN_TOKEN", admin.currentToken);
        },
        success: function (data) {
            if (data.apiStatus == 0) {
                var groups = data.data.content;
                $('#batchs').combobox({
                    data: groups,
                    valueField: 'id',
                    textField: 'title'
                });
            } else {
                message(data.info)
            }
        }
    });

    $(function () {
        $(document).bind('contextmenu', function (e) {
            e.preventDefault();
            $('#mm').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
        });
    });
}

function preview(){
    previewFlag = 1;
    previewPaper = buildPaper();
    $('#w').window('open');
    paperDetailInit();
}

function buildPaper(){
    var paper = {};
    paper.title = $("#title").val();
    paper.batchId =  $('#batchs').combobox('getValues').toString();
    paper.batchTitle = $('#batchs').combobox('getText').toString();
    paper.description = $("#description").val();

    var point = 0;
    var questionNumber = 0;
    if(paperId != 0){
        paper.id = paperId;
    }

    var questionList = [];
    paper.questionAddVoList = questionList;
    var questionListDom = $("#questionList").children("div.question").toArray();
    for (i = 0; i < questionListDom.length; i++) {
        var question = {};
        questionList[i] = question;
        var questionDom = questionListDom[i];

        question.type = $(questionDom).attr("qType");
        question.point = $(questionDom).children("span.spinner").children("input.textbox-value").val();
        question.title = $(questionDom).children("span.easyui-fluid").children("input.textbox-value").val();

        point += Number(question.point);
        questionNumber++;

        var optionList = [];
        question.optionAddVoList = optionList;
        var optionListDom = $(questionDom).children("div").last().children("form.optionForm").children("div.optionDiv").toArray();
        for (let j = 0; j < optionListDom.length; j++) {
            var option = {};
            optionList[j] = option;
            var optionDom = optionListDom[j];

            option.correctFlag = $(optionDom).attr("correctFlag");
            option.point = $(optionDom).children("span.spinner").children("input.textbox-value").val()
            option.title = $(optionDom).children("span.textbox").children("input.textbox-value").val()
        }
    }
    paper.point = point;
    paper.questionNumber = questionNumber;
    return paper;
}

function savePaper(obj){
    var paper = buildPaper();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/admin/exam/paper",
        data: JSON.stringify(paper),
        dataType: "json",
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                message(data.info)
                paperId = data.data;
                $(obj).linkbutton({
                    text: '更新'
                });
            }else {
                messageError(data.info)
            }
        }
    });
}

function clear(){
    $("#title").text("");
    $("#batchs").text("");
    $("#description").text("");
}

function showMenu(){
    $('#mm').menu('show');
}

function markCorrect(obj){
    if($(obj).parents("div.optionDiv").attr("correctFlag") == undefined){
        $(obj).linkbutton({
            text: '取消标记正确'
        });
        $(obj).parents("div.optionDiv").attr("correctFlag",true);
        return;
    }
    if($(obj).parents("div.optionDiv").attr("correctFlag") == 'true'){
        $(obj).linkbutton({
            text: '标记正确'
        });
        $(obj).parents("div.optionDiv").attr("correctFlag",false)
    }else {
        $(obj).linkbutton({
            text: '取消标记正确'
        });
        $(obj).parents("div.optionDiv").attr("correctFlag",true)
    }
}

function addOption(obj) {
    var optionHtml;
    if ("UN_SETTLED_CHOICE" == $(obj).parent().parent().attr("qType")) {
        optionHtml = "<div class=\"optionDiv\" correctFlag=\'true\'>\n" +
            "                                <input class=\"easyui-radiobutton\" name=\"fruit\" value=\"Apple\">\n" +
            "                                <input class=\"easyui-textbox\" name=\"fruit\" data-options=\"prompt:'描述'\">\n" +
            "                                <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onclick=\"removeOption(this)\">删除选项</a>\n" +
            "                               分数：<input class=\"easyui-numberspinner \" style=\"width:80px;display: block\"\n" +
            "                            required=\"required\" data-options=\"min:1,max:10,editable:false\">\n" +
            "                            </div>";
    } else {
        optionHtml = "<div class=\"optionDiv\" correctFlag=\'false\'>\n" +
        "                                <input class=\"easyui-radiobutton\" name=\"fruit\" value=\"Apple\">\n" +
        "                                <input class=\"easyui-textbox\" name=\"fruit\" data-options=\"prompt:'描述'\">\n" +
        "                                <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onclick=\"removeOption(this)\">删除选项</a>\n" +
        "                                <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" onclick=\"markCorrect(this)\">标记正确</a>\n" +
        "                            </div>";
    }
    var appendDoc = $(obj).parent().next().children("form.optionForm").append(optionHtml);
    $.parser.parse($(obj).parent().next().children("form.optionForm").children("div.optionDiv").last());
    return appendDoc;
}

function removeOption(obj){
    $(obj).parents("div.optionDiv").remove();
}

function removeQuestion(obj){
    $(obj).parents("div.question").remove();
}

function getLastQuestionId(){
    return $("div.question:last").attr("id");
}

function addQuestion(type){
    var appendStr = "";
    if(1 == type){
    appendStr = "<div class=\"question\" qType=\"INPUT\">\n" +
    "                    <h5>主观题</h5>\n" +
    "                    题目：<input class=\"easyui-textbox\"\n" +
    "                           style=\"width:90%;height:32px\" data-options=\"prompt:'题目'\"/>\n" +
    "                    <div style=\"margin: 10px\"></div>\n" +
    "                    分数：<input class=\"easyui-numberspinner \" style=\"width:80px;display: block\"\n" +
    "                           required=\"required\" data-options=\"min:1,max:10,editable:false\">\n" +
    "                    <div>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-cancel\" style=\"margin-top: 10px\" onclick=\"removeQuestion(this)\">\n" +
    "                            撤销</a>\n" +
    "                    </div>\n" +
    "                </div>";
}else if(2 == type){
    appendStr = "<div class=\"question\" qType=\"JUDGMENT\">\n" +
    "                    <h5>判断题</h5>\n" +
    "                    题目：<input class=\"easyui-textbox\"\n" +
    "                              style=\"width:90%;height:32px\" data-options=\"prompt:'题目'\"/>\n" +
    "                    <div style=\"margin: 10px\"></div>\n" +
    "                    分数：<input class=\"easyui-numberspinner \" style=\"width:80px;display: block\"\n" +
    "                              required=\"required\" data-options=\"min:1,max:10,editable:false\">\n" +
    "                    <div>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-cancel\" style=\"margin-top: 10px\" onclick=\"removeQuestion(this)\">\n" +
    "                            撤销</a>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" style=\"margin-top: 10px\" onclick=\"addOption(this)\">\n" +
    "                            添加选项</a>\n" +
    "                    </div>\n" +
    "                    <div>\n" +
    "                        <form class=\"optionForm\">\n" +
    "                        </form>\n" +
    "                    </div>\n" +
    "                </div>";
}else if(3 == type){
    appendStr = "<div class=\"question\" qType=\"SINGLE_CHOICE\">\n" +
    "                    <h5>单选题</h5>\n" +
    "                    题目：<input class=\"easyui-textbox\"\n" +
    "                              style=\"width:90%;height:32px\" data-options=\"prompt:'题目'\"/>\n" +
    "                    <div style=\"margin: 10px\"></div>\n" +
    "                    分数：<input class=\"easyui-numberspinner \" style=\"width:80px;display: block\"\n" +
    "                              required=\"required\" data-options=\"min:1,max:10,editable:false\">\n" +
    "                    <div>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-cancel\" style=\"margin-top: 10px\" onclick=\"removeQuestion(this)\">\n" +
    "                            撤销</a>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" style=\"margin-top:\n" +
    "                        10px\" onclick=\"addOption(this)\">\n" +
    "                            添加选项</a>\n" +
    "                    </div>\n" +
    "                    <div>\n" +
    "                        <form class=\"optionForm\">\n" +
    "                        </form>\n" +
    "                    </div>\n" +
    "                </div>";
}else if(4 == type){
    appendStr = " <div class=\"question\" qType=\"MULTIPLE_CHOICE\">\n" +
    "                    <h5>多选题</h5>\n" +
    "                    题目：<input class=\"easyui-textbox\"\n" +
    "                              style=\"width:90%;height:32px\" data-options=\"prompt:'题目'\"/>\n" +
    "                    <div style=\"margin: 10px\"></div>\n" +
    "                    分数：<input class=\"easyui-numberspinner \" style=\"width:80px;display: block\"\n" +
    "                              required=\"required\" data-options=\"min:1,max:10,editable:false\">\n" +
    "                    <div>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-cancel\" style=\"margin-top: 10px\" onclick=\"removeQuestion(this)\">\n" +
    "                            撤销</a>\n" +
    "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" style=\"margin-top:\n" +
    "                        10px\" onclick=\"addOption(this)\">\n" +
    "                            添加选项</a>\n" +
    "                    </div>\n" +
    "                    <div>\n" +
    "                        <form class=\"optionForm\">\n" +
    "                        </form>\n" +
    "                    </div>\n" +
    "                </div>";
}else if(5 == type){
        appendStr = " <div class=\"question\" qType=\"UN_SETTLED_CHOICE\">\n" +
            "                    <h5>不定项选则题</h5>\n" +
            "                    题目：<input class=\"easyui-textbox\"\n" +
            "                              style=\"width:90%;height:32px\" data-options=\"prompt:'题目'\"/>\n" +
            "                    <div style=\"margin: 10px\"></div>\n" +
            "                    分数：<input class=\"easyui-numberspinner \" style=\"width:80px;display: block\"\n" +
            "                              required=\"required\" data-options=\"min:1,max:10,editable:false\">\n" +
            "                    <div>\n" +
            "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-cancel\" style=\"margin-top: 10px\" onclick=\"removeQuestion(this)\">\n" +
            "                            撤销</a>\n" +
            "                        <a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" iconCls=\"icon-add\" style=\"margin-top:\n" +
            "                        10px\" onclick=\"addOption(this)\">\n" +
            "                            添加选项</a>\n" +
            "                    </div>\n" +
            "                    <div>\n" +
            "                        <form class=\"optionForm\">\n" +
            "                        </form>\n" +
            "                    </div>\n" +
            "                </div>";
    }
    var appendDoc = $("#questionList").append(appendStr);
    $.parser.parse(appendDoc.children("div.question").last());
    return appendDoc;
}