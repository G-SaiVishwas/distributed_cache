package distributedcache;

public class Main {
    public static void main(String[] args) {
        Database<String, String> database = new InMemoryDatabase<>();
        database.put("user:1", "Alice");
        database.put("user:2", "Bob");
        database.put("user:3", "Charlie");

        DistributedCache<String, String> cache = new DistributedCache<>(
                3,
                2,
                new ModuloDistributionStrategy<>(),
                LruEvictionPolicy::new,
                database
        );

        System.out.println(cache.get("user:1"));
        System.out.println(cache.get("user:1"));
        System.out.println(cache.get("user:2"));
        cache.put("user:4", "Diana");
        System.out.println(cache.get("user:4"));
    }
}
