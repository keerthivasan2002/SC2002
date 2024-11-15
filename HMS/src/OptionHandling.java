import java.util.Scanner;

public class OptionHandling {

    public int getOption(int min, int max){
        int option = 0; 
        boolean valid  =false;
        int limit = 0;
        Scanner sc = new Scanner(System.in);

        while (!valid){

            // Limit the number of invalid attempts
            if (limit > 2){
                System.out.println("Too many invalid attempts. Exiting program.");
                System.exit(0);

            }   
            

            try{                
                System.out.println("Enter your choice: ");
                option = sc.nextInt();
                // System.out.println("You entered: " + option);
                if (option < 0){
                    throw new IntNonNegativeException("Invalid input. Please enter a positive number.");
                }else if (option < min || option > max){
                    throw new InvalidPositiveOptionException("Invalid input. Please enter a number between " + min + " and " + max + ".");
                }else {
                    valid = true;
                    limit = 0;
                }
            }catch (InvalidPositiveOptionException e){
                System.out.println(e.getMessage());
                limit++;
            }catch (IntNonNegativeException e){
                System.out.println(e.getMessage());
                limit++;
            }catch (Exception e){
                System.out.println("Invalid input. Please enter a number.");
                limit++;
            }finally{
                sc.nextLine();
            }
        }
        return option;
    }
}