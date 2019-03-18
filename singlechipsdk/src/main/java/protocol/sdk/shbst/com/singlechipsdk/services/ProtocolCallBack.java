package protocol.sdk.shbst.com.singlechipsdk.services;

/**
 * Created by Smoyan on 2018/1/5.
 */

public interface ProtocolCallBack {

    void onReceiveData(byte[] data);

    void onUpdateProcess(int process);

    /**
     *
     * @param isNormalReady 是否为正常的升级流程结束而准备好
     *  为false的情况:1.该机器MCU版本为17年以前旧协议  2.其他异常情况
     *  为true的情况:1.不需要升级正常结束   2.走完完整的升级流程结束
     */
    void onReady(boolean isNormalReady);

}
