package distributedcache;

import java.util.ArrayList;
import java.util.List;

public class DistributedCache<K, V> {
    private final List<CacheNode<K, V>> nodes;
    private final DistributionStrategy<K> distributionStrategy;
    private final Database<K, V> database;

    public DistributedCache(
            int numberOfNodes,
            int nodeCapacity,
            DistributionStrategy<K> distributionStrategy,
            EvictionPolicyFactory<K> evictionPolicyFactory,
            Database<K, V> database) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("numberOfNodes must be greater than 0");
        }
        if (distributionStrategy == null) {
            throw new IllegalArgumentException("distributionStrategy cannot be null");
        }
        if (evictionPolicyFactory == null) {
            throw new IllegalArgumentException("evictionPolicyFactory cannot be null");
        }
        if (database == null) {
            throw new IllegalArgumentException("database cannot be null");
        }

        this.distributionStrategy = distributionStrategy;
        this.database = database;
        this.nodes = new ArrayList<>(numberOfNodes);

        for (int i = 0; i < numberOfNodes; i++) {
            EvictionPolicy<K> evictionPolicy = evictionPolicyFactory.create();
            nodes.add(new InMemoryCacheNode<>(nodeCapacity, evictionPolicy));
        }
    }

    public V get(K key) {
        CacheNode<K, V> node = resolveNode(key);
        return node.get(key)
                .orElseGet(() -> {
                    V valueFromDatabase = database.get(key);
                    if (valueFromDatabase != null) {
                        node.put(key, valueFromDatabase);
                    }
                    return valueFromDatabase;
                });
    }

    public void put(K key, V value) {
        CacheNode<K, V> node = resolveNode(key);
        node.put(key, value);
    }

    private CacheNode<K, V> resolveNode(K key) {
        int index = distributionStrategy.selectNodeIndex(key, nodes.size());
        return nodes.get(index);
    }
}
