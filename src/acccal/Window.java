package acccal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import static acccal.Math.calculate;
import static acccal.Math.reverseCalculate;
import static acccal.Reference.*;
import static acccal.Utils.*;

public class Window implements ActionListener, ItemListener {

    //草拟吗java为什么不能在接口重写的位置调用构造函数的对象，，
    //导致一气之下所有初始化全放这里了
    JPanel panel = new JPanel();
    JFrame window = new JFrame("Dan Acc Calculator 2.1");
    JButton calculate = new JButton("Calculate");
//    JButton revCal = new JButton("Reverse Calculate");
    JLabel[] text = new JLabel[TextBounds.length];
    JCheckBox[] chkBox = new JCheckBox[3];
    JTextField[] AccArea = new JTextField[4];
    JComboBox<String> DanChooser = new JComboBox<>();

    public Window() {

        //窗口初始化
        setResolution(window, getResolution(), WindowSize);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        //窗口图标（可选）
        window.setIconImage(getImage("src/acccal/images/icon.png"));

        //JPanel初始化
        panel.setLayout(null);
        window.add(panel);

        //JButton初始化
        calculate.setBounds(590, 25, 100, 40);
        calculate.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        calculate.setBackground(new Color(33, 173, 75));

        //数学工作做好了，但java这边没想好怎么实现
//        revCal.setBounds(WindowSize[0]-160, 65, 100, 40);
//        revCal.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
//        revCal.setBackground(new Color(33, 173, 75));

        //文字初始化
        for (int i = 0; i < TextBounds.length; i++) {
            text[i] = new JLabel(TextTips[i]);
            text[i].setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
        }
        setBounds(TextBounds, text);
        for (int i = 11; i < 15; i++) {
            text[i].setText(String.valueOf(volume[0][0][i-11]));
        }

        //JComboBox初始化
        DanChooser.setBounds(144, 25, 90, 40);
        DanChooser.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        boxRepaint(DanChooser, CbBoxText[0]);
        panel.repaint();

        //复选框初始化
        for (int i = 0; i < chkBox.length; i++) {
            chkBox[i] = new JCheckBox();
            chkBox[i].addItemListener(this);
        }
        setBounds(ChkBoxBounds, chkBox);

        //文字框初始化
        for (int i = 0; i < AccArea.length; i++) {
            AccArea[i] = new JTextField();
        }
        setBounds(AccAreaBounds, AccArea);

        //添加监听（cnm的java 为什么JComboBox的getIndex老是返回-1越界）
        calculate.addActionListener(this);
        DanChooser.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int indexDan = 0;
                int indexSong = DanChooser.getSelectedIndex();
                if(chkBox[0].isSelected()){
                    indexDan += 2;
                }
                if(chkBox[1].isSelected()){
                    indexDan += 1;
                }
                labelRepaint(Arrays.copyOfRange(text, 11, 15), volume[indexDan][indexSong]);
            }
        });

        //统统添加到面板
        add(panel, calculate, DanChooser);
        add(panel, text, chkBox, AccArea);
        //最后设置可见，避免重绘且减少卡顿
        window.setVisible(true);

    }

    public static void main(String[] args) {new Window();}

    //还没写完 别急
    @Override
    public void itemStateChanged(ItemEvent e) {
        int indexDan = 0;
        if(chkBox[0].isSelected()){
            indexDan += 2;
        }
        if(chkBox[1].isSelected()){
            indexDan += 1;
        }
        int indexSong = DanChooser.getSelectedIndex();
//        System.out.print(indexDan+"; "+indexSong+";;");
        DanChooser.removeAllItems();
        boxRepaint(DanChooser, CbBoxText[indexDan]);
        labelRepaint(Arrays.copyOfRange(text, 11, 15), volume[indexDan][indexSong]);
        if(e.getSource() == chkBox[2]){
            panel.setVisible(false);
            if(chkBox[2].isSelected()){
                text[3].setText("目标acc");
            } else {
                text[3].setText("阶段acc");
            }
            labelRepaint(Arrays.copyOfRange(text, 15, 19), new String[]{"", "", "", ""});
            reverseBounds(AccArea, Arrays.copyOfRange(text, 15, 19));
            panel.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int indexDan = 0;
        int indexSong = DanChooser.getSelectedIndex();
        if(chkBox[0].isSelected()){
            indexDan += 2;
        }
        if(chkBox[1].isSelected()){
            indexDan += 1;
        }
        if(chkBox[2].isSelected()){
//            System.out.println(Arrays.toString(getAccFromJT(AccArea)));
            labelRepaint(Arrays.copyOfRange(text, 15, 19), reverseCalculate(getAccFromJT(AccArea), volume[indexDan][indexSong]));
        }else {
            float[] AccFinal = calculate(getAccFromJT(AccArea), volume[indexDan][indexSong]);
            labelRepaint(Arrays.copyOfRange(text, 15, 19), AccFinal);
        }
    }
}
