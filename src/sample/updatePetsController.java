package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.*;

public class updatePetsController {
    @FXML
    public TextField itemNamefld;
    public TextField desc;
    public Button picfld;
    private String picname="";
    public Button btn_saveChange;
    public Button findItem_btn;
    public TextField itemID;
    public TextField petKind;
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

    public void save() throws Exception {
        if (itemNamefld.getText().isEmpty() || petKind.getText().isEmpty() || picname.equals("")) {
            showAlertError("לא מילאת את כל השדות");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement("UPDATE pets SET name=?, picture=?, description=?, petKind=? WHERE ID=?;");
        try {
            prep.setString(1, itemNamefld.getText());
            prep.setString(2,picname);
            prep.setString(3, desc.getText());
            prep.setString(4, petKind.getText());
            prep.setInt(5,Integer.parseInt(itemID.getText()));

            prep.executeUpdate();
            conn.close();

        } catch (SQLException e) {
            showAlertError("ארעה שגיאה");
            conn.close();
            return;
        }

        showAlertInfo("הפריט עודכן בהצלחה");
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


    public void findItem(ActionEvent actionEvent) {
        try
        {
            int item=Integer.parseInt(itemID.getText());
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM pets WHERE ID=?;");
            prep.setString(1,item+"");
            ResultSet rs=prep.executeQuery();
            if(!rs.next() || !(Main.x).equals(rs.getString(1)))
            {
                showAlertError("מספר פריט לא חוקי");
                return;
            }
            //rs.getString(1);
            itemNamefld.setText(rs.getString(3));
            picname=rs.getString(5);
            desc.setText(rs.getString(6));
            petKind.setText(rs.getString(7));
            conn.close();
        }
        catch(NumberFormatException e)
        {
            showAlertError("מספר פריט לא חוקי");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ActionEvent actionEvent)
    {
        try
        {
            int item=Integer.parseInt(itemID.getText());
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            PreparedStatement prep = conn.prepareStatement("DELETE FROM pets WHERE ID=?;");
            prep.setInt(1,item);
            prep.executeUpdate();
            itemNamefld.setText("");
            picname="";
            desc.setText("");
            petKind.setText("");
            conn.close();

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            prep = conn.prepareStatement("DELETE FROM packItems WHERE itemnum=?;");
            prep.setInt(1,item);
            prep.executeUpdate();
            conn.close();
            itemID.setText("");
            showAlertInfo("הפריט נמחק בהצלחה");

        }
        catch(NumberFormatException e)
        {
            showAlertError("מספר פריט לא חוקי");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
