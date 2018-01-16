package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;


public class updateVihcleController {

    public TextField nameItem_fld;
    public TextField brand_fld;
    public TextField year_fld;
    public TextField color_fld;
    public TextField numOfPeople_fld;
    public TextField desc_fld;
    public Button picfld;
    private String picname="";
    public TextField itemID;

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
        if (nameItem_fld.getText().isEmpty() || brand_fld.getText().isEmpty() || year_fld.getText().isEmpty()
                || color_fld.getText().isEmpty() || numOfPeople_fld.getText().isEmpty() ) {
            showAlertError("לא מילאת את כל השדות");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement("UPDATE Vehicle SET name=?, picture=?, description=?, color=?, year=?, brand=?, numOfPeople=? WHERE ID=?;");
        try {
            prep.setString(1, nameItem_fld.getText());
            prep.setString(2,picname);
            prep.setString(3, desc_fld.getText());
            prep.setString(4, color_fld.getText());
            prep.setInt(5, Integer.parseInt(year_fld.getText()));
            prep.setString(6, brand_fld.getText());
            prep.setInt(7, Integer.parseInt(numOfPeople_fld.getText()));
            prep.setInt(8,Integer.parseInt(itemID.getText()));

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
        File f = fileC.showOpenDialog((Stage) nameItem_fld.getScene().getWindow());
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
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM Vehicle WHERE ID=?;");
            prep.setInt(1,item);
            ResultSet rs=prep.executeQuery();
            if(!rs.next() || !(Main.x).equals(rs.getString(1)))
            {
                showAlertError("מספר פריט לא חוקי");
                return;
            }
            //rs.getString(1);
            nameItem_fld.setText(rs.getString(3));
            picname=rs.getString(5);
            desc_fld.setText(rs.getString(6));
            color_fld.setText(rs.getString(7));
            year_fld.setText(rs.getString(8));
            brand_fld.setText(rs.getString(9));
            numOfPeople_fld.setText(rs.getString(10));
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
            PreparedStatement prep = conn.prepareStatement("DELETE FROM Vehicle WHERE ID=?;");
            prep.setInt(1,item);
            prep.executeUpdate();
            nameItem_fld.setText("");
            picname="";
            desc_fld.setText("");
            color_fld.setText("");
            year_fld.setText("");
            brand_fld.setText("");
            numOfPeople_fld.setText("");
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
