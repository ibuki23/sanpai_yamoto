package aks.industrialwaste.recycle.ui.master;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipDecompresser {

    //ディレクトリ削除
    public static void directoryDelete(File f) {
        if (f.exists() == false) {
            return;
        }

        if (f.isFile()) {
            f.delete();
        }

        if (f.isDirectory()) {
            File[] files = f.listFiles();
            for (int i = 0; i < files.length; i++) {
                directoryDelete(files[i]);
            }
            f.delete();
        }
    }

    /**
     * Zip ファイルの解凍メソッド
     *
     * @param file 対象のZIPファイル
     * @throws ZipException
     * @throws IOException
     */
    public static void unzip(File file, File file2) throws ZipException, IOException {

        //ディレクトリは削除(フォルダが存在すると解答が失敗するため)
        directoryDelete(new File(file2.toString().replaceAll(".zip", "")));

        // 対象のZipファイルと同名のディレクトリを作成する。
        String fileName = file2.getName();
        int exindex = fileName.lastIndexOf(".");
        String baseDirName = fileName.substring(0, exindex);
        File baseDir = new File(file2.getParent(), baseDirName);
        if (!baseDir.mkdir()) {
            throw new FileNotFoundException(baseDir + "の生成に失敗しました。");
        }

        // Zip ファイルから ZipEntry を一つずつ取り出し、ファイルに保存していく。
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry ze = entries.nextElement();

            // 出力先ファイル
            File outFile = new File(baseDir, ze.getName());
            if (ze.isDirectory()) {
                // ZipEntry がディレクトリの場合はディレクトリを作成。
                outFile.mkdirs();
            } else {

                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    // ZipFile から 対象ZipEntry の InputStream を取り出す。
                    InputStream is = zipFile.getInputStream(ze);
                    // 効率よく読み込むため BufferedInputStream でラップする。
                    bis = new BufferedInputStream(is);

                    if (!outFile.getParentFile().exists()) {
                        // 出力先ファイルの保存先ディレクトリが存在しない場合は、
                        // ディレクトリを作成しておく。
                        outFile.getParentFile().mkdirs();
                    }

                    // 出力先 OutputStream を作成。
                    bos
                             = new BufferedOutputStream(new FileOutputStream(
                                      outFile));

                    // 入力ストリームから読み込み、出力ストリームへ書き込む。
                    int ava;
                    while ((ava = bis.available()) > 0) {
                        byte[] bs = new byte[ava];
                        // 入力
                        bis.read(bs);

                        // 出力
                        bos.write(bs);
                    }
                } catch (FileNotFoundException e) {
                    throw e;
                } catch (IOException e) {
                    throw e;
                } finally {
                    try {
                        if (bis != null) // ストリームは必ず close する。
                        {
                            bis.close();
                        }
                    } catch (IOException e) {
                    }
                    try {
                        if (bos != null) // ストリームは必ず close する。
                        {
                            bos.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
        }
    }
}
