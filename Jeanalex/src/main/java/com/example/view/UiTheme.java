package com.example.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

final class UiTheme {
    static final Color BACKGROUND = new Color(248, 250, 252);
    static final Color SURFACE = Color.WHITE;
    static final Color PRIMARY = new Color(30, 58, 138);
    static final Color PRIMARY_DARK = new Color(23, 37, 84);
    static final Color ACCENT = new Color(37, 99, 235);
    static final Color TEXT = new Color(30, 41, 59);
    static final Color MUTED = new Color(100, 116, 139);
    static final Color BORDER = new Color(226, 232, 240);

    private UiTheme() {
    }

    static JLabel title(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        label.setForeground(TEXT);
        return label;
    }

    static JLabel muted(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(MUTED);
        return label;
    }

    static JButton primaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(ACCENT);
        button.setForeground(Color.WHITE);
        return button;
    }

    static JButton secondaryButton(String text) {
        JButton button = baseButton(text);
        button.setBackground(new Color(232, 241, 240));
        button.setForeground(PRIMARY);
        return button;
    }

    static JButton menuButton(String text) {
        JButton button = baseButton(text);
        button.setHorizontalAlignment(JButton.LEFT);
        button.setBackground(new Color(240, 247, 247));
        button.setForeground(PRIMARY);
        button.setBorder(new EmptyBorder(11, 16, 11, 16));
        button.setMinimumSize(new Dimension(0, 44));
        button.setPreferredSize(new Dimension(220, 44));
        return button;
    }

    static void selectedMenuButton(JButton button, boolean selected) {
        button.setBackground(selected ? PRIMARY : new Color(240, 247, 247));
        button.setForeground(selected ? Color.WHITE : PRIMARY);
    }

    static void styleField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT);
        field.setMargin(new Insets(8, 10, 8, 10));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(6, 8, 6, 8)
        ));
    }

    static void styleTable(JTable table) {
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(220, 242, 240));
        table.setSelectionForeground(PRIMARY_DARK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(232, 241, 240));
        header.setForeground(PRIMARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
    }

    static JScrollPane scroll(Component component) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(SURFACE);
        return scrollPane;
    }

    static RoundedPanel card(int radius, int top, int left, int bottom, int right) {
        RoundedPanel panel = new RoundedPanel(radius, SURFACE);
        panel.setBorder(new EmptyBorder(top, left, bottom, right));
        return panel;
    }

    private static JButton baseButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        return button;
    }

    static final class RoundedPanel extends JPanel {
        private final int radius;
        private final Color color;

        RoundedPanel(int radius, Color color) {
            this.radius = radius;
            this.color = color;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(color);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }
}
