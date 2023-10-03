
var admin = JSON.parse(window.localStorage.getItem("admin"));
init();
function init(){

    $('#minStartTime').datetimebox({
        required: false,
        showSeconds: true
    });
    $('#maxStartTime').datetimebox({
        required: false,
        showSeconds: true
    });

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
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/admin/bank/top",
        dataType: "json",
        async: false,
        beforeSend: function(request) {
            request.setRequestHeader("XC_ADMIN_TOKEN",admin.currentToken);
            request.setRequestHeader("XC_ADMIN_BANK_CODE",admin.bankCode);
        },
        success: function(data){
            if(data.apiStatus == 0){
                var banks = data.data;
                $('#banks').combobox({
                    data:banks,
                    valueField:'code',
                    textField:'name'
                });
            }else {
                message(data.info)
            }
        }
    });
    $('#instanceTable').datagrid({
        url: "/admin/report/paper/instance/list",
        fitColumns: true,
        pagination: true,
        striped: true,
        method: "get",
        pageList: [2,5,10,20],
        pageSize: 10,
        idField:'id',
        columns: [[
            { field: 'id', title: '编号', width: 50, align: "center" ,formatter:function (value,row,index) {
                    return row.id.toString();
                }},
            { field: 'paperTitle', title: '问卷名', width: 50, align: "center" },
            { field: 'bankName', title: '调查银行', width: 50, align: "center" },
            { field: 'userName', title: '用户', width: 20, align: "center"},
            { field: 'startTime', title: '开始时间', width: 50, align: "center" },
            { field: 'endTime', title: '结束时间', width: 50, align: "center"},
            { field: 'status', title: '状态', width: 40, align: "center"},
            { field: 'questionNumber', title: '答题数', width: 15, align: "center"},
            { field: 'point', title: '得分', width: 15, align: "center"}
        ]],
        queryParams: {
            userName: $("#userName").val(),
            paperTitle: $("#paperTitle").val(),
            batchId: $('#batchs').combobox('getValues').toString(),
            bankCode: $('#banks').combobox('getValues').toString(),
            minPoint: $('#minPoint').val(),
            maxPoint: $("#maxPoint").val(),
            minStartTime: $('#minStartTime').datetimebox('getValue'),
            maxStartTime: $('#maxStartTime').datetimebox('getValue')
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
            instanceDetail(rowData.id);
        },
        onLoadSuccess:function(data){
        },
    });

}

function instanceList(){
    $('#instanceTable').datagrid('load',{
        userName: $("#userName").val(),
        paperTitle: $("#paperTitle").val(),
        batchId: $('#batchs').combobox('getValues').toString(),
        bankCode: $('#banks').combobox('getValues').toString(),
        minPoint: $("#minPoint").val(),
        maxPoint: $("#maxPoint").val(),
        minStartTime: $('#minStartTime').datetimebox('getValue'),
        maxStartTime: $('#maxStartTime').datetimebox('getValue')
    });
}

function instanceDetail(id){
    var tab = {};
    tab.text = "问卷实例详情";
    tab.innerPage = 'examPaperInstance-detail.html?paperInstanceId=' + id;
    window.parent.addMainTab(tab);
}
