package jp.co.ysd.spring_form_keeper.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jp.co.ysd.spring_form_keeper.storage.AbstractStorage;

@RestController
@RequestMapping("form-keeper")
public class FormKeeperController {

    @Value("${spring.form-keeper.storage}")
    private String storageType;

    @Autowired
    private List<AbstractStorage> storages;

    private AbstractStorage getStorage() {
        return storages.stream().filter(storage -> storage.name().equals(storageType)).findAny().orElseThrow();
    }

    @GetMapping
    public String get(@RequestParam("id") String id, @RequestParam("viewName") String viewName) throws Exception {
        return getStorage().load(id, viewName);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void post(@RequestBody Map<String, String> data) {
        var id = data.get("id");
        var viewName = data.get("viewName");
        var name = data.get("name");
        var value = data.get("value");
        getStorage().persist(id, viewName, name, value);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam(value = "id", required = true) String id,
            @RequestParam(value = "viewName", required = false) String viewName,
            @RequestParam(value = "name", required = false) String name) {
        getStorage().delete(id, viewName, name);
    }

}
