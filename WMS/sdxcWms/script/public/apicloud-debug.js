function getMethodNames(obj){
    var names = {};
    for(var name in obj){
        if(typeof obj[name] == "function") {
            names[name] = 'function';
        }
    }
    return names
}

function serialize(obj, name){
    var result = "";
    function serializeInternal(o, path) {
        for (p in o) {
            var value = o[p];
            if (typeof value != "object") {
                if (typeof value == "string") {
                    result += "\n" + path + "[" + (isNaN(p)?"\""+p+"\"":p) + "] = " + "\"" + value.replace(/\"/g,"\\\"") + "\""+";";
                }else {
                    result += "\n" + path + "[" + (isNaN(p)?"\""+p+"\"":p) + "] = " + value+";";
                }
            }
            else {
                if (value instanceof Array) {
                    result += "\n" + path +"[" + (isNaN(p)?"\""+p+"\"":p) + "]"+"="+"new Array();";
                    serializeInternal(value, path + "[" + (isNaN(p)?"\""+p+"\"":p) + "]");
                } else {
                    result += "\n" + path  + "[" + (isNaN(p)?"\""+p+"\"":p) + "]"+"="+"new Object();";
                    serializeInternal(value, path +"[" + (isNaN(p)?"\""+p+"\"":p) + "]");
                }
            }
        }
    }
    serializeInternal(obj, name);
    return result;
}
//console.log(serialize(api,"api"));

setTimeout(function () {
    if(window.api){
        return;
    }
    // console.log(9999)
    window.api = {
        "version":"1.3.11",
        "systemType":"android",
        "systemVersion":"4.4.2",
        "deviceId":"127943198919366",
        "deviceModel":"SM-G900F",
        "deviceName":"samsung",
        "connectionType":"wifi",
        "wgtParam":{},
        "pageParam":{},
        "appParam":"{}",
        "wgtRootDir":"file:///storage/sdcard/UZMap/wgt/A6003863990187",
        "winName":"menu_win",
        "frameName":"menu_fram",
        "winWidth":414,
        "winHeight":736,
        "frameWidth":414,
        "frameHeight":690,
        "appId":"A6003863990187",
        "appName":"HgzyWms",
        "wgtLoaderDir":"/storage/sdcard/UZMap/wgt/",
        "appVersion":"00.00.06",
        "screenWidth":621,
        "screenHeight":1104,
        "fsDir":"/storage/sdcard/UZMap/A6003863990187",
        "cacheDir":"/storage/sdcard/UZMap/A6003863990187/cache",
        "operator":"中国移动",
        "deviceToken":"127943198919366",
        "fingerPrint":"samsung/kltexx/klte:4.4.2/KOT49H/G900FXXU1ANCE:user/release-keys",
        "statusBarAppearance":false,
        "debug":false,
        "boxDir":"/data/data/com.k1004093755.obm/box",
        "channel":"apicloud",
        "jailbreak":false,
        "uiMode":"phone",
        "safeArea":{"bottom":0,
            "right":0,
            "left":0,
            "top":26},
        "require":function (){},
        "toLauncher":function (){},
        "installApp":function (){},
        "openApp":function (){},
        "openWidget":function (){},
        "closeWidget":function (){},
        "getFsWidgets":function (){},
        "openWin":function (){},
        "openSlidLayout":function (){},
        "openSlidPane":function (){},
        "closeSlidPane":function (){},
        "setWinAttr":function (){},
        "closeWin":function (){},
        "closeToWin":function (){},
        "execScript":function (){},
        "openFrame":function (){},
        "setFrameAttr":function (){},
        "bringFrameToFront":function (){},
        "sendFrameToBack":function (){},
        "closeFrame":function (){},
        "animation":function (){},
        "openFrameGroup":function (){},
        "setFrameGroupAttr":function (){},
        "setFrameGroupIndex":function (){},
        "closeFrameGroup":function (){},
        "setRefreshHeaderInfo":function (){},
        "refreshHeaderLoadDone":function (){},
        "addEventListener":function (){},
        "removeEventListener":function (){},
        "log":function (){},
        "alert":function (){},
        "confirm":function (){},
        "prompt":function (){},
        "showProgress":function (){},
        "hideProgress":function (){},
        "setPrefs":function (){},
        "getPrefs":function (){},
        "loadSecureValue":function (){},
        "removePrefs":function (){},
        "getPicture":function (){},
        "ajax":function (){},
        "call":function (){},
        "sms":function (){},
        "mail":function (){},
        "readFile":function (){},
        "writeFile":function (){},
        "startRecord":function (){},
        "stopRecord":function (){},
        "startPlay":function (){},
        "stopPlay":function (){},
        "startLocation":function (){},
        "stopLocation":function (){},
        "getLocation":function (){},
        "startSensor":function (){},
        "stopSensor":function (){},
        "setStatusBarStyle":function (){},
        "setFullScreen":function (){},
        "removeLaunchView":function (){},
        "openContacts":function (){},
        "openPicker":function (){},
        "openVideo":function (){},
        "download":function (){},
        "cancelDownload":function (){},
        "clearCache":function (){},
        "actionSheet":function (){},
        "toast":function (){},
        "showFloatBox":function (){},
        "notification":function (){},
        "cancelNotification":function (){},
        "lockSlidPane":function (){},
        "unlockSlidPane":function (){},
        "setScreenOrientation":function (){},
        "setKeepScreenOn":function (){},
        "sendEvent":function (){},
        "historyBack":function (){},
        "historyForward":function (){},
        "appInstalled":function (){},
        "imageCache":function (){},
        "requestFocus":function (){},
        "onTvPeak":function (){},
        "setTvFocusElement":function (){},
        "pageDown":function (){},
        "pageUp":function (){},
        "scrollBy":function (){},
        "scrollTo":function (){},
        "saveMediaToAlbum":function (){},
        "setScreenSecure":function (){},
        "setAppIconBadge":function (){},
        "getCacheSize":function (){},
        "getFreeDiskSpace":function (){},
        "cancelAjax":function (){},
        "refreshHeaderLoading":function (){},
        "accessNative":function (){},
        "unInstallApp":function (){},
        "uninstallApp":function (){},
        "openDrawerLayout":function (){},
        "openDrawerPane":function (){},
        "closeDrawerPane":function (){},
        "setCustomRefreshHeaderInfo":function (){},
        "setFrameClient":function (){},
        "rebootApp":function (){},
        "getPhoneNumber":function (){},
        "getTotalSpace":function (){},
        "loadData":function (){},
        "showLaunchView":function (){},
        "setBlurEffect":function (){},
        "hasPermission":function (){},
        "requestPermission":function (){},
        "applyCertificates":function (){},
        "setGlobalData":function (){},
        "getGlobalData":function (){},
        "windows":function (){},
        "frames":function (){},
        "openTabLayout":function (){},
        "setTabLayoutAttr":function (){},
        "setTabBarAttr":function (){},
        "setTabBarItemAttr":function (){},
        "setMenuItems":function (){},
        "setNavBarAttr":function (){},
        "parseTapmode":function (){}
    }
    apiready();

    localStorage.setItem('un','xiangzhou');
    localStorage.setItem('pw','MdYkh6KFDkw');
    localStorage.setItem('serverIp','');
    localStorage.setItem('serverPort', '80');
    localStorage.setItem('serverProject', 'wms');
    localStorage.setItem('mWCFlag',0);
    localStorage.setItem('workCenterGid', 'CENTER201903081005170000000002');

},1000);
