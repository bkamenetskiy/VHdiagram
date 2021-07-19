package models;

public final class ModelUnits {

    // конвертация футов в метры
    public float getLengthFoot (float meter) {
        return meter * 3.280839895f;
    }

    // конвертация метров в километры
    public float getLengthKilometer (float meter) {
        return meter / 1000.000f;
    }

    // конвертация м/с в узлы
    public float getLengthVelocityKt (float meterPerSecond) {
        return meterPerSecond / 0.514444444f;
    }

    // конвертация м/с в км/ч
    public float getLengthVelocity (float meterPerSecond) {
        return meterPerSecond * 60.0f * 60.0f / 1000.0f;
    }






}
