package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
public class GUIController {

    @FXML
    public  javafx.scene.control.Button loginApp;
    public  javafx.scene.control.Button logOut_btn;
    public  javafx.scene.control.Button myProducts;
    public  javafx.scene.control.Button myOrders;
    public  javafx.scene.control.Button addProducts;
    public  javafx.scene.control.Button newOrder_btn;

    public void login(ActionEvent actionEvent)throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Login");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("login.fxml").openStream());
        Scene scene = new Scene(root, 450, 400);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        if(!Main.x.equals("")) {
            loginApp.setDisable(true);
            myProducts.setDisable(false);
            myOrders.setDisable(false);
            logOut_btn.setDisable(false);
            addProducts.setDisable(false);
            newOrder_btn.setDisable(false);
        }
    }

    public void setMyProducts(ActionEvent actionEvent)throws IOException{
        Stage stage = new Stage();
        stage.setTitle("add items");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("item.fxml").openStream());
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.showAndWait();
    }

    public void MyProducts(ActionEvent actionEvent)throws IOException{
        Stage stage = new Stage();
        stage.setTitle("my products");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyProducts.fxml").openStream());
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void logOut(ActionEvent actionEvent){
        Main.x="";
        loginApp.setDisable(false);
        myProducts.setDisable(true);
        myOrders.setDisable(true);
        logOut_btn.setDisable(true);
        addProducts.setDisable(true);
        newOrder_btn.setDisable(true);
    }

    public void searchProduct(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Search Product");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Search.fxml").openStream());
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void createNewOrder(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Order Package");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("lending.fxml").openStream());
        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void myOrdes(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("My Orders");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyOrders.fxml").openStream());
        Scene scene = new Scene(root, 300, 300);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}