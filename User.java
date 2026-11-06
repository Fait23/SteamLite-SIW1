import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class User {
    private int userId;
    private String username;
    private List<Game> ownedGames;

    public User(int userId, String username) {
        this.userId = userId;
        this.username = username;
        this.ownedGames = new ArrayList<>();
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public List<Game> getOwnedGames() { return ownedGames; }

    public boolean buyGame(Game game) {
        if (game != null && !ownedGames.contains(game)) {
            ownedGames.add(game);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}