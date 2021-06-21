package models;

public final class ModelVelocity {

    public float getDynPress(float dens, float velocityTAS) {
        return (float) ((dens * Math.pow(velocityTAS , 2))/2);
    }

    float getMach (float soundVel, float velocity) {
        return velocity / soundVel;
    }

    public float getVcasToMach(float PressStatic, float casV) {
        float mach0, mach, dynPress, press;
        float pressRel = (float) (PressStatic / 101325.0); // P0
        mach0 = (float) (casV / 340.294); // soundVel0
        dynPress = (float) (Math.pow(1 + 0.2 * Math.pow(mach0 , 2) , 3.5) - 1);
        press = dynPress / pressRel + 1;
        mach = (float) Math.pow(5 * (Math.pow(press, (2.0/7.0)) - 1) , 0.5);
        return mach;
    }

    public float getMachToVtas(float soundVelocity, float mach) {
        return mach * soundVelocity;
    }

    public float getVtasToVeas(float dens, float tasV) {
        float dynPress = (float) ((dens * Math.pow(tasV , 2))/2);
        return (float) Math.sqrt((2.0 * dynPress) / 1.225);
    }








}
