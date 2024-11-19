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

    //getter functions
    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public int getLowStockAlert() {
        return lowStockAlert;
    }

    public int getRequestQuantity() {
        return requestQuantity;
    }

    public status getStatus1() {
        return status1;
    }


    //setter functions
    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setLowStockAlert(int lowStockAlert) {
        this.lowStockAlert = lowStockAlert;
    }

    public void setRequestQuantity(int requestQuantity) {
        this.requestQuantity = requestQuantity;
    }

    public void setStatus1(status status1) {
        this.status1 = status1;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Medicines{" +
                "name='" + name + '\'' +
                ", stock=" + stock +
                ", lowStockAlert=" + lowStockAlert +
                ", requestQuantity=" + requestQuantity +
                ", status1=" + status1 +
                '}';
    }

}
