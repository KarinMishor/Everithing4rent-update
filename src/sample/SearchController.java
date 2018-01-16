package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchController {
    public void searchRealEstate(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Search Real Estate");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("SearchRealEstate.fxml").openStream());
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void searchPets(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Search Pets");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("SearchPet.fxml").openStream());
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void searchSecondHand(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Search Second Hand");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("searchSecondHand.fxml").openStream());
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void searchVehicle(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("Search Vehicle");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("searchVehicle.fxml").openStream());
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
