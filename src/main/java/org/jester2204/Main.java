package org.jester2204;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String CONFIG_PROPERTIES_FILE = "config.properties";

    public static void main(String[] args) {
        logger.info("Додаток запущено.");

        Properties properties = getProperties();
        if (properties == null) return;

        logger.info("Конфігурація успішно завантажена");
        String csvFilePath = properties.getProperty("csvFilePath");
        String dbUrl = properties.getProperty("dbUrl");
        String user = properties.getProperty("dbUser");
        String password = properties.getProperty("dbPassword");

        try {
            logger.info("Розпочато процес завантаження данних");
            Connection connection = DriverManager.getConnection(dbUrl, user, password);
            CSVLoader.loadCSV(csvFilePath, connection);
            logger.info("Дані успішно завантажені.");
        } catch (Exception e) {
            logger.error("Помилка при завантаженні даних: " +  e.getMessage());
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
            if (input == null) {
                throw new Exception("Не вдалося знайти файл конфігурації " + CONFIG_PROPERTIES_FILE);
            }
            properties.load(input);
        } catch (Exception e) {
            logger.error("Помилка при завантаженні конфігурації: " + e.getMessage());
            return null;
        }
        return properties;
    }
}
