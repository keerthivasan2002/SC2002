public class Medicines {
    String name;
    int stock;
    int lowStockAlert;
    int requestQuantity;
    enum status{
        NIL, TOREQUEST, PENDING
    }
    status status1;

    public Medicines(String name, int stock, int lowStockAlert, status status1, int requestQuantity){
        this.name = name;
        this.lowStockAlert = lowStockAlert;
        this.stock = stock;
        this.requestQuantity = requestQuantity;
        this.status1 = status1;
    }

    public String[] toArray() {
        return new String[]{
            name,
            String.valueOf(stock),
            String.valueOf(lowStockAlert),
            String.valueOf(status1),
            String.valueOf(requestQuantity)
        };
    }
}
