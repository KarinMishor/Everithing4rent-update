package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class itemController{

    @FXML
    public ChoiceBox priority;
    public Button addbtm;
    public TextField minrating;
    public TextField cost;
    public CheckBox policy;
    public CheckBox tredingfld;
    public int num=0;
    public  javafx.scene.control.TextField number;
    public boolean ifMinrate=false;
    public Button addpets;
    public Button addsecondhand;
    public Button addvehicle;
    public Button addreales;
    public DatePicker startDate;
    public DatePicker endDate;
    public Button addExistItem;
    public TextField NumExistItem;
    public TextField deposit;
    public CheckBox donation;


    ObservableList<String> q1= FXCollections.observableArrayList( "כל הקודם זוכה","הגישה השמרנית","הגישה הבטוחה");


    @FXML
    public void initialize() {
        priority.setItems(q1);
        priority.setValue("");

        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(startDate.getValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffcccc;");
                        }
                    }
                };
            }
        };
        endDate.setDayCellFactory(dayCellFactory);
    }

    private void showAlertError(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void donationFunc() throws Exception{
        if(donation.isSelected()) {
            deposit.setDisable(false);
            cost.clear();
            cost.setDisable(true);
        }else{
            deposit.setDisable(true);
            deposit.clear();
            cost.setDisable(false);
        }
    }

    public void addExistItemToPack() throws Exception{

        if(NumExistItem.getText().isEmpty()) {
            showAlertError("אנא הכנס מספר פריט קיים להוספה");
            return;
        }

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement statement = conn.prepareStatement("SELECT email,ID FROM pets WHERE ID =? and email=? union all SELECT email,ID FROM Vehicle WHERE ID =? and email=? union all SELECT email,ID FROM realEstate WHERE ID =? and email=? union all SELECT email,ID FROM secondHand WHERE ID =? and email=?");
        statement.setInt(1, Integer.parseInt(NumExistItem.getText()));
        statement.setInt(3, Integer.parseInt(NumExistItem.getText()));
        statement.setInt(5, Integer.parseInt(NumExistItem.getText()));
        statement.setInt(7, Integer.parseInt(NumExistItem.getText()));
        statement.setString(2, Main.x);
        statement.setString(4, Main.x);
        statement.setString(6, Main.x);
        statement.setString(8, Main.x);
        ResultSet rs=statement.executeQuery();
        if(!rs.next()) {
            showAlertError("מספר הפריט אינו קיים");
            conn.close();
            return;
        }

        //check if dates are OK
        PreparedStatement statement2 = conn.prepareStatement("SELECT b.startDate, b.endDate FROM packItems a JOIN packages b ON a.packnum=b.packNum WHERE a.itemnum=?;");
        statement2.setInt(1, Integer.parseInt(NumExistItem.getText()));
        ResultSet itemPackets=statement2.executeQuery();

        //for each package - find the package and compare dates
        while(itemPackets.next())
        {
            String currPackStart=startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String currPackEnd=endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String existPackStart=itemPackets.getString("startDate");
            String existPackEnd=itemPackets.getString("endDate");
            //if there is overlap
            if(currPackStart.compareTo(existPackStart)>=0 && currPackStart.compareTo(existPackEnd)<=0)
            {
                showAlertError("לא ניתן להוסיף את הפריט, קיימת חבילה נוספת המכילה אותו בתאריכים אלו");
                conn.close();
                return;
            }

            if(existPackStart.compareTo(currPackStart)>0 && existPackStart.compareTo(currPackEnd)<0)
            {
                showAlertError("לא ניתן להוסיף את הפריט, קיימת חבילה נוספת המכילה אותו בתאריכים אלו");
                conn.close();
                return;
            }
        }
        conn.close();

        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into packItems values (?,?);");
        prep.setInt(1,Main.packNum);
        prep.setInt(2, Integer.parseInt(NumExistItem.getText()));
        prep.addBatch();
        conn.setAutoCommit(false);
        prep.executeBatch();
        conn.setAutoCommit(true);

        showAlertInfo("הפריט נוסף לחבילה בהצלחה");
        conn.close();
        return;
    }

    private void showAlertInfo(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void minRate(){
        if( priority.getSelectionModel().getSelectedItem().toString().equals("הגישה השמרנית")){
            minrating.setDisable(false);
            ifMinrate=true;
        }
        else{
            minrating.setDisable(true);
        }
    }

    public void add() throws Exception {
        if (!(priority.getSelectionModel().getSelectedItem().toString().equals("כל הקודם זוכה")||priority.getSelectionModel().getSelectedItem().toString().equals("הגישה הבטוחה") ||
                priority.getSelectionModel().getSelectedItem().toString().equals("הגישה השמרנית"))) {
            showAlertError("אנא בחר גישה העדפה");
            return;
        }

        Boolean numeric=true;
        try {
            int numOfP;
            if(!donation.isSelected())
                numOfP=Integer.parseInt(cost.getText());
            else
                numOfP=Integer.parseInt(deposit.getText());
        }catch (NumberFormatException e) {
            numeric = false;
        }
        if(!numeric){
            showAlertError("אנא הוסף מחיר תקין!");
            return;
        }

        if(ifMinrate){
            Boolean x=true;
            int numOfP=0;
            try {
                numOfP=Integer.parseInt(minrating.getText());
            }catch (NumberFormatException e) {
                numeric = false;
            }
            if(!x || numOfP>10 || numOfP<0){
                showAlertError("אנא הוסף דירוג מינמלי תקין!");
                return;
            }
        }

        if(donation.isSelected()){
            if((ifMinrate&& minrating.getText().isEmpty()) || deposit.getText().isEmpty() || startDate.getValue()==null || endDate.getValue()==null){
                showAlertError("לא מילאת את כל השדות");
                return;
            }
        }
        else{
            if((ifMinrate&& minrating.getText().isEmpty()) || cost.getText().isEmpty() || startDate.getValue()==null || endDate.getValue()==null){
                showAlertError("לא מילאת את כל השדות");
                return;
            }
        }

        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into packages values (?,?,?,?,?,?,?,?,?,?);");
        try {
            prep.setString(1, Main.x);
            prep.setString(2, ""+Main.packNum);
            prep.setString(3, priority.getSelectionModel().getSelectedItem().toString());
            if(ifMinrate){
                prep.setString(4, minrating.getText());
            }
            else {
                prep.setString(4, null);
            }
            prep.setString(5, String.valueOf(policy.isSelected()));
            if(donation.isSelected())
                prep.setInt(6, 0);
            else
                prep.setInt(6, Integer.parseInt(cost.getText()));
            prep.setString(7, String.valueOf(tredingfld.isSelected()));
            prep.setString(8, startDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            prep.setString(9, endDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            if(donation.isSelected())
                prep.setInt(6, Integer.parseInt(deposit.getText()));
            else
                prep.setInt(6, 0);
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            conn.close();
            Main.packNum++;
            showAlertInfo("החבילה נוספה בהצלחה");
            minrating.clear();
            minrating.setDisable(true);
            cost.clear();
            policy.setSelected(false);
            tredingfld.setSelected(false);
            number.setText("0");
            num=0;
            cost.setDisable(false);
            ifMinrate=false;
            NumExistItem.clear();
            deposit.clear();
            deposit.setDisable(true);
            donation.setSelected(false);
        }catch (SQLException e)
        {
            showAlertError("פריט זה קיים כבר במערכת");
            cost.clear();
            conn.close();
            return;
        }


    }

    public void addSecondHand() throws Exception{
        Stage stage = new Stage();
        stage.setTitle("add item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("secondHand.fxml").openStream());
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        num++;
        number.setText(""+num);

    }

    public void addRealEstate() throws Exception{
        Stage stage = new Stage();
        stage.setTitle("add item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("addRealEstate.fxml").openStream());
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        stage.showAndWait();
        num++;
        number.setText(""+num);

    }

    public void addPets() throws Exception{
        Stage stage = new Stage();
        stage.setTitle("add item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("pets.fxml").openStream());
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        num++;
        number.setText(""+num);

    }

    public void addVehicle() throws Exception{
        Stage stage = new Stage();
        stage.setTitle("add item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("Vehicle.fxml").openStream());
        Scene scene = new Scene(root, 700, 600);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        num++;
        number.setText(""+num);

    }
}