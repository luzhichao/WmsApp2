/**
 * 佳博打印机打印方法
 *
 * @param {string} mrlCode 物料编号
 * @param {string} mrlName 物料名称
 * @param {string} lotCode 批次号
 * @param {int} qty 数量
 * @param {string} barCode 容器编号
 */
function printToJiaBo(mrlCode, mrlName, lotCode, qty, barCode) {
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    var privacy = api.require("ble");
    var printModule = api.require('moduleDemo');
    var uuid = localStorage.getItem('hgzyUuid');
    if (!uuid) {
        api.alert({
            title: '打印失败',
            msg: '在个人中心-选择打印机IP中设置打印机后可恢复打印条码功能！'
        });
    } else {
        privacy.initManager(function(ret) {
            if (ret.state == "poweredOn") {
                var params = {
                    mac: uuid,
                    code: mrlCode,
                    name: mrlName,
                    lot: lotCode,
                    num: qty,
                    con: barCode
                }
                // params.mac = uuid;
                console.log("打印的参数======" + JSON.stringify(params));
                printModule.printData(params, function(ret) {
                    console.log("打印的结果======" + JSON.stringify(ret));
                    api.toast({ msg: ret.result, location: 'middle' });
                });
            } else {
                api.alert({
                    title: '打印失败',
                    msg: '请开启蓝牙！'
                });
            }
        })
    }
    api.hideProgress();
}


function printToJiaBoAndRefresh(mrlCode, mrlName, lotCode, qty, barCode, isRefresh) {
    api.showProgress({
        style: 'default',
        animationType: 'fade',
        title: '努力加载中...',
        text: '请稍后...',
        modal: true
    });
    var privacy = api.require("ble");
    var printModule = api.require('moduleDemo');
    var uuid = localStorage.getItem('hgzyUuid');
    if (!uuid) {
        api.alert({
            title: '打印失败',
            msg: '在个人中心-选择打印机IP中设置打印机后可恢复打印条码功能！'
        });
    } else {
        privacy.initManager(function(ret) {
            if (ret.state == "poweredOn") {
                var params = {
                    mac: uuid,
                    code: mrlCode,
                    name: mrlName,
                    lot: lotCode,
                    num: qty,
                    con: barCode
                }
                // params.mac = uuid;
                // console.log("打印的参数======" + JSON.stringify(params));
                printModule.printData(params, function(ret) {
                    // console.log("打印的结果======" + JSON.stringify(ret));
                    api.toast({ msg: ret.result, location: 'middle' });
                    if (isRefresh) {
                        window.location.reload();
                    }
                });
            } else {
                api.alert({
                    title: '打印失败',
                    msg: '请开启蓝牙！'
                });
            }
        })
    }
    api.hideProgress();
}
