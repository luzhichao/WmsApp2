/**
 * 仓库
 * @type {string | null}
 */
WCEN_GID = localStorage.getItem('workCenterGid');

/**
 * 工厂编码
 * @type {string | null}
 */
SITE_CODE = localStorage.getItem('siteCode');

SITE_CODE_VALUE = {
    FH: '1001',
    HW: '1002'
};


/**
 * 不同页签的table
 * @type {{DEFAULT: number, FIR: number, SEC: number, TRD: number}}
 */
TABLE_INDEX = {
    DEFAULT: 0,
    FIR: 1,
    SEC: 2,
    TRD: 3,
    FOR: 4,
    FIF: 5
};

/**
 * 条码类型
 * @type {{NORMAL: number, LOT: number, PACKAGE: number, SN: number}}
 */
BAR_CODE_TYPE = {
    NORMAL: 0, //箱码
    LOT: 1, //批次码
    PACKAGE: 2, //栈板码
    SN: 3 //彩盒号
};

/**
 * 业务类型
 * @type {{IN_TASK: string, OUT_TASK: string, ARR_Bill: string, IN_BILL: string, OUT_BILL: string, DOWN_TASK: string}}
 */
TYPE = {
    IN_TASK: 'inTask',
    IN_TASK_CRLCODE: 'inTaskByBarCode',
    OUT_TASK: 'outTask',
    UP_TASK: 'upTask',
    OUT_TASK_MRL: 'outTask_Mrl',
    ARR_Bill: 'arrivalBill',
    ARR_CONS_BILL: 'arrConsBill',
    UP_BILL: 'upBill',
    IN_BILL: 'inBill',
    OUT_BILL: 'outBill',
    DOWN_TASK: 'downTask',
    DOWN_TASK_MRL: 'downTask_Mrl',
    INVENTORY_BILL: 'inventoryBill',
    MOVE_BILL: 'moveBill',
    TRANSFER_TASK: 'transferTask',
    FRZ_BILL: 'freezeBill'
};
TYPE_2 = {
    IN_TASK: 10,
    ARR_Bill: 11,
    IN_BILL: 12,
    UP_TASK: 13,
    UP_BILL: 14,
    OUT_TASK: 20,
    DOWN_TASK: 21,
    DOWN_BILL: 22,
    OUT_BILL: 23,
    INVENTORY_BILL: 30,
    FRZ_BILL: 35
};

/**
 * 入库类型
 */
INSTORE_TYPE = {
    PURCHASE: 10, // 采购入库
    PRODUCT: 11, // 生产入库
    CONSIGN_PRODUCT: 12, //委外产品入库
    MATERIAL_RETURN: 13, //领料退
    CONSIGN_MATERIAL_RETURN: 14, // 委外材料退
    SALE_RETURN: 15, // 销售退
    OTHER: 17, //其他入库
    TRANSFER: 50, // 调拨
    MOVE: 51 // 转移
};
/**
 * 入库状态
 */
INSTORE_STATUS = {
    NEW: 0, // 新建
    RECEIPTED: 2, // 收货
    STORAGED: 4, // 入库
    PLACED: 6, // 上架
    CONFIRMED: 8, // 确认
    CLOSED: 10, // 关闭
    CANCELLED: 12 // 取消
};
INSTORE_STATUS_2 = [
    { key: '', value: '' },
    { key: '0', value: '新建' },
    { key: '2', value: '收货' },
    { key: '4', value: '入库' },
    { key: '6', value: '上架' },
    { key: '8', value: '确认' },
    { key: '10', value: '关闭' },
    { key: '12', value: '取消' }
];
INSTORE_STATUS_MAP = { "0": "新建", "2": "收货", "4": "入库", "6": "上架", "8": "确认", "10": "关闭", "12": "取消", };
/**
 * 质检状态
 */
QC_STATUS = {
    NEW: 0, // 未质检
    SUBMITTED: 2, // 已提交质检
    PARTLY_CHECKED: 4, //部分质检
    CHECKED: 6 //  部分质检
};

/**
 * 出库类型
 */
OUTSTORE_TYPE = {
    MATERIAL: 30, //材料出库
    CONSIGN_MATERIAL: 31, //委外材料出库
    SALE: 32, // 销售出库
    PURCHASE_RETURN: 33, // 采购退
    PRODUCT_RETURN: 34, // 生产退
    CONSIGN_PRODUCT_RETURN: 35, // 委外产品退
    OTHER: 37, //其他出库
    TRANSFER: 50, // 调拨
    MOVE: 51 // 转移
};
/**
 * 出库状态
 */
OUTSTORE_STATUS = {
    NEW: 0, // 新建
    ALLOCATED: 2, // 已分配
    PICKED: 4, // 拣货
    OUTTED: 6, // 出库
    INED: 7, // 入库(调拨用)
    CONFIRMED: 8, // 确认
    CLOSED: 10, // 关闭
    CANCELLED: 12 // 取消
};
OUTSTORE_STATUS_2 = [
    { key: '', value: '' },
    { key: '0', value: '新建' },
    { key: '2', value: '已分配' },
    { key: '4', value: '拣货' },
    { key: '6', value: '出库' },
    { key: '7', value: '入库(调拨用)' },
    { key: '8', value: '确认' },
    { key: '10', value: '关闭' },
    { key: '12', value: '取消' }
];

//盘点单状态
INV_BILL_STATUS = {
    NEW: 0 //新建

};

REC_WCODE_ARR = [];
/**
 * 汽车年份
 */
Car_VIM_YEAR_ARR = [{
    key: 'J',
    value: '2018'
}, {
    key: 'K',
    value: '2019'
}, {
    key: 'L',
    value: '2020'
}, {
    key: 'M',
    value: '2021'
}, {
    key: 'N',
    value: '2022'
}, {
    key: 'P',
    value: '2023'
}, {
    key: 'R',
    value: '2024'
}, {
    key: 'S',
    value: '2025'
}, {
    key: 'T',
    value: '2026'
}, {
    key: 'V',
    value: '2027'
}, {
    key: 'W',
    value: '2028'
}, {
    key: 'X',
    value: '2029'
}, {
    key: 'Y',
    value: '2030'
}, {
    key: '1',
    value: '2031'
}, {
    key: '2',
    value: '2032'
}, {
    key: '3',
    value: '2033'
}, {
    key: '4',
    value: '2034'
}, {
    key: '5',
    value: '2035'
}, {
    key: '6',
    value: '2036'
}, {
    key: '7',
    value: '2037'
}, {
    key: '8',
    value: '2038'
}, {
    key: '9',
    value: '2039'
}, {
    key: 'A',
    value: '2040'
}, {
    key: 'B',
    value: '2041'
}, {
    key: 'C',
    value: '2042'
}, {
    key: 'D',
    value: '2043'
}, {
    key: 'E',
    value: '2044'
}, {
    key: 'F',
    value: '2045'
}, {
    key: 'G',
    value: '2046'
}, {
    key: 'H',
    value: '2047'
}];

/**
 * 汽车月份
 */
Car_VIM_MONTH_ARR = [{
    key: "1",
    value: "01"
}, {
    key: "2",
    value: "02"
}, {
    key: "3",
    value: "03"
}, {
    key: "4",
    value: "04"
}, {
    key: "5",
    value: "05"
}, {
    key: "6",
    value: "06"
}, {
    key: "7",
    value: "07"
}, {
    key: "8",
    value: "08"
}, {
    key: "9",
    value: "09"
}, {
    key: "A",
    value: "10"
}, {
    key: "B",
    value: "11"
}, {
    key: "C",
    value: "12"
}];

//绑箱数组
BOXLIST = [{
    value: 1,
    label: 1
}, {
    value: 2,
    label: 2
}, {
    value: 3,
    label: 3
}, {
    value: 4,
    label: 4
}, {
    value: 5,
    label: 5
}, {
    value: 6,
    label: 6
}, {
    value: 7,
    label: 7
}, {
    value: 8,
    label: 8
}]
