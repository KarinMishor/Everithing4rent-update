package sample;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class regController extends JFrame {
    static int count=0;
    @FXML
    public PasswordField pass1fld;
    public PasswordField pass2fld;
    @FXML
    Label passMsg=new Label();
    public Button regbtm;
    public TextField fullNamefld;
    public TextField addressfld;
    public TextField phoneNmbfld;
    public TextField paypalAccount;
    public DatePicker birthdayfld;
    public Button picfld;
    public TextField emailfld;
    private String picname="";


    @FXML
    public void initialize() {
        Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell(){
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isAfter(LocalDate.now().minusYears(18))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffcccc;");
                        }
                    }
                };
            }
        };
        birthdayfld.setDayCellFactory(dayCellFactory);
        birthdayfld.setValue(LocalDate.now().minusYears(18));
        //test
        for(int k=0; k<100;k++){}
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

    public void register() throws Exception {
        if(!pass1fld.getText().equals(pass2fld.getText())) {
            showAlertError("הסיסמאות לא תואמות");
            return;
        }
        if(!isvalidemail(emailfld.getText())) {
            showAlertError("אימייל לא חוקי");
            return;
        }
        if( pass1fld.getLength()<6) {
            showAlertError("אנא הכנס סיסמא בת 6 תווים");
            return;
        }
        if(pass1fld.getText().isEmpty() ||pass2fld.getText().isEmpty() || emailfld.getText().isEmpty() || fullNamefld.getText().isEmpty() ||
                picname.equals("") || birthdayfld.getValue()==null || addressfld.getText().isEmpty() || phoneNmbfld.getText().isEmpty()
                || paypalAccount.getText().isEmpty()){
            showAlertError("עליך למלא את כל השדות");
            return;
        }
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:db.db");
        PreparedStatement prep = conn.prepareStatement(
                "insert into users values (?,?,?,?,?,?,?,?);");
            try {
            prep.setString(1, emailfld.getText());
            prep.setString(2, pass1fld.getText());
            prep.setString(3, fullNamefld.getText());
            String s = birthdayfld.getValue().toString();
            prep.setString(4, birthdayfld.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            prep.setString(5, picname);
                prep.setString(6, addressfld.getText());
                prep.setString(7, paypalAccount.getText());
                prep.setString(8, phoneNmbfld.getText());
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);
            conn.close();
        }catch (SQLException e)
        {
            showAlertError("כתובת דואר אלקטרוני זו כבר קיימת במערכת");
            emailfld.clear();
            conn.close();
            return;
        }

        sendmail(emailfld.getText(),fullNamefld.getText());
        showAlertInfo("Success");
        Stage stage = (Stage) regbtm.getScene().getWindow();
        stage.close();

    }

    public static boolean isvalidemail(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public void uploadPicure() throws Exception {
        FileChooser fileC = new FileChooser();
        fileC.setTitle("Open Image File");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter fileExtensions1 = new FileChooser.ExtensionFilter("JPEG", "*.png");
        fileC.getExtensionFilters().add(fileExtensions);
        fileC.getExtensionFilters().add(fileExtensions1);
        File f = fileC.showOpenDialog((Stage) emailfld.getScene().getWindow());
        if (f != null) {
            picname= f.getAbsolutePath().toString();
            return;
        }
        picname="";
    }

    public void sendmail(String mail,String fullNAme){
        final String from="everything4rentbest@gmail.com";
        final String password="0523495561";

        Properties props=new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail));
            message.setSubject("Thank you for you registration to Everything4Rent");
            message.setText("Dear "+fullNAme+" ,"
                    + "\n\n Thank you for your registration \n\n" +
                    "now you can rent Everything! \n\n" +
            "--------------------------------------------------------- \n\n");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
