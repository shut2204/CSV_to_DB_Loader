package org.jester2204;

import org.jester2204.model.Product;
import org.jester2204.model.ProductType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVLoader {
    private static final String SQL_QUERY = "INSERT INTO products (id, name, price, type) VALUES (?, ?, ?, ?)";

    public static void loadCSV(String csvFilePath, Connection connection) throws IOException, SQLException {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t"); // Тут ми використовуємо TAB як розділовий знак
                // Тут ми припускаємо, що значення для поля 'type' буде '0' або '1'
                ProductType type = "1".equals(values[3]) ? ProductType.WEIGHABLE : ProductType.UNITS;
                Product product = new Product(
                        Long.parseLong(values[0]),
                        values[1],
                        Double.parseDouble(values[2].replace(",", ".")), // Заміна коми на крапку для чисел з плаваючою крапкою
                        type
                );

                insertProduct(product, connection);
            }
        }
    }

    private static void insertProduct(Product product, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {
            preparedStatement.setLong(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setString(4, product.getType().getValue());
            preparedStatement.executeUpdate();
        }
    }
}
