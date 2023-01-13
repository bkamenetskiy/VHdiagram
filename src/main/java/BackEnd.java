import enums.UnitInput;
import enums.UnitOutput;
import java.io.IOException;
import java.util.ArrayList;

public class BackEnd {

    public BackEnd (double[] inputVelocity, double[] inputMaxM, double[] inputAltitude, UnitInput[] unitInput, UnitOutput[] unitOutput,
                    String path)
    {
        this.inputVelocity = inputVelocity;
        this.inputMaxM = inputMaxM;
        this.inputAltitude = inputAltitude;
        this.unitInput = unitInput;
        this.unitOutput = unitOutput;
        this.path = path;
    }

    private double[] inputVelocity;
    private double[] inputMaxM;
    private double[] inputAltitude;
    private UnitInput[] unitInput;
    private UnitOutput[] unitOutput;
    private String path;

    protected void back() {

        // индикаторные земные скорости Vcas в следующем порядке: Vd; Vc; Va; Vs
        double[] inputVelocity = new double[] {622.0, 570.0, 487.0, 201.0}; // 172.777 158.333 135.2777 55.8333 м/с

        // законодательное ограничение скоростей Vd и Vc по числу М: 0 - max Md; 1 - max Mc
        double[] inputMaxM = new double[] {0.88, 0.82};

        // диапазоны высот и приращение
        double[] inputAltitude = new double[] {-300.0, 12200.0, 1.0};

        // единицы измерения ввода
        // 0. Единицы измерения высоты метрические:
        // 0 - метры;
        // 1 - километры.
        // 1. Единицы измерения скорости:
        // 0 - м/с;
        // 1 - км/ч;
        // 2 - knot.
        UnitInput[] unitInput = new UnitInput[] {UnitInput.Meter, UnitInput.KilometerPerHr};

        // единицы измерения вывода
        // 0. Единицы измерения высоты метрические:
        // 0 - метры;
        // 1 - километры.
        // 0. Единицы измерения высоты имперские:
        // 0 - футы.
        // 2. Единицы измерения скорости:
        // 0 - м/с;
        // 1 - км/ч;
        // 2 - knot.
        // 3. Единицы измерения плотности:
        // 0 - кг/м3.
        // 4. Единицы измерения давления:
        // 0 - Па;
        // 1 - кг/м2.
        // 5. Единицы измерения температуры:
        // 0 - K;
        // 1 - C;
        // 2 - F.
        UnitOutput[] unitOutput = new UnitOutput[] {UnitOutput.Kilometer, UnitOutput.Foot, UnitOutput.KilometerPerHr, UnitOutput.KgPerM3,
                UnitOutput.Pa, UnitOutput.Kelvin, UnitOutput.Dimensionless_Mach};

        // ограничения для скоростей в следующем порядке: Vd, Vc, Va, Vs
        // типы ограничений: 1 - ограничение сверху по значению маха; 2 - ограничение сверху махом другой скорости; 0 - ограничения отсутствуют
        int[] settingLimitType = new int[] {1, 1, 2, 0};

        // относительные положения параметров (внутренние смещения) в соответствующих массивах (блоках):
        // 0. Блок высот:
        // 0 - положение высоты (в метрах) в блоке высот (индекс) - по ним проходят внутренние рассчеты;
        // 1 - положение высоты (в футах) в блоке высот (индекс) - заразервирована, не используется;
        // 2 - положение высоты в исходных данных без конвертации.
        // 1. Блок параметров атмосферы:
        // 0 - положение плотноти (индекс);
        // 1 - положение атмосферного давления (индекс);
        // 2 - положение температуры (индекс);
        // 3 - положение скорости звука (индекс).
        // 2. Блок скоростей:
        // 0 - относительное положение в блоке V (CAS) (индекс);
        // 1 - относительное положение числа Маха (индекс);
        // 2 - относительное положение V (TAS) (индекс);
        // 3 - относительное положение V (EAS) (индекс);
        // 4 - относительное положение q (индекс).
        int[] internalOffsetsAltitude = new int[] {0, 1, 2};
        int[] internalOffsetsAtmParam = new int[] {0, 1, 2, 3};
        int[] internalOffsetsVelocity = new int[] {0, 1, 2, 3, 4};

        // путь к файлу результатов
        String path = "d:\\result.xlsx";



        ArrayList <int []> listInternalOffsets = new ArrayList <>();                                                    // хранилище внутренних (относительных) смещений
        listInternalOffsets.add(internalOffsetsAltitude);
        listInternalOffsets.add(internalOffsetsAtmParam);
        listInternalOffsets.add(internalOffsetsVelocity);


        Engine engine = new Engine(settingLimitType, listInternalOffsets, this.inputAltitude, this.inputVelocity, this.unitOutput, this.unitInput);

        // экспортируем второй вариант
        engine.dataArray(this.inputMaxM);
        engine.exportExcel();
        engine.writeFile(this.path);
        engine = null;
    }

}










