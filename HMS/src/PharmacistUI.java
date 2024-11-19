import java.util.Scanner;

public class PharmacistUI implements UserUI{
    Staff pharmacist;
    StaffManager sm;
    private MedicineInventory mi;
    private ReplenishmentRequest request;
    private String userID;

    Scanner sc = new Scanner(System.in);
    OptionHandling oh = new OptionHandling();

    public PharmacistUI(String userID, StaffManager sm, MedicineInventory mi){
        this.userID = userID;
        this.sm = sm;
        this.pharmacist = sm.selectStaff(userID);
        this.mi = mi;

        if(this.pharmacist == null){
            System.out.println("No pharmacists found with the given ID: " + userID);
        }else{
            userOption();
        }
    }

    public void userOption(){
        int choice = 0;
        System.out.println("Hello " + pharmacist.getName() + ".");
        System.out.println("What would you like to do today?");

        while(true){  //change accordingly
            pharmacistMenu();
            choice = oh.getOption(1, 5);
            switch (choice){
                case 1: //view appointment outcome records
                
                    break;
                case 2: //update prescription status
                    mi.updateMedicalInventory();
                    mi.saveMedicines();
                    break;
                case 3: //view medical inventory
                    mi.viewMedicalInventory();
                    mi.saveMedicines();
                    break;
                case 4: //submit replenishment request
                    ReplenishmentRequest.submitReplenishmentRequest(mi);
                    mi.saveMedicines();
                    break;
                case 5: //log out
                    System.out.println("Thank you! Hope to see you soon :)\n");
                    System.exit(0);
                    return;
            }
            // System.out.println("What else would you like to do today?");
            // sc.nextLine();
        }
        

    }

    private void pharmacistMenu(){
        System.out.println("-----------------------------------");
        System.out.println("Pharmacist Menu:");
        System.out.println("1. View Appointment Outcome Records");
        System.out.println("2. Prescribe Medication");
        System.out.println("3. View Medical Inventory");
        System.out.println("4. Submit Replenishment Request");
        System.out.println("5. Logout");
        System.out.println("-----------------------------------");
    }

}
