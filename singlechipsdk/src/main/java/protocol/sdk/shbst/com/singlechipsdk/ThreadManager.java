package protocol.sdk.shbst.com.singlechipsdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by suzhiming on 2017/12/1.
 */

public class ThreadManager {

    private static ExecutorService mExecutorService = Executors.newCachedThreadPool();

    public static ExecutorService getInstance() {
        if (null == mExecutorService || mExecutorService.isShutdown()) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        return mExecutorService;
    }

    public static void shutDownAllThread(boolean now) {
        if (now) {
            mExecutorService.shutdownNow();
        } else {
            mExecutorService.shutdown();
        }
    }

}
