package com.andreev.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {
    private static final Properties properties = new Properties();

    static{
        loadProperty();
    }

    private PropertiesUtil(){}

    public static void loadProperty(){
        try(InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")){
            properties.load(stream);
        }
        catch(IOException exc){
            throw new RuntimeException(exc);
        }
    }

    public static String get(String key){
        return properties.getProperty(key);
    }


}
