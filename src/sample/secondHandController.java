package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;

public class secondHandController{

    @FXML
    public ChoiceBox condition;
    public Button regbtm;
    public TextField itemNamefld;
    public Button picfld;
    private String picname="";
    private String category="Second-hand";
    public TextField desc;

    ObservableList<String> q1= FXCollections.observableArrayList( "דרוש תיקון","במצב טוב","חדש");

    @FXML
    public void initialize() {
        condition.setItems(q1);
        condition.setValue("");

    }
    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void add(ActionEvent actionEvent) throws Exception {
        if (!(condition.getSelectionModel().getSelectedItem().toString().equals("חדש") ||
                condition.getSelectionModel().getSelectedItem().toString().equals("במצב טוב")||
                condition.getSelectionModel().getSelectedItem().toString().equals("דרוש תיקון"))) {
            showAlertError("בחר סוג פריט");
            return;
        }

        if(itemNamefld.getText().isEmpty()){
            showAlertError("לא מילאת את כל השדות");
            return;
        }

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into secondHand values (?,?,?,?,?,?,?);");
        try {
            prep.setString(1, Main.x);
            prep.setInt(2, Main.itenNum);
            prep.setString(3, itemNamefld.getText());
            prep.setString(4, category);
            prep.setString(5, picname);
            prep.setString(6, desc.getText());
            prep.setString(7, condition.getSelectionModel().getSelectedItem().toString());
            prep.addBatch();
            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            conn.close();
        }catch (SQLException e)
        {
            showAlertError("ארעה שגיאה");
            itemNamefld.clear();
            conn.close();
            return;
        }
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        prep = conn.prepareStatement(
                "insert into packItems values (?,?);");
        prep.setInt(1, Main.packNum);
        prep.setInt(2, Main.itenNum);
        prep.addBatch();
        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        showAlertInfo("הפריט נוסף לחבילה בהצלחה");
        Main.itenNum++;
        conn.close();
        itemNamefld.clear();
        desc.clear();
        picname="";

    }

    public void uploadPicure() throws Exception {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions1 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileC.getExtensionFilters().add(fileExtensions);
        fileC.getExtensionFilters().add(fileExtensions1);
        File f = fileC.showOpenDialog((Stage) itemNamefld.getScene().getWindow());
        if (f != null) {
            picname= f.getAbsolutePath().toString();
            return;
        }
        picname="";
    }


}