package com.khrd;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class CustomerShowSwing extends JFrame {
    private List<Customer> customers;
    private int currentIndex;

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

        loadCustomerData();
        showRecord(0);

        setVisible(true);
    }

    private void loadCustomerData() {
        customers = new ArrayList<>();
        customers.add(new Customer(1, "Chhunly", "Chorn", "085787179"));
        customers.add(new Customer(2, "Seng", "Thiarong", "071558809"));
        customers.add(new Customer(3, "Erling", "Haaland", "0975032729"));
        customers.add(new Customer(4, "Chhan", "Bunthorn", "071558822"));

        currentIndex = 0;
    }

    private void showRecord(int index) {
        if (index >= 0 && index < customers.size()) {
            Customer customer = customers.get(index);
            idField.setText(String.valueOf(customer.customerId()));
            lastNameField.setText(customer.lastName());
            firstNameField.setText(customer.firstName());
            phoneField.setText(customer.phone());
        }
    }

    private void showNextRecord() {
        if (currentIndex < customers.size() - 1) {
            currentIndex++;
            showRecord(currentIndex);
        } else {
            JOptionPane.showMessageDialog(this, "This is the last record.");
        }
    }

    private void showPreviousRecord() {
        if (currentIndex > 0) {
            currentIndex--;
            showRecord(currentIndex);
        } else {
            JOptionPane.showMessageDialog(this, "This is the first record.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerShowSwing::new);
    }
}
