package distributedcache;

public interface EvictionPolicyFactory<K> {
    EvictionPolicy<K> create();
}
