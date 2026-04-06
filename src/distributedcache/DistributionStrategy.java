package distributedcache;

public interface DistributionStrategy<K> {
    int selectNodeIndex(K key, int numberOfNodes);
}
