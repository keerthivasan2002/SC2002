public class InvalidPositiveOptionException extends Exception
{
    //construct
    public InvalidPositiveOptionException(){
        super("Invalid Option, Please enter option between 1 to 9. ");
    }

    // public IntNonNegativeException(String meassage){
    //     super(message);
    // }
}