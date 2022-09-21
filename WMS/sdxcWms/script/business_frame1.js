var businessUrl = {
    searchInTask: 'padWmsController!getInTasksByParams.m', //查询入库源单
    searchOutTask: 'padWmsController!getOutTasksByParams.m', //查询出库源单
    searchAcceptBill: 'padWmsController!getAcceptBillsByParams.m',//查询收货单
    getInTask: 'padWmsController!getInTaskByCode.m', //查询入库任
    getOutTask: 'padWmsController!getOutTaskByCode.m', //查询出库任务
    getAcceptBill: 'padWmsController!getAcceptBillByCode.m', //查询收货单
    byBarCode: 'padWmsController!getUbcBarCode.m', //根据code查询物料
    createAcceptBill: 'padWmsController!createAcceptBill.m', //
    createInBill: 'padWmsController!createInBill.m', //创建入库单
    createOutBill: 'padWmsController!createOutBill.m', //创建出库单
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
    if (state.htmlName == 'MATERIAL_IN') {
        $.extend(true, state, {
            searchUrl: 'searchAcceptBill',
            queryUrl: 'getAcceptBill',
        });
    } else if (state.htmlName == 'MATERIAL_ACCEPT') {
        $.extend(true, state, {
            searchUrl: 'searchInTask',
            queryUrl: 'getInTask',
        });
    } else {
        $.extend(true, state, {
            searchUrl: 'searchInTask', //源单查询
            queryUrl: 'getInTask', //任务数据查询
            submitUrl: 'createInBill',
            sourceBillType: 'IN_TASK',
            sourceBillCodeRecord: 'inTaskCode'
        });
    }
}
if (state.warehouseType == 'Out') {
    $.extend(true, state, {
        searchUrl: 'searchOutTask', //源单查询
        queryUrl: 'getOutTask', //任务数据查询
        submitUrl: 'createOutBill',
        sourceBillType: 'OUT_TASK',
        sourceBillCodeRecord: 'outTaskCode'
    });
}

/* 采购退货和销售退货修改业务类型 */
var businessType = state.businessType;

if (localStorage.getItem('projectName') == 'MSTK') {
    if (state.warehouseType == 'In' && businessType == '12') {
        businessType = '7';//销售退货 查询销售出库任务
    }
    if (state.warehouseType == 'Out' && businessType == '12') {
        businessType = '0';//采购退货 查询采购入库任务
    }
}
var tableField = []
/* 页面初始化对象 ↑ */

var TASKDATA = {}

function initTable(gridArr, data, onClickRowFunction) {
    var $table = $table || $("#table")
    gridArr = gridArr || tableField
    data = data || []
    var onClickRow;
    if (onClickRowFunction) {
        onClickRow = onClickRowFunction
    } else if (state.htmlName == 'ACCEPT') {
        var onClickRow = function (row, $element, field) {
            var pageii = layer.open({
                type: 1,
                title: '收货确认',
                className: 'confrim-modal',
                content: '<div>' + $("#modal").html() + '<div>',
                btn: ['确认', '取消'],
                style: 'width:80%;',
                yes: function (index) {
                    // alert('点击了是')
                    layer.close(index)
                },
                no: function (index) {
                    // alert('点击了否')
                    layer.close(index)
                }
            });
        }
        // }else{
        //     onClickRow = function (row, $element, field) {
        //         dialog.prompt({
        //             title: "确认数量",
        //             text: '',
        //             buttons: ['取消', '确定']
        //         }, function (ret) {
        //             if (ret.buttonIndex == 2) {
        //                 update(ret.text, $element.data('index'), field);
        //             }
        //         })
        //         $('.aui-dialog').find('input').focus()
        //     }
    }
    $.each(gridArr, function (idx, val) {
        val.field = (val.name || val.field) //name 必要
        val.title = (val.title || val.value) //title 必要
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

//源单查询
function selectFormData() {
    toast.loading({title: "加载中", duration: 10000});
    var params = {
        "workCenterGid": localStorage.getItem('workCenterGid'),
        "businessType": businessType
    };
    if (state.sourceBillType == 'ACCEPT') {
        params.finishState = 1;
    } else {
        params.state = 1;
    }
    api.ajax({
        url: getUrl(businessUrl[state.searchUrl]),
        method: 'post',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
            values: {
                data: (JSON.stringify(params))
            }
        }
    }, function (res, err) {
        if (res.errCode == 0 && res.data) {
            res.sourceBillType = state.sourceBillType;
            if (state.htmlName === 'MATERIAL_ACCEPT') {
                api.openWin({
                    name: 'wmsPurChReceive_Query',
                    url: 'wmsPurChReceive_Query.html',
                    pageParam: res
                })
            } else if(state.htmlName2 === 'PRODUCT_ACCEPT'){
                api.openWin({
                    name: 'wmsInStore_Query',
                    url: 'wmsInStore_Query.html', //产成品暂收
                    pageParam: res
                })
            } else {
                api.openWin({
                    name: 'wmsAllocationOutStore_Query',
                    url: 'wmsAllocationOutStore_Query.html',
                    pageParam: res
                })
            }

        } else {
            api.alert({
                msg: res.msg
            });
        }
        toast.hide()
    });
}

//根据code查询任务
function loadTaskByCode(taskCode) {
    var params = {
        "workCenterGid": localStorage.getItem('workCenterGid'),
        "businessType": businessType
    };
    if (state.sourceBillType == 'ACCEPT') {
        params.finishState = 1;
    } else {
        params.state = 1;
    }

    if (!taskCode) {
        api.toast({
            msg: '没有取到任务条码！',
            duration: 2000,
            location: 'middle'
        });
        return false
    } else {
        params['code'] = taskCode;
        localStorage.setItem('inTaskCode', taskCode);
    }
    toast.loading({title: "加载中", duration: 10000});
    api.ajax({
        url: getUrl(businessUrl[state.queryUrl]),
        method: 'post',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        data: {
            values: params
        }
    }, function (res, err) {
        toast.hide()
        if (res && res.errCode == 0 && res.data) {

            TASKDATA = res.data || null;
            var gridArr = res.data ? gridData(res.data.details) : null;

            if (state.htmlName === 'PRODUCT_ACCEPT' || state.htmlName === 'ACCEPT' || state.htmlName === 'ALLOCATION_OUT') {
                var tab1_mrlCodes = "";
                for (var i = 0; i < gridArr.length; i++) {
                    // 最后一个数据不需要添加符号： ','
                    if (i == gridArr.length - 1) {
                        tab1_mrlCodes = tab1_mrlCodes + gridArr[i].mrlCode;
                    } else {
                        tab1_mrlCodes = tab1_mrlCodes + gridArr[i].mrlCode + ',';
                    }
                }
                localStorage.setItem('tab1_mrlCodes', tab1_mrlCodes);
            }

			//tab4用
			localStorage.setItem('tab1Code', res.data.code);
			localStorage.setItem('colinSupplierGid', res.data.supplierGid||"");
			localStorage.setItem('colinDepartmentGid', res.data.departmentGid||"");


            var formData = $('form').serializeArray()
            $.each(formData, function (idx, val) {
                if (val.name == 'supplierName') {
                    var tmpSupplierName = res.data[val.name] || "";
                    var tmpSupplierCode = res.data['supplierCode'] || "";
                    if (tmpSupplierName != '') {
                        $(":input[name=" + val.name + "]").val(tmpSupplierName + '(' + tmpSupplierCode + ')');
                    }

                } else if (val.name == 'departmentName') {
                    var tmpDepartmentName = res.data[val.name] || "";
                    var tmpDepartmentCode = res.data['departmentCode'] || "";
                    if (tmpDepartmentName != '') {
                        $(":input[name=" + val.name + "]").val(tmpDepartmentName + '(' + tmpDepartmentCode + ')');
                    }

                } else {
                    $(":input[name=" + val.name + "]").val(res.data[val.name]);
                }
            })
            $("#barCode").val('');

            //
            localStorage.setItem('tab1Data', TASKDATA);
            api.sendEvent({
                name: 'taskdata',
                extra: {
                    data: TASKDATA
                }
            });
            initTable(null, gridArr)

        } else {
            api.alert({
                title: '任务查询出错',
                msg: res.msg
            });
        }

        // 调拨 tab1中的明细是汇总过的，需要通过单据号找到对应下架作业中的条码
        if (state.htmlName2 === 'ALLOCATION_IN') {
            api.ajax({
                url: getUrl('padWmsController!getDownBillDetailsBarcodeBySourceCode.m'),
                method: 'post',
                data: {
                    values: {
                        code: taskCode
                    }
                }
            }, function (res2, err2) {
                if (res2 && res2.errCode == 0 && res2.data) {
                    localStorage.setItem('tab1MrlArr', res2.data);
                } else {
                    api.alert({title: '查询出错！', msg: err2.msg});
                }
            });
        }

    });
}

function submit() {
    if (!TASKDATA.details) {
        api.toast({
            msg: '请先获取任务！'
        })
        return
    }
    //表头数据
    var submitData = {
        "workCenterGid": TASKDATA.workCenterGid,
        "workCenterCode": TASKDATA.workCenterCode,
        "businessType": state.businessType,
        "subStoreType": null,
        "deliverGid": TASKDATA.deliverGid,
        "supplierGid": TASKDATA.supplierGid,
        "workOrderCode": TASKDATA.workOrderCode,
        "remark": '',
        "details": []
    }
    //表单数据
    var rowNum = 0;
    var tableData = $table.bootstrapTable('getData');
    $.each(tableData, function (idx, val) {
        if (!val.actualPriQty) {
            toast.custom({
                title: "请填写" + val.mrlName + "实际数量",
                html: '<i class="aui-iconfont aui-icon-info"></i>',
                duration: 2000
            })
            return false
        }
        rowNum++;
        var mapData = {
            "sourceBillType": state.sourceBillType,
            "sourceBillCode": $("#taskCode").val(),
            "sourceDetailGid": val.id,
            "mrlGid": val.mrlGid,
            "mrlCode": val.mrlCode,
            "mrlName": val.mrlName,
            "targetWareroomGid": val.targetWareroomGid,
            "targetWareroomName": val.targetWareroomName,
            "targetWareroomCode": val.targetWareroomCode,
            "primaryMeasureGid": val.priMeasureGid,
            "primaryMeasureCode": val.primaryMeasureCode,
            "primaryMeasureName": val.priMeasureName,
            "lotCode": val.lotCode,
            "rowNum": rowNum
        }
        if (state.sourceBillType == 'ACCEPT') {
            mapData.actualPriQty = val.qualifiedPriQty;
            mapData.trayMrlNum = val.trayMrlNum;
            //mapData.details = val.details;
        }
        else {
            mapData.actualPriQty = val.actualPriQty;
        }
        submitData.details.push(mapData)
    })
    if (TASKDATA.details.length != submitData.details.length) {
        return false
    }

    toast.loading({
        title: "提交中",
        duration: 10000
    })
    // api.alert({
    // 	msg: JSON.stringify(submitData)
    // })
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
        // api.alert({
        // 	msg: JSON.stringify(res) + '______' + JSON.stringify(err)
        // });
        if (res && res.errCode == 0) {
            toast.success({
                title: res.msg,
                duration: 2000
            });
            //提交成功后清空页面数据
            clearTable()

        } else {
            toast.fail({
                title: res.msg,
                duration: 2000
            });
        }
    });
}

function update(value, index, field) {
    if (value) {
        if (state.sourceBillType == 'ACCEPT') {
            TASKDATA.details[index]['qualifiedPriQty'] = value
            //更新表格
            $('#table').bootstrapTable('updateCell', {
                "index": index,
                "field": 'qualifiedPriQty',
                "value": value
            });

            //添加已更新样式
            $.each(TASKDATA.details, function (idx, val) {
                if (val.qualifiedPriQty == undefined || val.qualifiedPriQty == null) {
                    return true
                } else {
                    $("#table").find('tr').eq(idx + 1).addClass('aui-bg-green')
                }
            })
        }
        else {
            TASKDATA.details[index]['actualPriQty'] = value
            //更新表格
            $('#table').bootstrapTable('updateCell', {
                "index": index,
                "field": 'actualPriQty',
                "value": value
            });

            //添加已更新样式
            $.each(TASKDATA.details, function (idx, val) {
                if (val.actualPriQty == undefined || val.actualPriQty == null) {
                    return true
                } else {
                    $("#table").find('tr').eq(idx + 1).addClass('aui-bg-green')
                }
            })
        }
    }
}

function setQueryListener(api) {
    //新增源单通用之后 用这个
    api.addEventListener({
        name: 'searchByCode'
    }, function (ret, err) {
        if (ret.value.code) {
            loadTaskByCode(ret.value.code)
        } else {
            api.toast({
                msg: '暂无源单',
                duration: 2000,
                location: 'middle'
            });
        }
    });
    api.addEventListener({
        name: 'wmsPurchReceive1PageEvent'
    }, function (ret, err) {
        if (ret.value.code) {
            loadTaskByCode(ret.value.code)
        }
    })
    api.addEventListener({
        name: 'clear'
    }, function (ret, err) {
        clearTable()
    })
    api.addEventListener({
        name: 'wmsCheckInStore_mstk'
    }, function (ret, err) {
        $table.bootstrapTable('updateCell', {
            "index": ret.value.rowNum,
            "field": 'qualifiedPriQty',
            "value": ret.value.qty
        });
        /*
        $table.bootstrapTable('updateCell', {
            "index" : ret.value.rowNum,
            "field" : 'details',
            "value" : ret.value.details
        });
        */
        $table.bootstrapTable('updateCell', {
            "index": ret.value.rowNum,
            "field": 'trayMrlNum',
            "value": ret.value.trayMrlNum
        });

        //添加已更新样式
        $.each(TASKDATA.details, function (idx, val) {
            if (val.qualifiedPriQty == undefined || val.qualifiedPriQty == null) {
                return true
            } else {
                $("#table").find('tr').eq(idx + 1).addClass('aui-bg-green')
            }
        })
    });
}

//清除输入框，表格，任务数据
function clearTable() {
    var formData = $('form').serializeArray()
    $.each(formData, function (idx, val) {
        $(":input[name=" + val.name + "]").val('')
    })
    $table.bootstrapTable('removeAll')
    TASKDATA = {}
}

//返回符合表格字段的对象数组
function gridData(details) {
    var gridArr = []
    $.each(details, function (idx, val) {
        var gridItem = {}
        $.each(tableField, function (i, item) {
            gridItem[item.name] = val[item.name]
        })
        gridArr.push(gridItem)
    })
    return gridArr
}


//页面ready方法
apiready = function () {
    //提前定义表格字段数组gridArr
    initTable();
    //initSelect();
    if (state.htmlName = 'ACCEPT') {
        localStorage.setItem('colinSupplierGid', '');
        localStorage.setItem('colinDepartmentGid', '');

        localStorage.setItem('tab1_mrlCodes', '');
        localStorage.setItem('areaGid', '');
        localStorage.setItem('subAreaGid', '');
        localStorage.setItem('areaName', '');
        localStorage.setItem('subAreaName', '');
    }

    //设置监听
    setQueryListener(api)
    //监听条码输入
    $("#barCode").bind('keydown', function (event) {
        var code = $("#barCode").val()
        if (event.keyCode == "13") {
            if (!code) {
                api.toast({
                    msg: '请填入任务条码',
                    duration: 2000,
                    location: 'bottom'
                });
                return false
            }
            loadTaskByCode(code)
        }
    });
}
