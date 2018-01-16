package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class loginController extends JFrame{

    @FXML
    public TextField emailfld;
    public PasswordField passfld;
    public Button loginbtm;
    public Button register;
    public AnchorPane anchorPane;
    public String email;


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


    public void Login() throws Exception{
        if(emailfld.getText().isEmpty()||passfld.getText().isEmpty()) {
            showAlertError("על כל השדות להיות מלאים");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement statement = conn.prepareStatement("SELECT password,email,fullName FROM users WHERE email =?");
        statement.setString(1, emailfld.getText());
        ResultSet rs=statement.executeQuery();
        String name=null;
        if(!rs.next()) {
            showAlertError("שם משתמש לא קיים");
            emailfld.clear();
            passfld.clear();
            statement.clearParameters();
        }
        else
        {
            name=rs.getString("fullName");
            String orig=rs.getString("password");
            if(!orig.equals(passfld.getText()))
            {
                showAlertError("סיסמא שגויה");
                emailfld.clear();
                passfld.clear();
            }
            else{
                showAlertInfo(name+" is connected");
                Main.x=emailfld.getText();
                Stage stage = (Stage) loginbtm.getScene().getWindow();
                stage.close();
            }
            statement.clearParameters();
        }
        statement.clearParameters();
        conn.close();
    }

    public void registration(ActionEvent actionEvent)throws IOException {
        Stage newStage = new Stage();
        anchorPane = (AnchorPane) FXMLLoader.load(getClass().getResource("register.fxml"));
        Scene scene = new Scene(anchorPane);
        newStage.setScene(scene);
        newStage.initModality(Modality.APPLICATION_MODAL);
        newStage.setTitle("register");
        newStage.showAndWait();

    }
}
