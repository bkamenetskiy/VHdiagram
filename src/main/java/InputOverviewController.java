import enums.UnitInput;
import enums.UnitOutput;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javax.swing.filechooser.FileSystemView;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class InputOverviewController {

    private static final Pattern DECIMAL_INPUT = Pattern.compile("[0-9]*(?:\\.[0-9]*)?");
    private static final Pattern SIGNED_DECIMAL_INPUT = Pattern.compile("-?" + DECIMAL_INPUT.pattern());
    private static final Pattern POSITIVE_INTEGER_INPUT = Pattern.compile("(?:[1-9][0-9]*)?");
    private static final String SAVE_ERROR = "Ошибка при сохранении файла";

    private final UnitInput[] unitInput = new UnitInput[2];
    private final UnitOutput[] unitOutput = new UnitOutput[7];

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
    private Label saveStatus;
    @FXML
    private Hyperlink savedFileLink;
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
    private CheckBox simplifiedPlotting;
    @FXML
    private HBox plotStepControls;
    @FXML
    private TextField plotStepField;
    @FXML
    private Label plotStepUnit;


    /**
     * Настраивает единицы измерения, числовые поля и отображение шага построения.
     * Кнопка расчёта доступна, когда заполнены все видимые поля ввода.
     */
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

        onComboBoxChanged();

        // Фиксированные единицы измерения результата.
        this.unitOutput[1] = UnitOutput.Foot;
        this.unitOutput[3] = UnitOutput.KgPerM3;
        this.unitOutput[6] = UnitOutput.Dimensionless_Mach;

        TextField[] numericFields = {textField1, textField2, textField3, textField4, textField5,
                textField6, textField7, textField8, textField9};
        for (TextField field : numericFields) {
            Pattern inputPattern = field == textField7 ? SIGNED_DECIMAL_INPUT : DECIMAL_INPUT;
            field.setTextFormatter(new TextFormatter<>(change -> filterDecimalInput(change, inputPattern)));
        }

        configurePlotStep();
        Observable[] fieldLengths = Arrays.stream(numericFields)
                .map(TextField::lengthProperty)
                .toArray(Observable[]::new);
        button1.disableProperty().bind(Bindings.createBooleanBinding(
                () -> Arrays.stream(numericFields).anyMatch(field -> field.getLength() == 0), fieldLengths)
                .or(simplifiedPlotting.selectedProperty().and(plotStepField.textProperty().isEmpty())));
        savedFileLink.managedProperty().bind(savedFileLink.visibleProperty());
    }

    /**
     * Показывает шаг при упрощённом построении и выводит выбранную единицу исходной высоты.
     * Фильтр допускает положительные целые числа и позволяет очистить поле.
     */
    private void configurePlotStep() {
        plotStepControls.visibleProperty().bind(simplifiedPlotting.selectedProperty());
        plotStepControls.managedProperty().bind(plotStepControls.visibleProperty());
        plotStepUnit.textProperty().bind(comboBox1.valueProperty());
        plotStepField.setTextFormatter(new TextFormatter<>(change ->
                POSITIVE_INTEGER_INPUT.matcher(change.getControlNewText()).matches() ? change : null));
    }

    /**
     * Заменяет запятую точкой и пропускает правку, если итоговый текст соответствует формату поля.
     */
    private TextFormatter.Change filterDecimalInput(TextFormatter.Change change, Pattern inputPattern) {
        change.setText(change.getText().replace(',', '.'));
        return inputPattern.matcher(change.getControlNewText()).matches() ? change : null;
    }

    /**
     * Считывает выбранные в форме единицы измерения для исходных данных и результатов.
     */
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

    /**
     * Проверяет поля и предлагает сохранить диаграмму, начиная с рабочего стола.
     */
    public void onButtonClick() {
        showSaveStatus(null, "");
        double[] inputVelocity;
        double[] inputMaxM;
        double[] inputAltitude;
        try {
            inputVelocity = readNumbers(textField1, textField2, textField3, textField4);
            inputMaxM = readNumbers(textField5, textField6);
            inputAltitude = readNumbers(textField7, textField8, textField9);
        } catch (NumberFormatException ex) {
            new Alert(Alert.AlertType.ERROR, "Проверьте числовые значения во всех полях ввода.").showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select directory for save");
        fileChooser.setInitialFileName("Result");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel", "*.xlsx"));
        File desktop = FileSystemView.getFileSystemView().getHomeDirectory();
        if (desktop != null && desktop.isDirectory()) {
            fileChooser.setInitialDirectory(desktop);
        }
        File file = fileChooser.showSaveDialog(button1.getScene().getWindow());
        saveDiagram(file, inputVelocity, inputMaxM, inputAltitude);
    }

    /**
     * Запускает расчёт и экспорт, затем отображает итог записи выбранного файла.
     */
    private void saveDiagram(File file, double[] inputVelocity, double[] inputMaxM, double[] inputAltitude) {
        if (file == null) {
            showSaveStatus(null, SAVE_ERROR);
            return;
        }

        try {
            BackEnd back = new BackEnd(inputVelocity, inputMaxM, inputAltitude, unitInput, unitOutput, file.getAbsolutePath());
            back.back();
            showSaveStatus(file, "");
        } catch (IOException | RuntimeException ex) {
            showSaveStatus(null, SAVE_ERROR);
            ex.printStackTrace();
        }
    }

    /**
     * Показывает ссылку на сохранённый файл или текст статуса рядом с кнопкой расчёта.
     */
    private void showSaveStatus(File savedFile, String message) {
        saveStatus.setText(message);
        savedFileLink.setOnAction(savedFile == null ? null : event -> openSavedFile(savedFile));
        savedFileLink.setVisited(false);
        savedFileLink.setVisible(savedFile != null);
    }

    /**
     * Открывает сохранённую книгу в приложении, выбранном системой для файлов Excel.
     */
    private void openSavedFile(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException | IllegalArgumentException | UnsupportedOperationException | SecurityException ex) {
            new Alert(Alert.AlertType.ERROR, "Не удалось открыть сохраненный файл.").showAndWait();
        }
    }

    /**
     * Преобразует текст полей в массив конечных чисел в порядке передачи полей.
     *
     * @throws NumberFormatException если текст поля нельзя преобразовать в конечное число
     */
    private double[] readNumbers(TextField... fields) {
        double[] numbers = new double[fields.length];
        for (int i = 0; i < fields.length; i++) {
            double number = Double.parseDouble(fields[i].getText());
            if (!Double.isFinite(number)) {
                throw new NumberFormatException();
            }
            numbers[i] = number;
        }
        return numbers;
    }
}
