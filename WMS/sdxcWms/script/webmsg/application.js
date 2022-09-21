var logTime = (new Date()).toLocaleString();//日志时间
var errorTime = (new Date()).toLocaleString();//错误时间
var transport = 'websocket';//'long-polling';

function initAtmosphere(msgUrl,message,error){
	var socket = atmosphere;
	var subSocket;
	var request = {
		url : msgUrl,
		contentType : "application/json",
		transport : transport,
		clientId : (new Date()).getTime().toString()
	};
	//接收消息
	request.onMessage = function(response) {
		if($.isFunction(message)){
			message(response);
		}
	};
	request.onError = function(response) {
		socket.subscribe(request);//先建立，后调用error，可以在error里面调用ajax发送请求
		if($.isFunction(error)){
			error(response);
		}
	};
	
	subSocket = socket.subscribe(request);
	return subSocket;
}
function getLog(){//平板使用
	api.toast({
        msg : "接收:"+logTime+"\n错误:"+errorTime,
        duration : 5000
    });
}
function changeLogo(){//平板使用
	logTime= (new Date()).toLocaleString();
	var logoSrc = $("#Epichus-logo")[0].src;
	var indexStr = logoSrc.indexOf("logo-right1");
	var indexStr1 = logoSrc.indexOf("logo-right2");
	if(indexStr != -1)
	{
		$("#Epichus-logo").attr('src','../image/logo-right2.png');
	}else{
		$("#Epichus-logo").attr('src','../image/logo-right1.png');
	}
}
