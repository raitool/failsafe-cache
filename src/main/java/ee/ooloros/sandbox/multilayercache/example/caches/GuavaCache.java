package ee.ooloros.sandbox.multilayercache.example.caches;

import org.springframework.stereotype.Component;

import ee.ooloros.sandbox.multilayercache.common.caches.BaseGuavaCache;

@Component
public class GuavaCache extends BaseGuavaCache<String, String> {

}
