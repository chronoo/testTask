package task;

/**
 * Класс компании
 */
public class Company {
    private static String PATH_TO_PROPERTIES = "src/main/resources/mycompany.properties";

    @Property(propertyName = "com.mycompany.name")
    private String myCompanyName;

    @Property(propertyName = "com.mycompany.owner", defaultValue = "I am owner.")
    private String myCompanyOwner;

    @Property(propertyName = "com.mycompany.address")
    private Address address;

    private static Company company;

    private Company() {
    }

    /** 
     * Создаёт (при первом вызове) и возвращает экземпляр класса
     * @return Company
     */
    public static synchronized Company getCompany() {
        if (company == null) {
            company = new Company();
        }
        return company;
    }

    /**
     * Заполняет поля экземпляра класса параметрами из файла properties
     */
    public void doRefresh() {
        Company myCompany = getCompany();
        PropertyManager.fillObject(myCompany, PATH_TO_PROPERTIES);
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