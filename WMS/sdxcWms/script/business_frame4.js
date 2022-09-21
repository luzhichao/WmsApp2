var businessUrl = {
    byBarCode: 'padWmsController!getUbcBarCode.m', //根据code查询物料
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
        submitUrl: 'createInBill'
    });
}
if (state.warehouseType == 'Out') {
    $.extend(true, state, {
        submitUrl: 'createOutBill'
    });
}

var tableField = [] //表格字段数组

var MRL_DATA = {};
var TEMP_DATA = {};

/* 页面初始化对象 ↑ */
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

function submit() {
    var tableData = $table.bootstrapTable('getData');
    if (tableData.length === 0) {
        api.toast({
            msg: "没有可以提交的数据",
            duration: 3000,
            location: 'middle'
        });
        return;
    }

    toast.loading({title: "提交中", duration: 10000});

    for (var i = 0; i < tableData.length; i++) {
        // 字段名映射
        tableData[i]["barCode"] = tableData[i]["barcode"];
        tableData[i]["actualPriQty"] = tableData[i]["qty"];
        tableData[i]["sourceDetailGid"] = tableData[i]["businessId"];
        //tableData[i]["sourceBillCode"] = tableData[i]["businessCode"];
        tableData[i]["sourceBillCode"] = localStorage.getItem('tab1Code');
        if(state.htmlName == 'MATERIAL_IN'){
        	tableData[i]["rowNum"] = i+1;
        }
        
    }

    MRL_DATA.workCenterGid = localStorage.getItem('workCenterGid');
    
    //colin
    MRL_DATA.supplierGid = localStorage.getItem('colinSupplierGid');
    MRL_DATA.departmentGid = localStorage.getItem('colinDepartmentGid');
    
    //根据工厂来判别条码规则
   	var siteGid = localStorage.getItem("siteGid");
   	
   	var barCodeRule="297eceea68828e20016882e5c97e0000";//默认为烽火
   	
   	//武汉工厂  华为
    if(siteGid=="2c924978692e6f7b016933617709003a"){
    	barCodeRule="297eceea685974b101685b7c78140005";
    }
    
    // 成品暂收
    if (state.htmlName == 'PRODUCT_ACCEPT') {
        MRL_DATA.details = $.map(tableData, function (val) {
            return mrlData(val)
        })
        
        
        api.ajax({
            url: getUrl('padWmsController!createAcpPrd.m'),
            method: 'post',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: {
                values: {
                    data: JSON.stringify(MRL_DATA),
                    barCodeRule: barCodeRule,
                    //barCodeRule: '297eceea685974b101685b7c78140005',   //华为成品  正源内部使用的栈板码规则
                    // coditionMap: '',   //栈板码规则 使用的字段: 物料编码
                    // barCodes: ''    //条码数组  用于保存栈板码与箱码的对应关系
                }
            }
        }, function (res, err) {
            toast.hide()
            if (res) {
                if (res.errCode == 0) {
                    // 二开： 提交成功清空tab1页面保存的mrlCodes
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
        //成品入库
    } else if (state.htmlName == 'PRODUCT_IN') {
        MRL_DATA.businessType = state.businessType;
        MRL_DATA.details = $.map(tableData, function (val) {
            return mrlData(val)
        })
        api.ajax({
            url: getUrl('padWmsController!createInBill.m'),
            method: 'post',
            timeout: 60,
            data: {
                values: {
                    data: JSON.stringify(MRL_DATA)
                }
            }
        }, function (ret, err) {
            toast.hide()
            if (ret) {
                var myobj = eval(ret);
                if (myobj.errCode == 0) {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                    api.sendEvent({
                        name: 'clear',
                        extra: {
                            type: 'reload'
                        }
                    });
                    //reloadWin();
                } else {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                }
            } else {
                api.toast({
                    msg: '数据错误',
                    duration: 3000,
                    location: 'middle'
                });
            }
        });
    } else if (state.htmlName == 'MATERIAL_ACCEPT') {
        MRL_DATA.businessType = state.businessType;
        MRL_DATA.code = printCode;
        MRL_DATA.details = $.map(tableData, function (val) {
            return mrlData(val)
        })
        api.ajax({
            url: getUrl('padWmsController!createAcceptBillForMaterial.m'),
            method: 'post',
            timeout: 60,
            data: {
                values: {
                    data: JSON.stringify(MRL_DATA)
                }
            }
        }, function (ret, err) {
            toast.hide()
            if (ret) {
                var myobj = eval(ret);
                if (myobj.errCode == 0) {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                    api.sendEvent({
                        name: 'clear',
                        extra: {
                            type: 'reload'
                        }
                    });
                    //reloadWin();
                } else {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                }
            } else {
                api.toast({
                    msg: '数据错误',
                    duration: 3000,
                    location: 'middle'
                });
            }
        });
    } else if (state.htmlName == 'MATERIAL_IN') {
        MRL_DATA.businessType = state.businessType;
        MRL_DATA.details = $.map(tableData, function (val) {
            return mrlData(val)
        });
        api.ajax({
            url: getUrl('padWmsController!createInBillForMaterial.m'),
            method: 'post',
            timeout: 60,
            data: {
                values: {
                    data: JSON.stringify(MRL_DATA)
                }
            }
        }, function (ret, err) {
            toast.hide()
            if (ret) {
                var myobj = eval(ret);
                if (myobj.errCode == 0) {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                    api.sendEvent({
                        name: 'clear',
                        extra: {
                            type: 'reload'
                        }
                    });
                    //reloadWin();
                } else {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                }
            } else {
                api.toast({
                    msg: '数据错误',
                    duration: 3000,
                    location: 'middle'
                });
            }
        });
    }
    else {

        MRL_DATA.details = $.map(tableData, function (val) {
            return mrlData(val)
        })
        api.ajax({
            url: getUrl(businessUrl[state.submitUrl]),
            method: 'post',
            timeout: 60,
            data: {
                values: {
                    data: JSON.stringify(MRL_DATA)
                }
            }
        }, function (ret, err) {
            toast.hide()
            if (ret) {
                var myobj = eval(ret);
                if (myobj.errCode == 0) {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                    api.sendEvent({
                        name: 'clear',
                        extra: {
                            type: 'reload'
                        }
                    });
                    //reloadWin();
                } else {
                    api.toast({
                        msg: myobj.msg,
                        duration: 3000,
                        location: 'middle'
                    });
                }
            } else {
                api.toast({
                    msg: '数据错误',
                    duration: 3000,
                    location: 'middle'
                });
            }
        });
    }
}

function mrlData(theObj) {
    var _mrlData = {};
    
    
    
    if(state.htmlName == 'MATERIAL_IN'){
    	_mrlData.rowNum = theObj.rowNum;
    }
    
    //成品暂收
    if (state.htmlName == 'PRODUCT_ACCEPT') {
        _mrlData.sourceBillType = "IN_TASK";

        _mrlData.sourceBillCode = theObj.barcodeRecord['inTaskCode'];
        _mrlData.sourceDetailGid = theObj.barcodeRecord['inTaskDetailGid'];
        _mrlData.mrlCode = theObj.mrlCode;
        _mrlData.mrlName = theObj.mrlName;
        if (theObj.targetWareroomGid2 != '' && theObj.targetWareroomGid2 != undefined) {
            _mrlData.targetWareroomGid2 = theObj.targetWareroomGid2;
        }
        if (theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined) {
            _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        }
        _mrlData.barCode = theObj.barcode;
        _mrlData.lotCode = theObj.lotCode;
        _mrlData.actualPriQty = theObj.actualPriQty;
        //成品入库
    } else if (state.htmlName == 'PRODUCT_IN') {
        _mrlData.sourceBillCode = theObj.barcodeRecord['acceptBillCode'];
        _mrlData.sourceBillType = "ACCEPT";
        _mrlData.sourceDetailGid = theObj.barcodeRecord['acceptBillDetailGid'];
        _mrlData.mrlCode = theObj.mrlCode;
        _mrlData.mrlName = theObj.mrlName;
        if (theObj.targetWareroomGid2 != '' && theObj.targetWareroomGid2 != undefined) {
            _mrlData.targetWareroomGid2 = theObj.targetWareroomGid2;
        }
        if (theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined) {
            _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        }
        _mrlData.locationCode = theObj.locationCode;
        _mrlData.locationGid = theObj.locationGid;
        _mrlData.barCode = theObj.barcode;
        _mrlData.lotCode = theObj.lotCode;
        _mrlData.actualPriQty = theObj.actualPriQty;
        //原材料暂收
    } else if (state.htmlName == 'MATERIAL_ACCEPT') {
        _mrlData.sourceBillType = "IN_TASK";
        
		_mrlData.sourceBillCode=localStorage.getItem('tab1Code');
		
        for (var i = 0; i < TEMP_DATA.length; i++) {
            if (TEMP_DATA[i].mrlCode == theObj.mrlCode) {
                _mrlData.sourceBillCode = TEMP_DATA[i].taskCode;
                _mrlData.sourceDetailGid = TEMP_DATA[i].id;
            }
        }
        _mrlData.mrlCode = theObj.mrlCode;
        if (theObj.targetWareroomGid2 != '' && theObj.targetWareroomGid2 != undefined) {
            _mrlData.targetWareroomGid2 = theObj.targetWareroomGid2;
        }
        if (theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined) {
            _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        }
        _mrlData.barCode = theObj.barcode;
        _mrlData.lotCode = theObj.lotCode;
        _mrlData.actualPriQty = theObj.actualPriQty;
        //原材料入库
    } else if (state.htmlName == 'MATERIAL_IN') {
        _mrlData.sourceBillType = "ACCEPT";
		
		_mrlData.sourceBillCode=theObj.sourceBillCode;

        for (var i = 0; i < TEMP_DATA.length; i++) {
            if (TEMP_DATA[i].mrlCode == theObj.mrlCode) {
                _mrlData.sourceBillCode = TEMP_DATA[i].billCode;
                _mrlData.sourceDetailGid = TEMP_DATA[i].id;
            }
        }
        _mrlData.mrlCode = theObj.mrlCode;
        if (theObj.targetWareroomGid2 != '' && theObj.targetWareroomGid2 != undefined) {
            _mrlData.targetWareroomGid2 = theObj.targetWareroomGid2;
        }
        if (theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined) {
            _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        }
        _mrlData.barCode = theObj.barcode;
        _mrlData.lotCode = theObj.lotCode;
        _mrlData.actualPriQty = theObj.qty;
        _mrlData.locationCode = theObj.locationCode;
        _mrlData.proDate = theObj.proDate;
        _mrlData.lotCode2 = theObj.lotCode2;
    } else {
        _mrlData.sourceBillCode = theObj.barcodeRecord.acceptBillCode;
        _mrlData.sourceBillType = "ACCEPT";
        _mrlData.sourceDetailGid = theObj.barcodeRecord.acceptBillDetailGid;
        _mrlData.mrlCode = theObj.mrlCode;
        _mrlData.mrlName = theObj.mrlName;
        if (theObj.targetWareroomGid2 != '' && theObj.targetWareroomGid2 != undefined) {
            _mrlData.targetWareroomGid2 = theObj.targetWareroomGid2;
        }
        if (theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined) {
            _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        }
        // if(theObj.targetWareroomGid3 != '' && theObj.targetWareroomGid3 != undefined){
        //     _mrlData.targetWareroomGid3 = theObj.targetWareroomGid3;
        // }
        _mrlData.barCode = theObj.barcode;
        _mrlData.lotCode = theObj.lotCode;
        _mrlData.actualPriQty = theObj.actualPriQty;
    }

    return _mrlData
}


function doDelete(row, index) {
    row.deleteFlag = "true";
    $table.bootstrapTable('remove', {field: 'deleteFlag', values: [row.deleteFlag]});
    var afterTableData = $table.bootstrapTable('getData');
    api.sendEvent({
        name: 'wmsDetailDataDelete',
        extra: {
            rowData: row,
            afterTableData: afterTableData,
            index: index,
            type: 'delete'
        }
    });
}

apiready = function () {
    initTable(tableField, null, function (row, $element, field) {
        dialog.alert({
            title: "确认删除吗?",
            buttons: ['取消', '确定']
        }, function (ret) {
            if (ret.buttonIndex == 2) {
                doDelete(row, $element.data('index'));
            }
        })
    });
    
    api.addEventListener({
        name: 'wmsDetailDataAdd'
    }, function (ret, err) {
        if (ret.value.type == 'add') {
            // api.alert({
            //     title: 'add', 
            //     msg: ret.value.data
            // });
            $table.bootstrapTable('prepend', JSON.parse(ret.value.data));
        }
    });
    api.addEventListener({
        name: 'clear'
    }, function (ret, err) {
        $table.bootstrapTable('removeAll')
    });
    api.addEventListener({
        name: 'taskData'
    }, function (ret, err) {
    
    
        if (ret.value.data) {
        
        	
            var taskData = ret.value.data;
            // api.alert({
            //  title: 'taskData', 
            //  msg: JSON.stringify(ret.value.data)
            // });
            MRL_DATA.workCenterGid = taskData.workCenterGid
            MRL_DATA.businessType = state.businessType
            MRL_DATA.subStoreType = taskData.subStoreType
            MRL_DATA.deliverGid = taskData.deliverGid
            MRL_DATA.supplierGid = taskData.supplierGid
            MRL_DATA.departmentGid = taskData.departmentGid
            MRL_DATA.customerGid = taskData.customerGid
            MRL_DATA.workOrderCode = taskData.workOrderCode
            MRL_DATA.billDate = taskData.taskDate;
            TEMP_DATA = taskData.details;
        }

    });
}