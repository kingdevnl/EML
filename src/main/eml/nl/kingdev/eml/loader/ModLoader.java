package nl.kingdev.eml.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.kingdev.eml.log.Logger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;

public class ModLoader {

    private HashMap<String, Object> loadedMods;

    public static File modsDir = new File("mods");
    private ArrayList<Object> mods = new ArrayList<>();


    public Logger logger;
    private Gson gson = new GsonBuilder().create();

    private URLClassLoader urlClassLoader;

    public ModLoader() {
        loadedMods = new HashMap<>();
        logger = new Logger(getClass());
    }


    private ArrayList<File> modJarsToLoad = new ArrayList<>();

    //Scan for all jarfiles in the mods folder.
    public void scan() {
        logger.logInfo("Starting to scan for mod jars..");
        for (File f : modsDir.listFiles()) {
            //To be sure it's an .jar file
            if (!f.isDirectory() && f.getName().contains(".jar")) {
                logger.logInfo("Found jar file " + f.getName());
                modJarsToLoad.add(f);

            }
        }
    }

    public void addToClassPath() {
        ArrayList<URL> urlArrayList = new ArrayList<>();

        modJarsToLoad.forEach(file -> {
            try {
                urlArrayList.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        urlClassLoader = new URLClassLoader(urlArrayList.toArray(new URL[urlArrayList.size()]), getClass().getClassLoader());

    }


    //Get the bytes of an InputSteam

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[0xFFFF];
        for (int len = is.read(buffer); len != -1; len = is.read(buffer))
            os.write(buffer, 0, len);

        return os.toByteArray();
    }

}
