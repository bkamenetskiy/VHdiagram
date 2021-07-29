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

        // максимальная высота полета
        float inputMaxAltitude = 12200.0f;
        // приращение по высоте при выводе диаграммы
        int outputAltitudeInc = 10;

        // размерности блоков:
        // 0 - высота блока/количество итераций (счетное, общая для всех);
        // 1 - ширина блока высоты (счетное);
        // 2 - ширина блока параметров атмосферы (счетное);
        // 3 - ширина блока скорости (счетное);
        // 4 - количество блоков скоростей (счетное).
        int [] settingDimension = new int[] {(int) Math.ceil (inputMaxAltitude / 1.0f), 2, 4, 5,inputVelocity.length};

        // ограничения для скоростей в следующем порядке: Vd, Vc, Va, Vs
        // типы ограничений: 1 - ограничение сверху по значению маха; 2 - ограничение сверху махом другой скорости; 0 - ограничения отсутствуют
        int [] settingLimitType = new int[] {1, 1, 2, 0};

        ArrayList <int[]> listSettings = new ArrayList <>();
        listSettings.add(settingDimension);
        listSettings.add(settingLimitType);

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
        int [] internalOffsetsAltitude = new int[] {1, 0};
        int [] internalOffsetsAtmParam = new int[] {3, 1, 2, 0};
        int [] internalOffsetsVelocity = new int[] {0, 1, 2, 3, 4};

        ArrayList <int []> listInternalOffsets = new ArrayList <>();                                                    // хранилище внутренних (относительных) смещений
        listInternalOffsets.add(internalOffsetsAltitude);
        listInternalOffsets.add(internalOffsetsAtmParam);
        listInternalOffsets.add(internalOffsetsVelocity);


        Engine engine = new Engine(listSettings, listInternalOffsets);

        // экспортируем второй вариант
        engine.dataArray(inputVelocity, inputMaxM);








    }




}










