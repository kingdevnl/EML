package nl.kingdev.eml.natives;

import nl.kingdev.eml.log.Logger;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class NativeExtractor {
    public static File nativesDir = new File("natives/");
    private static Logger logger = new Logger(NativeExtractor.class);


    public static void extract(File jar) {
        try {

            System.out.println("JarName "+jar.getName());

            if (!jar.getName().contains(".jar")) {
                return;
            }
            logger.logInfo("Starting native extraction.");
            ZipFile zipFile = new ZipFile(jar);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();


            if (nativesDir.exists()) {
                nativesDir.delete();
            }
            nativesDir.mkdir();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.endsWith(".dll") || name.endsWith(".so")) {
                    byte[] data = getBytesFromInputStream(zipFile.getInputStream(entry));

                    FileOutputStream fos = new FileOutputStream(new File(nativesDir, entry.getName()));
                    fos.write(data);
                    fos.flush();
                    fos.close();
                    logger.logInfo("Extracted native " + name);

                }

            }

            zipFile.close();
            logger.logInfo("Finished native extraction.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer))
            os.write(buffer, 0, len);

        return os.toByteArray();
    }
}
