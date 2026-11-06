import java.util.LinkedList;
import java.util.Queue;

public class AdminPanel {
    private Queue<Game> moderationQueue = new LinkedList<>();
    private GameEngine engine;

    public AdminPanel(GameEngine engine) {
        this.engine = engine;
    }

    public void submitGameForReview(Game game) {
        if (game != null) {
            moderationQueue.offer(game);
        }
    }

    public void processNextGameRequest(String category) {
        if (category == null || moderationQueue.isEmpty()) {
            System.out.println("[Moderation] Queue is empty. No games to process.");
            return;
        }
        Game game = moderationQueue.poll();
        if (game != null) {
            engine.addGame(category, game);
            System.out.println("[Admin] Approved & published: " + game.getTitle() + " | Genre: " + category);
        }
    }

    public void processAllRequests(String category) {
        if (moderationQueue.isEmpty()) {
            System.out.println("[Moderation] Queue is already empty.");
            return;
        }
        System.out.println("\n--- MASS MODERATION STARTED ---");
        while (!moderationQueue.isEmpty()) {
            processNextGameRequest(category);
        }
        System.out.println("--- MASS MODERATION COMPLETE ---\n");
    }
}