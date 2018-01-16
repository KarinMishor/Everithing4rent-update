package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class MyProducts {

    public void showProducts() throws ClassNotFoundException, SQLException {
        ListView<String> list = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM Vehicle WHERE email =?");
        statement1.setString(1, Main.x);
        ResultSet rs1=statement1.executeQuery();

        PreparedStatement statement2 = conn.prepareStatement("SELECT * FROM pets WHERE email =?");
        statement2.setString(1, Main.x);
        ResultSet rs2=statement2.executeQuery();

        PreparedStatement statement3 = conn.prepareStatement("SELECT * FROM realEstate WHERE email =?");
        statement3.setString(1, Main.x);
        ResultSet rs3=statement3.executeQuery();

        PreparedStatement statement4 = conn.prepareStatement("SELECT * FROM secondHand WHERE email =?");
        statement4.setString(1, Main.x);
        ResultSet rs4=statement4.executeQuery();

        items.add("פריטי רכב");
        while(rs1.next()) {
            items.add("ID: "+ rs1.getString(2)+", Name: "+rs1.getString(3)+", Category: "+rs1.getString(4)+", Pic: "+rs1.getString(5)+", Desc: "+rs1.getString(6)+", Color: "+rs1.getString(7)+", Year: "+rs1.getString(8)+", Brand:"+rs1.getString(9)+ ", Num of people:"+rs1.getString(9));
        }
        items.add("פריטי חיות");
        while(rs2.next()) {
            items.add("ID: "+rs2.getString(2)+", Name: "+rs2.getString(3)+", Category: "+rs2.getString(4)+", Pic: "+rs2.getString(5)+", Desc: "+rs2.getString(6)+", Pet kind: "+rs2.getString(7));
        }
        items.add("פריטי נדלן");
        while(rs3.next()) {
            items.add("ID: "+rs3.getString(2)+", Name: "+rs3.getString(3)+", Category: "+rs3.getString(4)+", Pic: "+rs3.getString(5)+", Desc: "+rs3.getString(6)+", Num of People: "+rs3.getString(7)+", Address: "+rs3.getString(8));
        }
        items.add("פריטי יד שניה");
        while(rs4.next()) {
            items.add("ID: "+rs4.getString(2)+", Name: "+rs4.getString(3)+", Category: "+rs4.getString(4)+", Pic: "+rs4.getString(5)+", Desc: "+rs4.getString(6)+", Condition: "+rs4.getString(7));
        }
        conn.close();

        list.setItems(items);
        Stage stage = new Stage();
        stage.setTitle("products");
        BorderPane pane = new BorderPane();
        Scene s = new Scene(pane, 500,700);
        stage.setScene(s);
        pane.setCenter(list);
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(e -> {
            e.consume();
            stage.close();
        });
        stage.showAndWait();
    }

    public void editRealEstate(ActionEvent actionEvent)throws IOException {
        Stage stage = new Stage();
        stage.setTitle("edit");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("updateRealEstate.fxml").openStream());
        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.showAndWait();
    }

    public void editSecondHand(ActionEvent actionEvent)throws IOException {
        Stage stage = new Stage();
        stage.setTitle("edit");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("updatesecondhand.fxml").openStream());
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.showAndWait();
    }

    public void editPets(ActionEvent actionEvent)throws IOException {
        Stage stage = new Stage();
        stage.setTitle("edit");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("updatePets.fxml").openStream());
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.showAndWait();
    }

    public void editvehicle(ActionEvent actionEvent)throws IOException {
        Stage stage = new Stage();
        stage.setTitle("edit");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("updateVehicle.fxml").openStream());
        Scene scene = new Scene(root, 650, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.showAndWait();
    }

}
