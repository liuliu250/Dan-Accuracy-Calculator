package acccal;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;

public class Utils {

    //add重写 太躺尸了JComponents居然不能一次性添加
    public static void add(JPanel panel, JComponent ... comp1) {
        for (JComponent c : comp1) {
            panel.add(c);
        }
    }

    //依旧add重写 jpanel添加jcomponent数组的
    public static void add(JPanel panel, JComponent[] ... comp2) {
        for (JComponent[] c: comp2){
            for (JComponent comp : c){
                panel.add(comp);
            }
        }
    }

    //panel到frame的 没什么用
    public static void add(JFrame frame, JPanel ... panel) {
        for (JComponent p : panel) {
            frame.add(p);
        }
    }

/*
//方便调用reference bounds数组的方法
    public static void setBounds(int[] bounds, JComponent comp) {
        comp.setBounds(bounds[0], bounds[1], bounds[2], bounds[3]);
    }//好像这个用处不大
*/

    //setBounds重写加强
    public static void setBounds(int[][] bounds, JComponent[] comp) {
        for (int i = 0; i < comp.length; i++) {
            comp[i].setBounds(bounds[i][0], bounds[i][1], bounds[i][2], bounds[i][3]);
        }
    }

    //获取屏幕分辨率
    public static int[] getResolution() {
        return new int[]{getDefaultToolkit().getScreenSize().width,
                getDefaultToolkit().getScreenSize().height};
    }

    //窗口居中
    public static void setResolution(JFrame window, int[] res, int[] size) {
        window.setBounds((res[0]- size[0])/2, (res[1]- size[1])/2, size[0], size[1]);
    }

    //img get太长了放这里简化
    public static Image getImage(String path) {
        return getDefaultToolkit().getImage(path);
    }

    //JComboBox重绘用
    public static void boxRepaint(JComboBox<String> box, String[] str) {
        //感谢java语法糖
        for (String s : str) {
            box.addItem(s);
        }
    }

    //JLabel重绘 他妈的acc重绘和物量重绘不能通用qswl
    public static void labelRepaint(JLabel[] label, float[] str) {
        for (int i = 0; i < label.length; i++) label[i].setText(String.format("%.4f", str[i]));
    }
    public static void labelRepaint(JLabel[] label, int[] str) {
        for (int i = 0; i < label.length; i++) label[i].setText(String.valueOf(str[i]));
    }
    public static void labelRepaint(JLabel[] label, String[] str) {
        for (int i = 0; i < label.length; i++) label[i].setText(str[i]);
    }
    public static float[] getAccFromJT(JTextField[] tf) {
        float[] acc = new float[tf.length];
        for (int i = 0; i < tf.length; i++) {
            if (tf[i].getText().isEmpty()) {
                acc[i] = 0;
            } else  {
                acc[i] = Float.parseFloat(tf[i].getText());
            }
        }
        return acc;
    }
    public static void reverseBounds(JComponent[] c1, JComponent[] c2) {
        Rectangle[] r = new Rectangle[c1.length];
        for (int i = 0; i < c1.length; i++) {
            r[i] = c1[i].getBounds();
            c1[i].setBounds(c2[i].getBounds());
            c2[i].setBounds(r[i]);
        }
    }
}
