package net.minheur.potoflux.screen.tabs.all;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minheur.potoflux.screen.tabs.BaseTab;
import net.minheur.potoflux.utils.InputWithCheckboxResult;
import net.minheur.potoflux.utils.Translations;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static net.minheur.potoflux.Functions.*;

public class TodoTab extends BaseTab {
    private final List<TodoItem> todos = new ArrayList<>();
    private final JPanel listPanel = new JPanel();
    private final Path saveFile;

    public TodoTab() {
        super();
        Path appData = Path.of(System.getenv("APPDATA"), "technomastery/PotoFlux");
        try {
            Files.createDirectories(appData);
        } catch (IOException ignored) {}
        this.saveFile = appData.resolve("todo.json");

        loadTodos();
        refreshList();
    }

    @Override
    protected void setPanel() {
        PANEL.setLayout(new BorderLayout());

        // -- main : title & button ---
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(10, 10, 10, 10));

        // title
        JLabel title = new JLabel(getTitle());
        title.setFont(new Font("Consolas", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        top.add(title);
        top.add(Box.createVerticalStrut(10));

        // add button
        JButton addButton = new JButton(Translations.get("tabs.todo.button_add"));
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.addActionListener(e -> addNewTodo());
        top.add(addButton);
        top.add(Box.createVerticalStrut(10));

        // remove all button
        JButton clearButton = new JButton(Translations.get("tabs.todo.button_clear"));
        clearButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        clearButton.addActionListener(e -> clearAll());
        top.add(clearButton);

        // top.add(row);

        PANEL.add(top, BorderLayout.NORTH);

        // center - list
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // task list
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);

        PANEL.add(scroll, BorderLayout.CENTER);
    }

    @Override
    protected boolean doPreset() {
        return false;
    }

    @Override
    protected String getTitle() {
        return "TODO"; // TODO: translate
    }

    private void clearAll() {
        todos.clear();
        saveTodos();
        refreshList();
    }

    private void addNewTodo() {
        InputWithCheckboxResult input = InputWithCheckboxResult.showInputWithCheckboxDialog(PANEL, Translations.get("tabs.todo.new.title"), Translations.get("tabs.todo.new.name"), Translations.get("tabs.todo.new.pinned"));
        String text = input.text;
        boolean pinned = input.pinned;

        if (text == null || text.trim().isEmpty()) {
            showTodoError();
            return;
        }
        text = removeProhibitedChar(text);

        if (!text.trim().isEmpty()) {
            todos.add(0, new TodoItem(text.trim(), false, pinned));
            saveTodos();
            refreshList();
        } else showTodoError();
    }

    private void showTodoError() {
        JOptionPane.showMessageDialog(PANEL, Translations.get("tabs.todo.no_empty"));
    }

    private void refreshList() {
        listPanel.removeAll();

        if (todos.isEmpty()) {
            JLabel empty = new JLabel(Translations.get("tabs.todo.empty_list"));
            listPanel.add(empty);
        } else {
            for (TodoItem todo : todos) if (todo.pinned) addTodoToList(todo);
            for (TodoItem todo : todos) if (!todo.pinned) addTodoToList(todo);
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private void addTodoToList(TodoItem todo) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(4, 0, 4, 0));

        JCheckBox check = new JCheckBox();
        check.setSelected(todo.done);

        JLabel label = new JLabel(todo.text);
        updateLabelStyle(label, todo.done);

        JButton settings = new JButton("⚙");
        settings.setMargin(new Insets(2, 6, 2, 6));
        settings.addActionListener(e -> openTodoSettings(todo));

        check.addItemListener(e -> {
            todo.done = (e.getStateChange() == ItemEvent.SELECTED);
            updateLabelStyle(label, todo.done);
            saveTodos();
        });

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        left.setOpaque(false);
        left.add(check);
        left.add(label);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(settings);

        row.add(left, BorderLayout.WEST);
        row.add(right, BorderLayout.EAST);
        listPanel.add(row);
    }

    private void updateLabelStyle(JLabel label, boolean done) {
        if (done) {
            label.setText("<html><strike>" + escapeHtml(label.getText()) + "</strike></html>");
            label.setFont(label.getFont().deriveFont(Font.ITALIC));
        } else {
            label.setText(label.getText().replaceAll("<[^>]+>", ""));
            label.setText(escapeHtml(label.getText()));
            label.setFont(label.getFont().deriveFont(Font.PLAIN));
        }
    }

    private void loadTodos() {
        if (Files.exists(saveFile)) {
            try (Reader reader = new InputStreamReader(new FileInputStream(saveFile.toFile()), StandardCharsets.UTF_8)) {
                Type listType = new TypeToken<List<TodoItem>>(){}.getType();
                List<TodoItem> loaded = new Gson().fromJson(reader, listType);
                if (loaded != null) todos.addAll(loaded);
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    private void saveTodos() {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile.toFile()), StandardCharsets.UTF_8)) {
            new Gson().toJson(todos, writer);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void openTodoSettings(TodoItem todo) {
        // --- modal window ---
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(PANEL), Translations.get("tabs.todo.edit.title"), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 260);
        dialog.setLocationRelativeTo(PANEL);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(10, 10, 10, 10));

        // task title
        JLabel titleLabel = new JLabel(todo.text);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(10));

        // checkbox - copy actual state
        JCheckBox doneCheck = new JCheckBox(Translations.get("tabs.todo.done"));
        doneCheck.setSelected(todo.done);
        doneCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(doneCheck);
        content.add(Box.createVerticalStrut(10));

        // pine checkbox
        JCheckBox pineCheck = new JCheckBox(Translations.get("tabs.todo.pined"));
        pineCheck.setSelected(todo.pinned);
        pineCheck.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(pineCheck);
        content.add(Box.createVerticalStrut(10));

        // rename button
        JButton renameButton = new JButton(Translations.get("tabs.todo.rename"));
        renameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        renameButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog(dialog, Translations.get("tabs.todo.rename.prompt"), todo.text);
            newName = removeProhibitedChar(newName);
            if (!newName.trim().isEmpty()) titleLabel.setText(escapeHtml(newName.trim()));
        });
        content.add(renameButton);
        content.add(Box.createVerticalStrut(10));

        // delete button
        JButton deleteButton = new JButton(Translations.get("tabs.todo.delete"));
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    Translations.get("tabs.todo.delete.confirm") + "\n\"" + todo.text + "\" ?",
                    Translations.get("tabs.todo.delete.title"),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.OK_OPTION) {
                todos.remove(todo);
                saveTodos();
                refreshList();
                dialog.dispose();
            }
        });
        content.add(deleteButton);

        // add content to dialog
        dialog.add(content, BorderLayout.CENTER);

        // down : ok - cancel
        // setup
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton(Translations.get("common.validate"));
        JButton cancelButton = new JButton(Translations.get("common.cancel"));

        okButton.addActionListener(e -> {
            todo.text = titleLabel.getText();
            todo.done = doneCheck.isSelected();
            todo.pinned = pineCheck.isSelected();
            saveTodos();
            refreshList();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        // adding
        bottom.add(okButton);
        bottom.add(cancelButton);

        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.getRootPane().setDefaultButton(okButton);
        dialog.setVisible(true);
    }


    private static class TodoItem {
        String text;
        boolean done;
        boolean pinned = false;

        public TodoItem(String text, boolean done, boolean pinned) {
            this.text = text;
            this.done = done;
            this.pinned = pinned;
        }
    }
}
