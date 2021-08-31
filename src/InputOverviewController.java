import javafx.application.Application;
import javafx.scene.control.*;
import javafx.fxml.FXML;

import java.io.IOException;


public class InputOverviewController {

    @FXML
    private ComboBox<String> comboBox1;
    @FXML
    private ComboBox<String> comboBox2;
    @FXML
    private ComboBox<String> comboBox3;
    @FXML
    private ComboBox<String> comboBox4;
    @FXML
    private ComboBox<String> comboBox5;
    @FXML
    private ComboBox<String> comboBox6;
    @FXML
    private Button button1;

    @FXML
    private void initialize() {

        this.comboBox1.getItems().setAll(
                "м/с",
                "км/ч",
                "knot"
        );

        this.comboBox2.getItems().setAll(
                "м",
                "км",
                "фт"
        );

        this.comboBox3.getItems().setAll(
                "м/с",
                "км/ч",
                "knot"
        );

        this.comboBox4.getItems().setAll(
                "м",
                "км",
                "фт"
        );

        this.comboBox5.getItems().setAll(
                "Па",
                "кПа",
                "кг/м^2"
        );

        this.comboBox6.getItems().setAll(
                "K",
                "C",
                "F"
        );

        this.comboBox1.getSelectionModel().select(1);
        this.comboBox2.getSelectionModel().select(0);
        this.comboBox3.getSelectionModel().select(1);
        this.comboBox4.getSelectionModel().select(0);
        this.comboBox5.getSelectionModel().select(0);
        this.comboBox6.getSelectionModel().select(0);

    }



    public void onComboBoxChanged() {

        // значение выбрано в combobox
        String valueCB1 = comboBox1.getSelectionModel().getSelectedItem();
        String valueCB2 = comboBox2.getSelectionModel().getSelectedItem();
        String valueCB3 = comboBox3.getSelectionModel().getSelectedItem();
        String valueCB4 = comboBox4.getSelectionModel().getSelectedItem();
        String valueCB5 = comboBox5.getSelectionModel().getSelectedItem();
        String valueCB6 = comboBox6.getSelectionModel().getSelectedItem();

    }

    public void onButtonClick() throws IOException {




    }





}
