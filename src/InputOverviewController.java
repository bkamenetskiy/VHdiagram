import com.microsoft.schemas.vml.ImageDocument;
import enums.UnitInput;
import enums.UnitOutput;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class InputOverviewController {

    UnitInput[] unitInput = new UnitInput[2];
    UnitOutput[] unitOutput = new UnitOutput[7];
    String path;

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
    private TextField textField8;


    @FXML
    private void initialize() {

        // высота
        this.comboBox1.getItems().setAll(
                "м",
                "км"
        );

        // скорость
        this.comboBox2.getItems().setAll(
                "м/с",
                "км/ч",
                "knot"
        );

        // высота
        this.comboBox3.getItems().setAll(
                "м",
                "км"
        );

        // скорость
        this.comboBox4.getItems().setAll(
                "м/с",
                "км/ч",
                "knot"
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

        this.comboBox1.getSelectionModel().select(0);   // значение по умолчанию - м
        this.comboBox2.getSelectionModel().select(0);   // значение по умолчанию - м/с

        this.comboBox3.getSelectionModel().select(0);   // значение по умолчанию - м
        this.comboBox4.getSelectionModel().select(0);   // значение по умолчанию - м/с
        this.comboBox5.getSelectionModel().select(0);   // значение по умолчанию - Па
        this.comboBox6.getSelectionModel().select(0);   // значение по уполчанию - К

        this.unitInput[0] = UnitInput.Meter;                                                                                 // высота
        this.unitInput[1] = UnitInput.MeterPerSecond;                                                                        // скорость

        this.unitOutput[0] = UnitOutput.Meter;                                                                               // высота метрическая
        this.unitOutput[1] = UnitOutput.Foot;                                                                                // высота имперская
        this.unitOutput[2] = UnitOutput.MeterPerSecond;                                                                      // скорость
        this.unitOutput[3] = UnitOutput.KgPerM3;                                                                             // плотность
        this.unitOutput[4] = UnitOutput.Pa;                                                                                  // давление
        this.unitOutput[5] = UnitOutput.Kelvin;                                                                              // температура
        this.unitOutput[6] = UnitOutput.Dimensionless_Mach;                                                                  // безразмерный мах


        this.path = "d:\\Result.xlsx";

        //System.out.println(this.path);
        System.out.println(unitInput[0] + "  " + unitInput[1]);
        System.out.println(unitOutput[0] + "  " + unitOutput[1] + "   " + unitOutput[2]+ "   " + unitOutput[3]+ "   " + unitOutput[4]+ "   " + unitOutput[5]+ "   " + unitOutput[6]);

    }

    public void onComboBoxChanged() {

        // единицы измерения исходных данных
        // высота
        switch (comboBox1.getSelectionModel().getSelectedIndex()) {
            case 0:
                this.unitInput[0] = UnitInput.Meter;
                break;
            case 1:
                this.unitInput[0] = UnitInput.Kilometer;
                break;
            default:
                this.unitInput[0] = UnitInput.Meter;
                break;
        }

        // скорость
        switch (comboBox2.getSelectionModel().getSelectedIndex()) {
            case 0:
                this.unitInput[1] = UnitInput.MeterPerSecond;
                break;
            case 1:
                this.unitInput[1] = UnitInput.KilometerPerHr;
                break;
            case 2:
                this.unitInput[1] = UnitInput.Knot;
                break;
            default:
                this.unitInput[1] = UnitInput.MeterPerSecond;
                break;
        }

        // единицы измерения результата
        // высота
        switch (comboBox3.getSelectionModel().getSelectedIndex()) {
            case 0:
                this.unitOutput[0] = UnitOutput.Meter;
                break;
            case 1:
                this.unitOutput[0] = UnitOutput.Kilometer;
                break;
            default:
                this.unitOutput[0] = UnitOutput.Meter;
                break;
        }

        // скорость
        switch (comboBox4.getSelectionModel().getSelectedIndex()) {
            case 0:
                this.unitOutput[2] = UnitOutput.MeterPerSecond;
                break;
            case 1:
                this.unitOutput[2] = UnitOutput.KilometerPerHr;
                break;
            case 2:
                this.unitOutput[2] = UnitOutput.Knot;
                break;
            default:
                this.unitOutput[2] = UnitOutput.MeterPerSecond;
                break;
        }

        // давление
        switch (comboBox5.getSelectionModel().getSelectedIndex()) {
            case 0:
                this.unitOutput[4] = UnitOutput.Pa;
                break;
            case 1:
                this.unitOutput[4] = UnitOutput.KPa;
                break;
            case 2:
                this.unitOutput[4] = UnitOutput.KgsPerM2;
                break;
            default:
                this.unitOutput[4] = UnitOutput.Pa;
                break;
        }

        // температура
        switch (comboBox6.getSelectionModel().getSelectedIndex()) {
            case 0:
                this.unitOutput[5] = UnitOutput.Kelvin;
                break;
            case 1:
                this.unitOutput[5] = UnitOutput.Celsius;
                break;
            case 2:
                this.unitOutput[5] = UnitOutput.Fahrenheit;
                break;
            default:
                this.unitOutput[5] = UnitOutput.Kelvin;
                break;
        }

    }

    @FXML
    public void onTextField8Changed() {

        String aaa = "";
        aaa = textField8.getText();
        System.out.println(aaa);

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select directory for save");
        fileChooser.setInitialFileName("Result");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        fileChooser.setInitialDirectory(new File("D:\\"));
        File file = fileChooser.showSaveDialog(stage);
        this.path = file.getAbsolutePath();

        System.out.println(path);
    }



    public void onButtonClick() throws IOException {



    }





}
