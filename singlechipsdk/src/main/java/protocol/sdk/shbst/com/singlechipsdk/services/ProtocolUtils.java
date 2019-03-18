package protocol.sdk.shbst.com.singlechipsdk.services;

import android.util.Log;

/**
 * Created by suzhiming on 2017/11/1.
 */

public class ProtocolUtils {

    /* 校验函数 */
    public static byte CRC_Check(byte[] data) {
        byte result = 0;
        for (int i : data) {
            result ^= i;
        }
        return (byte) (result & 0x7F);
    }

    /* 进行Escape处理 */
    public static byte[] doEscape(byte[] data) {
        int length = data.length;
        if (length > Config.FRAME_MAX_LENGTH) {
            return null;
        }

        byte[] frame = new byte[Config.FRAME_MAX_LENGTH];
        int i, k;
        for (i = 0; i < length; i++) {
            frame[i] = data[i];
        }

        for (i = 0; i < length; i++) {
            if (frame[i] == (byte) Config.STX) {
                length++;
                if (length > Config.FRAME_MAX_LENGTH) {
                    return null;
                }

                for (k = length - 1; k > i; k--) {
                    frame[k] = frame[k - 1];
                }

                frame[i] = (byte) Config.ESC;
                frame[i + 1] = (byte) 0xE7;
            } else if (frame[i] == (byte) Config.ETX) {
                length++;
                if (length > Config.FRAME_MAX_LENGTH) {
                    return null;
                }

                for (k = length - 1; k > i; k--) {
                    frame[k] = frame[k - 1];
                }
                frame[i] = (byte) Config.ESC;
                frame[i + 1] = (byte) 0xE8;

            } else if (frame[i] == (byte) Config.ESC) {
                length++;
                if (length > Config.FRAME_MAX_LENGTH) {
                    return null;
                }

                for (k = length - 1; k > i; k--) {
                    frame[k] = frame[k - 1];
                }

                frame[i] = (byte) Config.ESC;
                frame[i + 1] = 0x00;
            }
        }

        byte[] ret = new byte[length];
        for (i = 0; i < length; i++) {
            ret[i] = frame[i];
        }
        return ret;
    }

    /* 从已经Escape中的数据恢复原始数据 */
    public static byte[] AntiEscape(byte[] data, int length) {
        int i, k;
        try {
            for (i = 0; i < length; i++) {
                if (data[i] == Config.ESC) {
                    if (data[i + 1] == (byte) 0xE7) {
                        data[i] = (byte) Config.STX;
                        length--;

                        for (k = i + 1; k < length; k++) {
                            data[k] = data[k + 1];
                        }
                    } else if (data[i + 1] == (byte) 0xE8) {
                        data[i] = (byte) Config.ETX;
                        length--;

                        for (k = i + 1; k < length; k++) {
                            data[k] = data[k + 1];
                        }
                    } else if (data[i + 1] == 0x00) {
                        data[i] = Config.ESC;
                        length--;

                        for (k = i + 1; k < length; k++) {
                            data[k] = data[k + 1];
                        }
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e("AntiEscape", "AntiEscape 协议数据转换  出错 ");
        }


        byte[] ret = new byte[length];
        for (i = 0; i < length; i++) {
            ret[i] = data[i];
        }
        return ret;
    }
}
