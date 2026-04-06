package distributedcache;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class LruEvictionPolicy<K> implements EvictionPolicy<K> {
    private final Set<K> order = new LinkedHashSet<>();

    @Override
    public void recordAccess(K key) {
        order.remove(key);
        order.add(key);
    }

    @Override
    public void recordInsertion(K key) {
        order.remove(key);
        order.add(key);
    }

    @Override
    public void recordRemoval(K key) {
        order.remove(key);
    }

    @Override
    public K selectEvictionCandidate() {
        Iterator<K> iterator = order.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }
}
