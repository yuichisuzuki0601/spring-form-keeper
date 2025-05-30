package jp.co.ysd.spring_form_keeper.storage;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class NoneStorage extends AbstractStorage {

    @Override
    public String name() {
        return "none";
    }

    @Override
    public String load(@NonNull String id, @NonNull String viewName) {
        return "{}";
    }

    @Override
    public void persist(@NonNull String id, @NonNull String viewName, @NonNull String name, @NonNull String value) {
    }

    @Override
    public void delete(@NonNull String id, @Nullable String viewName, @Nullable String name) {
    }

}
