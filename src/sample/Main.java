package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {

    public static String x="";
    public static int packNum=0;
    public static int itenNum=0;
    public static int orderNum=0;

    @Override
    public void start(Stage primaryStage) throws Exception{
        getIDS();
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Everything4Rent");
        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void getIDS() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "SELECT MAX(packnum) as packnum FROM packItems");
        ResultSet rs=prep.executeQuery();
        if(rs.next())
            packNum=Integer.valueOf(rs.getString("packnum"))+1;

        prep = conn.prepareStatement(
                "SELECT MAX(itemnum) as itemnum FROM packItems");
        rs=prep.executeQuery();
        if(rs.next())
            itenNum=Integer.valueOf(rs.getString("itemnum"))+1;

        prep = conn.prepareStatement(
                "SELECT MAX(orderNum) as orderNum FROM orders");
        rs=prep.executeQuery();
        if(rs.next())
            orderNum=Integer.valueOf(rs.getString("orderNum"))+1;
        conn.close();
    }
}
