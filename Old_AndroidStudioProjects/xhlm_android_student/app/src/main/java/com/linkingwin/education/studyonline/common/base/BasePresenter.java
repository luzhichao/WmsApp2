package com.linkingwin.education.studyonline.common.base;

/**
 * @描述
 * @作者 gsh
 * @时间 2017/6/14 11:43
 */

public abstract class BasePresenter {

//
//    private Context mContext;
//    private IHttpService httpService;
//
//    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
//
//    public BasePresenter(Context mContext) {
//        this.mContext = mContext;
//        this.httpService = new HttpServiceImpl(mContext);
//    }
//
//    /**
//     * 同步【数据字典】至本地缓存
//     *
//     * @param result
//     * @param systemId
//     * @throws Exception
//     */
//    public void syncDic2Sp(Object result, String systemId) throws Exception {
//        JSONObject obj = JSON.parseObject(result.toString());
//        List<SysDicVO> dicList = JSON.parseArray(obj.getJSONArray("result").toJSONString(), SysDicVO.class);
//        MyApplication.getInstance().setDicList(systemId, dicList);
//    }
//
//
//    /**
//     * 同步【数据字典】至本地缓存
//     *
//     * @param result
//     * @param userCode
//     * @throws Exception
//     */
//    public void syncUserSys2Sp(Object result, String userCode) throws Exception {
//        JSONObject obj = JSON.parseObject(result.toString());
//        List<SystemInfo> syss = JSON.parseArray(obj.getJSONArray("result").toJSONString(), SystemInfo.class);
//        MyApplication.getInstance().setSystemInfo(userCode, syss);
//    }
//
//
//
//    public boolean isNetWorkConnected() {
//        //检查网络
//        if (!HttpManager.isNetworkConnected(mContext)) {
//            return false;
//        }
//        return true;
//    }
}
