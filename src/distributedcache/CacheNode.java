package distributedcache;

import java.util.Optional;

public interface CacheNode<K, V> {
    Optional<V> get(K key);
    void put(K key, V value);
}
