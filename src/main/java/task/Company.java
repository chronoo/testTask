package task;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore.Entry;
import java.util.Map;
import java.util.Properties;
import java.util.logging.*;

import org.json.JSONObject;

public class Company {
    public static String PATH_TO_PROPERTIES = "src/main/resources/mycompany.properties";

    private static Logger log = Logger.getLogger(Company.class.getName());

    @Property(propertyName = "com.mycompany.name")
    private String myCompanyName;

    @Property(propertyName = "com.mycompany.owner", defaultValue = "I am owner.")
    private String myCompanyOwner;

    @Property(propertyName = "com.mycompany.address")
    private Address address;

    private static Company company;

    private Company() {
    }

    public static Company createCompany() {
        if (company == null) {
            company = new Company();
        }
        return company;
    }

    public void doRefresh() {
        Properties parameters = new Properties();
        Company myCompany = createCompany();

        try {
            FileInputStream inputStream = new FileInputStream(PATH_TO_PROPERTIES);
            parameters.load(inputStream);

            Class<?> myClass = this.getClass();
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

                    propertyValue = getFieldFromJSON(field, propertyValue.toString());

                    String fieldName = field.getName();
                    String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

                    try {
                        Method setter = myClass.getDeclaredMethod(setterName, field.getType());
                        setter.invoke(myCompany, propertyValue);
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

    private Object getFieldFromJSON(Field field, String property) {
        Object propertyValue = null;
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

    public String getMyCompanyName() {
        return myCompanyName;
    }

    public void setMyCompanyName(String myCompanyName) {
        this.myCompanyName = myCompanyName;
    }

    public String getMyCompanyOwner() {
        return myCompanyOwner;
    }

    public void setMyCompanyOwner(String myCompanyOwner) {
        this.myCompanyOwner = myCompanyOwner;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}