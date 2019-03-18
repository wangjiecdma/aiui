package com.shbst.kone_home.Utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by zhouwenchao on 2018-03-08.
 */

public class FileUtil {
    private static final String enter = "\n";
    private static final String error0 = "发生了未知异常";
    private static final String error1 = "无读写文件权限或文件不存在";
    public static final String error2 = "有正在复制的线程，请等待复制完成";
    private static final String error3 = "创建文件或文件夹异常";
    private static final String error4 = "文件流异常";
    private static final String error5 = "关闭文件流异常";


    // 本地根目录
    public final static String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath();


    public static void copyAssasFileToPath(Context context, String sourceName, String topath, CopyFileCallBack callBack) {
        copyAssasFileToPath(context, sourceName, new File(topath), callBack);
    }

    public static void copyAssasFileToPath(Context context, String sourceName, File tofile, CopyFileCallBack callBack) {
        if (hasRootDirPermmiss()) {  // 有读写文件夹权限
            new Thread(new CopyResourceRunnable(context, sourceName, tofile, callBack)).start();
        } else {
            if (callBack != null) {
                callBack.CopyOver(sourceName, tofile.getPath(), false, error1);
            }
        }
    }

    /**
     * 判断根目录文件夹是否可读写
     *
     * @return 文件可读可写
     */
    public static boolean hasRootDirPermmiss() {
        File rootDirFile = new File(rootDir);
        return rootDirFile.canWrite() && rootDirFile.canRead();
    }

    static class CopyResourceRunnable implements Runnable {
        Context context;
        int resourID;
        String sourcename;
        File toFile;
        CopyFileCallBack callBack;
        String errorTag = "";
        int copyMode = -1;
        boolean notify = true;

        long copyFileProgress = 0;

        private CopyResourceRunnable(Context context, int resourceID, File tofile, CopyFileCallBack callBack) {
            this.context = context;
            this.resourID = resourceID;
            this.toFile = tofile;
            this.callBack = callBack;
            copyMode = 1;
        }

        private CopyResourceRunnable(Context context, String sourceName, File tofile, CopyFileCallBack callBack) {
            this.context = context;
            this.sourcename = sourceName;
            this.toFile = tofile;
            this.callBack = callBack;
            copyMode = 2;
        }

        @Override
        public void run() {
//            FileOutputStream lOutputStream = context.openFileOutput(target, 0);
            notify = true;
            copyFileProgress = 0;
            copyImpl();
            System.gc();
            notify = false;
            if (callBack != null) {
                callBack.CopyOver(sourcename, toFile.getPath(), TextUtils.isEmpty(errorTag), errorTag);
            }
        }

        private void copyImpl() {
            OutputStream outputStream = null;
            InputStream lInputStream = null;
            try {
                if (copyMode == 1 | copyMode == 2) {
                    if (!toFile.exists()) {  //  如果文件不存在
                        try {
                            if (mkDir(toFile.getParent())) {
                                errorTag = apendError(errorTag, error3);
                                return;
                            }
                            toFile.createNewFile();
                        } catch (RuntimeException | IOException e) {
                            errorTag = apendError(errorTag, error3);
                        }
                    } else {
                        deleteFileSafely(toFile);
                        copyImpl();
                        return;
                    }
                    outputStream = new FileOutputStream(toFile);
                    if (copyMode == 1) {
                        lInputStream = context.getResources().openRawResource(resourID);
                    } else if (copyMode == 2) {
                        lInputStream = context.getAssets().open(sourcename);
                    }
                    new Thread(new CopyResourceRunnable.notifyProgressRunnable(String.valueOf(resourID), toFile.getPath()
                            , 0, callBack)).start(); //启用通知线程
                    int readByte;
                    byte[] buff = new byte[8048];
                    while ((readByte = lInputStream.read(buff)) > 0) {
                        outputStream.write(buff, 0, readByte);
                        copyFileProgress += readByte;
                    }
                } else {
                    errorTag = apendError(errorTag, error0);
                }
            } catch (IOException e) {
                errorTag = apendError(errorTag, error4);
            } catch (Exception e) {
                errorTag = apendError(errorTag, error0);
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.flush();
                        outputStream.close();
                    }
                    if (lInputStream != null)
                        lInputStream.close();
                } catch (IOException e) {
                    errorTag = apendError(errorTag, error5);
                }
            }
        }

        class notifyProgressRunnable implements Runnable {
            long fileLenght = 0;
            CopyFileCallBack copyFileCallBack;
            String path;
            String toPath;

            private notifyProgressRunnable(String path, String toPath, long fileLenght, CopyFileCallBack callBack) {
                this.fileLenght = fileLenght;
                this.copyFileCallBack = callBack;
                this.path = path;
                this.toPath = toPath;
            }

            @Override
            public void run() {
                while (notify) {
                    if (copyFileCallBack != null) {
                        copyFileCallBack.CopyProgress(path, toPath, copyFileProgress, fileLenght); //因为是复制资源文件，无法读取到文件总长度，故为0
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static boolean mkDir(String path) {
        File dir = new File(path);
        return dir.mkdirs();
    }

    /**
     * 将异常信息进行拼接
     *
     * @param error
     */
    private static String apendError(String errorTag, String error) {
        if (TextUtils.isEmpty(errorTag)) {
            errorTag += error;
            return errorTag;
        } else {
            errorTag += enter;
            errorTag += error;
            return errorTag;
        }
    }

    /**
     * @param file 要删除的文件
     */
    private static boolean deleteFileSafely(File file) {
        if (file != null && file.exists()) {
            File tmp = getTmpFile(file, System.currentTimeMillis(), -1);
            if (file.renameTo(tmp)) {    // 将源文件重命名
                return tmp.delete();  //  删除重命名后的文件
            } else {
                return file.delete();
            }
        }
        return false;
    }


    private static File getTmpFile(File file, long time, int index) {
        File tmp;
        if (index == -1) {
            tmp = new File(file.getParent() + File.separator + time);
        } else {
            tmp = new File(file.getParent() + File.separator + time + "(" + index + ")");
        }
        if (!tmp.exists() || index >= 1000) {
            return tmp;
        } else {
            return getTmpFile(file, time, index >= 1000 ? index : ++index);
        }
    }

    public interface CopyFileCallBack {
        void startCopy(String path, String toPath, long filelenght);

        void CopyProgress(String path, String toPath, long progress, long fileLenght);

        void CopyOver(String path, String toPath, boolean over, String error);
    }
}
