/**
 * 
 */
package app;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import util.FileUtil;

/**
 * @author Pijush
 *
 */
public class MainLauncher {

    /**
     * @param args
     */
    private static final FileUtil fileUtil = new FileUtil();
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        
        fileUtil.openScreen();
    }

}
