/**
 * 
 */
package com.dayler.common.util.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import com.dayler.common.util.Parameters;

/**
 * @author dayler
 *
 */
public final class FileUtils {

    private FileUtils() {
        // No op
    }

    /**
     * Copy two files in the destination file. Overwrite destination file if it exists. This method
     * is implemented for Java 6, if is using java 7 or latter use <b>File.copy(f1, f2)</b>
     * 
     * @param file File to copy.
     * @param copyTo Destination file.
     * @throws IOException If the Origin file does not exists or the copy operation could not be 
     * performed.
     */
    public static void copyFile(File file, File copyTo) throws IOException {
        Parameters.checkNull(file, "origin");
        Parameters.checkNull(copyTo, "destination");

        FileChannel fileCahnnel = null;
        FileChannel copyToChannel = null;
        FileInputStream inFile = null;
        FileOutputStream osCopyTo = null;

        // Ensure target directory exists.
        File targetDir = copyTo.getParentFile();
        if (!targetDir.exists()) {
            targetDir.mkdir();
        }

        try {
            inFile = new FileInputStream(file);
            fileCahnnel = inFile.getChannel();
            osCopyTo = new FileOutputStream(copyTo, false);
            copyToChannel = osCopyTo.getChannel();
            long bytesTransferred = 0;
            // Defensive loop.
            while (bytesTransferred < fileCahnnel.size()) {
                bytesTransferred += fileCahnnel.transferTo(0, fileCahnnel.size(), copyToChannel);
            }
        } finally {
            if (fileCahnnel != null) {
                fileCahnnel.close();
            }

            if (copyToChannel != null) {
                copyToChannel.close();
            }

            if (inFile != null) {
                inFile.close();
            }

            if (osCopyTo != null) {
                osCopyTo.close();
            }
        }
    }
}
