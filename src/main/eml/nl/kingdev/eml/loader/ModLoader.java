package nl.kingdev.eml.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nl.kingdev.eml.eventapi.EventManager;
import nl.kingdev.eml.log.Logger;
import nl.kingdev.eml.mod.EMLModID;
import nl.kingdev.eml.mod.EMLModInfo;
import nl.kingdev.eml.mod.EMLModInstance;
import nl.kingdev.eml.mod.ModInfo;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

    ArrayList<URL> urls = new ArrayList<>();

    public void addToClassPath() {


        modJarsToLoad.forEach(file -> {
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

        urlClassLoader = new URLClassLoader(urls.toArray(new URL[urls.size()]), getClass().getClassLoader());

    }

    public ModInfo readModInfo(File modFile) {

        System.out.println("modFile = [" + modFile .getAbsolutePath()+ "]");
        try {
            ZipFile zipFile = new ZipFile(modFile);
            ZipEntry infoEntry = zipFile.getEntry("modinfo.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(infoEntry)));
            StringBuilder jsonString = new StringBuilder();
            reader.lines().forEach(jsonString::append);

            ModInfo modInfo = gson.fromJson(jsonString.toString(), ModInfo.class);

            zipFile.close();

            return modInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void initMods() {

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        configurationBuilder.addClassLoader(urlClassLoader);
        configurationBuilder.addUrls(urls.toArray(new URL[urls.size()]));


        Reflections r = new Reflections(configurationBuilder);

        Set<Class<?>> typesAnnotatedWith = r.getTypesAnnotatedWith(EMLModID.class);

        for (Class c : typesAnnotatedWith) {
            try {
                Object modInstance = c.newInstance();

                for (Field f : c.getDeclaredFields()) {
                    if (f.isAnnotationPresent(EMLModInstance.class)) {
                        logger.logInfo("Setting mod instance.");
                        f.set(null, modInstance);
                    }
                    if (f.isAnnotationPresent(EMLModInfo.class)) {
                        String url = c.getProtectionDomain().getCodeSource().getLocation().getFile();
                        File modJar = new File(url.replaceFirst("/", ""));
                        ModInfo modInfo = readModInfo(modJar);
                        System.out.println(modInfo.name);
                        f.set(modInstance, modInfo);

                        logger.logInfo("Setting mod info.");
                    }



                }
                mods.add(modInstance);
                EventManager.register(modInstance);
                logger.logInfo("Initialized mod " + c.getName());


            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

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
