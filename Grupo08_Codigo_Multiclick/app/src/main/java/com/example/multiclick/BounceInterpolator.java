package com.example.multiclick;

import android.view.animation.Interpolator;

public class BounceInterpolator implements Interpolator {

    private double miAmplitud = 1;
    private double miFrecuencia = 10;

    BounceInterpolator(double amplitud, double frecuencia){
        miAmplitud = amplitud;
        miFrecuencia = frecuencia;
    }

    @Override
    public float getInterpolation(float time) {
        return (float) (-1*Math.pow(Math.E, -time/miAmplitud)*Math.cos(miFrecuencia*time)+1);
    }
}
