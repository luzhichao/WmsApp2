var SearchUrl = {   //按条件查询，返回多个结果
    inTask: 'padWmsController!getInTasksByParams.m',
    arrivalBill: 'padWmsController!getArrivalBillsByParams.m',
    outTask: 'padWmsController!getOutTasksByParams.m',
    downTask: 'padWmsController!getDownTasksByParams.m',
    freezeBill: 'padWmsController!getStockFreezesByParams.m',
    inventoryBill: 'padWmsController!getInventoryBillsByParams.m'
};
var GetUrl = {  //通过code查询，返回单个结果
    inTask: 'padWmsController!getInTaskByCode.m',
    arrivalBill: 'padWmsController!getArrivalBillByCode.m',
    outTask: 'padWmsController!getOutTaskByCode.m',
    downTask: 'padWmsController!getDownTaskByCode.m',
    barCode: 'padWmsController!getUbcBarCode.m',
    barCodes: 'padWmsController!getUbcBarCodes.m',
    freezeBill: 'padWmsController!getStockFreezeByCode.m',
    //packageBarCode: 'padWmsController!getUbcPackageBarCode.m', //根据包装条码code查询物料
    inventoryBill: 'padWmsController!getInventoryBillByCode.m',
    inventoryBill_Prd: 'padWmsController!getInventoryBillByCodeForPro.m',
    transferTask: 'padWmsController!getOutTaskByCode.m'
};
var CreateUrl = {   //生成单据
    arrivalBill: 'padWmsController!createArrivalBill.m',
    inBill: 'padWmsController!createInBill.m',
    outBill: 'padWmsController!createOutBill.m',
    inventoryBill: 'padWmsController!saveInventoryBill.m',
    moveBill: 'padWmsController!createMoveBill.m',
    freezeBill: 'padWmsController!createStockFreeze.m',
    lotCode: 'padWmsController!generalLotCode.m' //取lotcode
};
var OtherUrl = {
    getWorkCenterByPid: 'padWmsController!getWcByPid.m',    //通过pid查询子仓库
    getPrinters: 'padWmsController!getSysPrinterIp.m',
    getTemplets: 'padWmsController!getSysPrintTemplet.m',
    getAreaAndSubAreaByWorkCell: 'padWmsController!getAreaAndSubAreaByLocation.m',   //通过库位获取对应的区域库区
    getParentCodeByBarCode: 'padWmsController!getParentCodeByBarCode.m',   //通过箱码获取栈板码
    getBarCodesByParentCode: 'padWmsController!getBarCodesByParentCode.m',   //通过栈板码获取箱码
    packSave: 'padWmsController!packSave.m',   //合托
    unPackSave: 'padWmsController!unpackSave.m',   //拆托
    splitBox: 'padWmsController!splitBox.m',   //原材料分箱
    createPalletCode: 'padWmsController!createPackCode.m',   //生成新的栈板码
    checkBillOper: 'padWmsController!checkBillOper.m',   //判断条码是否已操作
    checkBillOper2: 'padWmsController!checkBillOper2.m',   //判断栈板码是否已操作
    checkBillOper3: 'padWmsController!checkBillOper3.m',   //判断条码是否已经收货
    getStockInfos: 'padWmsController!getStockInfos.m',
    getServicePrinterList: 'padWmsController!getServicePrinterList.m',   //显示系统可用打印机
    printBarCode: 'padWmsController!printBarCode.m', //调用打印功能
    createAcpPrd: 'padWmsController!createAcpPrd.m',    //成品暂收合托
    getWorkCellByWorkCellCode: 'padWmsController!getWorkCellByWorkCellCode.m', //通过库位编码查找库位
    getMrlByMrlCode: 'padWmsController!getMrlByMrlCode.m',  //通过物料编码查找物料
    unFreeze: 'padWmsController!unFreezeStock.m',  //通过物料编码查找物料
    getBarStockInfoByBarCode: 'padWmsController!getBarStockInfo.m',   //通过条码获取库存信息
    getBarStockInfoByBarCodeForFrz: 'padWmsController!getBarStockInfoByParams.m'   //通过条码获取库存信息(专用于冻结)
};

/**
 * 返回符合table字段的对象数组
 * @param details
 * @param tableField
 * @returns {Array}
 */
function formatTableData(details, tableField, businessType) {
    var gridArr = [];
    $.each(details, function (idx, val) {
        var gridItem = {};
        $.each(tableField, function (i, item) {
            gridItem[item.name] = val[item.name]
        });
        // 计算得出剩余数量
        switch (businessType) {
            case TYPE.IN_TASK:
                if (gridItem['arrivalQty']) {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['arrivalQty'];
                } else if (gridItem['inStockQty']) {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['inStockQty'];
                }
                break;
            case TYPE.ARR_Bill:
                if (gridItem['qualifiedQty'] && gridItem['qualifiedQty'] != 0) {       //质检数量存在且不等于0
                    gridItem['remainQty'] = gridItem['qualifiedQty'] - gridItem['inStockQty'];
                } else {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['inStockQty'];
                }
                break;
            case TYPE.OUT_TASK:
                gridItem['remainQty'] = gridItem['qty'] - gridItem['outStockQty'];
                break;
            case TYPE.DOWN_TASK:
                gridItem['remainQty'] = gridItem['qty'] - gridItem['downShelfQty'];
                break;
            case TYPE.TRANSFER_TASK:
                break;
            case TYPE.INVENTORY_BILL:
                //前台不需要做数据处理
                break;
            case 'inventoryBill_Prd':
                //前台不需要做数据处理
                break;
            case TYPE.FRZ_BILL:
                //前台不需要做数据处理
                break;
            default:
                alert('业务类型出错!')
        }
        gridArr.push(gridItem)
    });
    return gridArr
}

/**
 * 初始化Bootstrap Table
 * @param table
 * @param tableField
 * @param tableData
 * @param onClickRowFunction
 */
function initTable(table, tableField, tableData, onClickRowFunction, rowStyle) {
    var $table;
    switch (table) {
        case 0:
            $table = $("#bootstrap-tab");       //单tab结构：tab
            break;
        case 1:
            $table = $("#bootstrap-tab-1");     //三tab结构：tab1
            break;
        case 2:
            $table = $("#bootstrap-tab-2");     //三tab结构：tab2
            break;
        case 3:
            $table = $("#bootstrap-tab-3");     //三tab结构：tab3
            break;
        default:
            alert('初始化BootstrapTable出错!')
    }
    tableData = tableData || [];
    tableField = tableField || [];
    $.each(tableField, function (idx, val) {
        val.field = (val.name || val.field);
        val.title = (val.title || val.value);
        val.visible = (val.visible === undefined ? true : val.visible);
        val.align = 'center';
    });
    //先清除
    //$table.bootstrapTable('destroy');
    $table.bootstrapTable({
        columns: tableField,
        data: tableData,
        onClickRow: onClickRowFunction,
        rowStyle: rowStyle,
        clickToSelect: true
    });
}

/**
 *
 * tab2扫码后更新表格(tab1修改完成数量,tab2新增或者修改,tab3新增)
 * @param data
 */
function updateTableByInsert(data, tableField2, tableField3, isNotContainWorkCell) {
    var tableData2 = $("#bootstrap-tab-2").bootstrapTable('getData');
    var rowId = '';
    if (data.workCellCode && data.mrlCode.substring(0, 4) == '30.K') {
        if (isNotContainWorkCell && isNotContainWorkCell != null) {
            rowId = data.mrlCode + '+' + data.lotCode;
        } else {
            rowId = data.mrlCode + '+' + data.lotCode + '+' + data.workCellCode;
        }
    } else {
        rowId = data.mrlCode + '+' + data.lotCode;
    }
    //更新tab3,必须先更新,后面会对data的数据进行操作
    var data2 = {};
    var data3 = {};
    for (var i = 0; i < tableField2.length; i++) {
        var key = tableField2[i]['name'];
        data2[key] = data[key];
    }
    for (var i = 0; i < tableField3.length; i++) {
        var key = tableField3[i]['name'];
        data3[key] = data[key];
    }
    data3.rowPid = rowId;

    //更新tab2
    var result2 = $.grep(tableData2, function (val) {
        if (val.workCellCode && data.workCellCode) {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode && val.workCellCode === data.workCellCode)
        } else {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
        }
        // return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
    });
    if (result2.length > 0) {
        tableData2 = $.map(tableData2, function (item) {
            if (data.workCellCode && item.workCellCode) {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode && item.workCellCode === data.workCellCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    data.qty = item.qty; //更新数量，供tab1更新
                    data2.qty = item.qty; //更新数量，供tab1更新
                }
            } else {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    data.qty = item.qty; //更新数量，供tab1更新
                    data2.qty = item.qty; //更新数量，供tab1更新
                }
            }

            return item
        })
    } else {
        data2.rowId = rowId;
        tableData2 = tableData2.concat(new Array(data2))
    }
    $('#bootstrap-tab-2').bootstrapTable('load', tableData2);
    $("#bootstrap-tab-3").bootstrapTable('prepend', data3);
    //更新tab1
    setTimeout(function f() {
        updateTable1(data);
    }, 10)
}

/**
 * 更新tab1数据（当前只能通过setTimeout后调用才能更新，未找到原因，可能是因为vue渲染时机的问题，改变time的值很小会加载不出来数据）
 * @param data
 */
function updateTable1(data) {
    var tableData1 = $("#bootstrap-tab-1").bootstrapTable('getData');
    for (var index in tableData1) {
        //成品 不需要判断货位
        // if (data.mrlCode.substring(0, 4) != '30.K' && tableData1[index].workCellCode && tableData1[index].lotCode) {
        //     if (data.mrlCode == tableData1[index].mrlCode && data.workCellCode == tableData1[index].workCellCode && data.lotCode == tableData1[index].lotCode) {
        //         tableData1[index].completeQty = data.qty;
        //         tableData1[index].isUpdate = 1;
        //         break;
        //     }
        // }else
        if (tableData1[index].lotCode) {
            if (data.mrlCode == tableData1[index].mrlCode && data.lotCode == tableData1[index].lotCode) {
                tableData1[index].completeQty = data.qty;
                tableData1[index].isUpdate = 1;
                break;
            }
        } else {
            if (data.mrlCode == tableData1[index].mrlCode) {
                tableData1[index].completeQty = data.qty;
                tableData1[index].isUpdate = 1;
                break;
            }
        }
    }
    $("#bootstrap-tab-1").bootstrapTable('load', tableData1)
}


/**
 * tab3删除一行数据更新表格(tab1与tab2数据保持一致)
 * @param row
 * @param index
 */
function updateTableByDelete(row, index, $element) {
    var $table1 = $("#bootstrap-tab-1");
    var $table2 = $("#bootstrap-tab-2");
    var $table3 = $("#bootstrap-tab-3");
    var tableData1 = $table1.bootstrapTable('getData');
    var tableData2 = $table2.bootstrapTable('getData');

    var rowId = row.mrlCode + '+' + row.lotCode;
    var mrlCode = row.mrlCode;
    var lotCode = row.lotCode;
    var qty = row.qty;
    //更新tab3,删除对应的一行数据
    row.deleteFlag = "true";
    $table3.bootstrapTable('remove', {field: 'deleteFlag', values: [row.deleteFlag]});
    //更新tab2
    for (var i = 0; i < tableData2.length; i++) {
        if (tableData2[i].rowId == rowId) {
            tableData2[i].qty = Number(tableData2[i].qty) - Number(qty);
            if (tableData2[i].qty == 0) {
                $table2.bootstrapTable('remove', {field: 'qty', values: [0]});
            } else {
                $table2.bootstrapTable('updateRow', {index: i, row: tableData2[i]});
            }
            break;
        }
    }
    //更新tab1
    for (var i = 0; i < tableData1.length; i++) {
        var row = tableData1[i];
        if (row.lotCode) {
            if (row.mrlCode === mrlCode && row.lotCode === lotCode) {
                row.completeQty = Number(row.completeQty) - Number(qty);

                //$table1.bootstrapTable('updateRow', {index: i, row: tableData2[i]});
                row.isUpdate = 1;
                $table1.bootstrapTable('updateRow', {index: i, row: row});

                //TODO:更新颜色
                $($element).addClass('info');
                break;
            }
        } else {
            if (row.mrlCode === mrlCode) {
                row.completeQty = Number(row.completeQty) - Number(qty);
                row.isUpdate = 1;
                $table1.bootstrapTable('updateRow', {index: i, row: row});
                $($element).addClass('info');
                break;
            }
        }

    }
}


/**
 * 通过编码加载任务或者单据
 * @param params    查询条件
 * @param urlType   url类型：inTask..
 * @param formData  vue双向数据绑定需要
 * @returns {boolean}
 */
function loadTaskOrBillByCode(params, urlType, formData) {
    //有数据的时，再次加载任务需要确认，以免数据被覆盖
    if (formData.code && formData.code != params.code) {
        api.confirm({
            title: '提示',
            msg: '当前页面存在数据，确认加载覆盖当前数据？',
            buttons: ['确定', '取消']
        }, function (ret) {
            if (ret.buttonIndex === 1) {
                //清空Tab2以及Tab3的数据
                $("#bootstrap-tab-2").bootstrapTable('removeAll');
                $("#bootstrap-tab-3").bootstrapTable('removeAll');
                for (var key in vueData2.locationData) {
                    vueData2.locationData[key] = ''
                }
                for (var key in vueData2.formData) {
                    vueData2.formData[key] = ''
                }
                vueData2.tab2ScanWorkCell = '';
                vueData2.isShowScanWC = true;
                //清空 记录已扫描条码的数组
                cleanUpBarcodeArr();
                loadTaskOrBillByCode_Ajax(params, urlType, formData)
            } else {
                return false;
            }
        })
    } else {
        loadTaskOrBillByCode_Ajax(params, urlType, formData)
    }
}

/**
 * 通过编码加载任务或者单据---ajax请求
 * @param params
 * @param urlType
 * @param formData
 */
function loadTaskOrBillByCode_Ajax(params, urlType, formData) {
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(GetUrl[urlType]),
        dataType: "json",
        data: params,
        async: false,

        success: function (ret) {
            api.hideProgress();
            if (ret && ret.errCode === 0 && ret.data) {
                // console.log(JSON.stringify(ret.data))
                //step 1.表单数据加载
                for (var dataName in formData) {
                    if (dataName == 'workCenterGid') {
                        continue
                    } else if (dataName == 'originBillGid') {
                        if (ret.data['originBillGid']) {
                            formData['originBillGid'] = ret.data['originBillGid'];
                            formData['originBillCode'] = ret.data['originBillCode'];
                        } else {
                            formData['originBillGid'] = ret.data['id'];
                            formData['originBillCode'] = ret.data['code'];
                        }
                    } else if (dataName == 'state') {
                        $('select option[value=\"' + ret.data[dataName] + '\"]').attr('selected', 'selected');
                    } else {
                        formData[dataName] = ret.data[dataName];
                    }

                }
                //step 2.表格数据加载
                var tableData = formatTableData(ret.data.details, tableField1, urlType);
                $("#bootstrap-tab-1").bootstrapTable('load', tableData)
            } else {
                api.toast({msg: '查询出错！', location: 'middle'});
                return false;
            }
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
        }
    });
}

/**
 * 源单查询
 * @param urlType        单据类型
 * @param businessType   业务类型
 */
function querySourceList(urlType, businessType) {
    switch (urlType) {
        case 'inTask':
            api.openWin({
                name: 'inTask_Query',
                url: '../query/inTask_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'outTask':
            api.openWin({
                name: 'outTask_Query',
                url: '../query/outTask_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'outTask_Mrl':
            api.openWin({
                name: 'outTaskMrl_Query',
                url: '../query/outTask_Mrl_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'arrivalBill':
            api.openWin({
                name: 'arrivalBill_Query',
                url: '../query/arrivalBill_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'arrConsBill' :
            api.openWin({
                name: 'arrivalConsBill_Query',
                url: '../query/arrivalConsBill_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'downTask':
            api.openWin({
                name: 'downTask_Query',
                url: '../query/downTask_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'downTask_Mrl':
            api.openWin({
                name: 'downTaskMrl_Query',
                url: '../query/downTask_Mrl_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'inventoryBill':
            api.openWin({
                name: 'inventoryBill_Query',
                url: '../query/inventoryBill_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'inventoryBill_Prd':
            api.openWin({
                name: 'inventoryBill_Prd_Query',
                url: '../query/inventoryBill_Prd_Query.html',
    pageParam: {
        'businessType': businessType
    }
});
break;
case 'transferTask':
api.openWin({
    name: 'transfer_Query',
    url: '../query/transferTask_Query.html',
    pageParam: {
        'businessType': businessType            //'in' or 'out'
    }
});
break;
case 'freezeBill':
api.openWin({
    name: 'stockFreeze_Query',
    url: '../query/stockFreeze_Query.html',
    pageParam: {
        // 'businessType': businessType
    }
});
break;
default:
api.toast({msg: '源单查询类型有误！', location: 'middle'});
}
}

/**
 * 返回源单查询的单据code
 * @param code
 */
function setSourceListBack(code, type) {
    api.closeWin();
    api.sendEvent({
        name: 'setSourceListBack',
        extra: {
            code: code,
            type: type
        }
    });
}

/**
 * 通过上级仓库gid获取下级仓库数组
 * @returns {Array}
 */
function getWCByPid(workCenterPid) {
    var wcArr = []; //仓库数组
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getWorkCenterByPid),
        dataType: "json",
        data: {
            workCenterGid: workCenterPid
        },
        async: false,
        success: function (ret) {
            $.each(ret, function (i, item) {
                var areaObj = {};
                areaObj.key = item.key;
                areaObj.value = item.value;
                wcArr.push(areaObj);
            });
        },
        error: function (e) {
            console.log(JSON.stringify(e));
            alert('加载区域或库区出错！');
        }
    });
    return wcArr;
}

/**
 * 通过库位查找对应的区域库区
 * @param workCellCode
 */
function getAreaAndSubAreaByWorkCell(workCellCode) {
    var retObj = {};
    toast.loading({title: "加载中...", duration: 10000});
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getAreaAndSubAreaByWorkCell),
        async: false,
        dataType: "json",
        data: {
            locationCode: workCellCode,
            workCenterGid: WCEN_GID  //等同于localStorage.getItem('workCenterGid')
        },
        success: function (ret) {
            toast.hide();
            if (ret && ret.errCode === 0 && ret.data) {
                retObj.workCellGid = ret.data.locationGid;
                retObj.areaGid = ret.data.areaGid;
                retObj.areaName = ret.data.areaName;
                retObj.subAreaGid = ret.data.subAreaGid;
                retObj.subAreaName = ret.data.subAreaName;
            } else {
                alert(ret.msg);
            }
        }
    });
    return retObj;
}


/**
 * 以ajax请求方式加载条码信息
 * @param barCode
 */
function loadBarCodeInfoByQuery(barCode, isEnableNull) {
    var retObj = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl('padWmsController!getUbcBarCode.m'),
        dataType: "json",
        data: {
            barCode: barCode,
            isEnableNull: isEnableNull
        },
        async: false,
        success: function (ret) {
            api.hideProgress();
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.toast({msg: ret.msg, location: 'middle'});
            }
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
        }
    });
    return retObj;
}

/**
 * 以ajax请求方式加载条码信息
 * @param barCode
 */
function loadBarCodeInfosByQuery(barCode, type) {
    var retObj = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl('padWmsController!getUbcBarCodes.m'),
        dataType: "json",
        data: {
            barCode: barCode,
            type: type
        },
        async: false,
        success: function (ret) {
            api.hideProgress();
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.toast({msg: ret.msg, location: 'middle'});
            }
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
        }
    });
    return retObj;
}

/**
 * 以解析方式加载条码信息
 * @param barCode
 * @returns {boolean}
 */
function loadBarCodeInfoBySplit(barCode) {
    var mrlCodeArr = barCode.split(',');
    //验证数据准确性
    if (mrlCodeArr.length != 8) {
        api.toast({msg: '条码格式错误：内容不完整！' + msg, duration: 3000, location: 'middle'});
        return false
    }
    //华工正源-条码构成：采购合同号（入库任务）+供应商代码+物料编码+生产日期+生产批次+本箱数量+供货日期+6位流水号
    var retObj = {};
    retObj.inTaskCode = mrlCodeArr[0];             //入库任务编码
    retObj.supplierCode = mrlCodeArr[1];             //供应商编码
    retObj.mrlCode = mrlCodeArr[2];                //物料编码
    retObj.uda2 = mrlCodeArr[3];                //生产日期
    retObj.lotCode = retObj.uda2.replace(/-/g, '');    //批次号（供应商传的批次号是一串字符串，不带日期信息，而wms下架策略是通过带有日期信息的批次号来计算的，所以需要把生产日期作为批次号）
    retObj.lotCode2 = mrlCodeArr[4];               //来料批号（真实的批次号，需要回传给接口）
    retObj.qty = mrlCodeArr[5];                    //本箱数量
    retObj.supplyDate = mrlCodeArr[6];             //供货日期
    retObj.snCode = mrlCodeArr[7];                 //6位流水号

    return retObj;
}

/**
 * 提交
 */
function submit(type, bill, details, barCodes) {
    api.ajax({
        url: getUrl(CreateUrl[type]),
        method: 'post',
        timeout: 60,
        data: {
            values: {
                bill: JSON.stringify(bill),
                details: JSON.stringify(details),
                barCodes: JSON.stringify(barCodes),
                un: localStorage.getItem("un")
            }
        }
    }, function (ret) {
        api.hideProgress();
        if (ret && ret.errCode === 0) {
            api.toast({msg: '创建成功！', location: 'middle'});
            window.location.reload();
        } else {
            api.alert({msg: ret.msg});
        }
    });
}


/**
 * 监听
 * @param api
 */
function setQueryListener(api) {
    api.addEventListener({
        name: 'setSourceListBack'
    }, function (ret) {
        if (ret.value.code) {
            var params = {code: ret.value.code};
            loadTaskOrBillByCode(params, ret.value.type, vueData1.formData)
        } else {
            api.toast({msg: '选中源单编码不存在！', location: 'middle'});
        }
    });
}

/**
 * 冻结解冻
 * @param codeType  条码类型（支持箱码，批次码，栈板码）
 * @param barCode
 * @param formData
 * @param tableIndex
 */
function getStockInfo(codeType, barCode, formData, tableIndex) {
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    api.ajax({
        url: getUrl("padWmsController!getInventoryInfoByBarcode.m"),
        method: 'post',
        timeout: 60,
        data: {
            values: {
                type: codeType,
                barcode: barCode
            }
        }
    }, function (ret) {
        api.hideProgress();
        if (ret && ret.errCode == 0) {
            for (var dataName in formData) {
                formData[dataName] = ret.data[0][dataName];
            }
            for (var i = 0; i < ret.data.length; i++) {
                insertDateAtTable(tableIndex, 'prepend', [{
                    barCode: ret.data[i].barCode,
                    qty: ret.data[i].qty
                }]);
            }
        } else {
            api.toast({msg: ret.msg, duration: 3000, location: 'middle'});
        }
    })
}


//通过条码查库存信息
function getBarStockInfoByBarCode(barCode) {
    var retObj = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getBarStockInfoByBarCode),
        dataType: "json",
        data: {
            barCode: barCode
        },
        async: false,
        success: function (ret) {
            api.hideProgress();
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.toast({msg: "通过条码查询库存失败！ " + JSON.stringify(ret.msg), location: 'middle'});
                return false
            }
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
        }
    });
    return retObj;
}

function getBarStockInfoByBarCodeForFrz(barCode, type, extendCode) {
    var retObj = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getBarStockInfoByBarCodeForFrz),
        dataType: "json",
        data: {
            barCode: barCode,
            type: type,
            extendCode: extendCode
        },
        async: false,
        success: function (ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.hideProgress();
                api.toast({msg: "通过条码查询库存失败" + JSON.stringify(ret.msg), location: 'middle'});
            }
        },
        error: function (e) {
            api.hideProgress();
            console.log(JSON.stringify(e));
        }
    });
    return retObj;
}

function searchStock(workCellCode, mrlCode, lotCode) {
    if (workCellCode == "") {
        workCellCode = null;
    }
    if (mrlCode == "") {
        mrlCode = null;
    }
    if (lotCode == "") {
        lotCode = null;
    }
    var params = {
        //workCenterCode: WCEN_GID,
        workCellCode: workCellCode,
        mrlCode: mrlCode,
        lotCode: lotCode
    };
    console.log("---------------682" + JSON.stringify(params));
    api.ajax({
        url: getUrl(OtherUrl['getStockInfos']),
        method: 'post',
        data: {
            values: {
                data: JSON.stringify(params)
            }
        }
    }, function (ret, err) {
        toast.hide();
        listData = ret;
        if (ret && ret.errCode == 0) {

            $("#bootstrap-tab-1").bootstrapTable('load', ret.data)
        } else {
            api.toast({msg: '查询出错！', location: 'middle'});
            return;
        }
    });
}
