package oop.demo1;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.ArrayList;

public class HelloController
{
    @javafx.fxml.FXML
    private Label notification;
    @javafx.fxml.FXML
    private TextField txt2;
    @javafx.fxml.FXML
    private TextField txt1;
    @javafx.fxml.FXML
    private TableColumn<hello, String> c1;
    @javafx.fxml.FXML
    private TableColumn<hello, String> c2;

    ArrayList<hello> hlist;
    @javafx.fxml.FXML
    private TableView<hello> table;
    @javafx.fxml.FXML
    private TextField txt3;

    @javafx.fxml.FXML
    public void initialize() {
        hlist = new ArrayList<>();

        c1.setCellValueFactory(new PropertyValueFactory<hello, String>("data1"));
        c2.setCellValueFactory(new PropertyValueFactory<hello, String>("data2"));
        loadtable(new ActionEvent());

    }

    @javafx.fxml.FXML
    public void saveToBinFIle(ActionEvent actionEvent) {
        String data1 = txt1.getText();
        String data2 = txt2.getText();

        hello obj = new hello(data1, data2);
        hlist.add(obj);

        try {
            File dir = new File("data");
            if (!dir.exists()) dir.mkdir();

            File file = new File(dir, "data.bin");

            FileOutputStream fos;
            ObjectOutputStream oos;

            if (!file.exists() || file.length() == 0) {
                fos = new FileOutputStream(file);          // new/empty file -> normal header
                oos = new ObjectOutputStream(fos);
            } else {
                fos = new FileOutputStream(file, true);    // existing file -> append
                oos = new AppendableObjectOutputStream(fos);                     // no header
            }

            oos.writeObject(obj); // ✅ write only the new object

            oos.close();
            notification.setText("File Saved");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            notification.setText("Save Failed");
        }
    }



    @javafx.fxml.FXML
    public void loadtable(ActionEvent actionEvent) {
        table.getItems().clear();
        try {
            File file = new File("data/data.bin");
            if(!file.exists() ||  file.length() == 0) return;

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true){
                try {
                    table.getItems().add((hello) ois.readObject());
                }catch (EOFException eof){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }

    }

    @javafx.fxml.FXML
    public void searchData(ActionEvent actionEvent) {
        String data = txt3.getText();

        try {
            File file = new File("data/data.bin");
            if (!file.exists() || file.length() == 0) {
                notification.setText("No Data File");
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            boolean found = false;

            while (true) {
                try {
                    hello obj = (hello) ois.readObject();
                    if (obj != null && obj.toString().contains(data)) {
                        found = true;
                        table.getItems().clear();
                        table.getItems().add(obj);
                        break;
                    }
                } catch (EOFException eof) {
                    break;
                }
            }

            ois.close();
            notification.setText(found ? "Data Found" : "Data Not Found");

        } catch (Exception e) {
            System.out.println("Error checking file: " + e.getMessage());
        }
    }



    public static class AppendableObjectOutputStream extends ObjectOutputStream {
        public AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        protected void writeStreamHeader() throws IOException {}

    }
}