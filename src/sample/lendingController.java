package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;

import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;

/**
 * Created by linoy on 15-Jan-18.
 */
public class lendingController {
    /* public TextField packet_txtfld;
     public TextField packetToTrade_txtfld;
     public CheckBox isTrading;
     public Button doLending_btn;
 */
    public String packetAStartDate;
    public String packetAEndtDate;
    public String packetBStartDate;
    public String packetBEndtDate;
    public String emailA;
    public String paypalA;
    public String paypalB;
    public int price;

    public  javafx.scene.control.TextField packet_txtfld;
    public  javafx.scene.control.TextField packetToTrade_txtfld;
    public  javafx.scene.control.CheckBox isTrading;
    public  javafx.scene.control.Button doLending_btn;
    public javafx.scene.control.TextField addition1_txtfld;
    public javafx.scene.control.TextField addition2_txtfld;

    public void checkTrading(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if(!packet_txtfld.getText().trim().isEmpty())
        {
            String packet=packet_txtfld.getText();
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM packages WHERE packNum =?");
            statement1.setInt(1, Integer.parseInt(packet));
            ResultSet rs1=statement1.executeQuery();
            if(rs1.next())
            {
                if(rs1.getString("trading").equals("false"))
                {
                    isTrading.setSelected(false);
                    //trading is not possible
                    showAlertError("החבילה אינה ניתנת להשאלה הדדית!");

                }
                else
                {
                    packetToTrade_txtfld.setDisable(false);
                    addition1_txtfld.setDisable(false);
                    addition2_txtfld.setDisable(false);
                    packetAStartDate=rs1.getString("startDate");
                    packetAEndtDate=rs1.getString("endDate");
                    emailA=rs1.getString("email");
                    PreparedStatement statement2 = conn.prepareStatement("SELECT * FROM users WHERE email =?");
                    statement2.setString(1, emailA);
                    ResultSet rs2=statement2.executeQuery();
                    paypalA=rs2.getString("paypalAccount");
                    statement2 = conn.prepareStatement("SELECT * FROM users WHERE email =?");
                    statement2.setString(1, Main.x);
                    rs2=statement2.executeQuery();
                    paypalB=rs2.getString("paypalAccount");
                }
            }
            else
            {
                //there is not such packet
                showAlertError("מספר חבילה אינו חוקי!");
            }
            conn.close();
        }
        else{
            showAlertError("אנא הכנס מספר חבילה");
            isTrading.setSelected(false);
            return;
        }
    }

    public void doLending(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        if (isTrading.isSelected()) {
            String packet = packet_txtfld.getText();
            String packet2 = packetToTrade_txtfld.getText();
            //find if packet 2 belong to user
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM packages WHERE packNum =? and email=?");
            statement1.setInt(1, Integer.parseInt(packet2));
            statement1.setString(2, Main.x);
            ResultSet rs1 = statement1.executeQuery();
            if (rs1.next()) {

                packetBStartDate = rs1.getString("startDate");
                packetBEndtDate = rs1.getString("endDate");
                if (!rs1.getString("trading").equals("true")) {
                    showAlertError("חבילה שבבעולתך אינה ניתנת להשאלה הדדית!");
                } else if (!(packetAStartDate.compareTo(packetBStartDate) >= 0 && packetAEndtDate.compareTo(packetBEndtDate) <= 0)) {
                    showAlertError("תאריכי ההחלפה אינם תואמים, לא ניתן לבצע החלפה!");
                } else {

                    PreparedStatement prep = conn.prepareStatement(
                            "insert into orders values (?,?,?,?,?,?,?,?,?,?,?);");
                    try {
                        prep.setInt(1, Main.orderNum);
                        prep.setInt(2, Integer.parseInt(packet));
                        prep.setString(3, emailA);
                        prep.setString(4, Main.x);
                        prep.setString(5, packetAStartDate);
                        prep.setString(6, packetAEndtDate);
                        if(addition1_txtfld.getText().isEmpty())
                            prep.setInt(7, 0);
                        else
                            prep.setInt(7, Integer.parseInt(addition1_txtfld.getText()));
                        prep.setString(8, paypalB);
                        prep.setString(9, "ממתין לאישור");
                        prep.setString(10, "false");
                        prep.setString(11, "true");
                        prep.addBatch();
                        conn.setAutoCommit(false);
                        prep.executeBatch();
                        conn.setAutoCommit(true);

                    } catch (SQLException e) {
                        conn.close();
                        return;
                    }

                    prep = conn.prepareStatement(
                            "insert into orders values (?,?,?,?,?,?,?,?,?,?,?);");
                    try {
                        prep.setInt(1, Main.orderNum);
                        prep.setInt(2, Integer.parseInt(packet2));
                        prep.setString(3, Main.x);
                        prep.setString(4, emailA);
                        prep.setString(5, packetAStartDate);
                        prep.setString(6, packetAEndtDate);
                        if(addition2_txtfld.getText().isEmpty())
                            prep.setInt(7, 0);
                        else
                            prep.setInt(7, Integer.parseInt(addition2_txtfld.getText()));
                        prep.setString(8, paypalA);
                        prep.setString(9, "ממתין לאישור");
                        prep.setString(10, "false");
                        prep.setString(11, "true");
                        prep.addBatch();
                        conn.setAutoCommit(false);
                        prep.executeBatch();
                        conn.setAutoCommit(true);
                        showAlertInfo("הגשת בקשת ההחלפה בוצעה בהצלחה");
                        Main.orderNum++;
                    } catch (SQLException e) {
                        conn.close();
                        return;
                    }

                    //Main.orderNum++;
                    //showAlertInfo("הגשת בקשת ההחלפה בוצעה בהצלחה");
                    conn.close();
                }
            }
            else
            {
                showAlertError("מספר חבילה לא קיים!");
            }
            conn.close();

        }
        //regular rental
        else
        {
            String packet = packet_txtfld.getText();
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
            PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM packages WHERE packNum =?");
            statement1.setInt(1, Integer.parseInt(packet));
            ResultSet rs1=statement1.executeQuery();
            packetAStartDate=rs1.getString("startDate");
            packetAEndtDate=rs1.getString("endDate");
            emailA=rs1.getString("email");
            price=rs1.getInt("price");
            PreparedStatement statement2 = conn.prepareStatement("SELECT * FROM users WHERE email =?");
            statement2.setString(1, Main.x);
            ResultSet rs2=statement2.executeQuery();
            paypalB=rs2.getString("paypalAccount");


            PreparedStatement prep = conn.prepareStatement(
                    "insert into orders values (?,?,?,?,?,?,?,?,?,?,?);");
            try {
                prep.setInt(1, Main.orderNum);
                prep.setInt(2, Integer.parseInt(packet));
                prep.setString(3, emailA);
                prep.setString(4, Main.x);
                prep.setString(5, packetAStartDate);
                prep.setString(6, packetAEndtDate);
                prep.setInt(7, price);
                prep.setString(8, paypalB);
                prep.setString(9, "ממתין לאישור");
                prep.setString(10, "false");
                prep.setString(11, "false");
                prep.addBatch();
                conn.setAutoCommit(false);
                prep.executeBatch();
                conn.setAutoCommit(true);
                Main.orderNum++;
                conn.close();
                showAlertInfo("הגשת בקשת השכירות בוצעה בהצלחה");
            } catch (SQLException e) {
                conn.close();
            }

        }

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
}
