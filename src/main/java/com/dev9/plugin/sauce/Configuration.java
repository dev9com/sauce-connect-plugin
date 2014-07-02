package com.dev9.plugin.sauce;

/**
 * Tries to load a configuration value. You can pass in a configuration value
 * from either a Java system property or an environment property. Java
 * properties have priority over environment properties.
 * <p/>
 * Note that system property values that contain a $ in the result are ignored.
 * This is to work around an issue with Maven, where non-existent environment
 * values are passed in as literals.
 * <p/>
 * You can also pass in the text string "null" as an override mechanism (because
 * there is no "unset" option to completely null out an already set value in
 * Maven)
 */
public class Configuration {

    private Configuration() throws IllegalAccessException {
        throw new IllegalAccessException("Utility class should not be constructed");
    }

    public static String getValue(String key) {
        String value = null;
        if (System.getProperties().containsKey(key)) {
            value = System.getProperty(key);
        } else if (System.getenv().containsKey(key)) {
            value = System.getenv(key);
        }
        return value;
    }

}
