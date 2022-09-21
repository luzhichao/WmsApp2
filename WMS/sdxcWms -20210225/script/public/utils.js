/**
 *@authour:qiyuan.xie
 *@createDate : 2018-05-09
 *@desc:获取url
 */
function getServerUrl() {
    var serverUrl = localStorage.getItem('serverUrl');
    var serverIp = localStorage.getItem('serverIp');
    var serverPort = localStorage.getItem('serverPort');
    var serverProject = localStorage.getItem('serverProject');
    return "http://" + serverIp + ":" + serverPort + "/" + serverProject + "/";
}

function getUrl(url) {
    var un = localStorage.getItem('un'), pw = localStorage.getItem('pw');
    var mWCFlag = localStorage.getItem('mWCFlag'), datarole = localStorage.getItem('workCenterGid');
    if (mWCFlag != "-1" && datarole != null) {
        if (un != '' && un != null) {
            return getServerUrl() + "autoLoginController!login.m?_u=" + un + "&_p=" + pw + "&_dr=" + datarole + "&_to=" + url;
        } else {
            return url;
        }
    } else {
        if (un != '' && un != null) {
            return getServerUrl() + "autoLoginController!login.m?_u=" + un + "&_p=" + pw + "&_to=" + url;
        } else {
            return url;
        }
    }

}

/**
 * 刷新页面
 */
function reloadWin() {
    api.confirm({
        title: '提示',
        msg: '是否刷新当前菜单？',
        buttons: ['确定', '取消']
    }, function (ret) {
        if (ret.buttonIndex == 1) {
            window.location.reload();
        }
    });
}

/**
 * 格式化条码（1.逗号中英文统一  2.前后空格去除）
 * @param barCode
 * @returns {string | *}
 */
function formatBarCode(barCode) {
    barCode = barCode.replace(/，/g, ',');
    barCode = $.trim(barCode);
    // barCode = barCode.replace(/\s+/g, '');
    return barCode;
}

/**
 * 验证条码格式
 * @param barCode
 * @returns {boolean}
 */
function validateBarCode(barCode) {
    var arr = barCode.split(',');
    var msg = "";
    var isError = false;

    //校验
    if (arr.length != 8) {
        msg = "内容不完整！";
        isError = true;
    }

    // if (obj.snCode.length !== 6) {
    //     msg = "流水码长度不对！";
    //     isError = true;
    // }


    //
    // if (obj.supplierCode.length !== 9) {
    //     msg += "供应商编码长度不对！";
    //     isError = true;
    //     $("#supplierCode").css({'color': 'red'});
    // }
    //
    // var dateFormat = /^(\d{4})-(\d{2})-(\d{2})$/;
    // if (!dateFormat.test(document.getElementById("supplyDate").value)) {
    //     msg += "供货日期格式不正确！";
    //     isError = true;
    //     $("#supplyDate").css({'color': 'red'});
    // }
    // if (!dateFormat.test(document.getElementById("proDate").value)) {
    //     msg += "生产日期格式不正确！";
    //     isError = true;
    //     $("#proDate").css({'color': 'red'});
    // }
    //
    if (isError) {
        api.toast({msg: '条码格式错误：' + msg, duration: 3000, location: 'middle'})
        return false
    }
}

//通过库位编码查找库位
function getWorkCellByWorkCellCode(workCellCode) {
    var retObj = {};
    $.ajax({
        type: "POST",
        //TODO 代码整合 url 用commons.js里的
        url: getUrl('padWmsController!getWorkCellByWorkCellCode.m'),
        dataType: "json",
        data: {
            workCellCode: workCellCode
        },
        async: false,
        success: function (ret) {
            retObj = ret;
        },
        error: function (e) {
            console.log(JSON.stringify(e));
        }
    });
    return retObj;
}

//通过物料编码查找物料
function getMrlByMrlCode(mrlCode) {
    var retObj = {};
    $.ajax({
        type: "POST",
        url: getUrl('padWmsController!getMrlByMrlCode.m'),
        dataType: "json",
        data: {
            mrlCode: mrlCode
        },
        async: false,
        success: function (ret) {
            retObj = ret;
        },
        error: function (e) {
            console.log(JSON.stringify(e));
        }
    });
    return retObj;
}

var barCodeArr = [];

/**
 * 验证: 之前是否已扫描该条码
 * @returns {boolean}
 */
function checkIsSameBarcode(barCode) {
    var isScaned = false;
    if ($.inArray(barCode, barCodeArr) == -1) {
        barCodeArr.push(barCode);
    } else {
        isScaned = true;
    }
    return isScaned;
}


function deleteBarcode() {
    barCodeArr.pop();
}

/**
 *2010-05-25 colin 根据tab3来判断条码是否已被扫描
 * 验证: 之前是否已扫描该条码
 * @returns {boolean}
 */
function checkIsSameBarcodeByTab3(barCode) {
    var isScaned = false;
    var tableData3 = $("#bootstrap-tab-3").bootstrapTable('getData');
    var resultTab3 = $.grep(tableData3, function (val) {
        return (val.barCode === barCode)
    });
    if (resultTab3.length > 0) {
        isScaned = true;
    }
    return isScaned;
}


function cleanUpBarcodeArr() {
    barCodeArr = [];
}


/**
 * 验证table中是否包含扫描条码
 * @param barCode   条码
 * @param arr       table中已有数据(必须包含条码字段barCode)
 * @returns {boolean}
 */
function checkIsInBarCodeArr(barCode, arr) {
    var isIn = false;
    for (var i = 0; i < arr.length; i++) {
        if (arr[i].barCode == barCode) {
            isIn = true;
            api.toast({msg: '条码 【' + barCode + '】 已被扫描!', duration: 3000, location: 'middle'});
            break;
        }
    }
    return isIn;
}

/**
 * 联动时，判断是否存在于table中  物料和批次要吻合
 */
function isInTableData(obj, tableData) {

    console.log("----->tableDataLength"+tableData.length);
    var isIn = false;
    var isMrlSame = false;
    var isLotSame = false;
    var isWorkCellSame = false;
    for (var i = 0; i < tableData.length; i++) {
        if (obj.mrlCode == tableData[i].mrlCode) {
            //存在批次，则继续判断批次是否相等
            console.log("----->obj.mrlCode:"+obj.mrlCode+"---------->tableData[i].mrlCode:"+tableData[i].mrlCode);
            isMrlSame = true;
            if (obj.lotCode && tableData[i].lotCode) {
                if (obj.lotCode == tableData[i].lotCode) {
                    isLotSame = true;
                    //存在库位，则继续判断库位是否相等
                    if (obj.workCellCode && tableData[i].workCellCode) {
                        if (obj.workCellCode == tableData[i].workCellCode) {
                            isWorkCellSame = true;
                            isIn = true;
                            break;
                        }
                    } else {
                        isWorkCellSame = true;
                        //不存在库位，则只需要判断物料和批次是否相等
                        isIn = true;
                        break;
                    }
                }
            } else {
                isLotSame = true;
                isWorkCellSame = true;
                //不存在批次，则只需要判断物料
                isIn = true;
                break;
            }

        }
    }

    if (!isMrlSame) {
        api.alert({
            title: '',
            msg: '条码物料：【' + obj.mrlCode + '】 与任务明细物料不一致!'
        });
    } else if (!isLotSame) {
        api.alert({
            title: '',
            msg: '条码批次：【' + obj.lotCode + '】 与Tab1中任务明细批次不一致!'
        });
    } else if (!isWorkCellSame) {
        api.alert({
            title: '',
            msg: '所选库位：【' + obj.workCellCode + '】 与Tab1中任务明细库位不一致!'
        });
    }
    return isIn;
}

/**
 * 选取table
 * @param index
 * @returns {*}
 */
function chooseTable(index) {
    var $table;
    switch (index) {
        case 0:
            $table = $("#bootstrap-tab");
            break;
        case 1:
            $table = $("#bootstrap-tab-1");
            break;
        case 2:
            $table = $("#bootstrap-tab-2");
            break;
        case 3:
            $table = $("#bootstrap-tab-3");
            break;
            case 4:
                $table = $("#bootstrap-tab-4");
                break;
        default:
            alert('tableIndex错误!')
    }
    return $table;
}

/**
 * 在table中插入数据
 * @param tableIndex
 * @param type  插入类型（头部插入：prepend；尾部插入:append）
 * @param date  插入数据，对象数组类型
 */
function insertDateAtTable(tableIndex, type, date) {
    var $table = chooseTable(tableIndex);
    $table.bootstrapTable(type, date);
}

function removeAllAtTable(tableIndex){
    var $table = chooseTable(tableIndex);
    $table.bootstrapTable("removeAll");
}

/**
 * 在table中删除数据
 * @param tableIndex
 * @param row
 */
function deleteDateAtTable(tableIndex, row) {
    var $table = chooseTable(tableIndex);
    row.deleteFlag = "true";
    $table.bootstrapTable('remove', {field: 'deleteFlag', values: [row.deleteFlag]});
}

/**
 * 获取打印列表
 * @returns {Array}
 */
function getPrinters() {
    var printerArr = []; //仓库数组
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getPrinters),
        dataType: "json",
        data: {
        },
        async: false,
        success: function (ret) {
            api.hideProgress();
            var defaultObj = {code: '', name: '--请选择打印机--', val: ''};
            printerArr.push(defaultObj);
            $.each(ret, function (i, item) {
                var printObj = {};
                printObj.code = item.code;
                printObj.name = item.name;
                printObj.val = item.val;
                printerArr.push(printObj);
            });
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
            alert('加载打印机IP列表出错！');
        }
    });
    return printerArr;
}

/**
 * 获取打印模板
 * @returns {Array}
 */
function getTemplets() {
    var templetArr = []; //仓库数组
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getTemplets),
        dataType: "json",
        data: {
        },
        async: false,
        success: function (ret) {
            api.hideProgress();
            var defaultObj = {code: '', name: '--请选择打印模板--', val: ''};
            templetArr.push(defaultObj);
            $.each(ret, function (i, item) {
                var templetObj = {};
                templetObj.code = item.code;
                templetObj.name = item.remark;
                templetObj.val = item.id;
                templetArr.push(templetObj);
            });
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
            alert('加载打印模板列表出错！');
        }
    });
    return templetArr;
}

/**
 * 获取栈板生成规则
 * @returns {Array}
 */
function getSysBarCodeRules() {
    var printerArr = [];
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getSysBarCodeRules),
        dataType: "json",
        data: {
        },
        async: false,
        success: function (ret) {
            api.hideProgress();
            var defaultObj = {code: '', name: '--请选择条码规则--', val: ''};
            printerArr.push(defaultObj);
            $.each(ret, function (i, item) {
                var printObj = {};
                printObj.code = item.barRuleCode;
                printObj.name = item.barRuleName;
                printerArr.push(printObj);
            });
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
            alert('加载条码规则列表出错！');
        }
    });
    return printerArr;
}

/**
 * 选择打印机，打印条码
 * @param barCode
 */
function printBarCode(barCode) {
    //查询出打印机列表
    var ipCode = localStorage.getItem('hgzyPrintCode');
    var ipId = localStorage.getItem('hgzyPrintIp');
    var printTemplateId = localStorage.getItem('hgzyTempletGid');
    var printTemplateName = localStorage.getItem('hgzyTempletName');

    if (!ipCode){
        alert("请先在个人设置中设置打印机IP！");
        return false;
    }
    if (!printTemplateId){
        alert("请先在个人设置中设置打印模板！");
        return false;
    }

    var html = "<div class='aui-content aui-margin-b-15'><ul class='aui-list aui-form-list'><li class=\"aui-list-item\">\n" +
        "                <div class=\"aui-list-item-inner\">\n" +
        "                    <div class=\"aui-list-item-label\">IP</div>\n" +
        "                    <div class=\"aui-list-item-input\">"+ipId+"\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "           </li><li class=\"aui-list-item\">\n" +
        "                <div class=\"aui-list-item-inner\">\n" +
        "                    <div class=\"aui-list-item-label\">模板</div>\n" +
        "                    <div class=\"aui-list-item-input\">"+printTemplateName+"\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </li><li class='aui-list-item'><div class='aui-list-item-inner'><div class='aui-list-item-label'>打印机：</div><div class='aui-list-item-input'><select id='printerSelect' onchange='changePrinter()'>";

    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    api.ajax({
        url: getUrl(OtherUrl.getServicePrinterList),
        method: 'post',
        data: {
            values: {
                ipCode: ipCode
            }
        }
    }, function (ret1) {
        api.hideProgress();
        var printerCache = localStorage.getItem('printerCache');
        for (var i = 0; i < ret1.length; i++) {
            if (i == 0) {
                selectPrinter = ret1[0];
            }
            if (ret1[i] == printerCache) {
                html += "<option selected>";
                selectPrinter = ret1[i];
            } else {
                html += "<option>"
            }
            html += ret1[i] + "</option>";
        }
        html += "</select></div></div></li></ul></div>";
        dialog.alert({
            title: "打印条码",
            msg: html,
            buttons: ['取消', '确定']
        }, function (ret) {
            if (ret.buttonIndex == 2) {
                //条码打印模板

                //调用条码打印方法
                api.ajax({
                    url: getUrl(OtherUrl.printBarCode),
                    method: 'post',
                    data: {
                        values: {
                            barCodes: barCode,
                            printTemplateId: printTemplateId,
                            printer: selectPrinter,
                            ipCode: ipCode
                        }
                    }
                }, function (ret2) {
                    api.toast({msg: ret2.data, duration: 3000, location: 'middle'});
                    if (ret2.data != '打印成功！') {
                        return false;
                    }
                });
            }
        })
    });
}


function changePrinter() {
    selectPrinter = $("#printerSelect  option:selected").text();
    localStorage.setItem('printerCache', selectPrinter);
}


function HashMap(){
    //定义长度
    var length = 0;
    //创建一个对象
    var obj = new Object();

    /**
    * 判断Map是否为空
    */
    this.isEmpty = function(){
        return length == 0;
    };

    /**
    * 判断对象中是否包含给定Key
    */
    this.containsKey=function(key){
        return (key in obj);
    };

    /**
    * 判断对象中是否包含给定的Value
    */
    this.containsValue=function(value){
        for(var key in obj){
            if(obj[key] == value){
                return true;
            }
        }
        return false;
    };

    /**
    *向map中添加数据
    */
    this.put=function(key,value){
        if(!this.containsKey(key)){
            length++;
        }
        obj[key] = value;
    };

    /**
    * 根据给定的Key获得Value
    */
    this.get=function(key){
        return this.containsKey(key)?obj[key]:null;
    };

    /**
    * 根据给定的Key删除一个值
    */
    this.remove=function(key){
        if(this.containsKey(key)&&(delete obj[key])){
            length--;
        }
    };

    /**
    * 获得Map中的所有Value
    */
    this.values=function(){
        var _values= new Array();
        for(var key in obj){
            _values.push(obj[key]);
        }
        return _values;
    };

    /**
    * 获得Map中的所有Key
    */
    this.keySet=function(){
        var _keys = new Array();
        for(var key in obj){
            _keys.push(key);
        }
        return _keys;
    };

    /**
    * 获得Map的长度
    */
    this.size = function(){
        return length;
    };

    /**
    * 清空Map
    */
    this.clear = function(){
        length = 0;
        obj = new Object();
    };
}
