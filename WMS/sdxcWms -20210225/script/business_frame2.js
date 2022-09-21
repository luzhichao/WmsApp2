var businessUrl = {
    getInTask: 'padWmsController!getInTaskByCode.m', //查询入库任
    getOutTask: 'padWmsController!getOutTaskByCode.m', //查询出库任务
    getAcceptBill: 'padWmsController!getAcceptBillByCode.m', //查询收货单
    byBarCode: 'padWmsController!getUbcBarCode.m', //根据code查询物料
    byPackageBarCode: 'padWmsController!getUbcPackageBarCode.m', //根据包装条码code查询物料
    createAcceptBill: 'padWmsController!createAcceptBill.m', //
    createInBill: 'padWmsController!createInBill.m', //创建入库单
    createOutBill: 'padWmsController!createOutBill.m', //创建出库单
    getLotCode: 'padWmsController!generalLotCode.m' //取lotcode
}

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
//var TABELDATA = []  // 
var mrlInfo={}      //物料扫描lotCode信息map:{mrlCode:lotCode}

function initTable(gridArr, data, onClickRow) {
    var $table = $table || $("#table")
    gridArr = gridArr || tableField
    data = data || []
    //惠科表格添加等级字段
    if(localStorage.getItem('projectName') == 'HKDZ'){
        gridArr.unshift({
            name: 'mrlGrade',
            title: '等级',
        })
    }
    // api.alert({
    //     title: 'table',
    //     msg: JSON.stringify(gridArr) + '-------' + JSON.stringify(data)
    // });
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

function loadMrlByCode(barCode) {
    if (!barCode) {
        api.toast({
            msg: '请先填入物料条码！',
            duration: 2000,
            location: 'middle'
        });
        return false
    }
    toast.loading({
        title: "加载中",
        duration: 10000
    })
    api.ajax({
        url: getUrl(businessUrl.byPackageBarCode),
        method: 'post',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
            values: {
                barCode: barCode
            }
        }
    }, function (res, err) {
        toast.hide()
        // api.alert({
        //     title: 'res', 
        //     msg: JSON.stringify(res)
        // });
        if (res && res.errCode == 0 && res.data) {
            if (SOURCE_CODE == res.data[0].code || !SOURCE_CODE) {
                SOURCE_CODE = res.data[0].code
                MRL_DATA.details = MRL_DATA.details.concat(res.data)
                api.sendEvent({
                    name: 'searchByCode',
                    extra: {
                        type: 'search',
                        code: SOURCE_CODE
                    }
                })
            } else {
                api.toast({
                    msg: '请先提交同一任务物料，再进行扫描',
                    duration: 2000,
                    location: 'bottom'
                });
                return false
            }
            //updateTable(res.data)
            getLotCode(res.data,{index:0})

        } else {
            api.alert({
                msg: res.msg
            });
        }
    });
}

function submit() {
    var submitData = $.extend(true, {}, MRL_DATA)
    api.alert({
        title: 'submitData',
        msg: JSON.stringify(submitData)
    })
    if (submitData.details.length == 0) {
        api.toast({
            msg: '请填入物料信息再提交',
            duration: 2000,
            location: 'middle'
        });
        return false
    }
    // api.alert({
    //     title:'submitData',
    //     msg: JSON.stringify(submitData)
    // })
    submitData.details = $.map(submitData.details, function (val) {
        return mrlData(val)
    })
    // api.alert({
    //     title:'submitData',
    //     msg: JSON.stringify(submitData)
    // })
    // return 
    toast.loading({
        title: "提交中",
        duration: 10000
    })
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
        //      api.alert({
        //          title: 'submit res1111',
        //          msg: JSON.stringify(res) + '______' + JSON.stringify(err)
        //      });
        toast.hide()
        if (res) {
            if (res.errCode == 0) {
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
                // toast.fail({
                //     title: res.msg,
                //     duration: 3000
                // });
                api.alert({
                    title: '错误',
                    msg: res.msg
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

function getLotCode(mrlArr,mrlState) { //(mrlArr:添加的物料数组，mrlState：物料当前状态)
    var mrlData = mrlArr[mrlState.index]
    if(mrlData.lotCode){
        //如果当前物料有lotCode
        //TABELDATA.push(mrlData)
        mrlInfo[mrlData.mrlCode] = mrlData.lotCode
        mrlState.index += 1
        if(mrlState.index == mrlArr.length){
            updateTable(mrlArr)
        }else{
            getLotCode(mrlArr,mrlState)
        }
    }else if(mrlInfo[mrlData.mrlCode]){
        //如果物料信息有当前物料的lotCode
        mrlArr[mrlState.index].lotCode = mrlInfo[mrlData.mrlCode]
        mrlState.index += 1
        if(mrlState.index == mrlArr.length){
            updateTable(mrlArr)
        }else{
            getLotCode(mrlArr,mrlState)
        }
    }else{
        //都没有的情况下去请求lotCodel
        api.ajax({
            url: getUrl(businessUrl.getLotCode),
            method: 'post',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                values: {
                    mrlCode: mrlData.mrlCode,
                    workCenterGid: ''
                }
            }
        }, function (res, err) {
            if (res.errCode == 0 && res.data) {
                mrlArr[mrlState.index]['lotCode'] = mrlInfo[mrlData.mrlCode] = res.data
                mrlState.index += 1
                if(mrlState.index == mrlArr.length){
                    updateTable(mrlArr)
                }else{
                    getLotCode(mrlArr,mrlState)
                }
                // data.lotCode = res.data
                // updateTable(data)
            }
        })
    }
    // if(mrlState.index == mrlArr.length){
    //     api.alert({
    //         title: 'mrlInfo', 
    //         msg: JSON.stringify(mrlInfo)
    //     });
    // }
}
function updateTable(addMrlArr, subMrlObj) {
    var tableData = $table.bootstrapTable('getData')
    // var groupByMrlcode = function(arr){
    //     var map = {};
    //     $.each(arr,function(i,v){
    //         if(!map[v.mrlCode]){
    //             dest.push(v)
    //             map[v.mrlCode] = new Array(v)
    //         }else{
    //             map[v.mrlCode].push(v)
    //         }
    //     })
    //     return map
    // }
    
    if (addMrlArr) {
        //var tableDataGroup = groupByMrlcode(tableData)
        addMrlArr = $.map(addMrlArr, function (item) {
            item.target = 'frame2'
            // if(tableDataGroup[item.mrlCode]){
            //     item.lotCode = tableDataGroup[item.mrlCode][0]['lotCode']
            // }
            return item
        })
        // var addMrlGroup = groupByMrlcode(addMrlArr)
        // $.each(addMrlGroup,function(key,val){
        //     if(!val[0]['lotCode']){
                
        //     }
        // })
        tableData = tableData.concat(addMrlArr)
        //增加page4 表格
        //var gridArr = gridData(new Array(data))
        api.sendEvent({
            name: 'wmsDetailDataAdd',
            extra: {
                type: 'add',
                data: JSON.stringify(addMrlArr),
            }
        })

    } else if (subMrlObj) {

        $.each(tableData, function (i, v) {
            if (v.id == subMrlObj.id) {
                tableData.splice(i, 1)
                return false
            }
        })
        // var map = [];
        // var dest = [];
        // $.each(details, function (idx, item) {
        //     var check = $.grep(map, function (val) {
        //         return (val.mrlCode == item.mrlCode && val.lotCode == item.lotCode)
        //     })
        //     if (check.length > 0) {
        //         return false
        //     }
        //     var result = $.grep(details, function (val) {
        //         return (val.mrlCode == item.mrlCode && val.lotCode == item.lotCode)
        //     })
        //     map.push({
        //         mrlCode: item.mrlCode,
        //         lotCode: item.lotCode
        //     })
        //     dest = dest.concat(result)
        // })
        // tableData = gridData(dest)
    }
    initTable(null, tableData)
}


function clearTable() {
    var formData = $('form').serializeArray()
    $.each(formData, function (idx, val) {
        $(":input[name=" + val.name + "]").val('')
    })
    $("#barCode").val('')
    $table.bootstrapTable('removeAll')
    MRL_DATA = {
        details: []
    }
    //清空源单
    SOURCE_CODE = ''
    mrlInfo = {}
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

    _mrlData.sourceBillType = state.sourceBillType
    // _mrlData.sourceBillCode = (theObj.barcodeRecord?theObj.barcodeRecord[state.sourceBillCodeRecord]:'')
    // _mrlData.sourceDetailGid = (theObj.barcodeRecord?theObj.barcodeRecord[state.sourceBillDetailRecord]:'')
    _mrlData.mrlGid = ''
    _mrlData.mrlCode = theObj.mrlCode
    _mrlData.mrlName = theObj.mrlName
    _mrlData.barCode = theObj.barcode
    _mrlData.lotCode = theObj.lotCode
    //_mrlData.targetWareroomCode = theObj.workCenterCode
    //_mrlData.targetWareroomGid = theObj.code
    //_mrlData.specModel = theObj.specModel
    //_mrlData.supplierCode = theObj.supplierCode
    _mrlData.actualPriQty = theObj.qty
    _mrlData.remark = theObj.remark
    _mrlData.rowNum = theObj.rowNum

    if (state.warehouseType == 'In') {
        _mrlData.targetWareroomCode = theObj.workCenterCode
    }
    return _mrlData
}

apiready = function () {
    initTable()
    api.addEventListener({
        name: 'wmsDetailDataDelete'
    }, function (ret, err) {
        if (ret.value.type == 'reload') {
            clearTable(ret.value.code)
        } else if (ret.value.type == 'delete' && ret.value.rowData.target == 'frame2') {

            updateTable(null, ret.value.rowData)
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
            /*api.alert({
            	title: 'taskData', 
            	msg: JSON.stringify(ret.value.data)
            });*/
            MRL_DATA.workCenterGid = taskData.workCenterGid
            MRL_DATA.businessType = state.businessType
            MRL_DATA.subStoreType = taskData.subStoreType
            MRL_DATA.deliverGid = taskData.deliverGid
            MRL_DATA.supplierGid = taskData.supplierGid
            MRL_DATA.customerGid = taskData.customerGid
            MRL_DATA.workOrderCode = taskData.workOrderCode
            MRL_DATA.billDate = taskData.taskDate
            var formData = $('form').serializeArray()
            $.each(formData, function (idx, val) {
                $(":input[name=" + val.name + "]").val(taskData[val.name])
            })
        }

    });
    $("#barCode").bind('keydown', function (event) {
        var code = $("#barCode").val()
        if (event.keyCode == "13") {
            if (!code) {
                api.toast({
                    msg: '请填入任务条码',
                    duration: 2000,
                    location: 'middle'
                });
                return false
            }
            loadMrlByCode(code)
        }
    });
};