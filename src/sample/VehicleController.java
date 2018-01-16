package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.*;


public class VehicleController {

    @FXML
    public TextField nameItem_fld;
    public TextField brand_fld;
    public TextField year_fld;
    public TextField color_fld;
    public TextField numOfPeople_fld;
    public TextField desc_fld;
    public Button picfld;
    private String picname="";



    @FXML
    public void initialize() {

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

    public void add() throws Exception {

        if (brand_fld.getText().isEmpty() || year_fld.getText().isEmpty() ||
                color_fld.getText().isEmpty() || picname.equals("") || numOfPeople_fld.getText().isEmpty() || nameItem_fld.getText().isEmpty()
               /* priority.getValue()==null || category.getValue() ==null*/) {
            showAlertError("לא מילאת את כל השדות");
            return;
        }
        Boolean numeric=true;
        try {
            int numOfP=Integer.parseInt(numOfPeople_fld.getText());
        }
        catch (NumberFormatException e) {
            numeric = false;
        }
        if(!numeric){
            showAlertError("Num Of people must to be a number!");
            return;}

        numeric=true;
        try {
            int numOfP=Integer.parseInt(year_fld.getText());
        }
        catch (NumberFormatException e) {
            numeric = false;
        }
        if(!numeric){
            showAlertError("year of vehicle must to be a number!");
            return;}


        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into Vehicle values (?,?,?,?,?,?,?,?,?,?);");

        try {
            prep.setString(1, Main.x);
            prep.setInt(2,Main.itenNum);
            prep.setString(3, nameItem_fld.getText());
            prep.setString(4, "vehicle");
            prep.setString(5, picname);
            prep.setString(6, desc_fld.getText());
            prep.setString(7, color_fld.getText());
            prep.setInt(8, Integer.parseInt(year_fld.getText()));
            prep.setString(9, brand_fld.getText());
            prep.setInt(10, Integer.parseInt(numOfPeople_fld.getText()));
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            conn.close();
            nameItem_fld.clear();
            brand_fld.clear();
            year_fld.clear();
            color_fld.clear();
            numOfPeople_fld.clear();
            desc_fld.clear();
            picname="";
        }
        catch (SQLException e)
        {
            showAlertError("ארעה שגיאה");
            nameItem_fld.clear();
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
    }

    public void uploadPicure() throws Exception {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions1 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileC.getExtensionFilters().add(fileExtensions);
        fileC.getExtensionFilters().add(fileExtensions1);
        File f = fileC.showOpenDialog((Stage) nameItem_fld.getScene().getWindow());
        if (f != null) {
            picname= f.getAbsolutePath().toString();
            return;
        }
        picname="";
    }


}