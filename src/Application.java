import java.io.IOException;
import java.text.ParseException;

class Application {





    public static void main(String[] args) throws IOException, ParseException {

        // индикаторные земные скорости Vcas в следующем порядке: Vd; Vc; Va; Vs
        float [] inputVelocity = new float[] {622.0f * 1000.0f / 60.0f / 60.0f,
                                              570.0f * 1000.0f / 60.0f / 60.0f,
                                              487.0f * 1000.0f / 60.0f / 60.0f,
                                              201.0f * 1000.0f / 60.0f / 60.0f};

        // законодательное ограничение скоростей Vd и Vc по числу М: 0 - max Md; 1 - max Mc
        float [] inputMaxM = new float[] {0.88f, 0.82f};

        // максимальная высота полета
        float inputMaxAltitude = 12200.0f;
        // приращение по высоте при выводе диаграммы
        int outputAltitudeInc = 10;

        // система координат в основном массиве данных:
        // 0 - глобальное смещение всех блоков скоростей (индекс); 1 - ширина блока скорости (счетное);
        // 2 - количество блоков скоростей (счетное); 3 - относительное положение (внутреннее смещение) в блоке V (CAS) (индекс);
        // 4 - относительное положение маха (индекс); 5 - относительное положение V (TAS) (индекс);
        // 6 - относительное положение V (EAS) (индекс); 7 - относительное положение q (индекс);
        int [] serviceCSYS = new int[] {5, 5, inputVelocity.length, 0, 1, 2, 3, 4};

        // индексы начал блоков каждой из скоростей в следующем порядке: Vd, Vc, Va, Vs
        // указывает на начальный индекс соответствующего блока
        int [] serviceBlockOffset = new int[] {
                serviceCSYS[0] + serviceCSYS[1] * 0,
                serviceCSYS[0] + serviceCSYS[1] * 1,
                serviceCSYS[0] + serviceCSYS[1] * 2,
                serviceCSYS[0] + serviceCSYS[1] * 3};

        // ограничения для скоростей в следующем порядке: Vd, Vc, Va, Vs
        // типы ограничений: 1 - ограничение сверху по значению маха; 2 - ограничение сверху махом другой скорости; 0 - ограничения отсутствуют
        int [] serviceLimitType = new int[] {1, 1, 2, 0};

        // размер массива с данными: 0 - высота массива; 1 - ширина массива. Y и X соответственно
        int [] serviceDimension = new int[] {(int) Math.ceil (inputMaxAltitude / 1.0f),
                serviceCSYS[0] + serviceCSYS[1] * serviceCSYS[2]};

        // ключи экспорта блоков скоростей: 0 - блок не экспортируется; 1 - экспортируется
        // порядок: высота, атмосфера; Vd; Vc; Va; Vs
        int [] outputBlock = new int[] {1, 1, 1, 1, 1, 1};

        // экспорт величин из блока скорости (атмосфера экспортируется целиком): 0 - величина не экспортируется; 1 - величина экспортируется
        // порядок: V (CAS); M; V (TAS); V (EAS); q
        int [] outputValue = new int[] {1, 1, 1, 0, 0};

        // Считаем массив данных
        Engine Engine = new Engine();
        float[][] dataArray = Engine.getDataArray(inputVelocity, inputMaxM, serviceCSYS, serviceBlockOffset, serviceLimitType, serviceDimension);

        // экспортируем
        Engine.dataOutput(dataArray, serviceCSYS, serviceBlockOffset, outputBlock, outputValue, outputAltitudeInc, inputMaxAltitude);

        // выводим в консоль
        for (float[] floats : dataArray) {
            System.out.println();
            for (float result : floats) {
                System.out.printf("%.4f", result);
                System.out.print("     ");
            }
        }


        /*        for (String s : dataTitle) {
            System.out.print(s + "    ");
        }*/






    }




}









/*
        for (int i = 0; i < dataArray.length; i++) {
            System.out.println();
            for (int j = 0; j < dataArray[i].length; j++) {
                float result = dataArray[i][j];
                System.out.printf("%.4f",result);
                System.out.print("     ");
            }
        }
*/




