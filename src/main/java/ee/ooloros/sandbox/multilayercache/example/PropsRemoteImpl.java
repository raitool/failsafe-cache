package ee.ooloros.sandbox.multilayercache.example;

import org.springframework.stereotype.Component;

@Component
public class PropsRemoteImpl {
    public String getValue(String key) {
        throw new UnsupportedOperationException("mocked");
    }
}
