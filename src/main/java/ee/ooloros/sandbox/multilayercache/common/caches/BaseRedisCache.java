
package ee.ooloros.sandbox.multilayercache.common.caches;

import java.time.Duration;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import ee.ooloros.sandbox.multilayercache.common.BaseCache;

public abstract class BaseRedisCache<T, U> extends BaseCache<T, U> {

    public static final Duration DURATION_EXPIRATION = Duration.ofSeconds(10);
    //FIXME: actually use redis
    private final Cache<T, U> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(DURATION_EXPIRATION)
            .build();

    @Override
    public U getValueIfPresent(T key) {
        return cache.getIfPresent(key);
    }

    @Override
    public U getValue(T key) {
        return getValueIfPresent(key);
    }

    @Override
    public void setValue(T key, U value) {
        cache.put(key, value);
    }
}
