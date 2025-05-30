package jp.co.ysd.spring_form_keeper.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InMemoryStorage extends AbstractStorage {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryStorage.class);

    private static final Map<String, Map<String, Map<String, String>>> memory = new ConcurrentHashMap<>();

    @Override
    public String name() {
        return "in-memory";
    }

    @Override
    public String load(@NonNull String id, @NonNull String viewName) throws JsonProcessingException {
        var view = memory
                .getOrDefault(id, new ConcurrentHashMap<>())
                .getOrDefault(viewName, new ConcurrentHashMap<>());
        var res = new ObjectMapper().writeValueAsString(view.isEmpty() ? new ConcurrentHashMap<>() : view);
        logger.debug(res);
        return res;
    }

    @Override
    public void persist(@NonNull String id, @NonNull String viewName, @NonNull String name, @NonNull String value) {
        memory
                .computeIfAbsent(id, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(viewName, k -> new ConcurrentHashMap<>())
                .put(name, value);
        logger.debug(memory.toString());
    }

    @Override
    public void delete(@NonNull String id, @Nullable String viewName, @Nullable String name) {
        // idのみ指定
        if (viewName == null) {
            memory.remove(id);
            logger.debug(memory.toString());
            return;
        }
        var views = memory.get(id);
        // idとviewNameを指定
        if (name == null) {
            if (views != null) {
                views.remove(viewName);
                if (views.isEmpty()) {
                    memory.remove(id);
                }
            }
            logger.debug(memory.toString());
            return;
        }
        // idとviewNameとnameを指定
        if (views != null) {
            var view = views.get(viewName);
            if (view != null) {
                view.remove(name);
                if (view.isEmpty()) {
                    views.remove(viewName);
                }
                if (views.isEmpty()) {
                    memory.remove(id);
                }
            }
        }
        logger.debug(memory.toString());
    }

}
