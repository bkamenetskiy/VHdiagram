import enums.UnitInput;
import enums.UnitOutput;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
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

    // значения по умолчанию: 0 - Vd; 1 - Vc; 2 - Va; 3 - Vs; 4 - Md; 5 - Mc; 6 - Hmin; 7 - Hmax; 8 - dH
    private String[] defaultValue = new String[] {"172.7777", "158.3333", "135.2777", "55.8333", "0.88", "0.82", "-300.0", "12200.0", "1.0"};

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
                "км",
                "фут"
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
        this.textField1.clear();
        this.textField1.appendText(defaultValue[0]);
        this.inputVelocity[0] = Double.parseDouble(textField1.getText());
        this.textField2.clear();
        this.textField2.appendText(defaultValue[1]);
        this.inputVelocity[1] = Double.parseDouble(textField2.getText());
        this.textField3.clear();
        this.textField3.appendText(defaultValue[2]);
        this.inputVelocity[2] = Double.parseDouble(textField3.getText());
        this.textField4.clear();
        this.textField4.appendText(defaultValue[3]);
        this.inputVelocity[3] = Double.parseDouble(textField4.getText());
        this.textField5.clear();
        this.textField5.appendText(defaultValue[4]);
        this.inputMaxM[0] = Double.parseDouble(textField5.getText());
        this.textField6.clear();
        this.textField6.appendText(defaultValue[5]);
        this.inputMaxM[1] = Double.parseDouble(textField6.getText());
        this.textField7.clear();
        this.textField7.appendText(defaultValue[6]);
        this.inputAltitude[0] = Double.parseDouble(textField7.getText());
        this.textField8.clear();
        this.textField8.appendText(defaultValue[7]);
        this.inputAltitude[1] = Double.parseDouble(textField8.getText());
        this.textField9.clear();
        this.textField9.appendText(defaultValue[8]);
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
            case 2:
                this.unitInput[0] = UnitInput.Foot;
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

        String initialText = this.textField1.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField1.getText().length() != 0) {
            this.textField1.clear();
            this.textField1.appendText(finalText.toString());
            this.inputVelocity[0] = Double.parseDouble(textField1.getText());
        }

        //Alert alert = new Alert(Alert.AlertType.ERROR, "Ввод недопустимого символа", ButtonType.OK);
        //alert.showAndWait();
    }

    public void onTextField2Changed() {

        String initialText = this.textField2.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField2.getText().length() != 0) {
            this.textField2.clear();
            this.textField2.appendText(finalText.toString());
            this.inputVelocity[1] = Double.parseDouble(textField2.getText());
        }

    }

    public void onTextField3Changed() {

        String initialText = this.textField3.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField3.getText().length() != 0) {
            this.textField3.clear();
            this.textField3.appendText(finalText.toString());
            this.inputVelocity[2] = Double.parseDouble(textField3.getText());
        }

    }

    public void onTextField4Changed() {

        String initialText = this.textField4.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField4.getText().length() != 0) {
            this.textField4.clear();
            this.textField4.appendText(finalText.toString());
            this.inputVelocity[3] = Double.parseDouble(textField4.getText());
        }
    }

    public void onTextField5Changed() {

        String initialText = this.textField5.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField5.getText().length() != 0) {
            this.textField5.clear();
            this.textField5.appendText(finalText.toString());
            this.inputMaxM[0] = Double.parseDouble(this.textField5.getText());
        }
    }

    public void onTextField6Changed() {

        String initialText = this.textField6.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField6.getText().length() != 0) {
            this.textField6.clear();
            this.textField6.appendText(finalText.toString());
            this.inputMaxM[1] = Double.parseDouble(this.textField6.getText());
        }
    }

    public void onTextField7Changed() {

        String initialText = this.textField7.getText();
        StringBuilder finalText = getCorrectText(initialText, true);

        if (this.textField7.getText().length() != 0) {
            this.textField7.clear();
            this.textField7.appendText(finalText.toString());
            this.inputAltitude[0] = Double.parseDouble(this.textField7.getText());
        }

    }

    public void onTextField8Changed() {

        String initialText = this.textField8.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField8.getText().length() != 0) {
            this.textField8.clear();
            this.textField8.appendText(finalText.toString());
            this.inputAltitude[1] = Double.parseDouble(this.textField8.getText());
        }
    }

    public void onTextField9Changed(KeyEvent event) {

        String initialText = this.textField9.getText();
        StringBuilder finalText = getCorrectText(initialText, false);

        if (this.textField9.getText().length() != 0) {
            this.textField9.clear();
            this.textField9.appendText(finalText.toString());
            this.inputAltitude[2] = Double.parseDouble(this.textField9.getText());
        }
    }

    public void onButtonClick(ActionEvent event) throws IOException {

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

    private StringBuilder getCorrectText (String initialText, boolean keyNegativeValue) {

        StringBuilder finalText = new StringBuilder();
        char ch;

        if ((!isValidateDot(initialText)) || (!isValidateABC(initialText, keyNegativeValue)))  {

            for (int i = 0; i <= initialText.length() - 2; i++) {

                ch = initialText.charAt(i);
                finalText.append(ch);
            }
        }
        else {
            finalText.append(initialText);
        }

        return finalText;
    }

    private boolean isValidateABC (String text, boolean keyNegativeValue) {

        boolean validate = false;
        char ch;

        if (!keyNegativeValue) {
            for (int i = 0; i <= text.length() - 1 ; i++) {

                ch = text.charAt(i);
                validate = (ch == '.') || (ch >= '0' && ch <= '9');

            }
        }

        if (keyNegativeValue) {
            for (int i = 0; i <= text.length() - 1 ; i++) {

                ch = text.charAt(i);
                validate = (ch == '.') || (ch >= '0' && ch <= '9') || (ch == '-');

            }
        }
        return validate;
    }


    private boolean isValidateDot (String text){

        boolean validate = false;
        char ch;
        int inc = 0;

        for (int i = 0; i <= text.length() - 1 ; i++) {

            ch = text.charAt(i);

            if (ch == '.') {

                inc++;
            }
        }

        if (inc <= 1) {

            validate = true;
        }

        return validate;
    }

    private boolean isValidateNegative (String value){

        return !(Double.parseDouble(value) <= 0);
    }







}




/*
    private boolean isValid(char ch) {

        return (ch >= '0' && ch <= '9') || (ch == '-') || (ch == '.');
    }


    private boolean validate(String text) {

        return text.matches("[0-9]");
    }
 */
