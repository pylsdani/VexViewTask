package ink.ptms.cronus.builder.editor.module.action;

/**
 * @Author 坏黑
 * @Since 2019-03-12 14:06
 */
public abstract class IActionEdit {

    private int line;
    private String value;

    public IActionEdit(int line, String value) {
        this.line = line;
        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "IActionEdit{" +
                "line=" + line +
                ", value='" + value + '\'' +
                '}';
    }
}
