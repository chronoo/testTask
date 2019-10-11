package task;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

import org.json.JSONObject;

/**
 * Класс, заполняющий объект произвольного класса настройками из файла properties
 */
public class PropertyManager {
    private static Logger log = Logger.getLogger(PropertyManager.class.getName());

    /**
     * Заполняет параметры объекта
     * @param object - заполняемый объект 
     * @param pathToProperty - путь к файлу параметров (properties)
     */
    public static void fillObject(Object object, String pathToProperty) {
        Properties parameters = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(pathToProperty);
            parameters.load(inputStream);

            Class<?> myClass = object.getClass();
            Field[] fields = myClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Property.class)) {
                    Property prop = field.getAnnotation(Property.class);
                    String propertyName = prop.propertyName();
                    String defaultValue = prop.defaultValue();
                    Object propertyValue = null;

                    if (defaultValue.isEmpty()) {
                        propertyValue = parameters.getProperty(propertyName);
                    } else {
                        propertyValue = defaultValue;
                    }

                    if(propertyValue != null){
                        propertyValue = getFieldFromJSON(field, propertyValue.toString());
                    }

                    String fieldName = field.getName();
                    String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    try {
                        Method setter = myClass.getDeclaredMethod(setterName, field.getType());
                        setter.invoke(object, propertyValue);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Method call Exception", e);
                        e.printStackTrace();        
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File not found exception", e);
            e.printStackTrace();        
        } catch (IOException e) {
            log.log(Level.SEVERE, "IO Exception", e);
            e.printStackTrace();
        }
    }

    private static Object getFieldFromJSON(Field field, String property) {
        Object propertyValue = property;
        Class<?> fieldType = field.getType();
        if (!(fieldType == String.class || fieldType == Integer.class || fieldType == Double.class)) {
            Map<String, Object> jsonParameters = new JSONObject(property).toMap();
            try {
                propertyValue = fieldType.newInstance();

                for (Map.Entry<String, Object> jsonParameter : jsonParameters.entrySet()) {
                    String parameterFieldName = jsonParameter.getKey();
                    try {
                        Field parameterField = fieldType.getDeclaredField(parameterFieldName);
                        Object parValue = jsonParameter.getValue();

                        String objSetterName = "set" + parameterFieldName.substring(0, 1).toUpperCase()
                                + parameterFieldName.substring(1);
                        Method parameterSetter;
                        try {
                            parameterSetter = fieldType.getDeclaredMethod(objSetterName, parameterField.getType());
                            parameterSetter.invoke(propertyValue, parValue);
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "Method call Exception", e);
                            e.printStackTrace();
                        }
                    } catch (NoSuchFieldException e) {
                        log.log(Level.SEVERE, "No Such Field Exception", e);
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "Instantiation Exception", e);
                e.printStackTrace();
            }
        }    
        return propertyValue;
    }
}