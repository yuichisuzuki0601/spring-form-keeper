package jp.co.ysd.spring_form_keeper.storage;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class H2Storage extends AbstractStorage {

    @Override
    public String name() {
        return "h2";
    }

    @Override
    public String load(@NonNull String id, @NonNull String viewName) {
        // TODO
        return "{}";
    }

    @Override
    public void persist(@NonNull String id, @NonNull String viewName, @NonNull String name, @NonNull String value) {
        // TODO
    }

    @Override
    public void delete(@NonNull String id, @Nullable String viewName, @Nullable String name) {
        // TODO
    }

}
