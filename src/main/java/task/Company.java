package task;

public class Company {
    private String myCompanyName;
    private String myCompanyOwner;
    private Address address;

    public void doRefresh() {
        
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