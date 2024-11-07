import java.util.Scanner;

public class PharmacistUI {
    private String userID;
    Staff pharmacist;
    StaffManager sm;
    private MedicineInventory medicines;

    Scanner sc = new Scanner(System.in);

    public PharmacistUI(String userID, StaffManager sm){
        this.userID = userID;
        this.sm = sm;
        this.pharmacist = sm.selectStaff(userID);
        medicines = new MedicineInventory();

        if(this.pharmacist == null){
            System.out.println("No pharmacists found with the given ID: " + userID);
        }else{
            pharmacistOption();
        }
    }

    public void pharmacistOption(){
        int choice = 0;
        System.out.println("Hello" + pharmacist.getName() + ".");
        System.out.println("What would you like to do today?");

        while(choice < 6){  //change accordingly
            pharmacistMenu();
            choice = sc.nextInt();
            switch (choice){
                case 1: //view appointment outcome records
                    break;
                case 2: //update prescription status
                    break;
                case 3: //view medical inventory
                    medicines.viewMedicalInventory();
                    break;
                case 4: //submit replenishment request
                    break;
                case 5: //log out
                    System.out.println("Thank you! Hope to see you soon :)\n");
                    System.exit(0);
                    return;
            }
            System.out.println("What else would you like to do today?");
        }
    }

    private void pharmacistMenu(){
        System.out.println("-----------------------------------");
        System.out.println("Pharmacist Menu:");
        System.out.println("1. View Appointment Outcome Records");
        System.out.println("2. Update Prescription Status");
        System.out.println("3. View Medical Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.println("-----------------------------------");
        System.out.print("Enter your choice: ");
    }

}
