package distributedcache;

public class ModuloDistributionStrategy<K> implements DistributionStrategy<K> {
    @Override
    public int selectNodeIndex(K key, int numberOfNodes) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("numberOfNodes must be greater than 0");
        }
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        return Math.floorMod(key.hashCode(), numberOfNodes);
    }
}
