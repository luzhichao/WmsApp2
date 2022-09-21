var SearchUrl = { //按条件查询，返回多个结果
    inTask: 'padWmsController!getInTasksByParams.m',
    arrivalBill: 'padWmsController!getArrivalBillsByParams.m',
    upTask: 'padWmsController!getUpTasksByParams.m',
    outTask: 'padWmsController!getOutTasksByParams.m',
    downTask: 'padWmsController!getDownTasksByParams.m',
    freezeBill: 'padWmsController!getStockFreezesByParams.m',
    inventoryBill: 'padWmsController!getInventoryTasks.m', //获取盘点任务单列表
    libraryBill: '', //获取越库单列表
};
var GetUrl = { //通过code查询，返回单个结果
    inTask: 'padWmsController!getInTaskByCode.m',
    inTaskByBarCode: 'padWmsController!getInTaskByBarCode.m',
    arrivalBill: 'padWmsController!getArrivalBillByCode.m',
    outTask: 'padWmsController!getOutTaskByCode.m',
    upTask: 'padWmsController!getUpTaskByCode.m',
    downTask: 'padWmsController!getDownTaskByCode.m',
    barCode: 'padWmsController!getUbcBarCode.m', //用于条码打印
    barCodes: 'padWmsController!getUbcBarCodes.m', // 未用到 html中查不到
    freezeBill: 'padWmsController!getStockFreezeByCode.m',
    MrlInfo: 'padWmsController!getMrlInfoByBarCode.m', //0623后台重构
    //packageBarCode: 'padWmsController!getUbcPackageBarCode.m', //根据包装条码code查询物料
    inventoryBill: 'padWmsController!getInventoryBillByCode.m',
    inventoryBill_Prd: 'padWmsController!getInventoryTaskDetailByTaskCode.m', //盘点任务待盘库位列表
    libraryBill: "", //获取越库任务单明细
    transferTask: 'padWmsController!getOutTaskByCode.m',
    getUpTasksByCtBarCode: 'padWmsController!getUpTasksByCtBarCode.m', //获取容器的推荐库位
    getMrlInfoByBarCode: 'padWmsController!getMrlInfoByBarCode.m' //获取容器的物料信息 0623后台重构 和上面重复
};
var CreateUrl = { //生成单据
    //arrivalBill: 'padWmsController!createArrivalBill.m',
    arrivalBill: 'uwmReceiptBillController!saveReceiptBillPda.m',
    inBill: 'padWmsController!createInBill.m',
    outBill: 'padWmsController!createOutBill.m',
    inventoryBill: 'padWmsController!saveInventoryBill.m',
    moveBill: 'padWmsController!createMoveBill.m',
    freezeBill: 'padWmsController!createStockFreeze.m',
    lotCode: 'padWmsController!generalLotCode.m', //取lotcode
    upBill: 'padWmsController!ctUpShelf.m'
};
var OtherUrl = {
    getWorkCenterByPid: 'padWmsController!getWcByPid.m', //通过pid查询子仓库
    getPrinters: 'padWmsController!getSysPrinterIp.m',
    getTemplets: 'padWmsController!getSysPrintTemplet.m',
    getSysBarCodeRules: 'padWmsController!getSysBarCodeRule.m',
    getAreaAndSubAreaByWorkCell: 'padWmsController!getAreaAndSubAreaByLocation.m', //通过库位获取对应的区域库区
    getParentCodeByBarCode: 'padWmsController!getParentCodeByBarCode.m', //通过箱码获取栈板码  正源代码 已不会用到
    getBarCodesByParentCode: 'padWmsController!getBarCodesByParentCode.m', //通过栈板码获取箱码  正源代码 已不会用到
    packSave: 'padWmsController!packSave.m', //合托
    unPackSave: 'padWmsController!unpackSave.m', //拆托
    allPackSave: 'padWmsController!packCt.m', //组拆托接口
    splitBox: 'padWmsController!splitBox.m', //原材料分箱
    createPalletCode: 'padWmsController!createPackCode.m', //生成新的栈板码
    createCntrCode: 'padWmsController!createCntrCode.m', //生成新的容器码  0623后台重构
    createBarCode: 'padWmsController!createBarCode.m', //生成新的栈板码
    checkBillOper: 'padWmsController!checkBillOper.m', //判断条码是否已操作   正源代码 已不会用到
    checkBillOper2: 'padWmsController!checkBillOper2.m', //判断栈板码是否已操作  正源代码 已不会用到
    checkBillOper3: 'padWmsController!checkBillOper3.m', //判断条码是否已经收货  正源代码 已不会用到
    getStockInfos: 'padWmsController!getStockInfos.m',
    getServicePrinterList: 'padWmsController!getServicePrinterList.m', //显示系统可用打印机
    printBarCode: 'padWmsController!printBarCode.m', //调用打印功能
    createAcpPrd: 'padWmsController!createAcpPrd.m', //成品暂收合托
    getWorkCellByWorkCellCode: 'padWmsController!getWorkCellByWorkCellCode.m', //通过库位编码查找库位
    getMrlByMrlCode: 'padWmsController!getMrlByMrlCode.m', //通过物料编码查找物料
    unFreeze: 'padWmsController!unFreezeStock.m', //通过物料编码查找物料
    getBarStockInfoByBarCode: 'padWmsController!getBarStockInfo.m', //通过条码获取库存信息  正源代码 已不会用到
    getBarStockInfoByBarCodeForFrz: 'padWmsController!getBarStockInfoByParams.m' //通过条码获取库存信息(专用于冻结)  正源代码 已不会用到
};

/**
 * 返回符合table字段的对象数组
 * @param details
 * @param tableField
 * @returns {Array}
 */
function formatTableData(details, tableField, businessType) {
    var gridArr = [];
    $.each(details, function(idx, val) {
        var gridItem = {};
        $.each(tableField, function(i, item) {
            gridItem[item.name] = val[item.name]
        });
        // 计算得出剩余数量
        switch (businessType) {
            case TYPE.IN_TASK:
                if (gridItem['arrivalQty']) {
                    //gridItem['remainQty'] = gridItem['qty'] - gridItem['arrivalQty'];
                } else if (gridItem['inStockQty']) {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['inStockQty'];
                }
                break;
            case TYPE.IN_TASK_CRLCODE:
                if (gridItem['arrivalQty']) {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['arrivalQty'];
                } else if (gridItem['inStockQty']) {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['inStockQty'];
                }
                break;
            case TYPE.ARR_Bill:
                if (gridItem['qualifiedQty'] && gridItem['qualifiedQty'] != 0) { //质检数量存在且不等于0
                    gridItem['remainQty'] = gridItem['qualifiedQty'] - gridItem['inStockQty'];
                } else {
                    gridItem['remainQty'] = gridItem['qty'] - gridItem['inStockQty'];
                }
                break;
            case TYPE.OUT_TASK:
                gridItem['completeQty'] = 0;
                gridItem['remainQty'] = gridItem['qty'] - gridItem['outStockQty'];
                break;
            case TYPE.UP_TASK:
                gridItem['remainQty'] = gridItem['qty'] - gridItem['upShelfQty'];
                break;
            case TYPE.DOWN_TASK:
                gridItem['remainQty'] = gridItem['qty'] - gridItem['downShelfQty'];
                break;
            case TYPE.TRANSFER_TASK:
                break;
            case TYPE.INVENTORY_BILL:
                //前台不需要做数据处理
                break;
            case "inventoryBill_Prd":
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
            $table = $("#bootstrap-tab"); //单tab结构：tab
            break;
        case 1:
            $table = $("#bootstrap-tab-1"); //三tab结构：tab1
            break;
        case 2:
            $table = $("#bootstrap-tab-2"); //三tab结构：tab2
            break;
        case 3:
            $table = $("#bootstrap-tab-3"); //三tab结构：tab3
            break;
        case 4:
            $table = $("#bootstrap-tab-4"); //三tab结构：tab3
            break;
        default:
            alert('初始化BootstrapTable出错!')
    }
    tableData = tableData || [];
    tableField = tableField || [];
    $.each(tableField, function(idx, val) {
        val.field = (val.name || val.field);
        val.title = (val.title || val.value);
        val.visible = (val.visible === undefined ? true : val.visible);
        val.cardVisible = true;
        val.align = 'center';
    });
    //先清除
    //$table.bootstrapTable('destroy');
    $table.bootstrapTable({
        columns: tableField,
        data: tableData,
        onClickRow: onClickRowFunction,
        rowStyle: rowStyle
    });
}

/**
 *
 * tab2扫码后更新表格(tab1修改完成数量,tab2新增或者修改,tab3新增)
 * @param data
 */
function updateTableByInsert(data, tableField2, tableField3, isNotContainWorkCell) {
    var tableData2 = $("#bootstrap-tab-2").bootstrapTable('getData');
    var rowId = data.mrlCode + '+' + data.lotCode;
    // if (data.workCellCode) {
    //     if (isNotContainWorkCell && isNotContainWorkCell != null) {
    //         rowId = data.mrlCode + '+' + data.lotCode;
    //     } else {
    //         rowId = data.mrlCode + '+' + data.lotCode + '+' + data.workCellCode;
    //     }
    // } else {
    //     rowId = data.mrlCode + '+' + data.lotCode;
    // }
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
    data3.rowId = rowId;

    //更新tab2
    var result2 = $.grep(tableData2, function(val) {
        if (val.workCellCode && data.workCellCode) {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode && val.workCellCode === data.workCellCode)
        } else {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
        }
        // return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
    });
    if (result2.length > 0) {
        tableData2 = $.map(tableData2, function(item) {
            if (data.workCellCode && item.workCellCode) {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode && item.workCellCode === data.workCellCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    //data.qty = item.qty; //更新数量，供tab1更新   汇总tap1上面的数量注释掉
                    data2.qty = item.qty; //更新数量，供tab1更新
                }
            } else {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    //data.qty = item.qty; //更新数量，供tab1更新  汇总tap1上面的数量注释掉
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

function updateTableByInsert_finish(data, tableField2, tableField3, isNotContainWorkCell) {
    var tableData2 = $("#bootstrap-tab-2").bootstrapTable('getData');
    var rowId = data.mrlCode + '+' + (tableData2.lenght + 1);
    // if (data.workCellCode) {
    //     if (isNotContainWorkCell && isNotContainWorkCell != null) {
    //         rowId = data.mrlCode + '+' + data.lotCode;
    //     } else {
    //         rowId = data.mrlCode + '+' + data.lotCode + '+' + data.workCellCode;
    //     }
    // } else {
    //     rowId = data.mrlCode + '+' + data.lotCode;
    // }
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
    data3.rowId = rowId;
    //更新tab2
    var result2 = $.grep(tableData2, function(val) {
        if (val.workCellCode && data.workCellCode) {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode && val.workCellCode === data.workCellCode)
        } else {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
        }
        // return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
    });
    if (result2.length > 0) {
        tableData2 = $.map(tableData2, function(item) {
            if (data.workCellCode && item.workCellCode) {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode && item.workCellCode === data.workCellCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    //data.qty = item.qty; //更新数量，供tab1更新   汇总tap1上面的数量注释掉
                    data2.qty = item.qty; //更新数量，供tab1更新
                }
            } else {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    //data.qty = item.qty; //更新数量，供tab1更新  汇总tap1上面的数量注释掉
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
                if (typeof(tableData1[index].completeQty) == "undefined") {
                    tableData1[index].completeQty = 0;
                }
                tableData1[index].completeQty = Number(data.qty) + Number(tableData1[index].completeQty);
                tableData1[index].isUpdate = 1;
                if (tableData1[index].remainQty == data.qty) {
                    api.alert({
                        title: '提示',
                        msg: '物料: 【' + data.mrlCode + '】 的扫描数量已经达到剩余数量！'
                    });
                } else if (tableData1[index].remainQty < data.qty) {
                    api.alert({
                        title: '警告',
                        msg: '物料: 【' + data.mrlCode + '】 的扫描数量已经超过剩余数量，请在已扫描界面进行删除！'
                    });
                    break;
                }
            }
        } else {
            if (data.mrlCode == tableData1[index].mrlCode) {

                if (typeof(tableData1[index].completeQty) == "undefined") {
                    tableData1[index].completeQty = 0;
                }
                tableData1[index].completeQty = Number(data.qty) + Number(tableData1[index].completeQty);
                tableData1[index].isUpdate = 1;
                if (tableData1[index].remainQty == data.qty) {
                    // api.alert({
                    //     title: '警告',
                    //     msg: '物料: 【' + data.mrlCode + '】 的扫描数量已经达到剩余数量！'
                    // });
                } else if (tableData1[index].remainQty < data.qty) {
                    api.alert({
                        title: '警告',
                        msg: '物料: 【' + data.mrlCode + '】 的扫描数量已经超过剩余数量，请在已扫描界面进行删除！'
                    });
                }
                break;
            }
        }
    }
    api.toast({ msg: '保存成功！', location: 'middle' });
    $("#bootstrap-tab-1").bootstrapTable('load', tableData1)
}

/**
 *
 * tab2扫码后更新表格(tab1修改完成数量,tab2新增或者修改,tab3新增)
 * @param data
 */
function updateTableByInsert_(data, tableField2, isNotContainWorkCell) {
    var tableData2 = $("#bootstrap-tab-2").bootstrapTable('getData');
    var rowId = '';
    if (data.workCellCode) {
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
    for (var i = 0; i < tableField2.length; i++) {
        var key = tableField2[i]['name'];
        data2[key] = data[key];
    }

    //更新tab2
    var result2 = $.grep(tableData2, function(val) {
        if (val.workCellCode && data.workCellCode) {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode && val.workCellCode === data.workCellCode)
        } else {
            return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
        }
        // return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode)
    });
    if (result2.length > 0) {
        tableData2 = $.map(tableData2, function(item) {
            if (data.workCellCode && item.workCellCode) {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode && item.workCellCode === data.workCellCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    //data.qty = item.qty; //更新数量，供tab1更新   汇总tap1上面的数量注释掉
                    data2.qty = item.qty; //更新数量，供tab1更新
                }
            } else {
                if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode) {
                    item.qty = parseInt(item.qty) + parseInt(data.qty || data.actualPriQty);
                    //data.qty = item.qty; //更新数量，供tab1更新  汇总tap1上面的数量注释掉
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
    //更新tab1
    // setTimeout(function f() {
    //     updateTable1(data);
    // }, 10)
}



/**
 *
 * tab2扫码后更新表格(tab1修改完成数量,tab2新增或者修改,tab3新增)
 * @param data
 */
function updateTable3ByInsertForPrd(data, tableField3) {
    var rowId = data.mrlCode + '+' + data.lotCode;
    var data3 = {};
    for (var i = 0; i < tableField3.length; i++) {
        var key = tableField3[i]['name'];
        data3[key] = data[key];
    }
    data3.rowId = rowId;
    $("#bootstrap-tab-3").bootstrapTable('prepend', data3);
}

/**
 * 更新tab1数据（当前只能通过setTimeout后调用才能更新，未找到原因，可能是因为vue渲染时机的问题，改变time的值很小会加载不出来数据）
 * @param data
 */
function updateTable23ByInsertForPrd(data, tableField2) {
    var rowId = data.mrlCode + '+' + data.lotCode;
    //更新tab2
    var data2 = {};
    for (var i = 0; i < tableField2.length; i++) {
        var key = tableField2[i]['name'];
        data2[key] = data[key];
    }
    var tableData2 = $("#bootstrap-tab-2").bootstrapTable('getData');
    var result2 = $.grep(tableData2, function(val) {
        return (val.mrlCode === data.mrlCode && val.lotCode === data.lotCode && val.workCellCode === data.workCellCode)
    });
    if (result2.length > 0) {
        tableData2 = $.map(tableData2, function(item) {
            if (item.mrlCode === data.mrlCode && item.lotCode === data.lotCode && item.workCellCode === data.workCellCode) {
                item.qty = parseInt(item.qty) + parseInt(data.qty);
                data.qty = item.qty; //更新数量，供tab1更新
                data2.qty = item.qty;
            }
            return item
        })
    } else {
        data2.rowId = rowId;
        tableData2 = tableData2.concat(new Array(data2))
    }
    $('#bootstrap-tab-2').bootstrapTable('load', tableData2);

    //更新tab1
    var tableData1 = $("#bootstrap-tab-1").bootstrapTable('getData');
    for (var index in tableData1) {
        //成品 不需要判断货位
        if (data.mrlCode == tableData1[index].mrlCode) {
            tableData1[index].completeQty = data.qty;
            tableData1[index].isUpdate = 1;
            break;
        }
    }
    $("#bootstrap-tab-1").bootstrapTable('load', tableData1)
}

/**
 * tab3删除一行数据更新表格(tab1与tab2数据保持一致)
 * @param row
 * @param index
 */
function updateTableByDelete(row, index, $element, isMatchWC) {
    var $table1 = $("#bootstrap-tab-1");
    var $table2 = $("#bootstrap-tab-2");
    var $table3 = $("#bootstrap-tab-3");
    var tableData1 = $table1.bootstrapTable('getData');
    var tableData2 = $table2.bootstrapTable('getData');
    var rowId = row.mrlCode + '+' + row.lotCode;
    var mrlCode = row.mrlCode;
    var lotCode = row.lotCode;
    var workCellCode = row.workCellCode;
    var qty = row.qty;
    //更新tab3,删除对应的一行数据
    row.deleteFlag = "true";
    $table3.bootstrapTable('remove', { field: 'deleteFlag', values: [row.deleteFlag] });
    //更新tab2

    for (var i = 0; i < tableData2.length; i++) {
        if (tableData2[i].rowId == row.rowId) {
            tableData2[i].qty = Number(tableData2[i].qty) - Number(qty);
            if (tableData2[i].qty == 0) {
                $table2.bootstrapTable('remove', { field: 'qty', values: [0] });
            } else {
                $table2.bootstrapTable('updateRow', { index: i, row: tableData2[i] });
            }
            break;
        }
    }
    //更新tab1
    for (var i = 0; i < tableData1.length; i++) {
        var row = tableData1[i];
        if (isMatchWC) {
            if (row.mrlCode === mrlCode && row.lotCode === lotCode && row.workCellCode === workCellCode) {
                row.completeQty = Number(row.completeQty) - Number(qty);
                row.isUpdate = 1;
                $table1.bootstrapTable('updateRow', { index: i, row: row });
                $($element).addClass('info');
                break;
            }
        } else {
            if (row.lotCode) {
                if (row.mrlCode === mrlCode && row.lotCode === lotCode) {
                    row.completeQty = Number(row.completeQty) - Number(qty);

                    //$table1.bootstrapTable('updateRow', {index: i, row: tableData2[i]});
                    row.isUpdate = 1;
                    $table1.bootstrapTable('updateRow', { index: i, row: row });

                    //TODO:更新颜色
                    $($element).addClass('info');
                    break;
                }
            } else {
                if (row.mrlCode === mrlCode) {
                    row.completeQty = Number(row.completeQty) - Number(qty);
                    row.isUpdate = 1;
                    $table1.bootstrapTable('updateRow', { index: i, row: row });
                    $($element).addClass('info');
                    break;
                }
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
        }, function(ret) {
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
    console.log(JSON.stringify(params));
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

        success: function(ret) {
            api.hideProgress();
            if (ret && ret.errCode === 0 && ret.data) {
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
                    } else if (dataName == "taskTypeName") {
                        formData['taskTypeName'] = ret.data['taskType'] == 0 ? '区域计划' : '物料计划'
                    } else {
                        formData[dataName] = ret.data[dataName];
                    }

                }
                //step 2.表格数据加载
                var tableData = formatTableData(ret.data.details, tableField1, urlType);
                $("#bootstrap-tab-1").bootstrapTable('load', tableData)
            } else {
                api.toast({ msg: '查询出错：' + ret.msg, location: 'middle' });
                return false;
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
}

/*
 *通过SN码获取任务单号
 *
 */
function loadTaskOrBillByMrlCode(params, urlType, formData) {
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
        data: {
            barCode: params.barCode
        },
        async: false,

        success: function(ret) {
            api.hideProgress();
            if (ret && ret.errCode === 0 && ret.data) {
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
                api.toast({ msg: '查询出错：' + ret.msg, location: 'middle' });
                return false;
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
}

function loadTaskOrBillByMrlCode_finish(params, urlType, formData, taskCode, requestCount, isEqual) {
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
        data: {
            barCode: params.barCode
        },
        async: false,

        success: function(ret) {
            api.hideProgress();
            if (ret && ret.errCode === 0 && ret.data) {
                if (!requestCount) {
                    if (taskCode == ret.data['code']) {
                        return isEqual = true;
                    } else {
                        isEqual = false;
                        api.toast({ msg: '该物料不存在于任务单号：' + taskCode + '之中', location: 'middle' });
                        return false;
                    }
                }
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
                $("#bootstrap-tab-1").bootstrapTable('load', tableData);
            } else {
                api.toast({ msg: '查询出错：' + ret.msg, location: 'middle' });
                return false;
            }
        },
        error: function(e) {
            api.hideProgress();
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
        case 'arrConsBill':
            api.openWin({
                name: 'arrivalConsBill_Query',
                url: '../query/arrivalConsBill_Query.html',
                pageParam: {
                    'businessType': businessType
                }
            });
            break;
        case 'upTask':
            api.openWin({
                name: 'upTask_Query',
                url: '../query/upTask_Query.html',
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
                    'businessType': businessType //'in' or 'out'
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
        case 'libarary_work':
            api.openWin({
                name: 'library_query',
                url: '../query/library_query.html'
            });
            break;
        default:
            api.toast({ msg: '源单查询类型有误！', location: 'middle' });
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
 * 返回源单查询的单据code
 * @param code
 */
function setSourceListBack_Task(code, type) {
    api.closeWin();
    api.sendEvent({
        name: 'setSourceListBack_Task',
        extra: {
            taskCode: code,
            type: type
        }
    });
}

/**
 * 返回源单查询的单据code
 * @param code
 */
function setSourceListBack_taskDetail() {
    api.closeWin();
    api.sendEvent({
        name: 'setSourceListBack'
    });
}

//出库单
function setOutBillSourceListBack(code, type) {
    api.closeWin();
    api.sendEvent({
        name: 'setOutBillSourceListBack',
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
        success: function(ret) {
            $.each(ret, function(i, item) {
                var areaObj = {};
                areaObj.key = item.key;
                areaObj.value = item.value;
                wcArr.push(areaObj);
            });
        },
        error: function(e) {
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
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '加载中...',
        text: '请稍候...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getMrlByMrlCode),
        async: false,
        dataType: "json",
        data: {
            mrlCode: workCellCode,
            workCenterGid: WCEN_GID //等同于localStorage.getItem('workCenterGid')
        },
        success: function(ret) {
            if (ret && ret.errCode === 0 && ret.data) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
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
function loadBarCodeInfoByQuery(mrlCode) {
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
        url: getUrl(OtherUrl.getMrlByMrlCode),
        dataType: "json",
        data: {
            mrlCode: mrlCode,
            workCenterGid: WCEN_GID
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.alert({ title: '提示', msg: ret.msg });
            }
            api.hideProgress();
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *扫描物料条码获取在收货缓存区相关数据
 */
function getMrlInformationByQuery(mrlCode, workCellCode) {
    var retObj = [];
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl('padWmsController!getUnPackMrlLotQty.m'),
        dataType: "json",
        data: {
            mrlCode: mrlCode,
            workCellCode: workCellCode
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                for (var i = 0; i < ret.data.length; i++) {
                    retObj.push(ret.data[i]);
                }
            } else {
                api.alert({ title: '提示', msg: ret.msg });
            }
            api.hideProgress();
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *根据sn查询库位
 */
function getMrlInformationBySN(barCode) {
    var res = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl('padWmsController!getWorkCellByBarcode.m'),
        dataType: "json",
        data: {
            barCode: barCode
        },
        async: false,
        success: function(ret) {
            console.log(JSON.stringify(ret));
            if (ret.errCode == 0) {
                res = ret.data;
            } else {
                api.alert({ title: '提示', msg: ret.msg });
            }
            api.hideProgress();
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return res;
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
        success: function(ret) {
            api.hideProgress();
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.toast({ msg: ret.msg, location: 'middle' });
            }
        },
        error: function(e) {
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
    // if (mrlCodeArr.length != 8) {
    //     api.toast({msg: '条码格式错误：内容不完整！' + msg, duration: 3000, location: 'middle'});
    //     return false
    // }
    //华工正源-条码构成：采购合同号（入库任务）+供应商代码+物料编码+生产日期+生产批次+本箱数量+供货日期+6位流水号
    var retObj = {};
    retObj.inTaskCode = $.trim(mrlCodeArr[0]); //入库任务编码
    retObj.supplierCode = $.trim(mrlCodeArr[1]); //供应商编码
    retObj.mrlCode = $.trim(mrlCodeArr[2]); //物料编码
    // retObj.uda2 = $.trim(mrlCodeArr[3]);                //生产日期
    // retObj.lotCode = retObj.uda2.replace(/-/g, '');    //批次号（供应商传的批次号是一串字符串，不带日期信息，而wms下架策略是通过带有日期信息的批次号来计算的，所以需要把生产日期作为批次号）
    retObj.lotCode = $.trim(mrlCodeArr[4]);
    // retObj.lotCode2 = $.trim(mrlCodeArr[4]);               //来料批号（真实的批次号，需要回传给接口）
    retObj.qty = $.trim(mrlCodeArr[5]); //本箱数量
    retObj.supplyDate = $.trim(mrlCodeArr[6]); //供货日期
    retObj.snCode = $.trim(mrlCodeArr[7]); //6位流水号

    return retObj;
}

/**
 * 提交
 */
function submit(type, bill, details, barCodes) {
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '提交中...',
        text: '请稍后...',
        modal: true
    });
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
    }, function(ret) {
        api.hideProgress();
        if (ret && ret.errCode === 0) {
            api.toast({ msg: '提交成功！', location: 'middle' });
            window.location.reload();
        } else {
            api.alert({ msg: ret.msg });
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
    }, function(ret) {
        if (ret.value.code) {
            var params = { code: ret.value.code };
            if (ret.value.type != "orderPick") {
                loadTaskOrBillByCode(params, ret.value.type, vueData1.formData)
            } else {
                app1.loadPickTaskForOrder(ret.value.code);
            }
        } else {
            api.toast({ msg: '选中源单编码不存在！', location: 'middle' });
        }
    });
}

/**
 * 监听
 * @param api
 */
function setQueryListener_Task(api) {
    api.addEventListener({
        name: 'setSourceListBack_Task'
    }, function(ret) {
        if (ret.value.taskCode) {
            var params = { taskCode: ret.value.taskCode };
            loadTaskOrBillByCode_Task(params, ret.value.type, vueData1.formData)
        } else {
            api.toast({ msg: '选中源单编码不存在！', location: 'middle' });
        }
    });
}

/**
 * 通过编码加载任务或者单据
 * @param params    查询条件
 * @param urlType   url类型：inTask..
 * @param formData  vue双向数据绑定需要
 * @returns {boolean}
 */
function loadTaskOrBillByCode_Task(params, urlType, formData) {
    //有数据的时，再次加载任务需要确认，以免数据被覆盖
    if (formData.taskCode && formData.taskCode != params.taskCode) {
        api.confirm({
            title: '提示',
            msg: '当前页面存在数据，确认加载覆盖当前数据？',
            buttons: ['确定', '取消']
        }, function(ret) {
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
                loadTaskOrBillByCode_Task_Ajax(params, urlType, formData)
            } else {
                return false;
            }
        })
    } else {
        loadTaskOrBillByCode_Task_Ajax(params, urlType, formData)
    }
}

/**
 * 通过编码加载任务或者单据---ajax请求
 * @param params
 * @param urlType
 * @param formData
 */
function loadTaskOrBillByCode_Task_Ajax(params, urlType, formData) {
    console.log(JSON.stringify(params));
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

        success: function(ret) {
            api.hideProgress();
            if (ret && ret.errCode === 0 && ret.data.task) {
                //step 1.表单数据加载
                console.log(JSON.stringify(ret.data));
                for (var dataName in formData) {
                    if (dataName == "taskTypeName") {
                        formData['taskTypeName'] = ret.data.task['taskType'] == 0 ? '区域计划' : '物料计划'
                    } else {
                        formData[dataName] = ret.data.task[dataName];
                    }
                }
                //step 2.表格数据加载
                console.log(JSON.stringify(ret.data.details));
                var tableData = formatTableData(ret.data.details, tableField1, urlType);
                $("#bootstrap-tab-1").bootstrapTable('load', tableData)
            } else {
                api.toast({ msg: '查询出错：' + ret.msg, location: 'middle' });
                return false;
            }
        },
        error: function(e) {
            api.hideProgress();
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
    }, function(ret) {
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
            api.toast({ msg: ret.msg, duration: 3000, location: 'middle' });
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
        success: function(ret) {
            api.hideProgress();
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
                return false
            }
        },
        error: function(e) {
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
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
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
    }, function(ret, err) {
        toast.hide();
        listData = ret;
        if (ret && ret.errCode == 0) {
            console.log(JSON.stringify(ret.data));
            $("#bootstrap-tab-1").bootstrapTable('load', ret.data)
        } else {
            api.toast({ msg: '查询出错！', location: 'middle' });
            return;
        }
    });
}

/**
 * 判断扫描条码是否存在于Tab3中(防止重复扫描条码)
 * @param barCode
 * @returns {boolean}
 */
function isExistInTab3(barCode) {
    var tableData3 = $("#bootstrap-tab-3").bootstrapTable('getData');
    if (tableData3.length >= 1) {
        var isIn = false;
        for (var i = 0; i < tableData3.length; i++) {
            if (tableData3[i].realCode == barCode) {
                isIn = true;
                api.toast({ msg: '条码 【' + barCode + '】 已被扫描!', duration: 3000, location: 'middle' });
                break;
            }
        }
        return isIn;
    }
}

function isExistInTab3_finish(barCode) {
    var tableData3 = $("#bootstrap-tab-3").bootstrapTable('getData');
    if (tableData3.length >= 1) {
        var isIn = false;
        for (var i = 0; i < tableData3.length; i++) {
            if (tableData3[i].realCode == barCode) {
                isIn = true;
                api.toast({ msg: '条码 【' + barCode + '】 已被扫描!', duration: 3000, location: 'middle' });
                break;
            }
        }
        return isIn;
    }
}

/**
 * 判断扫描条码是否已经出入库  防呆
 * @param barCode
 * @param billType
 * @returns {boolean}
 */
function isAlreadyOutOrInStore(barCode, billType, isMrlBarCode) {
    var isAlreadyOutOrIn = false;
    if (billType === TYPE_2.IN_BILL) {
        toast.loading({ title: "判断条码是否已入库...", duration: 10000 });
    } else if (billType === TYPE_2.OUT_BILL) {
        toast.loading({ title: "判断条码是否已出库...", duration: 10000 });
    } else if (billType === TYPE_2.UP_BILL) {
        toast.loading({ title: "判断条码是否已上架...", duration: 10000 });
    }
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.checkBillOper),
        dataType: "json",
        data: {
            barCode: barCode,
            billType: billType,
            isMrlBarCode: isMrlBarCode
        },
        async: false,
        success: function(ret) {
            toast.hide();
            isAlreadyOutOrIn = ret.data;
            if (isAlreadyOutOrIn == null) {
                api.toast({ msg: ret.msg, duration: 3000, location: 'middle' });
            }
        },
        error: function(e) {
            toast.hide();
            console.log(JSON.stringify(e));
        }
    });
    if (isAlreadyOutOrIn) {
        if (billType === TYPE_2.IN_BILL) {
            api.toast({ msg: '条码：【' + barCode + '】 已入库！', duration: 3000, location: 'middle' });
        } else if (billType === TYPE_2.OUT_BILL) {
            api.toast({ msg: '条码：【' + barCode + '】 已出库！', duration: 3000, location: 'middle' });
        } else if (billType === TYPE_2.UP_BILL) {
            api.toast({ msg: '条码：【' + barCode + '】 已上架！', duration: 3000, location: 'middle' });
        }
    }
    return isAlreadyOutOrIn;
}

/**
 * 判断是否条码是否已经收货
 * @param barCode
 * @returns {boolean}
 */
function isAlreadyArrivalStore(barCode) {
    var isAlreadyArrival = false;
    toast.loading({ title: "判断条码是否已收货...", duration: 10000 });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.checkBillOper3),
        dataType: "json",
        data: {
            barCode: barCode
        },
        async: false,
        success: function(ret) {
            toast.hide();
            isAlreadyArrival = ret.data;
            if (isAlreadyArrival == null) {
                api.toast({ msg: ret.msg, duration: 3000, location: 'middle' });
            }
        },
        error: function(e) {
            toast.hide();
            console.log(JSON.stringify(e));
        }
    });
    if (isAlreadyArrival) {
        api.toast({ msg: '条码: 【' + barCode + '】 已收货！', duration: 3000, location: 'middle' });
    }
    return isAlreadyArrival;
}

/**
 * 扫码出库时候，判断物料所在库位与实际扫描库位是否一致
 * @param scanWc
 * @param bcWc
 * @returns {boolean}
 */
function isSameWorkCellForOutStore(scanWc, bcWc) {
    var isSame = true;
    if (scanWc != bcWc) {
        api.alert({
            title: '',
            msg: '扫描库位: 【' + scanWc + '】 与条码所在库位: 【' + bcWc + '】 不一致！'
        });
        isSame = false;
    }
    return isSame;
}

/*
 *越库检查数量
 */
function updateTableByInsert_library(data, tableField2, tableField3) {
    var tableData1 = $("#bootstrap-tab-1").bootstrapTable('getData');
    var idx = 0;
    for (var i = 0; i < tableData1.length; i++) {
        if (data.rowNum == tableData1[i].rowNum) {
            idx = i;
        }
    }
    var allQty = parseInt(tableData1[idx].completeQty) + parseInt(data.qty);
    if (tableData1[idx].uda2 == tableData1[idx].qty) {
        if (tableData1[idx].remainQty > allQty || tableData1[idx].remainQty == allQty) {
            tableData1[idx].completeQty = allQty
        } else {
            return api.toast({ msg: '扫描数量超过可发数量，请重新填写', location: 'middle' });
        }
    } else {
        tableData1[idx].completeQty = allQty
    }
    $("#bootstrap-tab-1").bootstrapTable('load', tableData1);
    var tableData2 = $("#bootstrap-tab-2").bootstrapTable('getData');
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

    //更新tab2
    var result2 = $.grep(tableData2, function(val) {
        return (val.rowNum == data.rowNum)
    });
    if (result2.length > 0) {
        tableData2 = $.map(tableData2, function(item) {
            if (item.lotCode === data.lotCode) {
                item.qty = parseInt(item.qty) + parseInt(data.qty);
                data2.qty = item.qty; //更新数量，供tab1更新
            }
            return item
        })
    } else {
        tableData2 = tableData2.concat(new Array(data2))
    }
    $('#bootstrap-tab-2').bootstrapTable('load', tableData2);
    $("#bootstrap-tab-3").bootstrapTable('prepend', data3);
    //更新tab1
    api.toast({ msg: '保存成功！', location: 'middle' });
};

function updateTableByDelete_library(row, index, $element) {
    var $table1 = $("#bootstrap-tab-1");
    var $table2 = $("#bootstrap-tab-2");
    var $table3 = $("#bootstrap-tab-3");
    var tableData1 = $table1.bootstrapTable('getData');
    var tableData2 = $table2.bootstrapTable('getData');
    var isSNCode = row.isSNCode == 2 ? true : false;
    var mrlCode = row.mrlCode;
    var barCode = isSNCode ? row.barCode : row.lotCode;
    var qty = row.qty;
    //更新tab3,删除对应的一行数据
    row.deleteFlag = "true";
    $table3.bootstrapTable('remove', { field: 'deleteFlag', values: [row.deleteFlag] });
    //更新tab2
    if (isSNCode) {
        $table2.bootstrapTable('remove', { field: 'barCode', values: [barCode] });
    } else {
        for (var i = 0; i < tableData2.length; i++) {
            if (barCode == tableData2[i].lotCode && row.rowNum == tableData2[i].rowNum) {
                tableData2[i].qty = Number(tableData2[i].qty) - Number(qty);
                if (tableData2[i].qty == 0) {
                    $table2.bootstrapTable('remove', { field: 'qty', values: [0] });
                } else {
                    $table2.bootstrapTable('updateRow', { index: i, row: tableData2[i] });
                }
                break;
            }
        }
    }
    //更新tab1
    var index = 0;
    for (var i = 0; i < tableData1.length; i++) {
        // var row = tableData1[i];
        if (tableData1[i].rowNum == row.rowNum) {
            index = i
        }
    }
    tableData1[index].completeQty = Number(tableData1[index].completeQty) - Number(qty);
    $table1.bootstrapTable('updateRow', { index: index, row: tableData1[index] });
    $($element).addClass('info');
}

/*
 *根据容器编码获取当前任务，状态以及库位
 */
function getCtUwmInfoByCtBarCode(barCode) {
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
        url: getUrl("padWmsController!getCtUwmInfoByCtBarCode.m"),
        dataType: "json",
        data: {
            ctBarCode: barCode,
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *校验一个容器是否存在
 */
function getMrlInfoByBarCode(barCode) {
    var retObj = null;
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getMrlInfoByBarCode.m"),
        dataType: "json",
        data: {
            ctBarCode: barCode,
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *根据容器编码解绑该容器的绑定关系
 */
function splitCtAndDelTask(barCode) {
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!splitCtAndDelTask.m"),
        dataType: "json",
        data: {
            workCenterGid: WCEN_GID,
            ctBarCode: barCode,
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                api.hideProgress();
                api.toast({ msg: ret.msg, duration: 3000, location: 'middle' });
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
}

/*
 *扫描物料以及批次码获取当前库位容器信息
 */
function getWorkCellByMrlAndLotCode(mrlCode, lotCode) {
    var obj;
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getMrlLotInfo.m"),
        dataType: "json",
        data: {
            mrlCode: mrlCode,
            lotCode: lotCode
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                obj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return obj;
}

/*
 *获取库位信息
 */
function getWorkCellByWorkCellCode(workCellCode) {
    var obj;
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(OtherUrl.getWorkCellByWorkCellCode),
        dataType: "json",
        data: {
            workCellCode: workCellCode
        },
        async: false,
        success: function(ret) {
            console.log(JSON.stringify(ret));
            if (ret.errCode == 0) {
                obj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return obj;
}

/*
 *根据库位编码获取库位所在容器
 */
function getCtByWorkCellCode(workCellCode) {
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
        url: getUrl("padWmsController!getCtByWorkCellCode.m"),
        dataType: "json",
        data: {
            workCellCode: workCellCode,
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *根据库位编码或容器编码获取下一层级内容
 */
function getNextChildContent(barCode) {
    var retObj = [];
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getWorkCellCtInfo.m"),
        dataType: "json",
        data: {
            workCellCodeOrCtBarcode: barCode,
        },
        async: false,
        success: function(ret) {
            console.log(JSON.stringify(ret));
            if (ret.errCode == 0) {
                if (ret.data.sn) {
                    for (var i = 0; i < ret.data.sn.length; i++) {
                        retObj.push(ret.data.sn[i]);
                    }
                }
                if (ret.data.ct) {
                    for (var i = 0; i < ret.data.ct.length; i++) {
                        retObj.push({
                            "childCtBarcode": ret.data.ct[i]
                        });
                    }
                }
                if (ret.data.mrlLotQty) {
                    for (var i = 0; i < ret.data.mrlLotQty.length; i++) {
                        retObj.push(ret.data.mrlLotQty[i]);
                    }
                }
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *根据库位编码以及物料编码获取未组托批次数量
 */
function getUnPackMrlLotQty(workCellCode, mrlCode) {
    var retObj = [];
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getUnPackMrlLotQty.m"),
        dataType: "json",
        data: {
            workCellCode: workCellCode,
            mrlCode: mrlCode
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}
/*
 *根据序列件获取批次条码
 */
function getLotCodeBySN(barCode) {
    var retObj = "";
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getLotCodeBySn.m"),
        dataType: "json",
        data: {
            sn: barCode
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

/*
 *打印公共方法
 */
function printBarCodeOrLotCode(barCode) {
    var privacy = api.require("ble");
    var moduleDemo = api.require('moduleDemo');
    var uuid = localStorage.getItem('hgzyUuid')
    privacy.initManager(function(ret) {
        if (ret.state == "poweredOn") {
            moduleDemo.printData({ msg: uuid + ',' + barCode });
        } else {
            api.toast({ msg: '请开启蓝牙', location: 'middle' });
        }
    })
}

/*
 * 根据容器编码获取父级容器
 */
function getParentBarcodeByBarcode(barCode) {
    var retObj = "";
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getParentBarcodeByBarcode.m"),
        dataType: "json",
        data: {
            ctBarcode: barCode
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    console.log(JSON.stringify(retObj));
    return retObj;
}

//获取推荐库位
function getPickWorkCellList(barCode) {
    var retObj = "";
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("padWmsController!getStockWcByMrlCode.m"),
        dataType: "json",
        data: {
            mrlCode: barCode
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '查询失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}

function getAvailableStoresForSupp(mrlCode, qty){
    var retObj = "";
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl("bogePadWmsController!getAvailableStoresForSupp.m"),
        dataType: "json",
        data: {
            mrlCode: mrlCode,
            qty: qty,
            wcGid: WCEN_GID,
        },
        async: false,
        success: function(ret) {
            if (ret.errCode == 0) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                api.alert({ title: '查询失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            api.hideProgress();
        }
    });
    return retObj;
}
