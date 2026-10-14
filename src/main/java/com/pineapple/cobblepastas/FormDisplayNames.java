package com.pineapple.cobblepastas;

import net.minecraft.class_2561;
import net.minecraft.class_5250;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/** Shared translated names for selected species and aspect combinations. */
public final class FormDisplayNames {
    private static final Properties NAMES = load();
    private FormDisplayNames() {}

    private static Properties load() {
        Properties names = new Properties();
        try (InputStream stream = FormDisplayNames.class.getResourceAsStream("/assets/cobblepastas/form_names.properties")) {
            if (stream == null) throw new IllegalStateException("Missing form_names.properties");
            names.load(stream);
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(exception);
        }
        return names;
    }

    public static boolean manages(String speciesId) {
        return NAMES.stringPropertyNames().stream().anyMatch(key -> key.equals(speciesId) || key.startsWith(speciesId + "/"));
    }

    public static String translationKey(String speciesId, Set<String> aspects) {
        String selected = NAMES.getProperty(speciesId);
        int specificity = 0;
        for (String key : NAMES.stringPropertyNames()) {
            if (!key.startsWith(speciesId + "/")) continue;
            String[] required = key.substring(speciesId.length() + 1).split(",");
            boolean matches = true;
            for (String aspect : required) if (!aspects.contains(aspect)) { matches = false; break; }
            if (matches && required.length > specificity) {
                selected = NAMES.getProperty(key);
                specificity = required.length;
            }
        }
        return selected;
    }

    public static class_5250 name(String speciesId, Set<String> aspects) {
        String key = translationKey(speciesId, aspects);
        return key == null ? null : class_2561.method_43471(key);
    }

    public static boolean isDefaultName(class_5250 displayed, class_5250 speciesName, String rawName) {
        return displayed.equals(speciesName) || rawName.equals(displayed.getString());
    }
}
