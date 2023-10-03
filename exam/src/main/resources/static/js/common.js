function message(info){
    $.messager.show({
        title:'提示',
        msg: info,
        timeout:3000,
        showType:'show',
        icon:"info",
        style:{
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:'',
            textAlign:"center",
            width:'auto'
        }
    });
}

function messageError(info){
    $.messager.alert({
        title:'错误',
        msg: info,
        timeout:3000,
        showType:'show',
        icon:"error",
        style:{
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:'',
            textAlign:"center",
            height:'80px'
        }
    });
}

function addMainTab(item){
    var content = '<iframe scrolling="auto" frameborder="0"  src="'+item.innerPage+'" style="width:100%;height:100%;"></iframe>';

    var existFlag = $('#workSpaceTab').tabs("exists",item.text)
    if('问卷详情' === item.text){
        existFlag = false;
    }

    if(!existFlag) {
        $('#workSpaceTab').tabs('add', {
            title: item.text,
            closable: true,
            content: content
        });
    }else {
        $('#workSpaceTab').tabs("select",item.text);
    }
}

function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

var easyuiPanelOnMove = function(left, top) {
    var parentObj = $(this).panel('panel').parent();
    if (left < 0) {
        $(this).window('move', {
            left : 1
        });
    }
    if (top < 0) {
        $(this).window('move', {
            top : 1
        });
    }
    var width = $(this).panel('options').width;
    var height = $(this).panel('options').height;
    var right = left + width;
    var buttom = top + height;
    var parentWidth = parentObj.width();
    var parentHeight = parentObj.height();
    if(parentObj.css("overflow")=="hidden"){
        if(left > parentWidth-width){
            $(this).window('move', {
                "left":parentWidth-width
            });
        }
        if(top > parentHeight-height){
            $(this).window('move', {
                "top":parentHeight-height
            });
        }
    }
};
$.fn.panel.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;

function xcDateTimeFormatter(date){
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

function xcDateTimeParser(s){
    if (!s || s == -1 || s == 1){
        return new Date();
    }
    var ss = s.split(' ');
    var dateStr = ss[0].split('-');
    var timeStr = ss[1].split(':');

    var y = parseInt(dateStr[0],10);
    var M = parseInt(dateStr[1],10);
    var d = parseInt(dateStr[2],10);

    var h = parseInt(timeStr[0],10);
    var m = parseInt(timeStr[1],10);
    var s = parseInt(timeStr[2],10);

    if (!isNaN(y) && !isNaN(M) && !isNaN(d) && !isNaN(h) && !isNaN(m) && !isNaN(s)){
        return new Date(y,M-1,d,h,m,s);
    } else {
        return new Date();
    }
}