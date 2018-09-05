package org.ronald.learn.retrofit.interceptor.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.internal.platform.Platform;

/**
 * @author ihsan on 10/02/2017.
 */
class I {

    protected I() {
        throw new UnsupportedOperationException();
    }

    static void log(int type, String tag, String msg) {
        Logger logger = Logger.getLogger(tag);
        switch (type) {
            case Platform.INFO:
                logger.log(Level.INFO, msg);
                break;
            default:
                logger.log(Level.WARNING, msg);
                break;
        }
    }
}