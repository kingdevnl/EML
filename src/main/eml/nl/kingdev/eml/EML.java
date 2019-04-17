package nl.kingdev.eml;

import nl.kingdev.eml.loader.ModLoader;
import nl.kingdev.eml.log.Logger;

import java.io.File;
import java.net.URISyntaxException;

public class EML {


    private static Logger logger = new Logger(EML.class);

    public static ModLoader modLoader = new ModLoader();

    public static void emlMain() {
        logger.logInfo("preMain");
        try {
           File ownJar  = new File(EML.class.getProtectionDomain().getCodeSource().getLocation()
                   .toURI());
           NativeExtractor.extract(ownJar);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        System.setProperty("org.lwjgl.librarypath", NativeExtractor.nativesDir.getAbsolutePath());

        if (!ModLoader.modsDir.exists()) {
            ModLoader.modsDir.mkdirs();
        }
        //Start scanning for mod jars
        modLoader.scan();
        //Add all jars from the mods Folder to the classpath.
        modLoader.addToClassPath();


    }


}
