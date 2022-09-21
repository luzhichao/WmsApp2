
/**
 *任务状态
 */
var STATE_NEW = 0;
var STATE_PUBLIC = 1;
var STATE_FINISH = 2;

/**
 *入库类型
 */
var INSTORE_TYPE_MATERIAL = 0;
var INSTORE_TYPE_FINISHIN = 3;
var INSTORE_TYPE_TRANS_IN = 4;
var INSTORE_TYPE_INV_IN = 5;

/**
 *出库类型
 */
var OUTSTORE_TYPE_MATERIAL = 0;
var OUTSTORE_TYPE_FINISHIN = 3;
var OUTSTORE_TYPE_TRANS_IN = 4;
var OUTSTORE_TYPE_INV_IN = 5;


/**
 *上架类型
 */
var _upShelfTypePurchase = 0;  // 采购
var _upShelfTypeProduce = 1;  // 生产
var _upShelfTypeInv = 2;  // 盘点
var _upShelfTypeTransIn = 3;  // 库内转移
var _upShelfTypeTrans = 4;  // 库间调拨
var _upShelfTypeReturn = 5;  // 还回
var _upShelfTypeOther = 6;  // 其他
var _upShelfTypeSaleReturn = 12;  // 销售退货
var _upShelfTypeMaterialReturn = 13;  // 生产退料
var _upShelfTypeConversion = 18;  // 物料转换
var _upShelfTypeSys = 19;  // 系统生成

function getUpShelfType(type){
	console.log("comstantUtil.js");
	var strType = "";
	type = parseInt(type);
	switch (type) {
		case _upShelfTypePurchase:
			strType = "采购";
			break;
		case _upShelfTypeProduce:
			strType = "生产";
			break;
		case _upShelfTypeInv:
			strType = "盘点";
			break;
		case _upShelfTypeTransIn:
			strType = "库内转移";
			break;
		case _upShelfTypeTrans:
			strType = "库间调拨";
			break;
		case _upShelfTypeReturn:
			strType = "还回";
			break;
		case _upShelfTypeSaleReturn:
			strType = "销售退货";
			break;
		case _upShelfTypeMaterialReturn:
			strType = "生产退料";
			break;
		case _upShelfTypeConversion:
			strType = "物料转换";
			break;
		case _upShelfTypeSys:
			strType = "系统生成";
			break;
		case _upShelfTypeOther:
			strType = "其他";
			break;
		default:
  			strType = "其他";
	}
	return strType;
}

/**
 *下架类型
 */
var _downShelfTypeMaterial = 0;  // 材料
var _downShelfTypeOutSourcing = 1;  // 委外材料
var _downShelfTypeOther = 2;  // 其他
var _downShelfTypeLineSide = 3;  // 线边库
var _downShelfTypeTransOut = 4;  // 调拨
var _downShelfTypeInvOut = 5;  // 盘亏
var _downShelfTypeForward = 6;  // 转运单
var _downShelfTypeSale = 7;  // 销售
var _downShelfTypeBorrow = 8;  // 借出
var _downShelfTypeTransIn = 9;  // 库内转移
var _downShelfTypePurchaseReturn = 12;  // 采购退货
var _downShelfConsign = 10;  // 委外任务
var _downShelfBorrow = 11;  // 借出任务
var _downShelfTypeConversion = 18;  // 物料转换
var _downShelfTypeSys = 19;  // 系统生成

function getDownShelfType(type){
	console.log("comstantUtil.js");
	var strType = "";
	switch (type) {
		case _downShelfTypeMaterial:
			strType = "材料";
			break;
		case _downShelfTypeOutSourcing:
			strType = "委外材料";
			break;
		case _downShelfTypeOther:
			strType = "其他";
			break;
		case _downShelfTypeLineSide:
			strType = "线边库";
			break;
		case _downShelfTypeTransOut:
			strType = "调拨";
			break;
		case _downShelfTypeInvOut:
			strType = "盘亏";
			break;
		case _downShelfTypeForward:
			strType = "转运单";
			break;
		case _downShelfTypeSale:
			strType = "销售";
			break;
		case _downShelfTypeBorrow:
			strType = "借出";
			break;
		case _downShelfTypeTransIn:
			strType = "库内转移";
			break;
		case _downShelfTypePurchaseReturn:
			strType = "采购退货";
			break;
		case _downShelfConsign:
			strType = "委外任务";
			break;
		case _downShelfBorrow:
			strType = "借出任务";
			break;
		case _downShelfTypeConversion:
			strType = "物料转换";
			break;
		case _downShelfTypeSys:
			strType = "系统生成";
			break;
		default:
  			strType = "其他";
	}
	return strType;
}