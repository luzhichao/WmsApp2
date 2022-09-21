//博戈时代新材WMS专用
var commonUrl = {
    getMrlByMrlCode: 'padWmsController!getMrlByMrlCode.m', //通过物料编码查找物料
    getInTaskByCode: 'bogePadWmsController!getInTaskByCode.p', //通过入库单号查询入库任务详情
    arrivalBill: 'uwmReceiptBillController!saveReceiptBillPda.m', //收货提交
    getStockByCode: 'bogePadWmsController!getStockInfosByCode.p', //库存查询
    getWorkCellCodes: 'bogePadWmsController!getWorkCellCodes.p',
    getVariationReasons: 'bogePadWmsController!getVRS.m',
};

//容器\批次相关
var SeqUrl = {
    getContainerCode: 'bogePadWmsController!getContainerCode.p', //获取新容器号
    getLotCode: 'bogePadWmsController!getLotCode.p', //获取新批次号
    getContainStock: 'bogePadWmsController!getContainStock.p', //取样获取容器库存
    getWorkCellInfo: 'bogePadWmsController!getWorkCellInfo.p',
    samplingSubmit: 'bogePadWmsController!samplingSubmit.p', 
};

//生产订单相关
var ProductOrderUrl = {
    findProductionOrder: 'bogePadWmsController!findProductionOrder.p', //查询生产订单
    findBOMByAufnr: 'bogePadWmsController!findBOMByAufnr.p', //查询生产订单
    submitProductionReport: 'bogePadWmsController!submitProductionReport.p', //提交生产订单工序报工
    getMaterialInventory: 'bogePadWmsController!getMaterialInventory.p', //查询订单BOM原材料库存容量
    findUsersByTeamGroup: "bogePadWmsController!findUsersByTeamGroup.p",
    //人工叫料
    findProductionByMrlCode: 'bogePadWmsController!findProductionByMrlCode.p', //根据物料编码查询生产订单信息
    findBOMByAufnrAndMatnr: 'bogePadWmsController!findBOMByAufnrAndMatnr.p', //根据物料编码查询生产订单信息
    submitMaterielUse: 'bogePadWmsController!submitMaterielUse.p', //提交人工叫料
    getAllUseMater: 'bogePadWmsController!getAllUseMater.p',
    materialTranAction: 'bogePadWmsController!materialTranAction.p',
    getTranTaskBySourceBillGid: 'bogePadWmsController!getTranTaskBySourceBillGid.p',
    getTeamGroups: 'bogePadWmsController!getTeamGroups.p', //获取班组信息
    getProductByBarCode: 'bogePadWmsController!getProductByBarCode.p',
    productStorage: 'bogePadWmsController!productStorage.p',

}

//收货单
var receiptBillUrl = {
    createReceiptBill: "bogePadWmsController!createReceiptBill.p", //创建收货单
    monkeyTransfer: "bogePadWmsController!monkeyTransfer.p",
    createBatchReceiptBill: "bogePadWmsController!createBatchReceiptBill.p",
}

var inventoryUrl = {
    inventoryBill: 'bogePadWmsController!getInventoryTasks.m', //获取盘点任务单列表
    getCtInfoForInv: 'bogePadWmsController!getCtInfoForInv.p',
    submitInevtory: "padWmsController!saveBlindInventoryBill.m",
}

function getWorkCellCodesFn() {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getWorkCellCodesFn--url" + getUrl(commonUrl.getWorkCellCodes));
    $.ajax({
        type: "POST",
        url: getUrl(commonUrl.getWorkCellCodes),
        dataType: "json",
        async: false,
        data: {"workCenterCode": localStorage.getItem('workCenterCode')},
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: ret.data
                });
            }
        },
        error: function(e) {
            console.log("异常结果==>" + JSON.stringify(e));
            api.alert({
                title: '提示',
                msg: "系统异常，请联系管理员"
            });
        }
    });
    api.hideProgress();
    return result;
}

function productStorageFn(ctCode, cellCode, tctBatCode, lotCode) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("submitInevtoryFn--url" + getUrl(ProductOrderUrl.productStorage));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.productStorage),
        dataType: "json",
        async: false,
        data: { "ctBarCode": ctCode, "cellCode": cellCode, "tctBatCode": tctBatCode, "lotCode": lotCode },
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret));
            result = ret;
            if (ret && ret.type == "success") {
                api.toast({ msg: '操作成功', location: 'middle' });
            } else {
                api.hideProgress();
                api.alert({ msg: JSON.stringify(ret.data), buttons: ['确定'] });
            }
        },
        error: function(e) {
            console.log("异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

function getProductByBarCodeFn(ctCode) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getProductByBarCodeFn--url" + getUrl(ProductOrderUrl.getProductByBarCode));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.getProductByBarCode),
        dataType: "json",
        async: false,
        data: {
            "ctBarCode": ctCode,
        },
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: ret.data
                });
            }
        },
        error: function(e) {
            console.log("异常结果==>" + JSON.stringify(e));
            api.alert({
                title: '提示',
                msg: "系统异常，请联系管理员"
            });
        }
    });
    api.hideProgress();
    return result;
}

function submitInevtoryFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("submitInevtoryFn--url" + getUrl(inventoryUrl.submitInevtory));
    $.ajax({
        type: "POST",
        url: getUrl(inventoryUrl.submitInevtory),
        dataType: "json",
        async: false,
        data: { "invBillBO": param },
        success: function(ret) {
            console.log("ret===>" + JSON.stringify(ret));
            result = ret;
            if (ret.errCode == 0) {
                api.toast({ msg: '操作成功', location: 'middle' });
                window.location.reload();
            } else {
                api.hideProgress();
                api.alert({ title: '操作失败', msg: JSON.stringify(ret.msg) });
            }
        },
        error: function(e) {
            console.log("submitInevtoryFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

function getCtInfoForInvFn(planCode, ctCode) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getCtInfoForInvFn--url" + getUrl(inventoryUrl.getCtInfoForInv));
    $.ajax({
        type: "POST",
        url: getUrl(inventoryUrl.getCtInfoForInv),
        dataType: "json",
        async: false,
        data: {
            "planCode": planCode,
            "ctCode": ctCode,
        },
        success: function(ret) {
            console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: ret.data
                });
            }
        },
        error: function(e) {
            console.log("getCtInfoForInvFn异常结果==>" + JSON.stringify(e));
            api.alert({
                title: '提示',
                msg: ret.data
            });
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 获取所有叫料记录
 */
function getAllUseMaterFn() {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getAllUseMaterFn--url" + getUrl(ProductOrderUrl.getAllUseMater));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.getAllUseMater),
        dataType: "json",
        async: false,
        data: {
            "workCenterCode": localStorage.getItem('workCenterCode'),
        },
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("getAllUseMaterFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 根据叫料记录获取所有转移单据
 * @param {叫料记录ID} id
 */
function getTranTaskBySourceBillGidFn(id) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getTranTaskBySourceBillGidFn--url" + getUrl(ProductOrderUrl.getTranTaskBySourceBillGid));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.getTranTaskBySourceBillGid),
        dataType: "json",
        async: false,
        data: { "sourceBillGid": id },
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("getTranTaskBySourceBillGidFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

function findUsersByTeamGroupFn(id) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("findUsersByTeamGroupFn--url" + getUrl(ProductOrderUrl.findUsersByTeamGroup));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.findUsersByTeamGroup),
        dataType: "json",
        async: false,
        data: { "id": id },
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("findUsersByTeamGroupFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 人工叫料任务，作业完成
 * @param {*} 
 */
function materialTranActionFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("materialTranActionFn--url" + getUrl(ProductOrderUrl.materialTranAction));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.materialTranAction),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            result = ret
        },
        error: function(e) {
            console.log("materialTranActionFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

function getVariationReasonsFn(){
    var result = {};
    $.ajax({
        type: "POST",
        url: getUrl(commonUrl.getVariationReasons),
        dataType: "json",
        data: {"workCenterCode": localStorage.getItem('workCenterCode')},
        async: false,
        success: function(ret) {
            console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                return api.toast({
                    msg: '查询出错：' + ret.msg,
                    location: 'middle'
                });
            }
        },
        error: function(e) {
            console.log("getVariationReasonsFn异常结果==>" + JSON.stringify(e));
        }
    });
    return result;
}

/**
 * 容器库存查询
 * @param {*} params {"code":容器或库位编码, "type": 1库位、2容器}
 */
function getStockByCodeFn(code, type) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(commonUrl.getStockByCode),
        dataType: "json",
        data: {"code": code, "type": type},
        async: false,
        success: function(ret) {
            api.hideProgress();
            //console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                return api.toast({
                    msg: '查询出错：' + ret.msg,
                    location: 'middle'
                });
            }
        },
        error: function(e) {
            api.hideProgress();
            console.log("getStockByCode异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

function createBatchReceiptBillFn(param){
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(receiptBillUrl.createBatchReceiptBill),
        dataType: "json",
        data: {data: JSON.stringify(param)},
        async: false,
        success: function(ret) {
            result = ret;
        },
        error: function(e) {
            console.log("异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 创建收货单
 * @param {*} params
 */
function createReceiptBillFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(receiptBillUrl.createReceiptBill),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            result = ret;
        },
        error: function(e) {
            console.log("异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 根据入库任务号获取入库任务
 * @param {*} code
 */
function getInTaskByCodeFn(code) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(commonUrl.getInTaskByCode),
        dataType: "json",
        data: {"code": code, "workCenterCode": localStorage.getItem('workCenterCode'),},
        async: false,
        success: function(ret) {
            api.hideProgress();
            // console.log("ret===>" + JSON.stringify(ret))
            result = ret
        },
        error: function(e) {
            api.hideProgress();
            console.log("getInTaskByCodeFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 根据物料编号查询物料数据
 * @param {*} mrlCode
 */
function getMrlByMrlCodeFn(mrlCode) {
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
        url: getUrl(commonUrl.getMrlByMrlCode),
        async: false,
        dataType: "json",
        data: {
            mrlCode: mrlCode,
        },
        success: function(ret) {
            if (ret && ret.errCode === 0 && ret.data) {
                retObj = ret.data;
                api.hideProgress();
            } else {
                api.hideProgress();
                alert(ret.msg);
            }
        },
        error: function(e) {
            console.log("getMrlByMrlCodeFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return retObj;
}

/**
 * 获取班组信息
 * @param {*} param
 */
function getTeamGroupsFn() {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getTeamGroupsFn--url" + getUrl(ProductOrderUrl.getTeamGroups));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.getTeamGroups),
        dataType: "json",
        // data: param,
        async: false,
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("getTeamGroupsFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}
/**
 * 提交人工叫料
 * @param {*} param
 */
function submitMaterielUseFn(param) {
    var result = {};
    console.log("submitMaterielUseFn--url" + getUrl(ProductOrderUrl.submitMaterielUse));
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '加载中...',
        text: '请稍候...',
        modal: true
    });
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.submitMaterielUse),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            api.hideProgress();
            result = ret;
        },
        error: function(e) {
            api.hideProgress();
            console.log("submitMaterielUseFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 根据物料编码查询生产订单信息
 * @param {*} param
 */
function findBOMByAufnrAndMatnrFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("findBOMByAufnrAndMatnrFn--url" + getUrl(ProductOrderUrl.findBOMByAufnrAndMatnr));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.findBOMByAufnrAndMatnr),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("findBOMByAufnrAndMatnrFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 根据物料编码查询生产订单信息
 * @param {*} param
 */
function findProductionByMrlCodeFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("findProductionByMrlCodeFn--url" + getUrl(ProductOrderUrl.findProductionByMrlCode));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.findProductionByMrlCode),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("findProductionByMrlCodeFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 查询订单BOM原材料库存
 * @param {*} param
 */
function getMaterialInventoryFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getMaterialInventoryFn--url" + getUrl(ProductOrderUrl.getMaterialInventory));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.getMaterialInventory),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("getMaterialInventoryFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 提交报工
 * @param {*} param
 */
function submitProductionReportFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("submitProductionReportFn--url" + getUrl(ProductOrderUrl.submitProductionReport));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.submitProductionReport),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            result = ret;
        },
        error: function(e) {
            console.log("submitProductionReportFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 查询生产订单工序
 * @param {*} param
 */
function findBOMByAufnrFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("findBOMByAufnrFn--url" + getUrl(ProductOrderUrl.findBOMByAufnr));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.findBOMByAufnr),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("findBOMByAufnrFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 查询生产订单
 * @param {*} param
 */
function findProductionOrderFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("findProductionOrderFn--url" + getUrl(ProductOrderUrl.findProductionOrder));
    $.ajax({
        type: "POST",
        url: getUrl(ProductOrderUrl.findProductionOrder),
        dataType: "json",
        data: param,
        async: false,
        success: function(ret) {
            // console.log("ret===>" + JSON.stringify(ret))
            if (ret && ret.type == "success") {
                result = JSON.parse(ret.data);
            } else {
                api.alert({
                    title: '提示',
                    msg: '操作异常'
                });
            }
        },
        error: function(e) {
            console.log("findProductionOrderFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 取样提交
 * @param {*} param
 */
function samplingSubmitFn(param) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("samplingSubmitFn--url" + getUrl(SeqUrl.samplingSubmit));
    $.ajax({
        type: "POST",
        url: getUrl(SeqUrl.samplingSubmit),
        dataType: "json",
        data: {
            data: param
        },
        async: false,
        success: function(ret) {
            console.log("ret===>"+JSON.stringify(ret))
            result = ret;
        },
        error: function(e) {
            console.log("samplingSubmitFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 根据库位编码获取库位信息
 * @param {*} code
 */
function getWorkCellInfoFn(code) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getWorkCellInfoFn--url" + getUrl(SeqUrl.getWorkCellInfo));
    $.ajax({
        type: "POST",
        url: getUrl(SeqUrl.getWorkCellInfo),
        dataType: "json",
        data: {
            code: code
        },
        async: false,
        success: function(ret) {
            console.log("ret===>"+JSON.stringify(ret))
            if (ret) {
                result = ret;
            }
        },
        error: function(e) {
            api.hideProgress();
            api.toast({
                msg: '网络有点慢，稍后再试!',
                duration: 3000,
                location: 'middle'
            });
            console.log("getWorkCellInfoFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/**
 * 生成容器编码
 * @param {*} callback 异步回调函数function(ret){};
 * @param {*} center 库区编码，用localStorage.getItem('workCenterCode')获取
 */
function createPBarCodeFN(callback, center) {
    console.log("createPBarCodeFN==center==>" + center);
    $.ajax({
        type: "POST",
        url: getUrl(SeqUrl.getContainerCode),
        dataType: "json",
        data: {
            center: center
        },
        success: callback,
        error: function(e) {
            api.hideProgress();
            api.toast({
                msg: '网络有点慢，稍后再试!',
                duration: 3000,
                location: 'middle'
            });
            console.log("createPBarCodeFN异常结果==>" + JSON.stringify(e));
        }
    });
}

/**
 * 生成批次号
 * @param {*} callback 异步回调函数function(ret){};
 */
function getLotCodeFn(callback) {
    $.ajax({
        type: "GET",
        url: getUrl(SeqUrl.getLotCode),
        dataType: "json",
        success: callback,
        error: function(e) {
            api.hideProgress();
            api.toast({
                msg: '网络有点慢，稍后再试!',
                duration: 3000,
                location: 'middle'
            });
            console.log("getLotCodeFN异常结果==>" + JSON.stringify(e));
        }
    });
}

/**
 * 获取容器库存
 */
function getContainStockFn(ctBarCode) {
    var result = {};
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    console.log("getContainStockFn--url" + getUrl(SeqUrl.getContainStock));
    $.ajax({
        type: "POST",
        url: getUrl(SeqUrl.getContainStock),
        dataType: "json",
        data: {"sourceCtBarcodeCode": ctBarCode},
        async: false,
        success: function(ret) {
            if (ret && ret.errCode == 0) {
                result = ret.data;
            } else {
                api.alert({
                    title: '提示',
                    msg: ret.msg
                });
            }
        },
        error: function(e) {
            console.log("getContainStockFn异常结果==>" + JSON.stringify(e));
        }
    });
    api.hideProgress();
    return result;
}

/*************************************************************************************************** */
