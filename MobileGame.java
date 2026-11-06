public class MobileGame extends Game {
    private boolean hasInAppPurchases;
    private double downloadSizeMB;

    public MobileGame(int id, String title, double price, double rating, boolean hasInAppPurchases, double downloadSizeMB) {
        super(id, title, price, rating);
        this.hasInAppPurchases = hasInAppPurchases;
        this.downloadSizeMB = downloadSizeMB;
    }
    public void watchAdForRewards() {
        System.out.println(">>> Watching a 30-second unskippable ad in " + getTitle() + "...");
        System.out.println(">>> Reward claimed: +100 Gold Coins!");
    }

    @Override
    public String getPlatform() {
        return "MOBILE";
    }

    @Override
    public void play() {
        System.out.println("Starting Mobile app: " + getTitle());
    }

    @Override
    public void update() {
        System.out.println("App Store / Google Play updating: " + getTitle());
    }

    @Override
    public String toString() {
        String iap = hasInAppPurchases ? "Yes" : "No";
        return super.toString() + String.format(" | Size: %.1fMB | IAP: %s", downloadSizeMB, iap);
    }
}