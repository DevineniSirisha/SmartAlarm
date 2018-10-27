package com.example.s531505.smartalarm;

public class StepDetector {

    private static final int ACCELARATOR_RING_SIZE = 50;
    private static final int VEL_RING_SIZE = 10;
    // change this threshold according to your sensitivity preferences
    private static final float STEP_THRESHOLD = 50f;
    private static final int STEP_DELAY = 250000000;
    private int accelRingCounter = 0;
    private float[] accelRingX = new float[ACCELARATOR_RING_SIZE];
    private float[] accelRingY = new float[ACCELARATOR_RING_SIZE];
    private float[] accelRingZ = new float[ACCELARATOR_RING_SIZE];
    private int velRingCounter = 0;
    private float[] velRing = new float[VEL_RING_SIZE];
    private long lastStepTime = 0;
    private float prevVelocityEstimate = 0;
    private StepCounter counter;
    public void registerListener(StepCounter counter)
    {
        this.counter = counter;
    }
    public void updateAccel(long time, float x, float y, float z) {
        float[] accel = new float[3];
        accel[0] = x;
        accel[1] = y;
        accel[2] = z;
        // First step is to update our guess of where the global z vector is.
        accelRingCounter++;
        accelRingX[accelRingCounter % ACCELARATOR_RING_SIZE] = accel[0];
        accelRingY[accelRingCounter % ACCELARATOR_RING_SIZE] = accel[1];
        accelRingZ[accelRingCounter % ACCELARATOR_RING_SIZE] = accel[2];
        float[] world = new float[3];
        world[0] = SensorFilter.sum(accelRingX) / Math.min(accelRingCounter, ACCELARATOR_RING_SIZE);
        world[1] = SensorFilter.sum(accelRingY) / Math.min(accelRingCounter, ACCELARATOR_RING_SIZE);
        world[2] = SensorFilter.sum(accelRingZ) / Math.min(accelRingCounter, ACCELARATOR_RING_SIZE);
        float norm_fact = SensorFilter.norm(world);
        world[0] = world[0] / norm_fact;
        world[1] = world[1] / norm_fact;
        world[2] = world[2] / norm_fact;
        float currentZ = SensorFilter.dot(world, accel) - norm_fact;
        velRingCounter++;
        velRing[velRingCounter % VEL_RING_SIZE] = currentZ;
        float velocityEstimate = SensorFilter.sum(velRing);
        if (velocityEstimate > STEP_THRESHOLD && prevVelocityEstimate <= STEP_THRESHOLD
                && (time - lastStepTime > STEP_DELAY)) {
            counter.step(time);
            lastStepTime = time;
        }
        prevVelocityEstimate = velocityEstimate;
    }
}
