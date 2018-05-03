package com.forecast.forecasts;

import com.forecast.entries.ForecastResult;
import com.forecast.entries.Person;
import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.forecast.resources.ResourceUtils.getString;
import static com.forecast.rest.RestClient.restClient;
import static com.forecast.utils.MyGridBagConstraints.Anchor.*;
import static com.forecast.utils.MyGridBagConstraints.Fill.*;
import static com.forecast.utils.MyGridBagLayout.getSharedConstraints;
import static java.lang.ClassLoader.getSystemResource;
import static java.util.stream.Collectors.toList;
import static javax.swing.Box.createHorizontalStrut;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.JOptionPane.*;

public class AddForecastDialog extends JDialog {
    private static final int CONTENT_PADDING = 20;
    private final ForecastTableModelWrapper tableModel;
    private JComboBox<Person> personCombobox;
    private JTextArea forecastListArea;

    public AddForecastDialog(JFrame owner, ForecastTableModelWrapper tableModel) {
        super(owner, getString("addForecast"));
        this.tableModel = tableModel;
        setMinimumSize(new Dimension(400, 450));
        dialogInit();
    }

    @Override
    protected void dialogInit() {
        super.dialogInit();
        setLayout(new MyGridBagLayout());

        add(getNameSelectionPanel(),
                getSharedConstraints(0, 0, 1, 1, 1, 0, GB_NORTHEAST, GB_HORIZONTAL, CONTENT_PADDING, CONTENT_PADDING, 0, CONTENT_PADDING));
        add(getForecastListArea(),
                getSharedConstraints(0, 1, 1, 1, 1, 1, GB_CENTER, GB_BOTH, CONTENT_PADDING, CONTENT_PADDING, 0, CONTENT_PADDING));
        add(getOkAndCancelPanel(),
                getSharedConstraints(0, 2, 1, 1, 1, 0, GB_SOUTH, GB_HORIZONTAL, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING, CONTENT_PADDING));
    }

    private JPanel getNameSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, X_AXIS));
        personCombobox = new JComboBox<>();
        List<Person> people = restClient().loadPersons();
        DefaultComboBoxModel<Person> comboBoxModel = new DefaultComboBoxModel<>(people.toArray(new Person[people.size()]));
        personCombobox.setModel(comboBoxModel);
        JButton addPersonButton = new JButton(new ImageIcon(getSystemResource("icons/plus.png")));
        addPersonButton.addActionListener(e -> addPerson(panel, comboBoxModel));
        JButton removePersonButton = new JButton(new ImageIcon(getSystemResource("icons/minus.png")));
        removePersonButton.addActionListener(e -> removePerson(comboBoxModel, removePersonButton));
        panel.add(new JLabel(getString("addDialog.player") + ":"));
        panel.add(createHorizontalStrut(10));
        panel.add(personCombobox);
        panel.add(addPersonButton);
        panel.add(removePersonButton);
        return panel;
    }

    private void addPerson(JPanel panel, DefaultComboBoxModel<Person> comboBoxModel) {
        String newName = showInputDialog(panel, getString("addDialog.enterName"), "Name Lastname");

        if (newName != null && !(newName = newName.trim()).isEmpty()) {
            int space = newName.indexOf(" ");
            Person person;
            if (space > 0) {
                person = new Person(newName.substring(0, space), newName.substring(space + 1));
            } else {
                person = new Person(newName, "");
            }
            if (comboBoxModel.getIndexOf(person) < 0) {
                comboBoxModel.addElement(person);
            } else {
                JOptionPane.showMessageDialog(panel, getString("addDialog.exists"));
            }
            comboBoxModel.setSelectedItem(person);
        }
    }

    private void removePerson(DefaultComboBoxModel<Person> comboBoxModel, JButton parent) {
        Person selected = (Person) comboBoxModel.getSelectedItem();
        if (selected != null) {
            int result = showConfirmDialog(parent,
                    getString("addDialog.removePerson.sure", selected.toString()),
                    getString("addDialog.removePerson"),
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
        JButton cancelButton = new JButton(getString("cancel"));
        cancelButton.addActionListener(e -> cancel());
        buttons.add(okButton, getSharedConstraints(1, 1, 1, 2, 0.0, 0.0, GB_EAST, GB_NONE, 0, 8, 0, 0));
        buttons.add(cancelButton, getSharedConstraints(3, 1, 1, 2, 0.0, 0.0, GB_EAST, GB_NONE, 0, 8, 0, 0));
        return buttons;
    }

    private JPanel getForecastListArea() {
        forecastListArea = new JTextArea();
        forecastListArea.setPreferredSize(new Dimension(260, 200));
        forecastListArea.setWrapStyleWord(true);
        forecastListArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(forecastListArea);
        JPanel panel = new JPanel(new MyGridBagLayout());
        JLabel label = new JLabel(getString("forecast") + ":");
        panel.add(label, getSharedConstraints(0, 0, 1, 1, 0, 0, GB_NORTH, GB_NONE, 0, 0, 0, 0));
        panel.add(scrollPane, getSharedConstraints(0, 1, 1, 1, 1, 1, GB_CENTER, GB_BOTH, 0, 0, CONTENT_PADDING, 0));
        return panel;
    }

    private void cancel() {
        forecastListArea.setText("");
        setVisible(false);
    }

    private void ok() {
        Person person = (Person) personCombobox.getSelectedItem();
        if (person == null) {
            showMessageDialog(this, getString("addDialog.selectPlayer"));
            return;
        }
        String text = forecastListArea.getText().trim();
        if (text.isEmpty()) {
            showMessageDialog(this, getString("addDialog.fillForecast"));
            return;
        }
        int answer = showConfirmDialog(this, getString("addDialog.addForecastsQuestion", person.toString()));
        if (answer == YES_OPTION) {
            String[] forecasts = text.split("\n");
            tableModel.addEntry(person,
                    Stream.of(forecasts).map(ForecastResult::parseFromString)
                            .filter(Objects::nonNull).collect(toList()));
            forecastListArea.setText("");
            setVisible(false);
        }
    }

    @Override
    public void setVisible(boolean b) {
        Container parent = getParent();
        if (parent != null) {
            setLocationRelativeTo(parent);
        }
        super.setVisible(b);
    }
}
