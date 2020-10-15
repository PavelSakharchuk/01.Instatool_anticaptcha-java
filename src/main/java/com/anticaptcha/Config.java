package com.anticaptcha;

import lombok.Getter;
import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;


@Getter
@Resource.Classpath({Constant.Settings.ANTICAPTCHA_PROPERTIES})
public enum Config {
    INSTANCE;

    @Property("key")
    private String key;

    Config() {
        PropertyLoader.populate(this);
    }
}

