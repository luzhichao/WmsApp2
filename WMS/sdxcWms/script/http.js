
/*
* options
*    {url:serverUrl,data:null,tip:null,success:function,error:function}
* NotShowProgress
      默认显示loading动画
*/
var $http = {
  get:function(options,NotShowProgress){
    var tip = options.tip||'努力加载中...';
    var url = options.url;
    var data = options.data;
    if(!NotShowProgress){
      api.showProgress({
				style: 'default',
				animationType: 'fade',
				title: tip,
				text: '请稍后...',
				modal: true
			});
    }
    api.ajax({
      url: url,
      method: 'get',
      data: {
        values: data
      }
    }, function(ret, err) {
      if(!NotShowProgress){
        api.hideProgress();
      }
      if (ret) {
        var response = eval(ret);
        if(response.erroCode==0){
          var data = eval(response.data);
          options.success&&options.success.call(null,data);
        }else{
          api.toast({
            msg: response.msg,
            duration: 3000,
            location: 'top'
          });
          options.error&&options.error.call(null,response);
        }
      } else {
        api.toast({
          msg: err.msg,
          duration: 3000,
          location: 'top'
        });
        return;
      }
    });
  },
  post:function(options,NotShowProgress){
    var tip = options.tip||'努力加载中...';
    var url = options.url;
    var data = options.data;
    if(NotShowProgress){
      api.showProgress({
				style: 'default',
				animationType: 'fade',
				title: tip,
				text: '请稍后...',
				modal: true
			});
    }
    api.ajax({
      url: url,
      method: 'post',
      data: {
        values: data
      }
    }, function(ret, err) {
      if(!NotShowProgress){
        api.hideProgress();
      }
      if (ret) {
        var response = eval(ret);
        if(response.erroCode==0){
          var data = eval(response.data);
          options.success&&options.success.call(null,data);
        }else{
          api.toast({
            msg: response.msg,
            duration: 3000,
            location: 'top'
          });
          options.error&&options.error.call(null,response);
        }
      } else {
        api.toast({
          msg: err.msg,
          duration: 3000,
          location: 'top'
        });
        return;
      }
    });
  }
}

var Api = {
  getAppMenu:"padCommController!getAppMenu.m",//获取菜单

  //按条件查询，返回多个结果
  getInTasksByParams:"padWmsController!getInTasksByParams.m",
  getArrivalBillsByParams:"padWmsController!getArrivalBillsByParams.m",
  getOutTasksByParams:"padWmsController!getOutTasksByParams.m",
  getDownTasksByParams:"padWmsController!getDownTasksByParams.m",
  //通过code查询，返回单个结果
  getInTaskByCode:"padWmsController!getInTaskByCode.m",
  getArrivalBillsByParams:"padWmsController!getArrivalBillsByParams.m",
  getOutTaskByCode:"padWmsController!getOutTaskByCode.m",
  getDownTaskByCode:"padWmsController!getDownTaskByCode.m",
  getUbcBarCode:"padWmsController!getUbcBarCode.m",
  getUbcPackageBarCode:"padWmsController!getUbcPackageBarCode.m", //根据包装条码code查询物料

  createAcceptBill:"padWmsController!createAcceptBill.m",
  createInBill:"padWmsController!createInBill.m", //创建入库单
  createOutBill:"padWmsController!createOutBill.m", //创建出库单
  generalLotCode:"padWmsController!generalLotCode.m", //取lotcode

  getWcByPid:"padWmsController!getWcByPid.m", //通过pid查询子仓库
  getAreaAndSubAreaByLocation:"padWmsController!getAreaAndSubAreaByLocation.m", //通过库位获取对应的区域库区
}
