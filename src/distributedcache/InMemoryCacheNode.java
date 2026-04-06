package distributedcache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryCacheNode<K, V> implements CacheNode<K, V> {
    private final int capacity;
    private final EvictionPolicy<K> evictionPolicy;
    private final Map<K, V> values;

    public InMemoryCacheNode(int capacity, EvictionPolicy<K> evictionPolicy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than 0");
        }
        if (evictionPolicy == null) {
            throw new IllegalArgumentException("evictionPolicy cannot be null");
        }
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
        this.values = new HashMap<>();
    }

    @Override
    public Optional<V> get(K key) {
        V value = values.get(key);
        if (value == null) {
            return Optional.empty();
        }
        evictionPolicy.recordAccess(key);
        return Optional.of(value);
    }

    @Override
    public void put(K key, V value) {
        if (values.containsKey(key)) {
            values.put(key, value);
            evictionPolicy.recordAccess(key);
            return;
        }

        if (values.size() >= capacity) {
            K keyToEvict = evictionPolicy.selectEvictionCandidate();
            if (keyToEvict != null) {
                values.remove(keyToEvict);
                evictionPolicy.recordRemoval(keyToEvict);
            }
        }

        values.put(key, value);
        evictionPolicy.recordInsertion(key);
    }
}
