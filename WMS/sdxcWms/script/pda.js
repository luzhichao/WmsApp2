/**
 *@authour:qiyuan.xie
 *@createDate : 2018-05-09
 *@desc:获取url
 */
function getServerUrl() {
    var serverUrl = localStorage.getItem('serverUrl');
    var serverIp = localStorage.getItem('serverIp');
    var serverPort = localStorage.getItem('serverPort');
    var serverProject = localStorage.getItem('serverProject');
    return "http://" + serverIp + ":" + serverPort + "/" + serverProject + "/";
}

function getUrl(url) {
    var un = localStorage.getItem('un'),pw = localStorage.getItem('pw');
    var mWCFlag = localStorage.getItem('mWCFlag'),datarole = localStorage.getItem('workCenterGid');
  	if(mWCFlag != "-1" && datarole != null){
      if (un != '' && un != null) {
          return getServerUrl() + "autoLoginController!login.m?_u=" + un + "&_p=" + pw + "&_dr=" + datarole + "&_to=" + url;
      } else {
          return url;
      }
    }else{
      if (un != '' && un != null) {
          return getServerUrl() + "autoLoginController!login.m?_u=" + un + "&_p=" + pw + "&_to=" + url;
      } else {
          return url;
      }
    }

}
