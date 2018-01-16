package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class searchPetController {

    public TextField petKind_fld;
    public TextField maxPrice_fld;
    public TextField minPrice_fld;
    public TextField freeText_fld;
    public DatePicker start_date;
    public DatePicker end_date;

    public void doSearch(ActionEvent actionEvent)
    {
        if(petKind_fld.getText().isEmpty() || maxPrice_fld.getText().isEmpty() || minPrice_fld.getText().isEmpty()
                || start_date.getValue()==null || end_date.getValue()==null){
            showAlertError("יש להזין את סוג החיה, מחיר מינימלי, מחיר מקסימלי ותאריכים");
            return;
        }

        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            //String
            String h=freeText_fld.getText();
            PreparedStatement prep = conn.prepareStatement("select c.packNum, c.prPolicy, c.cancelPolicy, c.price, c.trading ,a.name, a.description, a.petKind, c.startDate, c.endDate from pets a join packItems b on a.ID=b.itemnum join packages c on b.packnum=c.packNum where a.petKind=? and a.description like '%"+h+"%' and c.price>=? and c.price<=? and c.startDate<=? and c.endDate>=?;");
            prep.setString(1,petKind_fld.getText());
            //prep.setString(2,freeText_fld.getText());
            prep.setInt(2,Integer.parseInt(minPrice_fld.getText()));
            prep.setInt(3,Integer.parseInt(maxPrice_fld.getText()));
            prep.setString(4,start_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            prep.setString(5,end_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            ResultSet rs=prep.executeQuery();
            ListView<String> list = new ListView<>();
            ObservableList<String> items = FXCollections.observableArrayList();
            while(rs.next()) {
                String s=
                        "pack ID: "+ rs.getString(1)+", Pack Policy: "+rs.getString(2)+", cancel policy: "+ rs.getString(3)+", Price: "+rs.getString(4)+", can be trade: "+rs.getString(5)+", start date : "+rs.getString(9)+", end date : "+rs.getString(10);
                items.add(s);
                items.add("Items In Package: ");
                itemsInPackage(items,rs.getString(1));
            }
            if(items.size()==0)
            {
                showAlertError("אין חבילות המכילות פריטים שעונים על החיפוש");
                conn.close();
                return;
            }
            conn.close();
            list.setItems(items);
            Stage stage = new Stage();
            stage.setTitle("תוצאות חיפוש: ");
            BorderPane pane = new BorderPane();
            Scene s = new Scene(pane);
            stage.setScene(s);
            pane.setCenter(list);
            stage.setAlwaysOnTop(true);
            stage.setOnCloseRequest(e -> {
                e.consume();
                stage.close();
            });
            stage.showAndWait();

        }
        catch(NumberFormatException e)
        {
            //showAlertError("מספר פריט לא חוקי");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void itemsInPackage(ObservableList<String> items, String packet) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement statement1 = conn.prepareStatement("SELECT v.ID, v.name,v.picture, v.description, v.color, v.year, v.brand, v.numOfPeople FROM Vehicle v JOIN packItems p ON v.ID=p.itemnum WHERE p.packnum =?");
        statement1.setString(1, packet);
        ResultSet rs1=statement1.executeQuery();
        items.add("פריטי רכב");
        while(rs1.next()) {
            items.add("ID: "+ rs1.getString(1)+", Name: "+rs1.getString(2)+", Picture: "+rs1.getString(3)+", Description: "+rs1.getString(4)+", Color: "+rs1.getString(5)+", Year: "+rs1.getString(6)+", Brand:"+rs1.getString(7)+ ", Num of people:"+rs1.getString(8));
        }

        PreparedStatement statement2 = conn.prepareStatement("SELECT v.ID, v.name,v.picture, v.description, v.petKind FROM pets v JOIN packItems p ON v.ID=p.itemnum WHERE p.packnum =?");
        statement2.setString(1, packet);
        ResultSet rs2=statement2.executeQuery();
        items.add("פריטי חיות");
        while(rs2.next()) {
            items.add("ID: "+rs2.getString(1)+", Name: "+rs2.getString(2)+", Picture: "+rs2.getString(3)+", Description: "+rs2.getString(4)+", Pet kind: "+rs2.getString(5));
        }


        PreparedStatement statement3 = conn.prepareStatement("SELECT v.ID, v.name, v.picture, v.description, v.numOfPeople, v.address FROM realEstate v JOIN packItems p ON v.ID=p.itemnum WHERE p.packnum =?");
        statement3.setString(1, Main.x);
        ResultSet rs3=statement3.executeQuery();
        items.add("פריטי נדלן");
        while(rs3.next()) {
            items.add("ID: "+rs3.getString(1)+", Name: "+rs3.getString(2)+", Picture: "+rs3.getString(3)+", Description: "+rs3.getString(4)+", Number of People: "+rs3.getString(5)+", Address: "+rs3.getString(6));
        }


        PreparedStatement statement4 = conn.prepareStatement("SELECT v.ID, v.name, v.picture, v.description, v.condition FROM secondHand v JOIN packItems p ON v.ID=p.itemnum WHERE p.packnum =?");
        statement4.setString(1, Main.x);
        ResultSet rs4=statement4.executeQuery();
        items.add("פריטי יד שניה");
        while(rs4.next()) {
            items.add("ID: "+rs4.getString(1)+", Name: "+rs4.getString(2)+", Picture: "+rs4.getString(3)+", Description: "+rs4.getString(4)+", Condition: "+rs4.getString(5));
        }
        items.add("-----------------------------------------------------------------------------------------------------------------");

        conn.close();
    }
}
