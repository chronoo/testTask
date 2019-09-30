package task;

import java.io.*;
import java.util.Properties;
import java.util.logging.*;

public class Company {
    public static String PATH_TO_PROPERTIES = "src/main/resources/mycompany.properties1";

    private static Logger log = Logger.getLogger(Company.class.getName());

    private String myCompanyName;
    private String myCompanyOwner;
    private Address address;
    private static Company company;

    private Company() {
    }

    public static Company createCompany() {
        if(company == null){
            company = new Company();
        }
        return company;
    }

    public void doRefresh() {
        Properties parameters = new Properties();

        try {
            FileInputStream inputStream = new FileInputStream(PATH_TO_PROPERTIES);
            parameters.load(inputStream);
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File not found exception", e);
            e.printStackTrace();
        } catch (IOException e) {
            log.log(Level.SEVERE, "IO Exception", e);
            e.printStackTrace();
        }
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