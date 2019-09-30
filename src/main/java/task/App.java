package task;

public class App 
{
    public static void main( String[] args )
    {
        Company company = Company.createCompany();
        company.doRefresh();
    }
}
