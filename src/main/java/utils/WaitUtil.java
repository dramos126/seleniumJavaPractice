package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WaitUtil {

    public void staticWait(int inMillis) {
        try {
            Thread.sleep(inMillis);
        } catch (InterruptedException e) {
            System.out.printf("failed to perform static wait for %d", inMillis);
        }
    }

}
