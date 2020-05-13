package ink.ptms.cronus.builder.editor.module.tag;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author 坏黑
 * @Since 2019-03-12 13:39
 */
public class TagResult {

    private String result;
    private Set<String> tags = Sets.newHashSet();
    private Map<String, String> attributes = Maps.newHashMap();

    public TagResult(String result) {
        setResult(result);
    }

    public void setResult(String result) {
        this.result = Optional.ofNullable(result).orElse("");
    }

    public String getResult() {
        return result;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
