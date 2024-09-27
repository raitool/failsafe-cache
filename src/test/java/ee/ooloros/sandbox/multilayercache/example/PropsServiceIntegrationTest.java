package ee.ooloros.sandbox.multilayercache.example;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ee.ooloros.sandbox.multilayercache.common.caches.BaseGuavaCache;
import ee.ooloros.sandbox.multilayercache.example.caches.GuavaCache;
import ee.ooloros.sandbox.multilayercache.example.caches.RedisCache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class PropsServiceIntegrationTest {

    @MockBean
    private PropsRemoteImpl propsRemote;
    @Autowired
    private GuavaCache inMemoryCache;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private PropsService propsService;

    @Nested
    class InMemoryCacheFresh {

        @Test
        void shouldReturnL1Result() {
            var key = UUID.randomUUID().toString();
            inMemoryCache.setValue(key, "IN_MEMORY");

            var result = propsService.getValue(key);
            assertThat(result).isEqualTo("IN_MEMORY");
            Mockito.verifyNoInteractions(propsRemote);
        }
    }

    @Nested
    class InMemoryExpiredAndSourceIsUp {

        void setup(String key) throws InterruptedException {
            inMemoryCache.setValue(key, "IN_MEMORY");
            inMemoryCache.setValue(key, "REDIS");
            doReturn("SOURCE").when(propsRemote).getValue(key);
            Thread.sleep(BaseGuavaCache.DURATION_EXPIRATION);
        }

        @Test
        void shouldReturnFromSource() throws InterruptedException {
            var key = UUID.randomUUID().toString();
            setup(key);

            var result = propsService.getValue(key);
            assertThat(result).isEqualTo("SOURCE");
            verify(propsRemote, times(1)).getValue(key);
        }

        @Test
        void shouldUpdateInMemory() throws InterruptedException {
            var key = UUID.randomUUID().toString();
            setup(key);

            propsService.getValue(key);

            var result = inMemoryCache.getValue(key);
            assertThat(result).isEqualTo("SOURCE");
            verify(propsRemote, times(1)).getValue(key);
        }

        @Test
        void shouldUpdateRedis() throws InterruptedException {
            var key = UUID.randomUUID().toString();
            setup(key);

            propsService.getValue(key);

            var result = redisCache.getValue(key);
            assertThat(result).isEqualTo("SOURCE");
            verify(propsRemote, times(1)).getValue(key);
        }
    }

    @Nested
    class L1ExpiredAndSourceIsDown {
        void setup(String key) throws InterruptedException {
            inMemoryCache.setValue(key, "IN_MEMORY");
            doThrow(new IllegalStateException()).when(propsRemote).getValue(key);
            Thread.sleep(BaseGuavaCache.DURATION_EXPIRATION);
        }

        @Test
        void shouldFail() throws InterruptedException {
            var key = UUID.randomUUID().toString();
            setup(key);

            assertThatThrownBy(() -> propsService.getValue(key))
                    .isInstanceOf(IllegalStateException.class);
            verify(propsRemote, times(1)).getValue(key);
        }

        @Test
        void shouldReturnFromRedis() throws InterruptedException {
            var key = UUID.randomUUID().toString();
            redisCache.setValue(key, "REDIS");
            setup(key);

            var result = propsService.getValue(key);
            assertThat(result).isEqualTo("REDIS");
            verify(propsRemote, times(1)).getValue(key);
        }

    }

}