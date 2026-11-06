public class PCGame extends Game {
    private int minRamGB;
    private double storageGB;

    public PCGame(int id, String title, double price, double rating, int minRamGB, double storageGB) {
        super(id, title, price, rating);
        this.minRamGB = minRamGB;
        this.storageGB = storageGB;
    }

    @Override
    public String getPlatform() {
        return "PC";
    }

    @Override
    public void play() {
        System.out.println("Starting PC session: " + getTitle());
    }

    @Override
    public void update() {
        System.out.println("Steam client updating: " + getTitle());
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | RAM: %dGB | HDD: %.1fGB", minRamGB, storageGB);
    }
}
