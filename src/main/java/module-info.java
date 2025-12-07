module oop.demo1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens oop.demo1 to javafx.fxml;
    exports oop.demo1;
}