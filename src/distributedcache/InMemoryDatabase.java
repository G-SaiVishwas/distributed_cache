package distributedcache;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDatabase<K, V> implements Database<K, V> {
    private final Map<K, V> values = new HashMap<>();

    @Override
    public V get(K key) {
        return values.get(key);
    }

    @Override
    public void put(K key, V value) {
        values.put(key, value);
    }
}
