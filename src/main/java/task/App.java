package task;

/**
 * Класс для проверки заполнения экземпляра класса Company
 */
public class App 
{
    public static void main( String[] args )
    {
        Company company = Company.getCompany();
        company.doRefresh();
    }
}
