public class InvalidPositiveOptionException extends Exception
{
    //construct
    public InvalidPositiveOptionException(String message) {
        super(message);
    }

    public InvalidPositiveOptionException(){
        System.out.println("Invalid value! Please enter a within the range.");
    }

}