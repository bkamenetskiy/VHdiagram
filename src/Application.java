import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

class Application {

    public static void main(String[] args) throws IOException, ParseException {



        // индикаторные земные скорости Vcas в следующем порядке: Vd; Vc; Va; Vs
        float [] inputVelocity = new float[] {622.0f * 1000.0f / 60.0f / 60.0f,
                                              570.0f * 1000.0f / 60.0f / 60.0f,
                                              487.0f * 1000.0f / 60.0f / 60.0f,
                                              201.0f * 1000.0f / 60.0f / 60.0f}; // 172.777 158.333 135.2777 55.8333

        // законодательное ограничение скоростей Vd и Vc по числу М: 0 - max Md; 1 - max Mc
        float [] inputMaxM = new float[] {0.88f, 0.82f};

        // диапазоны высот и приращение
        float[] inputAltitude = new float[] {-300.0f, 12200.0f, 10.0f};

        // единицы измерения вывода
        // 0. Единицы измерения высоты:
        // 1 - километры;
        // любой другой ключ - метры
        // 1. Единицы измерения скорости:
        // 0 - м/с;
        // 1 - км/ч;
        // 2 - knot
        int [] outputUnit = new int[] {0, 1};

        // ограничения для скоростей в следующем порядке: Vd, Vc, Va, Vs
        // типы ограничений: 1 - ограничение сверху по значению маха; 2 - ограничение сверху махом другой скорости; 0 - ограничения отсутствуют
        int [] settingLimitType = new int[] {1, 1, 2, 0};

        // относительные положения параметров (внутренние смещения) в соответствующих массивах (блоках):
        // 0. Блок скоростей:
        // 0 - положение высоты (в метрах) в блоке высот (индекс);
        // 1 - положение высоты (в футах) в блоке высот (индекс) - заразервирована, не используется.
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
        int [] internalOffsetsAltitude = new int[] {0, 1};
        int [] internalOffsetsAtmParam = new int[] {0, 1, 2, 3};
        int [] internalOffsetsVelocity = new int[] {4, 1, 2, 3, 0};

        ArrayList <int []> listInternalOffsets = new ArrayList <>();                                                    // хранилище внутренних (относительных) смещений
        listInternalOffsets.add(internalOffsetsAltitude);
        listInternalOffsets.add(internalOffsetsAtmParam);
        listInternalOffsets.add(internalOffsetsVelocity);


        Engine engine = new Engine(settingLimitType, listInternalOffsets, inputAltitude, outputUnit);

        // экспортируем второй вариант
        engine.dataArray(inputVelocity, inputMaxM);
        engine.dataOutput();









    }




}










