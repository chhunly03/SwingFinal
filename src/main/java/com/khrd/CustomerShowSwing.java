package com.khrd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerShowSwing extends JFrame {
    private int currentId = -1;

    private final JTextField idField;
    private final JTextField lastNameField;
    private final JTextField firstNameField;
    private final JTextField phoneField;

    record Customer(int customerId, String lastName, String firstName, String phone) {
    }

    public CustomerShowSwing() {
        setTitle("Customer Information");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2));

        JLabel idLabel = new JLabel("Customer ID:");
        idField = new JTextField();
        idField.setEditable(false);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField();
        lastNameField.setEditable(false);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField();
        firstNameField.setEditable(false);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        phoneField.setEditable(false);

        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousRecord();
            }
        });

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextRecord();
            }
        });

        add(idLabel);
        add(idField);
        add(lastNameLabel);
        add(lastNameField);
        add(firstNameLabel);
        add(firstNameField);
        add(phoneLabel);
        add(phoneField);
        add(previousButton);
        add(nextButton);

        showNextRecord();

        setVisible(true);
    }

    private Customer getCustomer(int id, boolean isNext) {
        Customer customer = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String url = "jdbc:postgresql://localhost:5432/customer_db";
            String user = "postgres";
            String password = "1111@2222@";

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            if (isNext) {
                if (id == -1) {
                    statement = connection.prepareStatement("SELECT * FROM customers ORDER BY customer_id LIMIT 1");
                } else {
                    statement = connection.prepareStatement("SELECT * FROM customers WHERE customer_id > ? ORDER BY customer_id LIMIT 1");
                    statement.setInt(1, id);
                }
            } else {
                statement = connection.prepareStatement("SELECT * FROM customers WHERE customer_id < ? ORDER BY customer_id DESC LIMIT 1");
                statement.setInt(1, id);
            }

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int customerId = resultSet.getInt("customer_id");
                String lastName = resultSet.getString("customer_last_name");
                String firstName = resultSet.getString("customer_first_name");
                String phone = resultSet.getString("customer_phone");
                customer = new Customer(customerId, lastName, firstName, phone);
            }

        } catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return customer;
    }

    private void showRecord(Customer customer) {
        if (customer != null) {
            currentId = customer.customerId();
            idField.setText(String.valueOf(customer.customerId()));
            lastNameField.setText(customer.lastName());
            firstNameField.setText(customer.firstName());
            phoneField.setText(customer.phone());
        }
    }

    private void showNextRecord() {
        Customer customer = getCustomer(currentId, true);
        if (customer != null) {
            showRecord(customer);
        } else {
            JOptionPane.showMessageDialog(this, "This is the last record.");
        }
    }

    private void showPreviousRecord() {
        Customer customer = getCustomer(currentId, false);
        if (customer != null) {
            showRecord(customer);
        } else {
            JOptionPane.showMessageDialog(this, "This is the first record.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerShowSwing::new);
    }
}
