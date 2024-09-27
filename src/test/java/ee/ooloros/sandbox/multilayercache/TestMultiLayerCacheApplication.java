package ee.ooloros.sandbox.multilayercache;

import org.springframework.boot.SpringApplication;

public class TestMultiLayerCacheApplication {

    public static void main(String[] args) {
        SpringApplication.from(MultiLayerCacheApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
