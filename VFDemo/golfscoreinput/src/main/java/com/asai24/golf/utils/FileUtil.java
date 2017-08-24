package com.asai24.golf.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.asai24.golf.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class FileUtil {
    public static void encryptFile(String srcFilePath, String dstFilePath) throws Exception {
        copyFile(srcFilePath, dstFilePath, Cipher.ENCRYPT_MODE);
    }

    public static void decryptFile(String srcFilePath, String dstFilePath) throws Exception {
        copyFile(srcFilePath, dstFilePath, Cipher.DECRYPT_MODE);
    }

    /**
     * ファイルコピー処理
     */
    private static void copyFile(String srcFilePath, String dstFilePath, int mode) throws Exception {
        File srcFile = new File(srcFilePath);
        File dstFile = new File(dstFilePath);
        InputStream input = null;
        OutputStream output = null;
        InputStream cis = null;
        String myDir = dstFile.getParent();

        try {
            byte[] salt = {(byte) 0x39, (byte) 0x4e, (byte) 0xa2, (byte) 0x18,
                    (byte) 0x79, (byte) 0x22, (byte) 0x10, (byte) 0xfe};
            int iteration = 10;
            PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iteration);

            PBEKeySpec keySpec = new PBEKeySpec("my password".toCharArray());
            SecretKeyFactory keyFac = SecretKeyFactory
                    .getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFac.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
            cipher.init(mode, key, paramSpec);

            if (myDir != null && !myDir.equals("")) {
                try {
                    mkdir_p(myDir);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }

            input = new FileInputStream(srcFile);
            cis = new CipherInputStream(input, cipher);
            if (!dstFile.exists()) {
                dstFile.createNewFile();
            }
            output = new FileOutputStream(dstFile);

            int DEFAULT_BUFFER_SIZE = 1024 * 4;
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = cis.read(buffer))) {
                output.write(buffer, 0, n);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * ディレクトリ作成処理
     */
    public static boolean mkdir_p(File dir) throws IOException {
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new IOException("File.mkdirs() failed.");
            }
            return true;
        } else if (!dir.isDirectory()) {
            throw new IOException("Cannot create path. " + dir.toString()
                    + " already exists and is not a directory.");
        } else {
            return false;
        }
    }

    public static boolean mkdir_p(String dir) throws IOException {
        return mkdir_p(new File(dir));
    }

    public static Uri getDefaultImage(Context context, String fileName) {

        try {
            // Create dest file
            String path = getExternalPath();
            File folder = new File(path + Constant.SAVING_DEFAULT_PHOTO_FOLDER);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            // Copy assets file into SDCard
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = context.getAssets().open(Constant.SHARED_PHOTO_ASSETS_FOLDER + fileName);
                outputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                return Uri.fromFile(file);
            } catch (IOException e) {
                YgoLog.e("FileUtil", "Exception when copping assets file...", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                    }
                }
            }
        } catch (Exception e) {
            YgoLog.e("FileUtil", "Exception when getting default image...", e);
        }
        return null;
    }

    /**
     * Get file path
     *
     * @return
     */
    public static String getExternalPath() {

        String path = null;
        boolean isMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (isMounted) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            YgoLog.e("FileUtil", "SDCard is unmounted!");
        }
        return path;
    }

}
