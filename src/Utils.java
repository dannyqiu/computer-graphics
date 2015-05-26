import java.util.*;
import java.io.*;

public class Utils {

    /**
     * Recursively deletes a directory including any contained files
     * @param directory File object representing the directory
     * @return true if and only if the file or directory is successfully
     *         deleted; false otherwise
     */
    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    }
                    else {
                        files[i].delete();
                    }
                }
            }
        }
        return (directory.delete());
    }

}
