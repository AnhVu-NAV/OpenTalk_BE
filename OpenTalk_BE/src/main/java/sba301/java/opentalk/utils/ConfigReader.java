package sba301.java.opentalk.utils;

import java.io.*;
import java.util.Properties;

public class ConfigReader {
    private final Properties props = new Properties();
    private final File file;

    public ConfigReader() {
        String activeProfile = System.getProperty("spring.profiles.active");
        if (activeProfile == null || activeProfile.trim().isEmpty()) {
            throw new RuntimeException("Environment variable 'spring.profiles.active' is not set or empty.");
        }
        String applicationPropertiesPath = "src/main/resources/application-" + activeProfile + ".properties";

        file = new File(applicationPropertiesPath);
        if (!file.exists()) {
            throw new RuntimeException("Properties file not found: " + applicationPropertiesPath);
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties file: " + applicationPropertiesPath, e);
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
            props.setProperty(key, defaultValue);
            saveProperties();
        }
        return value;
    }

    private void saveProperties() {
        try (OutputStream outputStream = new FileOutputStream(file)) {
            props.store(outputStream, "Updated missing or empty properties");
            System.out.println("Properties updated in: " + file.getPath());
        } catch (IOException e) {
            throw new RuntimeException("Error updating properties file: " + file.getPath(), e);
        }
    }

    public static void main(String[] args) {
        try {
            ConfigReader configReader = new ConfigReader();

            String username = configReader.getProperty("api.account.username", "default-username");
            System.out.println("Account Username: " + username);

            String password = configReader.getProperty("api.account.password", "default-password");
            System.out.println("Account Password: " + password);

            String isEnable = configReader.getProperty("api.account.enable", "true");
            System.out.println("Account is enable: " + isEnable);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
