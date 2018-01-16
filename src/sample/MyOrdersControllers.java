package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;

/**
 * Created by linoy on 15-Jan-18.
 */
public class MyOrdersControllers
{

    public void showOrdersAsOwner(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM orders WHERE ownerEmail =?");
        statement1.setString(1, Main.x);
        ResultSet rs1=statement1.executeQuery();

        while(rs1.next()) {
            items.add("Order Number: "+ rs1.getString(1)+", Package Number: "+rs1.getString(2)+", Buyer Email: "+rs1.getString(4)+", Start Date: "+rs1.getString(5)+", End Date: "+rs1.getString(6)+", Price: "+rs1.getString(7)+", Status: "+rs1.getString(9)+", Is Returned:"+rs1.getString(10)+ ", Is Trading:"+rs1.getString(11));
        }

        conn.close();

        list.setItems(items);
        Stage stage = new Stage();
        stage.setTitle("Orders As Owner");
        BorderPane pane = new BorderPane();
        Scene s = new Scene(pane,700,500);
        stage.setScene(s);
        pane.setCenter(list);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            e.consume();
            stage.close();
        });
        stage.showAndWait();
    }

    public void showOrdersAsBuyer(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM orders WHERE buyerEmail =?");
        statement1.setString(1, Main.x);
        ResultSet rs1=statement1.executeQuery();

        while(rs1.next()) {
            items.add("Order Number: "+ rs1.getString(1)+", Package Number: "+rs1.getString(2)+", Owner Email: "+rs1.getString(3)+", Start Date: "+rs1.getString(5)+", End Date: "+rs1.getString(6)+", Price: "+rs1.getString(7)+", Status: "+rs1.getString(9)+", Is Returned:"+rs1.getString(10)+ ", Is Trading:"+rs1.getString(11));
        }

        conn.close();

        list.setItems(items);
        Stage stage = new Stage();
        stage.setTitle("Orders As Buyer");
        BorderPane pane = new BorderPane();
        Scene s = new Scene(pane, 700, 500);
        stage.setScene(s);
        pane.setCenter(list);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            e.consume();
            stage.close();
        });
        stage.showAndWait();
    }
}
