public class ConsoleGame extends Game {
    private String consoleType;
    private boolean isPhysicalDisc;

    public ConsoleGame(int id, String title, double price, double rating, String consoleType, boolean isPhysicalDisc) {
        super(id, title, price, rating);
        this.consoleType = consoleType;
        this.isPhysicalDisc = isPhysicalDisc;
    }

    @Override
    public String getPlatform() {
        return "CONSOLE";
    }

    @Override
    public void play() {
        System.out.println("Starting Console session: " + getTitle());
    }

    @Override
    public void update() {
        System.out.println("PSN/Xbox Live updating: " + getTitle());
    }

    @Override
    public String toString() {
        String media = isPhysicalDisc ? "Blu-ray" : "Digital";
        return super.toString() + String.format(" | Console: %s | Media: %s", consoleType, media);
    }
}