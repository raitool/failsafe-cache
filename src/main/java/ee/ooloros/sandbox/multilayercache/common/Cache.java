package ee.ooloros.sandbox.multilayercache.common;

public interface Cache<T,U> {

    U getValue(T key);

    void setValue(T key, U value);

}
