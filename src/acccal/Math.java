package acccal;

import java.util.Arrays;

public class Math {

    //计算单曲acc
    //之前写了矩阵生成，写了高斯消元，时间复杂度拉爆了
    public static String[] calculate(float[] compAcc, int[] volume) {

        int sumVolume = Arrays.stream(volume).sum();
        float[] currAcc = new float[volume.length];
        String[] calcAcc = new String[volume.length];
        float[] weight =  new float[volume.length];
        for (int i = 0; i < volume.length; i++) {
            for (int j = i; j < volume.length; j++) {
                weight[j] += volume[j] / (float) sumVolume;
            }
        }
//        System.out.println(Arrays.toString(weight)); //debug

        calcAcc[0] = String.format("%.4f", compAcc[0]);
        currAcc[0] = compAcc[0] * weight[0];
//        System.out.println(calcAcc[0]+";"+currAcc[0]);
        for (int i = 1; i < volume.length; i++) {
//            确实不好写，下标老是乱飞（气）
            currAcc[i] = compAcc[i] * weight[i];
            calcAcc[i] = String.format("%.4f", (currAcc[i] - currAcc[i-1]) / (weight[i] -  weight[i-1]));
            if (Float.parseFloat(calcAcc[i]) > 100 || Float.parseFloat(calcAcc[i]) < 0) {
                calcAcc[i] = "数据有误！";
            }
//            System.out.println(currAcc[i]);
//            System.out.println(currAcc[i] -  currAcc[i-1]);
//            System.out.println(calcAcc[i] + "\n");

        }
        return calcAcc;
    }

    public static String[] reverseCalculate(float[] currAcc, int[] volume) {

        String[] calcAcc = new String[volume.length];
        float stdSum = Arrays.stream(volume).sum() * 96;//过段标准为96acc（默认）
        float curSum = innerProduct(currAcc, volume);
//        System.out.println(stdSum+";"+curSum);
        for (int i = 0; i < volume.length; i++) {
            if(currAcc[i] + (stdSum - curSum) / volume[i] <= 100){
                calcAcc[i] = String.format("%.4f", currAcc[i] + (stdSum - curSum) / volume[i]);
//              System.out.print(calcAcc[i] + " ");
            } else {
                calcAcc[i] = "过不了喵";
            }
        }
        return calcAcc;
    }

    public static float innerProduct(float[] a, int[] b) {
        float result = 0;
        for (int i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }
    //好像也没啥用
    public static float[] innerProduct(float[] a, float[] b) {
        float[] result = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
        return result;
    }
/*
    public static void main(String[] args) {

//        System.out.print(Arrays.toString(reverseCalculate(new float[] {95f, 95f, 95f, 95f},  new int[] {1,1,2,2})));
        //debug中
//        System.out.println(Arrays.toString(calculate(new float[]{96.0f, 96.0f, 97.0f}, new int[]{1,1,1})));
    }
*/

}
