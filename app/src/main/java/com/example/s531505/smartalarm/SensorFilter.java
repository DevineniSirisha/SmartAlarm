package com.example.s531505.smartalarm;


    public class SensorFilter {

        private SensorFilter() {
        }

        public static float sum(float[] array) {
            float result = 0;
            for (int i = 0; i < array.length; i++) {
                result += array[i];
            }
            return result;
        }

        public static float[] cross(float[] arrayA, float[] arrayB) {
            float[] retArray = new float[3];
            retArray[0] = arrayA[1] * arrayB[2] - arrayA[2] * arrayB[1];
            retArray[1] = arrayA[2] * arrayB[0] - arrayA[0] * arrayB[2];
            retArray[2] = arrayA[0] * arrayB[1] - arrayA[1] * arrayB[0];
            return retArray;
        }

        public static float norm(float[] arr) {
            float retnval = 0;
            for (int i = 0; i < arr.length; i++) {
                retnval += arr[i] * arr[i];
            }
            return (float) Math.sqrt(retnval);
        }


        public static float dot(float[] a1, float[] a2) {
            float retval = a1[0] * a2[0] + a1[1] * a2[1] + a1[2] * a2[2];
            return retval;
        }

        public static float[] normalize(float[] a) {
            float[] returnval = new float[a.length];
            float norm = norm(a);
            for (int i = 0; i < a.length; i++) {
                returnval[i] = a[i] / norm;
            }
            return returnval;
        }

    }

