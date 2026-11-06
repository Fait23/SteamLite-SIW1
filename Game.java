import java.util.Objects;

public abstract class Game implements Comparable<Game>, Playable, Updatable {
    private int id;
    private String title;
    private double price;
    private double rating;

    public Game(int id, String title, double price, double rating) {
        this.id = id;
        this.title = title;
        setPrice(price);
        setRating(rating);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }

    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        }
    }

    public void applyDiscount(double percentage) {
        if (percentage > 0 && percentage <= 100) {
            this.price -= this.price * (percentage / 100.0);
        }
    }

    public void setRating(double rating) {
        if (rating >= 0 && rating <= 10) {
            this.rating = rating;
        }
    }

    public abstract String getPlatform();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Game other) {
        if (this.title == null || other.title == null) return 0;
        return this.title.compareTo(other.title);
    }

    @Override
    public String toString() {
        return String.format("[%-7s] ID:%03d | %-16s | Price: $%5.2f | Rating: %4.1f/10",
                getPlatform(), id, title, price, rating);
    }
}