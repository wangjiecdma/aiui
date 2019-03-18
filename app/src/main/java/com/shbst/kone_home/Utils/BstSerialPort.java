package com.shbst.kone_home.Utils;

import android.content.Context;
import android.support.annotation.MainThread;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;


public class BstSerialPort {
    String  serialPaths[] = null;
    Object  mServer ;
    Class   mServerClass;
    Object  mFile;
    Class   mFileClass;
    Method  readMethod;
    Method  writeMethod;

    Method  byteReader;
    Method  byteWriter;

    public BstSerialPort(Context context){
        try {
            mServer = context.getSystemService("serial");
            mServerClass = Class.forName("android.hardware.SerialManager");
            Method method = mServerClass.getMethod("getSerialPorts");
            serialPaths = (String[]) method.invoke(mServer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean openSerialPort(int speed){
        return openSerialPort(serialPaths[0],speed);
    }

    public boolean openSerialPort(String path , int speed){
        try {
            Method method = mServerClass.getMethod("openSerialPort", String.class, int.class);
            mFile = method.invoke(mServer, path, speed);
            mFileClass = mFile.getClass();
            readMethod = mFileClass.getMethod("read", ByteBuffer.class);
            writeMethod = mFileClass.getMethod("write", ByteBuffer.class,int.class);
            Method array []= mFileClass.getDeclaredMethods();

            for (Method m:array){
                Log.d("www","getDeclaredMethods :"+m.getName());
            }


            byteReader = mFileClass.getDeclaredMethod("native_read_array",byte[].class,int.class);

            if(byteReader != null){
                byteReader.setAccessible(true);
                Log.d("www","native read array is ok ");
            }else{
                Log.d("www","native read array not ok ");
            }

            byteWriter= mFileClass.getDeclaredMethod("native_write_array",byte[].class,int.class);

            if(byteWriter != null){
                byteWriter.setAccessible(true);
                Log.d("www","native byteWriter array is ok ");
            }else{
                Log.d("www","native byteWriter array not ok ");
            }


            if (mFile != null) {
                return true;
            } else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            android.util.Log.d("www","openSerialPort error ",e);
        }
        return false;
    }


    public void close(){
        try{
            Method method = mFileClass.getMethod("close");
            method.invoke(mFile);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int read(ByteBuffer buffer) {
        try {
            return (int) readMethod.invoke(mFile, buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public void write(ByteBuffer buffer, int length){
        try{
            writeMethod.invoke(mFile,buffer,length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void write(byte[] buf , int length){

        try{
            byteWriter.invoke(mFile,buf,length);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int read(byte[]buf , int length){
        try{
            return (int)byteReader.invoke(mFile,buf,length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    public static void test(final Context context){

        Thread thread = new Thread(){
            @Override
            public void run() {

                BstSerialPort  port = new BstSerialPort(context);
                port.openSerialPort(9600);

                ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
                byte buffer[] = new byte[16];
                while(true){
                    int len = port.read(buffer,16);
                    Log.d("www","serial port read len :"+len);
                    Log.d("www",buffer);
                }
            }
        };
        thread.start();
    }

    //系统修改
    //frameworks/base/core/res/res/values/config.xml
    //在数组config_serialPorts中增加串口设备节点,bstIMX6-4.4系统的串口设备节点为：/dev/ttymxc2
    //frameworks/base/core/res/AndroidManifest.xml
    //android.permission.SERIAL_PORT 中修改android:protectionLevel="signature"
    //只要是有系统签名的APP都可以访问串口。
}
