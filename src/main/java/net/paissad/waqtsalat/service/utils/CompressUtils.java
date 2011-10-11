/*
 * This file is part of WaqtSalat-Service.
 * 
 * WaqtSalat-Service is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * WaqtSalat-Service is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with WaqtSalat-Service. If not, see <http://www.gnu.org/licenses/>.
 */

package net.paissad.waqtsalat.service.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author paissad
 * 
 */
public class CompressUtils {
    /*
     * TODO: add compress method (ZIP, TAR.GZ ...)
     */

    private static Logger       logger      = LoggerFactory.getLogger(CompressUtils.class);

    private static final String EXT_GZ      = "gz";
    private static final String EXT_TGZ     = "tgz";
    private static final String EXT_BZIP2   = "bz2";
    private static final String EXT_TAR_GZ  = "tar.gz";
    private static final String EXT_TAR_BZ2 = "tar.bz2";

    private CompressUtils() {
    }

    /**
     * Uncompress the specified file.<br>
     * This method is the equivalent of {@link #uncompress(File, File)}.
     * 
     * @param compressedFile - The file to uncompress.
     * @throws Exception
     * @see #uncompress(File, File)
     */
    public static void uncompress(final File compressedFile) throws Exception {
        uncompress(compressedFile, null);
    }

    /**
     * Uncompress an archive file to the specified location.
     * <p>
     * Supported types are 'jar', 'zip', 'tar', 'tar.gz', 'tar.bz2' and maybe
     * other ones.
     * </p>
     * 
     * @param compressedFile - The file to uncompress.
     * @param destDir - The directory where to save the uncompressed resources.
     *            If {@code null} then the parent directory where is stored the
     *            compressedFile will be used.
     * @throws Exception
     * @see #uncompress(File)
     */
    public static void uncompress(final File compressedFile, final File destDir) throws Exception {

        if (compressedFile == null) {
            throw new IllegalArgumentException("The compressed file may not be null.");
        }

        // If no output directory is specified, then we use the default parent
        // directory of the compressed file.
        final File outputDir = (destDir != null) ? destDir : compressedFile.getAbsoluteFile().getParentFile();
        if (outputDir == null) {
            throw new IllegalArgumentException("The output directory may not be null.");
        }

        final String filename = compressedFile.getName();
        final String extension = CommonUtils.getFilenameExtension(filename).toLowerCase();

        InputStream in = null, cis = null;
        OutputStream out = null;

        try {

            in = new BufferedInputStream(new FileInputStream(compressedFile));
            FileUtils.forceMkdir(outputDir);

            final String str = filename.toLowerCase();
            if (str.endsWith("." + EXT_TAR_GZ) || str.endsWith("." + EXT_TGZ) || str.endsWith("." + EXT_TAR_BZ2)) {
                cis = new BufferedInputStream(new CompressorStreamFactory().createCompressorInputStream(in));
                saveFileFromArchiveStream(cis, destDir);

            } else if (extension.equals(EXT_GZ) || extension.equals(EXT_BZIP2)) {
                String filenameWithoutExtension = filename.substring(0, filename.lastIndexOf("." + extension));
                cis = new BufferedInputStream(new CompressorStreamFactory().createCompressorInputStream(in));
                saveFileFromCompressorStream(cis, new File(destDir, filenameWithoutExtension));

            } else {
                // archive file ('zip', 'tar', 'cpio' ...)
                cis = new BufferedInputStream(new FileInputStream(compressedFile));
                saveFileFromArchiveStream(cis, destDir);
            }

        } catch (Exception e) {
            String errMsg = String.format("Error while uncompressing the file '%s' to '%s'\n", compressedFile, destDir);
            logger.error(errMsg, e);
            throw new Exception(errMsg, e);

        } finally {
            CommonUtils.closeAllStreamsQuietly(in, out, cis);
        }
    }

    /**
     * Use this method for a stream of type 'gz', 'bzip2'.
     * 
     * @param in
     * @param outputFile
     * @throws IOException
     */
    private static void saveFileFromCompressorStream(
            final InputStream in,
            final File outputFile) throws IOException {

        if (in == null) {
            throw new IllegalArgumentException("The inputstream may not be null.");
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(outputFile));
            IOUtils.copy(in, out);
        } finally {
            CommonUtils.closeStreamQuietly(out);
        }

    }

    /**
     * Use this stream for a type 'zip', 'jar, 'tar', 'cpio', 'ar'.
     * 
     * @param in
     * @param destDir - The directory where to place the extracted archive
     *            entries.
     * @throws ArchiveException
     * @throws IOException
     */
    private static void saveFileFromArchiveStream(
            final InputStream in,
            final File destDir) throws ArchiveException, IOException {

        if (in == null) {
            throw new IllegalArgumentException("The inputstream may not be null.");
        }

        ArchiveInputStream archiveStream = null;
        OutputStream out = null;
        try {
            archiveStream = new ArchiveStreamFactory().createArchiveInputStream(in);
            ArchiveEntry entry;
            while ((entry = archiveStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    FileUtils.forceMkdir(new File(destDir, entry.getName()));
                } else {
                    out = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())));
                    IOUtils.copy(archiveStream, out);
                }
            }
        } finally {
            CommonUtils.closeAllStreamsQuietly(archiveStream, out);
        }
    }

    /*
     * FOR TESTING PURPOSE ONLY ! XXX
     */
    public static void main(String[] args) throws Exception {

        File compressedFile = new File("worldcitiespop.txt.gz");

        System.out.println("Uncompresing file " + compressedFile);
        uncompress(compressedFile);
        System.out.println("File uncompressed successfully !");

    }
}
