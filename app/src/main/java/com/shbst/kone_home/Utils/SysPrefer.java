package com.shbst.kone_home.Utils;

import android.text.TextUtils;

import com.shbst.kone_home.Entity.FloorItemBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by zhouwenchao on 2018-02-08.
 * TODO 读取配置文件，获得物理楼层，显示楼层等数据
 */

// todo 用来读取本地系统配置文件，需要注意的是，读取方法是放在初始化方法中的
// todo 如果更改配置文件后，需要重启设备才能生效
public class SysPrefer {
    private static SysPrefer instence;
    private File jsonFile = null;
    private long fileLastModified = -1;

    private JSONObject jsonObject = null;

    private SysPrefer() {
        jsonFile = new File(Constant.PREFER_FILE_PATH);

        // TODO 写入测试数据到 本地
        //testData();
        readFileDataTOJson();
    }

    private void readFileDataTOJson() {
        //读取配置文件到 jsonObject 中
        if (!jsonFile.exists())
            return;
        fileLastModified = jsonFile.lastModified();

        InputStreamReader inputStreamReader = null;
        FileInputStream fileInputStream = null;
        try {
            StringBuilder strBuffer = new StringBuilder();
            inputStreamReader = new InputStreamReader(fileInputStream = new FileInputStream(jsonFile)
                    , "utf-8");
            BufferedReader bufferReader = new BufferedReader(inputStreamReader);
            String tmpStr;
            while ((tmpStr = bufferReader.readLine()) != null) {
                strBuffer.append(tmpStr);
            }
            jsonObject = new JSONObject(strBuffer.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("本地配置文件未找到");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("本地配置文件读取编码错误");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("读取本地配置文件 IO 异常");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("本地配置文件转换 JSON 失败");
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //生成假的配置文件到本地
    private void testData() {
        File outFile = jsonFile;
//        if (outFile.exists())
//            return;

        JSONObject testJsonObj = new JSONObject();
        JSONArray testJsonArray = new JSONArray();
        OutputStreamWriter inputStreamReader = null;
        FileOutputStream fileOutputStream = null;
        try {
            // TODO 模拟的物理楼层
            if(true) {


                for (int i = 1; i <= 5; i++) {
                    JSONObject childJsonData = new JSONObject();
                    childJsonData.put(Constant.PHYSICAL_FLOOR, i);
                    String displayText = null;
                    switch (i) {
                        case 1:
                            displayText = "P";
                            break;
                        case 2:
                            displayText = "G";
                            break;
                        case 3:
                            displayText = "2";
                            break;
                        case 4:
                            displayText = "3";
                            break;
                        case 5:
                            displayText = "5";
                            break;
                    }
                    childJsonData.put(Constant.DISPLAY_FLOOR, displayText);
                    //childJsonData.put(Constant.FLOOR_DESCRIPTION, "Floor");
                    childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
                    testJsonArray.put(childJsonData);
                }
            }else{
                for (int i = 1; i <= 2; i++) {
                    JSONObject childJsonData = new JSONObject();
                    childJsonData.put(Constant.PHYSICAL_FLOOR, i);
                    String displayText = null;
                    switch (i) {
                        case 1:
                            displayText = "1";
                            break;

                        case 2:
                            displayText = "2";
                            break;
                        case 3:
                            displayText = "3";
                            break;
                        case 4:
                            displayText = "4";
                            break;
                        case 5:
                            displayText = "5";
                            break;
                    }
                    childJsonData.put(Constant.DISPLAY_FLOOR, displayText);
                    //childJsonData.put(Constant.FLOOR_DESCRIPTION, "Floor");
                    childJsonData.put(Constant.FLOOR_DESCRIPTION, "");
                    testJsonArray.put(childJsonData);
                }
            }
            testJsonObj.put(Constant.FLOOR_DATA_ITEM_KEY, testJsonArray);
            testJsonObj.put(Constant.OPEN_DOOR_DELAY, false);
            //写入
            inputStreamReader = new OutputStreamWriter(fileOutputStream = new FileOutputStream(outFile)
                    , "utf-8");
            inputStreamReader.write(testJsonObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("测试 json 数据初始化失败");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("测试 json 数据文件未找到");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("测试 json 数据文件编码异常");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("测试 json 数据文件输出流异常");
        } finally {
            try {
                if (inputStreamReader != null)
                    inputStreamReader.close();

                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SysPrefer getInstence() {
        if (instence == null) {
            synchronized (SysPrefer.class) {
                if (instence == null) {
                    instence = new SysPrefer();
                }
            }
        }
        return instence;
    }

    public int getFloorCount() {
        if (judgeFileModified())
            readFileDataTOJson();

        if (jsonObject == null)
            return 5;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.FLOOR_DATA_ITEM_KEY);
            return jsonArray.length();
        } catch (JSONException e) {
            e.printStackTrace();
            return 5;
        }
    }

    public FloorItemBean getFloorItemBean(int floor, FloorItemBean itemBean) {



        if (judgeFileModified())
            readFileDataTOJson();

        if (itemBean == null)
            itemBean = new FloorItemBean();

        if (jsonObject == null)
            return itemBean;

        try {
            JSONArray jsonArray = jsonObject.getJSONArray(Constant.FLOOR_DATA_ITEM_KEY);

            JSONObject childJsonObj;
            String displayFloor;
            String floorDescription;
            for (int i = 0; i < jsonArray.length(); i++) {
                childJsonObj = jsonArray.optJSONObject(i);
                if (childJsonObj == null)
                    continue;
                if (childJsonObj.optInt(Constant.PHYSICAL_FLOOR) == floor) {
                    itemBean.setPhysicalFloor(floor);

                    displayFloor = childJsonObj.optString(Constant.DISPLAY_FLOOR);
                    floorDescription = childJsonObj.optString(Constant.FLOOR_DESCRIPTION);

                    itemBean.setDisplayFloor(displayFloor);
                    // FIXME: 2018/4/4 修复修改楼层映射问题
                    /*
                    楼层描述文字不支持配置，只有Floor和楼两种语言的默认
                    if (TextUtils.isEmpty(itemBean.getFloorDescription()))
                        itemBean.setFloorDescription(floorDescription);
                    */
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return itemBean;
    }

    public boolean OpenDoorDelay() {
        if (judgeFileModified())
            readFileDataTOJson();

        if (jsonObject == null)
            return false;
        try {
            return jsonObject.getBoolean(Constant.OPEN_DOOR_DELAY);
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 每次读取本地配置前，判断文件是否被修改
     *
     * @return 是否被修改
     */
    private boolean judgeFileModified() {
        return jsonFile.exists()
                && fileLastModified != jsonFile.lastModified();

    }
}
