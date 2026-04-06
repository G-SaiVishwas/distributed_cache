package distributedcache;

public interface EvictionPolicy<K> {
    void recordAccess(K key);
    void recordInsertion(K key);
    void recordRemoval(K key);
    K selectEvictionCandidate();
}
