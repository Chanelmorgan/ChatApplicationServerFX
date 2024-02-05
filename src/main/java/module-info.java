module com.example.chatapplicationserverfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.chatapplicationserverfx to javafx.fxml;
    exports com.example.chatapplicationserverfx;
}