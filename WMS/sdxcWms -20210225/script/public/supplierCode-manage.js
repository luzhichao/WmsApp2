/**
 * 解析供应商物料条码
 * @param {*} supplierCode
 */
function supplierCodeSplit(supplierCode) {
    var arr = supplierCode.split('_');
    var ret = {
        mrlCode: $.trim(arr[0]), //物料编码
        taskCode: $.trim(arr[1]), //订单号
        supLotCode: $.trim(arr[2]), //供应商批次号
        productDate: $.trim(arr[3]), //生产日期
        qty: $.trim(arr[4]), //数量
    };
    console.log("=supCode==>"+JSON.stringify(ret))
    return ret;
}
