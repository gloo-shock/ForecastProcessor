package com.forecast.forecasts;

import com.forecast.entries.Person;
import com.forecast.utils.ForecastTableModel;
import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import java.awt.*;

import static com.forecast.utils.MyGridBagConstraints.Anchor.*;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_HORIZONTAL;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_NONE;
import static com.forecast.utils.MyGridBagLayout.getSharedConstraints;
import static java.lang.ClassLoader.getSystemResource;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.JOptionPane.*;

public class AddForecastDialog extends JDialog {
    public static final int CONTENT_PADDING = 20;
    private final ForecastTableModel tableModel;
    private JComboBox<Person> personCombobox;

    public AddForecastDialog(JFrame owner, ForecastTableModel tableModel) {
        super(owner, "Добавить прогноз");
        this.tableModel = tableModel;
        setMinimumSize(new Dimension(300, 400));
        dialogInit();
    }

    @Override
    protected void dialogInit() {
        super.dialogInit();
        setLayout(new MyGridBagLayout());

        add(getNameSelectionPanel(), getSharedConstraints(0, 0, 1, 1, 1, 0, GB_NORTHEAST, GB_HORIZONTAL, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING));
        add(getOkAndCancelPanel(),
                getSharedConstraints(0, 2, 2, 1, 1, 0, GB_SOUTH, GB_HORIZONTAL, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING));
    }

    private JPanel getNameSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, X_AXIS));
        personCombobox = new JComboBox<>();
        DefaultComboBoxModel<Person> comboBoxModel = new DefaultComboBoxModel<>();
        personCombobox.setModel(comboBoxModel);
        JButton addPersonButton = new JButton(new ImageIcon(getSystemResource("icons/plus.png")));
        addPersonButton.addActionListener(e -> addPerson(panel, comboBoxModel));
        JButton removePersonButton = new JButton(new ImageIcon(getSystemResource("icons/minus.png")));
        removePersonButton.addActionListener(e -> removePerson(comboBoxModel, removePersonButton));
        panel.add(personCombobox);
        panel.add(addPersonButton);
        panel.add(removePersonButton);
        return panel;
    }

    private void addPerson(JPanel panel, DefaultComboBoxModel<Person> comboBoxModel) {
        String newName = showInputDialog(panel, "Введите имя и фамилию", "Имя Фамилия");

        if (newName != null && !(newName = newName.trim()).isEmpty()) {
            int space = newName.indexOf(" ");
            if (space > 0) {
                comboBoxModel.addElement(new Person(newName.substring(0, space), newName.substring(space + 1)));
            } else {
                comboBoxModel.addElement(new Person(newName, ""));
            }
        }
    }

    private void removePerson(DefaultComboBoxModel<Person> comboBoxModel, JButton parent) {
        Person selected = (Person) comboBoxModel.getSelectedItem();
        if (selected != null) {
            int result = showConfirmDialog(parent,
                    "Вы уверены, что хотите удалить участника " + selected + "?",
                    "Удалить участника",
                    YES_NO_OPTION);
            if (result == YES_OPTION) {
                comboBoxModel.removeElement(selected);
            }
        }
    }

    private JPanel getOkAndCancelPanel() {
        JPanel buttons = new JPanel(new MyGridBagLayout());
        buttons.setOpaque(false);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> ok());
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> cancel());
        buttons.add(okButton, getSharedConstraints(1, 1, 1, 2, 0.0, 0.0, GB_EAST, GB_NONE, 0, 8, 0, 0));
        buttons.add(cancelButton, getSharedConstraints(3, 1, 1, 2, 0.0, 0.0, GB_EAST, GB_NONE, 0, 8, 0, 0));
        return buttons;
    }

    private void cancel() {
        setVisible(false);
    }

    private void ok() {
        setVisible(false);
    }
}
