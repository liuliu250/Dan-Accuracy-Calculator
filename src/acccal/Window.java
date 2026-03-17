package acccal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;

import static acccal.Math.*;
import static acccal.Reference.*;
import static acccal.Utils.*;

public class Window implements ActionListener, ItemListener {

    //草拟吗java为什么不能在接口重写的位置调用构造函数的对象，，
    //导致一气之下所有初始化全放这里了
    JPanel panel = new JPanel();
    JFrame window = new JFrame("Dan Acc Calculator 2.3");
    JButton calculate = new JButton("Calculate");
//    JButton revCal = new JButton("Reverse Calculate");
    JLabel[] text = new JLabel[TextBounds.length];
    JCheckBox[] chkBox = new JCheckBox[ChkBoxBounds.length];
    JTextField[] AccArea = new JTextField[AccAreaBounds.length];
    JTextField stdArea = new JTextField();
    JComboBox<String> DanChooser = new JComboBox<>();

    public Window() {
        initWindowFrame();
        initPanel();
        initButtons();
        initLabels();
        initDanChooser();
        initCheckBoxes();
        initInputAreas();
        registerListeners();
        assemblePanelComponents();
        showWindow();
    }

    private void initWindowFrame() {
        setResolution(window, getResolution(), WindowSize);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        //窗口图标（可选）
        window.setIconImage(getImage("src/acccal/images/icon.png"));
    }

    private void initPanel() {
        panel.setLayout(null);
        window.add(panel);
    }

    private void initButtons() {
        calculate.setBounds(590, 25, 100, 40);
        calculate.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        calculate.setBackground(new Color(33, 173, 75));

        //数学工作做好了，但java这边没想好怎么实现
        //想好了这下 v2.1
//        revCal.setBounds(WindowSize[0]-160, 65, 100, 40);
//        revCal.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
//        revCal.setBackground(new Color(33, 173, 75));
    }

    private void initLabels() {
        for (int i = 0; i < TextBounds.length; i++) {
            text[i] = new JLabel(TextTips[i]);
            text[i].setFont(new Font("Microsoft YaHei", Font.PLAIN, 15));
        }
        setBounds(TextBounds, text);
        refreshVolumeLabels();
    }

    private void initDanChooser() {
        DanChooser.setBounds(144, 25, 90, 40);
        DanChooser.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        refreshDanOptions();
        panel.repaint();
    }

    private void initCheckBoxes() {
        for (int i = 0; i < chkBox.length; i++) {
            chkBox[i] = new JCheckBox();
            chkBox[i].addItemListener(this);
        }
        setBounds(ChkBoxBounds, chkBox);
    }

    private void initInputAreas() {
        for (int i = 0; i < AccArea.length; i++) {
            AccArea[i] = new JTextField();
        }
        setBounds(AccAreaBounds, AccArea);
        stdArea.setBounds(605, 198, 80, 30);
    }

    private void registerListeners() {
        calculate.addActionListener(this);
        DanChooser.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshVolumeLabels();
            }
        });
    }

    private void assemblePanelComponents() {
        add(panel, calculate, DanChooser, stdArea);
        add(panel, text, chkBox, AccArea);
        for (int i = 19; i < 23; i++) {
            panel.remove(text[i]);
        }
    }

    private void showWindow() {
        //最后设置可见，避免重绘且减少卡顿
        window.setVisible(true);
    }

    private int getDanIndex() {
        int indexDan = 0;
        if (chkBox[0].isSelected()) {
            indexDan += 2;
        }
        if (chkBox[1].isSelected()) {
            indexDan += 1;
        }
        return indexDan;
    }

    private void refreshDanOptions() {
        DanChooser.removeAllItems();
        boxRepaint(DanChooser, CbBoxText[getDanIndex()]);
    }

    private void refreshVolumeLabels() {
        int indexSong = DanChooser.getSelectedIndex();
        if (indexSong < 0) {
            indexSong = 0;
        }
        labelRepaint(Arrays.copyOfRange(text, 11, 15), volume[getDanIndex()][indexSong]);
    }

    private void updateReverseModeLayout() {
        panel.setVisible(false);
        text[3].setText(chkBox[2].isSelected() ? "目标acc" : "阶段acc");
        labelRepaint(Arrays.copyOfRange(text, 15, 19), new String[]{"", "", "", ""});
        reverseBounds(AccArea, Arrays.copyOfRange(text, 15, 19));
        panel.setVisible(true);
    }

    public static void main(String[] args) {new Window();}

    //还没写完 别急
    //这下写完了 v2.1
    @Override
    public void itemStateChanged(ItemEvent e) {
        refreshDanOptions();
        if (e.getSource() == chkBox[2]) {
            updateReverseModeLayout();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int indexDan = getDanIndex();
        int indexSong = DanChooser.getSelectedIndex();
        if (indexSong < 0) {
            indexSong = 0;
        }
        if(chkBox[2].isSelected()){
//            System.out.println(Arrays.toString(getAccFromJT(AccArea)));
            labelRepaint(Arrays.copyOfRange(text, 15, 19),
                reverseCalculate(getAccFromJT(AccArea), volume[indexDan][indexSong],
                getAccFromJT(stdArea))
            );
            text[26].setText(String.valueOf(innerProduct(getAccFromJT(AccArea),
                volume[indexDan][indexSong]) / Arrays.stream(volume[indexDan][indexSong]).sum())
            );
        } else {
            labelRepaint(Arrays.copyOfRange(text, 15, 19),
                calculate(getAccFromJT(AccArea), volume[indexDan][indexSong])
            );

        }
    }
}
