import java.util.*;


public class GameEngine {
    private Set<Game> gameRepository = new HashSet<>();
    private Map<Integer, Game> idIndex = new HashMap<>();
    private Map<String, List<Game>> gamesByCategory = new HashMap<>();

    public void addGame(String category, Game game) {
        if (game == null || category == null || category.trim().isEmpty()) return;

        gameRepository.add(game);
        idIndex.put(game.getId(), game);
        gamesByCategory.computeIfAbsent(category, k -> new ArrayList<>()).add(game);
    }

    public boolean removeGame(int gameId) {
        Game game = idIndex.remove(gameId);
        if (game != null) {
            gameRepository.remove(game);

            Iterator<Map.Entry<String, List<Game>>> iterator =
                    gamesByCategory.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<Game>> entry = iterator.next();
                List<Game> categoryList = entry.getValue();
                categoryList.remove(game);
                if (categoryList.isEmpty()) {
                    iterator.remove();
                }
            }
            return true;
        }
        return false;
    }

    private int totalGamesSold = 0;
    private double totalRevenue = 0.0;
    public void recordSale(double price) {
        totalGamesSold++;
        totalRevenue += price;
    }

    public boolean updateGamePrice(int gameId, double newPrice) {
        if (newPrice < 0) return false;
        Game game = idIndex.get(gameId);
        if (game != null) {
            game.setPrice(newPrice);
            return true;
        }
        return false;
    }

    public Game getGameById(int id) {
        return idIndex.get(id);
    }

    public List<Game> getGamesByCategory(String category) {
        return gamesByCategory.getOrDefault(category, new ArrayList<>());
    }

    public List<Game> getGamesSortedByPrice() {
        List<Game> sorted = new ArrayList<>(gameRepository);
        sorted.sort(Comparator.comparingDouble(Game::getPrice));
        return sorted;
    }

    public List<Game> getGamesSortedByRatingDesc() {
        List<Game> sorted = new ArrayList<>(gameRepository);
        sorted.sort(Comparator.comparingDouble(Game::getRating).reversed());
        return sorted;
    }

    public List<Game> getGamesSortedByPriceAndRating() {
        List<Game> sorted = new ArrayList<>(gameRepository);
        sorted.sort(Comparator.comparingDouble(Game::getPrice)
                .thenComparing(Comparator.comparingDouble(Game::getRating).reversed()));
        return sorted;
    }

    public List<Game> filterFreeGames() {
        List<Game> freeGames = new ArrayList<>();
        for (Game g : gameRepository) {
            if (g.getPrice() == 0.0) {
                freeGames.add(g);
            }
        }
        return freeGames;
    }

    public void printStoreStatistics() {
        System.out.println("\n========== STORE STATISTICS ==========");
        System.out.println("Total games in store : " + gameRepository.size());
        System.out.println("Total games sold     : " + totalGamesSold);
        System.out.println("Total revenue earned : $" + totalRevenue);

        if (gamesByCategory.isEmpty()) {
            System.out.println("Available genres     : None");
        } else {
            System.out.println("Available genres     : " + String.join(", ", gamesByCategory.keySet()));
        }

        double totalValue = 0;
        for (Game g : gameRepository) {
            totalValue += g.getPrice();
        }
        System.out.println("Total catalog value  : $" + totalValue);
        System.out.println("=========================================\n");
    }
}