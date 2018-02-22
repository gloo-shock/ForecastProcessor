package com.forecast.forecasts;

import com.forecast.utils.ForecastTableModel;
import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import java.awt.*;

import static com.forecast.utils.MyGridBagConstraints.Anchor.GB_EAST;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_NONE;
import static com.forecast.utils.MyGridBagLayout.getSharedConstraints;

public class AddForecastDialog extends JDialog {
    public static final int CONTENT_PADDING = 20;
    private final ForecastTableModel tableModel;

    public AddForecastDialog(JFrame owner, ForecastTableModel tableModel) {
        super(owner, "Добавить прогноз");
        this.tableModel = tableModel;
        setMinimumSize(new Dimension(300, 400));
        dialogInit();
    }

    @Override
    protected void dialogInit() {
        super.dialogInit();
        JPanel buttons = new JPanel(new MyGridBagLayout());
        buttons.setOpaque(false);

        setLayout(new BorderLayout());

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> ok());
        JButton cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> cancel());
        buttons.add(okButton, getSharedConstraints(1, 1, 1, 2, 0.0, 0.0, GB_EAST, GB_NONE, 0, 8, 0, 0));
        buttons.add(cancelButton, getSharedConstraints(3, 1, 1, 2, 0.0, 0.0, GB_EAST, GB_NONE, 0, 8, 0, 0));
        add(buttons, BorderLayout.SOUTH);
    }

    private void cancel() {
        setVisible(false);
    }

    private void ok() {
        setVisible(false);
    }
}
