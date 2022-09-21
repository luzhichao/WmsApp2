var businessUrl = {
    getInTask: 'padWmsController!getInTaskByCode.m', //查询入库任
    getOutTask: 'padWmsController!getOutTaskByCode.m', //查询出库任务
    getAcceptBill: 'padWmsController!getAcceptBillByCode.m', //查询收货单
    byBarCode: 'padWmsController!getUbcBarCode.m', //根据code查询物料
    createAcceptBill: 'padWmsController!createAcceptBill.m', //
    createInBill: 'padWmsController!createInBill.m', //创建入库单
    createOutBill: 'padWmsController!createOutBill.m', //创建出库单
    getLotCode: 'padWmsController!generalLotCode.m' //取lotcode
};

// UWM_BILL_TYPE_ACCEPT = "ACCEPT"; // 收货单
// UWM_BILL_TYPE_IN = "IN"; // 入库单
// UWM_BILL_TYPE_OUT = "OUT"; // 出库单
// UWM_BILL_TYPE_INV = "INV";// 盘点单
// UWM_BILL_TYPE_TRANS = "TRANS";// 移库单(库内转移)
// UWM_BILL_TYPE_DIS = "DIS";// 配送单
// UWM_BILL_TYPE_IN_TASK = "IN_TASK"; // 入库任务
// UWM_BILL_TYPE_OUT_TASK = "OUT_TASK"; // 出库任务
// UWM_BILL_TYPE_UP_TASK = "UP_TASK"; // 上架任务
// UWM_BILL_TYPE_DOWN_TASK = "DOWN_TASK"; // 下架任务
// UWM_BILL_TYPE_CONVERSION = "CONVER"; // 物料转换
// UWM_BILL_TYPE_UP = "UP"; // 上架单
// UWM_BILL_TYPE_DOWN = "DOWN"; // 下架单


// INSTORE_TYPE_MATERIAL = 0; // 采购入库
// INSTORE_TYPE_OUTSOURCE = 1; // 委外产品入库
// INSTORE_TYPE_OTHER = 2; // 其它入库
// INSTORE_TYPE_FINISHIN = 3; // 产品入库
// INSTORE_TYPE_TRANS_IN = 4;// 调拨入库
// INSTORE_TYPE_INV_IN = 5;// 盘盈入库
// INSTORE_TYPE_DIS_IN = 6;// 配送入库
// INSTORE_TYPE_LINESTORE_IN = 7;// 线边入库
// INSTORE_TYPE_FORWARD_IN = 8;// 线边入库
// INSTORE_TYPE_RETURN_BACK =9;// 借出还回
// INSTORE_TYPE_SALE_RETURN =12;// 销售退货
// INSTORE_TYPE_MATERIAL_RETURN =14;// 生产领料退货
// IN_STORE_TYPE_CONSIGN = 10; //委外任务


// OUTSTORE_TYPE_MATERIAL = 0; // 材料出库
// OUTSTORE_TYPE_OUTSOURCING = 1; // 委外材料出库
// OUTSTORE_TYPE_OTHER = 2; // 其他
// OUTSTORE_TYPE_LINESIDE = 3; // 线边库出库
// OUTSTORE_TYPE_TRANS_OUT = 4; // 调拨出库
// OUTSTORE_TYPE_INV_OUT = 5; // 盘亏出库
// OUTSTORE_TYPE_FORWARD = 6; // 转运单出库
// OUTSTORE_TYPE_SALE = 7; // 销售出库
// OUTSTORE_TYPE_BORROW = 11; //借出出库
// OUTSTORE_TYPE_PURCHASE_RETURN = 12; //采购退货
// OUTSTORE_TYPE_PRODUCT_RETURN = 8; //生产退产线

/* 页面初始化对象 ↓ */
if (state.warehouseType == 'In') {
    $.extend(true, state, {
        queryUrl: 'getInTask', //任务数据查询
        submitUrl: 'createInBill',
        sourceBillType: 'IN_TASK',
        sourceBillCodeRecord: 'inTaskCode',
        sourceBillDetailRecord: 'inTaskDetailGid',
    });
}
if (state.warehouseType == 'Out') {
    $.extend(true, state, {
        queryUrl: 'getOutTask', //任务数据查询
        submitUrl: 'createOutBill',
        sourceBillType: 'OUT_TASK',
        sourceBillCodeRecord: 'outTaskCode',
        sourceBillDetailRecord: 'outTaskDetailGid',
    });
}
var tableField = [] //表格字段数组
/* 页面初始化对象 ↑ */

var MRL_DATA = {
    details: []
} //物料数据
var SOURCE_CODE = '' //源单条码
var barCodeArr = []; //条码集合，用于遍历判断是否已被扫描

apiready = function () {
    initTable()

    // TAB4界面删除时，更新TAB3界面内容
    api.addEventListener({
        name: 'wmsDetailDataDelete'
    }, function (ret, err) {
        if (ret.value.type == 'reload') {
            clearTable(ret.value.code)
        } else if (ret.value.type == 'delete' && ret.value.rowData.target == 'frame3') {

            var rowData = ret.value.rowData;
            var afterTableData = ret.value.afterTableData;
            var mrlCode = rowData.mrlCode;
            var lotCode = rowData.lotCode;
            if (typeof(rowData.actualPriQty) == "undefined") {
                var qty = rowData.qty;
            } else {
                var actualPriQty = rowData.actualPriQty;
            }

            var tableData = $table.bootstrapTable('getData');
            for (var i = 0; i < tableData.length; i++) {
                if (tableData[i].mrlCode == mrlCode && tableData[i].lotCode == lotCode) {
                    if (typeof(rowData.actualPriQty) == "undefined") {
                        tableData[i].qty = Number(tableData[i].qty) - Number(qty);
                        if (tableData[i].qty == 0) {

                            $table.bootstrapTable('remove', {field: 'qty', values: [0]});
                        }
                    } else {
                        tableData[i].actualPriQty = Number(tableData[i].actualPriQty) - Number(actualPriQty);
                    }
                    $table.bootstrapTable('updateRow', {index: i, row: tableData[i]});
                    return;
                }
            }

            // $.each(tableData, function (i, v) {
            //     // if (v.id == ret.value.rowData.id) {
            //     //     tableData.splice(i, 1)
            //     //     return false
            //     // }
            // })
            MRL_DATA.details = tableData
            updateTable()
        }
    });

    api.addEventListener({
        name: 'clear'
    }, function (ret, err) {
        clearTable()
    });
    api.addEventListener({
        name: 'taskData'
    }, function (ret, err) {
        if (ret.value.data) {
            var taskData = ret.value.data
            // api.alert({
            // 	title: 'taskData',
            // 	msg: JSON.stringify(ret.value.data)
            // });
            MRL_DATA.workCenterGid = taskData.workCenterGid
            MRL_DATA.businessType = state.businessType
            MRL_DATA.subStoreType = taskData.subStoreType
            MRL_DATA.deliverGid = taskData.deliverGid
            MRL_DATA.supplierGid = taskData.supplierGid
            MRL_DATA.customerGid = taskData.customerGid
            MRL_DATA.workOrderCode = taskData.workOrderCode
            MRL_DATA.billDate = taskData.taskDate
        }

    });

    /**
     * 物料扫描
     */
    $("#barCode").bind('keydown', function (event) {
        var barCode = $("#barCode").val();
        if (event.keyCode == "13") {
            if (!barCode) {
                api.toast({msg: '请扫描条码！', duration: 2000, location: 'middle'});
                return false
            }
            //防呆->条码重复扫描
            var isScaned = false;
            if (barCodeArr.length != 0) {
                for (var i = 0; i < barCodeArr.length; i++) {
                    if (barCodeArr[i] == barCode) {
                        isScaned = true;
                    }
                }
            }
            if (isScaned) {
                api.toast({msg: '条码已被扫描!', duration: 2000, location: 'middle'});
                $("#barCode").val('');
                return false
            } else {
                barCodeArr.push(barCode);
            }

            //收货: 解析条码
            if (state.htmlName == 'ACCEPT') {
                var mrlCodeArr = localStorage.getItem('tab1_mrlCodes').split(',');

                if (!mrlCodeArr || mrlCodeArr[0] == '') {
                    api.toast({
                        msg: '请先选择来源单据',
                        duration: 2000,
                        location: 'middle'
                    });
                    return false
                }
                loadMrlByCode_HGZY_receive(barCode, mrlCodeArr);
            } else {
                // 产成品暂收
                loadMrlByCode(barCode);
            }
            $("#barCode").val('');
        }
    });
};


function initTable(gridArr, data, onClickRow) {
    var $table = $table || $("#table")
    gridArr = gridArr || tableField
    data = data || []
    $.each(gridArr, function (idx, val) {
        val.field = (val.name || val.field) //name 必要
        val.title = val.title //title 必要
        val.visible = (val.visible == undefined ? true : val.visible)
        val.align = 'center'
    })
    //先清除
    $table.bootstrapTable('destroy')

    $table.bootstrapTable({
        columns: gridArr,
        data: data,
        onClickRow: onClickRow,
    });
}


/**
 * 扫描物料条码加载数据
 * @param barCode
 * @returns {boolean}
 */
function loadMrlByCode(barCode) {
    toast.loading({title: "加载中", duration: 10000});
    //华为成品箱码可能重复，需要通过条码与任务令（inTaskCode）唯一确定
    var ajaxUrl;
    if (state.htmlName === 'PRODUCT_ACCEPT') {
        ajaxUrl = 'padWmsController!getUbcBarCodeForProduct.m';
    } else {
        ajaxUrl = businessUrl.byBarCode;
    }

    api.ajax({
        url: getUrl(ajaxUrl),
        method: 'post',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
            values: {
                barCode: barCode,
                inTaskCode: localStorage.getItem("inTaskCode")
            }
        }
    }, function (res, err) {
        toast.hide()
        if (res && res.errCode == 0 && res.data) {
            if (!SOURCE_CODE) {
                SOURCE_CODE = res.data.barcodeRecord[state.sourceBillCodeRecord];
                if (SOURCE_CODE) {
                    //第一次直接赋值 第二次等于前一次
                    api.sendEvent({
                        name: 'searchByCode',
                        extra: {
                            type: 'search',
                            code: SOURCE_CODE
                        }
                    })
                }
            } else if (SOURCE_CODE != res.data.barcodeRecord[state.sourceBillCodeRecord]) {
                api.toast({
                    msg: '请先提交同一批次物料，再进行扫描',
                    duration: 2000,
                    location: 'bottom'
                });
                return false
            }

            MRL_DATA.details.push(res.data)
            var formData = $('form').serializeArray()
            $.each(formData, function (idx, val) {
                // formStr += (val.name + ':' + val.value + ',')
                $(":input[name=" + val.name + "]").val(res.data[val.name])
            })

            if (state.htmlName == 'PRODUCT_ACCEPT') {
                res.data.targetWareroomGid2 = allData.areaGid;
                res.data.targetWareroomName2 = allData.areaName;
                res.data.targetWareroomGid3 = allData.subAreaGid;
                res.data.targetWareroomName3 = allData.subAreaName;

                if (SOURCE_CODE) {
                    $("#taskCode").val(SOURCE_CODE)
                } else {
                    $("#taskCode").val('')
                }
                $("#barCode").val('')

                //更新表格
                if (res.data.lotCode) {
                    updateTable(res.data)
                } else {
                    getLotCode(res.data)
                }
            }
            else {
                if (SOURCE_CODE) {
                    $("#taskCode").val(SOURCE_CODE)
                } else {
                    $("#taskCode").val('')
                }
                $("#barCode").val('')

                //更新表格
                if (res.data.lotCode) {
                    updateTable(res.data)
                } else {
                    getLotCode(res.data)
                }
            }
        }

        else {
            api.alert({
                msg: res.msg
            });
        }
    });
}

// 解析物料条码填充表格
function loadMrlByCode_HGZY_receive(barCode, mrlCodeArr) {
    // 规则：1)前14位为物料编码; 2)之后的9位为供应商编码; 3)之后的6位为入库日期; 4)之后的2位为来料批次; 5)之后（最后）2位为仓库编码

    var tempStr = barCode;
    var mrlCode = tempStr;
    // var mrlCode = tempStr.substring(0, 14);
    // tempStr = tempStr.substr(14);
    // var supplierCode = tempStr.substring(0, 9);
    // tempStr = tempStr.substr(9);
    // var proDate = tempStr.substring(0, 6);
    // tempStr = tempStr.substr(6);
    // var lotCode = tempStr.substring(0, 2);
    // tempStr = tempStr.substr(2);
    // var workCenterCode = tempStr;

    // 验证:扫描带出来的物料编码是否包含在tab1传过来的物料数组里
    var flag = false;
    for (var i = 0; i < mrlCodeArr.length; i++) {

        if (mrlCodeArr[i] == mrlCode) {
            flag = true;
            break;
        }
    }
    if (flag == true) {
        dialog.prompt({
            title: "确认数量",
            text: '',
            // 默认一箱的数量为20
            value: 20,
            input: true,
            buttons: ['取消', '确定']
        }, function (ret) {
            if (ret.buttonIndex == 2) {
                if (ret.text != "" || ret.text != undefined) {
                    // 数据回传
                    var obj = {};

                    $("#mrlCode").val(mrlCode || '');
                    // $("#lotCode").val(lotCode || '');
                    $("#qty").val(ret.text);

                    obj.barcode = barCode;
                    // obj.lotCode = lotCode;
                    obj.mrlCode = mrlCode;
                    if (localStorage.getItem('areaGid') != '' && localStorage.getItem('areaGid') != undefined) {
                        obj.targetWareroomGid2 = localStorage.getItem('areaGid');
                        obj.targetWareroomName2 = localStorage.getItem('areaName');
                    }
                    if (localStorage.getItem('subAreaGid') != '' && localStorage.getItem('subAreaGid') != undefined) {
                        obj.targetWareroomGid3 = localStorage.getItem('subAreaGid');
                        obj.targetWareroomName3 = localStorage.getItem('subAreaName');
                    }
                    obj.mrlCode = mrlCode;
                    obj.qty = ret.text;

                    MRL_DATA.details.push(obj);

                    $("#barCode").val('');
                    //更新表格
                    if (lotCode) {
                        updateTable(obj)
                    } else {
                        getLotCode(obj)
                    }
                }
            }
        })
    } else {
        api.toast({
            msg: '输入的物料与来源单据物料冲突！',
            duration: 3000,
            location: 'middle'
        });
        return false
    }
}

function submit() {
    var submitData = $.extend(true, {}, MRL_DATA)

    if (submitData.details.length == 0) {
        api.toast({msg: '请填入物料信息再提交', duration: 2000, location: 'middle'});
        return false
    }
    submitData.workCenterGid = localStorage.getItem('workCenterGid');
    // submitData.details = $table.bootstrapTable('getData');
    submitData.details = $.map(submitData.details, function (val) {
        return mrlData(val)
    })

    return;
    toast.loading({
        title: "提交中",
        duration: 10000
    })
    if (state.htmlName = 'PRODUCT_ACCEPT') {
        api.ajax({
            url: getUrl('padWmsController!createAcpPrd.m'),
            method: 'post',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                values: {
                    data: submitData,
                    barCodeRule: '297eceea685974b101685b7c78140005',   //华为成品  正源内部使用的栈板码规则
                    // coditionMap: '',   //栈板码规则 使用的字段: 物料编码
                    // barCodes: ''    //条码数组  用于保存栈板码与箱码的对应关系
                }
            }
        }, function (res, err) {
            toast.hide()
            if (res) {
                if (res.errCode == 0) {
                    // 二开： 提交成功清空tab1页面保存的mrlCodes
                    localStorage.removeItem('tab1_mrlCodes');
                    toast.success({title: res.msg, duration: 2000});
                    api.sendEvent({
                        name: 'clear',
                        extra: {
                            type: ' reload'
                        }
                    });
                } else {
                    toast.fail({
                        title: res.msg,
                        duration: 3000
                    });
                }
            } else {
                api.toast({
                    msg: '数据错误',
                    duration: 2000,
                    location: 'bottom'
                });
            }
        });
    } else {
        api.ajax({
            url: getUrl(businessUrl[state.submitUrl]),
            method: 'post',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                values: {
                    data: submitData
                }
            }
        }, function (res, err) {
            toast.hide()
            if (res) {
                if (res.errCode == 0) {
                    // 二开： 提交成功清空tab1页面保存的mrlCodes
                    localStorage.removeItem('tab1_mrlCodes');
                    toast.success({
                        title: res.msg,
                        duration: 2000
                    });
                    api.sendEvent({
                        name: 'clear',
                        extra: {
                            type: ' reload'
                        }
                    });
                } else {
                    toast.fail({
                        title: res.msg,
                        duration: 3000
                    });
                }
            } else {
                api.toast({
                    msg: '数据错误',
                    duration: 2000,
                    location: 'bottom'
                });
            }
        });
    }
}

function updateTable(data, code) {
    var tableData = $table.bootstrapTable('getData')

    // alert(JSON.stringify(data));
    if (data) {
        var result = $.grep(tableData, function (val) {
            return (val.mrlCode == data.mrlCode && val.lotCode == data.lotCode)
        })
        if (result.length > 0) {
            tableData = $.map(tableData, function (item) {
                if (item.mrlCode == data.mrlCode && item.lotCode == data.lotCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty)
                }
                return item
            })
        } else {
            tableData = tableData.concat(new Array(data))
        }
        data.target = 'frame3'
        //增加page4 表格
        api.sendEvent({
            name: 'wmsDetailDataAdd',
            extra: {
                type: 'add',
                data: JSON.stringify(new Array(data)),
            }
        })

    } else {
        var details = MRL_DATA.details
        var map = [];
        var dest = [];
        $.each(details, function (idx, item) {
            var check = $.grep(map, function (val) {
                return (val.mrlCode == item.mrlCode && val.lotCode == item.lotCode)
            })
            if (check.length > 0) {
                return false
            }
            var result = $.grep(details, function (val) {
                return (val.mrlCode == item.mrlCode && val.lotCode == item.lotCode)
            })
            map.push({
                mrlCode: item.mrlCode,
                lotCode: item.lotCode
            })
            dest = dest.concat(result)
        })
        tableData = dest
        // tableData = gridData(dest)
        // $table.bootstrapTable('prepend', data)
    }
    initTable(null, tableData)
}

function getLotCode(data) {
    var tableData = $table.bootstrapTable('getData')
    var result = $.grep(tableData, function (val) {
        return val.mrlCode == data.mrlCode
    })
    if (result.length > 0) {
        data.lotCode = result[0].lotCode
        updateTable(data)
    } else {
        api.ajax({
            url: getUrl(businessUrl.getLotCode),
            method: 'post',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                values: {
                    mrlCode: data.mrlCode,
                    workCenterGid: ''
                }
            }
        }, function (res, err) {
            if (res.errCode == 0 && res.data) {
                data.lotCode = res.data
                updateTable(data)
            }
        })
    }
}

function clearTable() {
    var formData = $('form').serializeArray()
    $.each(formData, function (idx, val) {
        $(":input[name=" + val.name + "]").val('')
    })
    $table.bootstrapTable('removeAll')
    MRL_DATA = {
        details: []
    }
    //清空源单
    SOURCE_CODE = ''
}

//返回符合表格字段的对象数组
// function gridData(details) {
//     var gridArr = []
//     $.each(details, function (idx, val) {
//         var gridItem = {}
//         $.each(tableField, function (i, item) {
//             gridItem[item.name] = val[item.name] ? val[item.name] : val.barcodeRecord[item.name]
//         })
//         gridArr.push(gridItem)
//     })
//     return gridArr
// }

function mrlData(theObj) {
    var _mrlData = {}
    _mrlData.sourceBillType = state.sourceBillType;
    if (state.htmlName != 'ACCEPT') {
        _mrlData.sourceBillCode = theObj.barcodeRecord[state.sourceBillCodeRecord]
        _mrlData.sourceDetailGid = theObj.barcodeRecord[state.sourceBillDetailRecord];
    }
    _mrlData.mrlGid = ''
    _mrlData.mrlCode = theObj.mrlCode
    _mrlData.mrlName = theObj.mrlName
    if (state.htmlName == 'ACCEPT') {
        if (theObj.targetWareroomGid2 != '' && theObj.targetWareroomGid2 != undefined) {
            _mrlData.targetWareroomGid2 = theObj.targetWareroomGid2;
        }
        if (theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined) {
            _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        }
    }
    _mrlData.barCode = theObj.barcode
    _mrlData.lotCode = theObj.lotCode
    //_mrlData.targetWareroomCode = theObj.workCenterCode
    // _mrlData.targetWareroomGid = theObj.code
    // _mrlData.primaryMeasureGid = theObj.code
    // _mrlData.primaryMeasureName = theObj.code
    _mrlData.actualPriQty = theObj.qty
    _mrlData.remark = theObj.remark

    if (state.warehouseType == 'In') {
        _mrlData.targetWareroomCode = theObj.workCenterCode
    }
    return _mrlData
}

