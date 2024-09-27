package ee.ooloros.sandbox.multilayercache.common;

public abstract class BaseCache<T, U> implements Cache<T, U> {

    public abstract U getValueIfPresent(T key);

}
