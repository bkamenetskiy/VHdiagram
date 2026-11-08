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

    /**
     * Рассчитывает данные диаграммы и сохраняет книгу Excel по выбранному пути.
     *
     * @throws IOException если книгу не удалось записать
     */
    protected void back() throws IOException {
        // ограничения для скоростей в следующем порядке: Vd, Vc, Va, Vs
        // типы ограничений: 1 - ограничение сверху по значению маха; 2 - ограничение сверху махом другой скорости; 0 - ограничения отсутствуют
        int[] settingLimitType = new int[] {1, 1, 2, 0};

        // относительные положения параметров (внутренние смещения) в соответствующих массивах (блоках):
        // 0. Блок высот:
        // 0 - положение высоты (в метрах) в блоке высот (индекс) - по ним проходят внутренние рассчеты;
        // 1 - положение высоты в футах для таблицы;
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

        // Собираем индексы столбцов внутри блоков данных в порядке, который ожидает Engine.
        ArrayList <int []> listInternalOffsets = new ArrayList <>();
        listInternalOffsets.add(internalOffsetsAltitude); // Блок высот.
        listInternalOffsets.add(internalOffsetsAtmParam);  // Блок параметров атмосферы.
        listInternalOffsets.add(internalOffsetsVelocity); // Блок скоростей.

        // Передаём расчётному модулю исходные данные, единицы измерения и типы ограничений скоростей.
        Engine engine = new Engine(settingLimitType, listInternalOffsets, this.inputAltitude, this.inputVelocity, this.unitOutput, this.unitInput);

        // Рассчитываем параметры атмосферы и скорости по высотам с учётом предельных чисел Маха.
        engine.dataArray(this.inputMaxM);

        // Формируем книгу Excel с таблицей и диаграммами и сохраняем её по выбранному пути.
        engine.exportExcel(this.path);
    }

}










