package protocol.sdk.shbst.com.singlechipsdk.services;

/**
 * Created by Smoyan on 2018/1/1.
 */

public class Config {

    public static final int FRAME_MAX_LENGTH = 128;
    public static final int STX = 0x80;//frame head
    public static final int ETX = 0x81;//frame tail
    public static final byte ESC = 0x1B;//frame AntiEscape

    //MCU info
    public static String MCU_INFO_MODEL = "BWspace";
    public static String MCU_INFO_COPYRIGHT = "YC";
    public static final int MCU_FRAME_LENGTH = 64;//升级包单次大小

}
