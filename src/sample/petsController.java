package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.*;

public class petsController {

    @FXML
    public Button regbtm;
    public TextField itemNamefld;
    public TextField petKind;
    public Button picfld;
    private String picname="";
    private String category="pets";
    public TextField desc;

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

    public void add() throws Exception {

        if(itemNamefld.getText().isEmpty()|| petKind.getText().isEmpty() || picname.equals("")){
            showAlertError("לא מילאת את כל השדות");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into pets values (?,?,?,?,?,?,?);");
        try {
            prep.setString(1, Main.x);
            prep.setInt(2, Main.itenNum);
            prep.setString(3, itemNamefld.getText());
            prep.setString(4, category);
            prep.setString(5, picname);
            prep.setString(6, desc.getText());
            prep.setString(7, petKind.getText());
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
        petKind.clear();
        picname="";
        desc.clear();
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