
var admin = JSON.parse(window.localStorage.getItem("admin"));
init();
function init(){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/exam/batch/list?row=2000&page=1",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                var batchs = data.data.content;
                $('#batchs').combobox({
                    data:batchs,
                    valueField:'id',
                    textField:'title'
                });
            }else {
                message(data.info)
            }
        }
    });
    $('#paperTable').datagrid({
        url: "/admin/exam/paper/list",
        fitColumns: true,
        pagination: true,
        striped: true,
        method: "get",
        pageList: [2,5,10,20],
        pageSize: 10,
        idField:'id',
        columns: [[
            { field: 'id', title: '编号', width: 60, align: "center" ,formatter:function (value,row,index) {
                    return row.id.toString();
                }},
            { field: 'title', title: '问卷名', width: 60, align: "center" },
            { field: 'description', title: '描述', width: 60, align: "center" },
            { field: 'batchTitle', title: '批次', width: 50, align: "center"},
            { field: 'status', title: '状态', width: 30, align: "center" },
            { field: 'point', title: '总分数', width: 20, align: "center"},
            { field: 'questionNumber', title: '总题数', width: 20, align: "center"},
            {field:'operate',title:'操作',align:'center',width:$(this).width()*0.07,
                formatter:function(value,rowData,rowIndex){
                    var str =
                        '<a href="#" name="edit" class="easyui-linkbutton" onclick="openEditTab(\''+rowData.id+'\')"></a>' +
                        '<a href="#" name="enable" class="easyui-linkbutton" onclick="enable(\''+rowData.id+'\')"></a>' +
                        '<a href="#" name="mapping" class="easyui-linkbutton" onclick="openMappingWindow(\''+rowData.id+'\')"></a>' +
                        '<a href="#" name="moreAct" class="easyui-linkbutton" onclick="moreAction(\''+rowData.id+'\')"></a>';
                    return str;
                }}
        ]],
        queryParams: {
            title: $("#name").val(),
            batchId: $('#batchs').combobox('getValues').toString()
        },
        headers:{
            "XC_ADMIN_TOKEN": admin.currentToken
        },
        loadFilter: function(data){
            if (data.apiStatus == 0){
                var page = {};
                page.total = data.data.totalElements;
                page.rows = data.data.content;
                return page;
            } else {
                message(data.info)
            }
        },
        onDblClickRow:function (rowIndex, rowData){
            paperDetail(rowData.id);
        },
        onLoadSuccess:function(data){
            $("a[name='edit']").linkbutton({text:'编辑',plain:true,iconCls:'icon-edit'});
            $("a[name='enable']").linkbutton({text:'启用',plain:true,iconCls:'icon-ok'});
            $("a[name='mapping']").linkbutton({text:'分配',plain:true,iconCls:'icon-man'});
            $("a[name='moreAct']").linkbutton({text:'更多',plain:true,iconCls:'icon-more'});
        },
    });
}

function paperList(){
    $('#paperTable').datagrid('load',{
        title: $("#name").val(),
        batchId: $('#batchs').combobox('getValues').toString()
    });
}

function paperDetail(id){
    var tab = {};
    tab.text = "问卷详情";
    tab.innerPage = 'examPaper-detail.html?paperId=' + id;
    window.parent.addMainTab(tab);
}

function editor() {
    var tab = {};
    tab.text = "创建问卷";
    tab.innerPage = 'examPaper-editor.html';
    window.parent.addMainTab(tab);
}

function openMappingWindow(id){
    $("#mappingPaper").text(id);
    $("#mappingWindow").window('open');
}

function mapping(){
    var paperId = $("#mappingPaper").text();
    var groupId = $('#groups').combobox('getValues').toString();
    var groupIds = [];
    groupIds[0] = groupId;
    var map = {};
    map.paperId = paperId;
    map.userGroupIdList = groupIds;
    $.messager.confirm('Confirm', '确定分配该问卷吗?', function(r){
        if (r){
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/admin/exam/paper/mapping",
                dataType: "json",
                async: false,
                data: JSON.stringify(map),
                beforeSend: function(request) {
                    request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
                },
                success: function(data){
                    if(data.apiStatus == 0){
                        message(data.info);
                        paperList();
                    }else {
                        message(data.info)
                    }
                }
            });
        }
    });
}

function openEditTab(id){
    var tab = {};
    tab.text = "问卷编辑";
    tab.innerPage = 'examPaper-update.html?paperId=' + id;
    window.parent.addMainTab(tab);
}

function enable(id){
    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: "/admin/exam/paper/completion/" + id,
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                message(data.info);
                paperList();
            }else {
                message(data.info)
            }
        }
    });
}

function remove(obj){
    var id = $(obj).parent().attr("paperId")
    $.messager.confirm('Confirm', '确定删除该问卷吗?', function(r){
        if (r){
            $.ajax({
                type: "DELETE",
                contentType: "application/json",
                url: "/admin/exam/paper/" + id,
                dataType: "json",
                async: false,
                beforeSend: function(request) {
                    request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
                },
                success: function(data){
                    if(data.apiStatus == 0){
                        message(data.info);
                        paperList();
                    }else {
                        message(data.info)
                    }
                }
            });
        }
    });
}

function moreAction(id){
    $('#moreActW').attr("paperId",id);
    $('#moreActW').menu('show', {
        left: window.event.x,
        top: window.event.y
    });
}
function openBatchMappingWindow(obj){
    $("#batchUserName").textbox('clear');
    $("#userSearchDiv").html('');
    $("#batchMappingWindow").window('restore');

    var id = $(obj).parent().attr("paperId");
    $("#batchMappingPaper").text(id);
    $("#batchMappingWindow").window('open');

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/user/group/list",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                var groups = data.data;
                $('#batchGroups').combobox({
                    data:groups,
                    valueField:'id',
                    textField:'title'
                });
            }else {
                message(data.info)
            }
        }
    });

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/exam/paper/mapping/info/" + id,
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                var mappingInfo = data.data;
                for (let i = 0; i < mappingInfo.groupList.length; i++) {
                    mappingInfo.groupList[i].selected = true;
                }
                for (let i = 0; i < mappingInfo.userList.length; i++) {
                    mappingInfo.userList[i].selected = true;
                }
                $('#batchOldGroupTag').tagbox({
                    valueField: 'groupId',
                    textField: 'groupName',
                    data: mappingInfo.groupList,
                    onRemoveTag: function (value) {
                        var groupIds = [];
                        groupIds[0] = value;
                        var map = {};
                        map.paperId = id;
                        map.userGroupIdList = groupIds;
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: "/admin/exam/paper/unMapping",
                            dataType: "json",
                            async: false,
                            data: JSON.stringify(map),
                            beforeSend: function(request) {
                                request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
                            },
                            success: function(data){
                                if(data.apiStatus == 0){
                                    message(data.info);
                                    var tags = $('#batchOldGroupTag').combobox("getData");
                                    var newTags = [];
                                    for (let i = 0; i < tags.length; i++) {
                                        if(tags[i].groupId === value){
                                            continue;
                                        }
                                        newTags.push(tags[i])
                                    }
                                    $('#batchOldGroupTag').tagbox("loadData",newTags);
                                }else {
                                    message(data.info)
                                }
                            }
                        });
                    }
                })
                $('#batchOldUserTag').tagbox({
                    valueField: 'userId',
                    textField: 'userName',
                    data: mappingInfo.userList,
                    onRemoveTag:function (value) {
                        var userIdList = [];
                        userIdList[0] = value;
                        var map = {};
                        map.paperId = id;
                        map.userIdList = userIdList;
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: "/admin/exam/paper/unMapping",
                            dataType: "json",
                            async: false,
                            data: JSON.stringify(map),
                            beforeSend: function(request) {
                                request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
                            },
                            success: function(data){
                                if(data.apiStatus == 0){
                                    message(data.info);
                                    var tags = $('#batchOldUserTag').combobox("getData");
                                    var newTags = [];
                                    for (let i = 0; i < tags.length; i++) {
                                        if(tags[i].userId === value){
                                            continue;
                                        }
                                        newTags.push(tags[i])
                                    }
                                    $('#batchOldUserTag').tagbox("loadData",newTags);
                                }else {
                                    message(data.info)
                                }
                            }
                        });
                    }
                })
                
            }else {
                message(data.info)
            }
        }
    });
}
function exportInstance(obj){
    var id = $(obj).parent().attr("paperId");
    var url = "/admin/exam/paper/"+id+"/export" ;
    var fileName = "testAjaxDownload.txt";
    var form = $("<form></form>").attr("action", url).attr("method", "post");
    form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", fileName));
    form.appendTo('body').submit().remove();
}
function disablePaper(obj){
    var id = $(obj).parent().attr("paperId");
    $.ajax({
        type: "PUT",
        contentType: "application/json",
        url: "/admin/exam/paper/disable/" + id,
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                message(data.info);
                paperList();
            }else {
                message(data.info)
            }
        }
    });
}

function searchMappingCandidate(){
    var paperId = $("#batchMappingPaper").text();
    var userName = $("#batchUserName").val();
    $("#userSearchDiv").html("");
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/exam/paper/mapping/"+ paperId +"/candidate?userName=" + userName,
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                var users = data.data;
                for (let i = 0; i < users.length; i++) {
                    var user = users[i];
                    var htmlStr = "<p>\n" +
                        "<label style='width: 10%;display: inline-block'>"+user.userName+"</label>" +
                        "<label style=\"margin-left: 10px;margin-right: 10px\">"+user.idNo+"</label>\n" +
                        "<a href=\"javascript:void(0)\" class=\"easyui-linkbutton\" style=\"width: 20%\" " +
                        "onclick=\"mappingCandidate(this,"+user.userId+",'"+ user.userName+"')\"\n" +
                        "               iconCls=\"icon-man\">分配\n" +
                        "            </a>\n" +
                        "        </p>";
                    var candidate = $("#userSearchDiv").append(htmlStr);
                    $.parser.parse(candidate);
                }
            }else {
                message(data.info)
            }
        }
    });
}
function mappingCandidate(obj,userId,userName){
    var paperId = $("#batchMappingPaper").text();
    var userIdList = [];
    userIdList[0] = userId;
    var map = {};
    map.paperId = paperId;
    map.userIdList = userIdList;
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/admin/exam/paper/mapping",
        dataType: "json",
        async: false,
        data: JSON.stringify(map),
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
        },
        success: function(data){
            if(data.apiStatus == 0){
                message(data.info);
                var newTag = {};
                newTag.userId = userId;
                newTag.userName = userName;
                newTag.selected = true;
                var tags = $('#batchOldUserTag').combobox("getData");
                tags[tags.length] = newTag;
                $('#batchOldUserTag').tagbox("setValues",tags);
                $(obj).parent("p").remove();
            }else {
                message(data.info)
            }
        }
    });
}

$("#batchGroups").combobox({
    onSelect: function(record){
        var flag = false;
        var oldGroups = $('#batchOldGroupTag').combobox("getValues");
        for (let i = 0; i < oldGroups.length; i++) {
            if(oldGroups[i] === record.id){
                flag = true;
                break;
            }
        }
        if(!flag){
            var groupIds = [];
            groupIds[0] = record.id;;
            var map = {};
            map.paperId = $("#batchMappingPaper").text();
            map.userGroupIdList = groupIds;
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "/admin/exam/paper/mapping",
                dataType: "json",
                async: false,
                data: JSON.stringify(map),
                beforeSend: function(request) {
                    request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
                },
                success: function(data){
                    if(data.apiStatus == 0){
                        message(data.info);
                        var newTag = {};
                        newTag.groupId = record.id;
                        newTag.groupName = record.title;
                        newTag.selected = true;
                        var tags = $('#batchOldGroupTag').combobox("getData");
                        tags[tags.length] = newTag;
                        $('#batchOldGroupTag').tagbox("setValues",tags);
                    }else {
                        message(data.info)
                    }
                }
            });
        }
    }
});
