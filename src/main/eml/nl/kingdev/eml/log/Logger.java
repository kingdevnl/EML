package nl.kingdev.eml.log;

public class Logger {

    private Class aClass;


    public Logger(Class c) {
        this.aClass = c;
    }
    public void logInfo(Object o) {
        System.out.println("[EML] ["+aClass.getName()+"] > "+String.valueOf(o));

    }

    public void logError(String o) {
        System.err.println("[EML] ["+aClass.getName()+"] > "+String.valueOf(o));
    }
}
