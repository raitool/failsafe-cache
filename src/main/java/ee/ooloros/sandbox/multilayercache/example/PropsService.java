package ee.ooloros.sandbox.multilayercache.example;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ee.ooloros.sandbox.multilayercache.common.Cache;
import ee.ooloros.sandbox.multilayercache.example.caches.GuavaCache;
import ee.ooloros.sandbox.multilayercache.example.caches.RedisCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropsService implements Cache<String, String> {
    private final PropsRemoteImpl propsRemote;
    private final GuavaCache inMemoryCache;
    private final RedisCache redisCache;

    @Override
    public String getValue(String key) {
        log.info("Get value");
        return Optional.ofNullable(inMemoryCache.getValueIfPresent(key))
                .orElseGet(() -> getFromSourceFailsafe(key));
    }

    private String getFromSourceFailsafe(String key) {
        try {
            var value = propsRemote.getValue(key);
            inMemoryCache.setValue(key, value);
            redisCache.setValue(key, value);
            return value;
        } catch (RuntimeException e) {
            return Optional.ofNullable(redisCache.getValueIfPresent(key))
                    .orElseThrow(() -> e);
        }
    }

    @Override
    public void setValue(String key, String value) {
        log.info("Set key {} to {}", key, value);
        inMemoryCache.setValue(key, value);
    }
}
