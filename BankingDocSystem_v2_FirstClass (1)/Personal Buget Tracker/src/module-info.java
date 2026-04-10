module com.budgettracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.logging;
    requires mysql.connector.j;

    opens com.budgettracker to javafx.fxml;
    opens com.budgettracker.controller to javafx.fxml;
    exports com.budgettracker;
    exports com.budgettracker.controller;
    exports com.budgettracker.model;
    exports com.budgettracker.service;
    exports com.budgettracker.util;
    exports com.budgettracker.dao;
}
