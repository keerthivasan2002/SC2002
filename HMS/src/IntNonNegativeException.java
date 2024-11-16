public class IntNonNegativeException extends Exception
{
    //construct
    public IntNonNegativeException(String message) {
        super(message);
    }

    public IntNonNegativeException(){
        System.out.println("Invalid value! Please enter a positive value.");
    }

}