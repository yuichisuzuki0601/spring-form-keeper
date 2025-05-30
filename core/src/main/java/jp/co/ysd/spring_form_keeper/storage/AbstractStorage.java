package jp.co.ysd.spring_form_keeper.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public abstract class AbstractStorage {

    @Value("${spring.form-keeper.ttl}")
    private int ttl;

    public abstract String name();

    public abstract String load(@NonNull String id, @NonNull String viewName) throws Exception;

    public abstract void persist(@NonNull String id, @NonNull String viewName, @NonNull String name,
            @NonNull String value);

    public abstract void delete(@NonNull String id, @Nullable String viewName, @Nullable String name);

    protected int ttl() {
        return ttl;
    }

}
