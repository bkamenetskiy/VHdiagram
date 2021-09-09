import enums.UnitInput;
import enums.UnitOutput;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;

public class InputOverviewController {

    private final UnitInput[] unitInput = new UnitInput[2];
    private final UnitOutput[] unitOutput = new UnitOutput[7];
    private String path;
    private final double[] inputMaxM = new double[2];
    private final double[] inputVelocity = new double[4];
    private final double[] inputAltitude = new double[3];

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
    private TextField textField1;   // Vd
    @FXML
    private TextField textField2;   // Vc
    @FXML
    private TextField textField3;   // Va
    @FXML
    private TextField textField4;   // Vs
    @FXML
    private TextField textField5;   // max Md
    @FXML
    private TextField textField6;   // max Mc
    @FXML
    private TextField textField7;   // H min
    @FXML
    private TextField textField8;   // H max
    @FXML
    private TextField textField9;   // dH


    @FXML
    protected void initialize() {

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

        // значения комбобоксов по умолчанию
        this.comboBox1.getSelectionModel().select(0);   // значение по умолчанию - м
        this.comboBox2.getSelectionModel().select(0);   // значение по умолчанию - м/с

        this.comboBox3.getSelectionModel().select(0);   // значение по умолчанию - м
        this.comboBox4.getSelectionModel().select(0);   // значение по умолчанию - м/с
        this.comboBox5.getSelectionModel().select(0);   // значение по умолчанию - Па
        this.comboBox6.getSelectionModel().select(0);   // значение по уполчанию - К

        // значения единиц измерения по умолчанию
        this.unitInput[0] = UnitInput.Meter;                                                                                 // высота
        this.unitInput[1] = UnitInput.MeterPerSecond;                                                                        // скорость

        this.unitOutput[0] = UnitOutput.Meter;                                                                               // высота метрическая
        this.unitOutput[1] = UnitOutput.Foot;                                                                                // высота имперская
        this.unitOutput[2] = UnitOutput.MeterPerSecond;                                                                      // скорость
        this.unitOutput[3] = UnitOutput.KgPerM3;                                                                             // плотность
        this.unitOutput[4] = UnitOutput.Pa;                                                                                  // давление
        this.unitOutput[5] = UnitOutput.Kelvin;                                                                              // температура
        this.unitOutput[6] = UnitOutput.Dimensionless_Mach;                                                                  // безразмерный мах

        // значения скоростей и высот
        this.inputVelocity[0] = Double.parseDouble(textField1.getText());
        this.inputVelocity[1] = Double.parseDouble(textField2.getText());
        this.inputVelocity[2] = Double.parseDouble(textField3.getText());
        this.inputVelocity[3] = Double.parseDouble(textField4.getText());
        this.inputMaxM[0] = Double.parseDouble(textField5.getText());
        this.inputMaxM[1] = Double.parseDouble(textField6.getText());
        this.inputAltitude[0] = Double.parseDouble(textField7.getText());
        this.inputAltitude[1] = Double.parseDouble(textField8.getText());
        this.inputAltitude[2] = Double.parseDouble(textField9.getText());

        this.path = "c:\\Result.xlsx";

    }

    // изменение позиций в комбобоксах
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

    public void onTextField1Changed() {

        this.inputVelocity[0] = Double.parseDouble(textField1.getText());

        //Alert alert = new Alert(Alert.AlertType.ERROR, "Ввод недопустимого символа", ButtonType.OK);
        //alert.showAndWait();
    }

    public void onTextField2Changed() {

        this.inputVelocity[1] = Double.parseDouble(textField2.getText());

    }

    public void onTextField3Changed() {

        this.inputVelocity[2] = Double.parseDouble(textField3.getText());

    }

    public void onTextField4Changed() {

        this.inputVelocity[3] = Double.parseDouble(textField4.getText());

    }

    public void onTextField5Changed() {

        this.inputMaxM[0] = Double.parseDouble(textField5.getText());

    }

    public void onTextField6Changed() {

        this.inputMaxM[1] = Double.parseDouble(textField6.getText());

    }

    public void onTextField7Changed() {

        this.inputAltitude[0] = Double.parseDouble(textField7.getText());

    }

    public void onTextField8Changed() {

        this.inputAltitude[1] = Double.parseDouble(textField8.getText());

    }

    public void onTextField9Changed() {

        this.inputAltitude[2] = Double.parseDouble(textField9.getText());

    }

    public void onButtonClick() throws IOException {

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select directory for save");
        fileChooser.setInitialFileName("Result");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        fileChooser.setInitialDirectory(new File("D:\\"));
        File file = fileChooser.showSaveDialog(stage);
        this.path = file.getAbsolutePath();

        BackEnd back = new BackEnd(this.inputVelocity, this.inputMaxM, this.inputAltitude, this.unitInput, this.unitOutput, this.path);
        back.back();


    }

    private boolean isValid(char ch) {
        return (ch >= '0' && ch <= '9') || (ch == '-') || (ch == '.');
    }


    private boolean validate(String text)
    {
        return text.matches("[0-9]");
    }






}
