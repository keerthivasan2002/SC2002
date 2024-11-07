public class Medicines {
    String name;
    int stock;
    int lowStockAlert;

    public Medicines(String name, int stock, int lowStockAlert){
        this.name = name;
        this.lowStockAlert = lowStockAlert;
        this.stock = stock;
    }
}
