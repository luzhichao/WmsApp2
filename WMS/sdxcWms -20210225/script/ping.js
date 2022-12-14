/**
 *@authour:qiyuan.xie
 *@createDate : 2018-08-09
 *@desc:ping网络,返回连接过程中用时(毫秒)
 */
(function(root, factory) {
    if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else if (typeof module === 'object' && module.exports) {
        module.exports = factory();
    } else {
        root.ping = factory();
    }
}(this, function() {

    /**
     * @param  {String} url
     * @return {Promise}
     */
    function request_image(url) {
        return new Promise(function(resolve, reject) {
            var img = new Image();
            img.onload = function() {
                resolve(img);
            };
            img.onerror = function() {
                reject(url);
            };
            img.src = url + '?random-no-cache=' + Math.floor((1 + Math.random()) * 0x10000).toString(16);
        });
    }

    /**
     * @param  {String} url
     * @param  {Number} multiplier
     * @return {Promise}
     */
    function ping(url, multiplier) {
        return new Promise(function(resolve, reject) {
            var start = (new Date()).getTime();
            var response = function() {
                var delta = ((new Date()).getTime() - start);
                delta *= (multiplier || 1);
                resolve(delta);
            };
            request_image(url).then(response).catch(response);
            setTimeout(function() {
                reject(Error('Timeout'));
            }, 5000);
        });
    }

    return ping;
}));
