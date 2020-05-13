package ink.ptms.cronus.builder.editor.data;

import com.google.common.collect.Lists;
import ink.ptms.cronus.builder.editor.EditorAPI;
import ink.ptms.cronus.builder.editor.module.action.IActionEdit;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author 坏黑
 * @Since 2019-03-11 18:33
 */
public class PlayerData {

    private final String name;
    private File current;
    private List<File> files;
    private List<String> lines;
    private List<IActionEdit> actionEdit;
    private LinkedList<File> history;
    private Map<String, Pattern> filter;
    private int index;
    private boolean saved;

    private Runnable saveTask;
    private Runnable closeTask;

    public PlayerData(String name) {
        this.name = name;
        this.current = new File("plugins");
        this.history = Lists.newLinkedList();
    }

    public String getCurrentCanonicalPath() {
        try {
            return EditorAPI.formatPath(current.getCanonicalPath());
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public void resetCache() {
        this.files = null;
        this.lines = null;
        this.actionEdit = null;
        this.index = 0;
        this.saved = false;
    }

    // *********************************
    //
    //        Getter and Setter
    //
    // *********************************

    public Runnable getSaveTask() {
        return saveTask;
    }

    public Runnable getCloseTask() {
        return closeTask;
    }

    public void setSaveTask(Runnable saveTask) {
        this.saveTask = saveTask;
    }

    public void setCloseTask(Runnable closeTask) {
        this.closeTask = closeTask;
    }

    public String getName() {
        return name;
    }

    public File getCurrent() {
        return current;
    }

    public void setCurrent(File current) {
        this.current = current;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public List<IActionEdit> getActionEdit() {
        return actionEdit;
    }

    public void setActionEdit(List<IActionEdit> actionEdit) {
        this.actionEdit = actionEdit;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public LinkedList<File> getHistory() {
        return history;
    }

    public Map<String, Pattern> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Pattern> filter) {
        this.filter = filter;
    }
}
