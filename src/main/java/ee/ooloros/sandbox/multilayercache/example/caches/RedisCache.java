package ee.ooloros.sandbox.multilayercache.example.caches;

import org.springframework.stereotype.Component;

import ee.ooloros.sandbox.multilayercache.common.caches.BaseRedisCache;
import ee.ooloros.sandbox.multilayercache.example.PropsRemoteImpl;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisCache extends BaseRedisCache<String, String> {

    private final PropsRemoteImpl propsRemote;

}
