package ink.ptms.cronus.builder.element.condition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ink.ptms.cronus.internal.condition.ConditionParser;
import ink.ptms.cronus.internal.condition.collect.Collect;
import ink.ptms.cronus.internal.condition.collect.CollectA;
import ink.ptms.cronus.internal.condition.collect.CollectO;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @Author 坏黑
 * @Since 2019-06-21 9:46
 */
public class MatchEntry {

    private MatchType type;
    private String single;
    private List<MatchEntry> collect;

    public MatchEntry(MatchType type, String single) {
        this.type = type;
        this.single = single;
    }

    public MatchEntry(MatchType type, List<MatchEntry> collect) {
        this.type = type;
        this.collect = collect;
    }

    public MatchEntry(Object obj) {
        if (obj instanceof String) {
            type = MatchType.SINGLE;
            single = String.valueOf(obj);
        } else if (obj instanceof Collect) {
            type = obj instanceof CollectA ? MatchType.ALL_MATCH : MatchType.ANY_MATCH;
            Object predicate = ((Collect) obj).serialize().get("predicate");
            if (predicate == null) {
                return;
            }
            for (Object element : ((List) predicate)) {
                collect.add(new MatchEntry(element));
            }
        }
    }

    public String toSimple() {
        return type == MatchType.SINGLE ? single : (collect.isEmpty() ? "-" : collect.get(0).toSimple());
    }

    public void save(ConfigurationSection section, String key) {
        section.set(key, toObject());
    }

    public Object toObject() {
        if (type == MatchType.SINGLE) {
            return single;
        } else {
            Map<String, Object> map = Maps.newHashMap();
            map.put("predicate", collect.stream().map(MatchEntry::toObject).collect(Collectors.toList()));
            return type == MatchType.ALL_MATCH ? new CollectA(Lists.newArrayList(), map) : new CollectO(Lists.newArrayList(), map);
        }
    }

    public List<String> asList(int index) {
        List<String> list = Lists.newArrayList();
        if (type == MatchType.SINGLE) {
            list.add(IntStream.range(0, index).mapToObj(i -> "  ").collect(Collectors.joining()) + (index > 0 ? "§f- " : "") + "§8" + getSingleTranslate());
        } else {
            list.add(IntStream.range(0, index).mapToObj(i -> "  ").collect(Collectors.joining()) + (index > 0 ? "§f- " : "") + "§7" + type.getName() + ":");
            for (MatchEntry matchEntry : collect) {
                list.addAll(matchEntry.asList(index + 1));
            }
        }
        return list;
    }

    public String getSingleTranslate() {
        try {
            return Optional.ofNullable(ConditionParser.parse(single).translate()).orElse(single);
        } catch (Throwable ignored) {
            return single;
        }
    }

    public MatchType getType() {
        return this.type;
    }

    public String getSingle() {
        return this.single;
    }

    public List<MatchEntry> getCollect() {
        return this.collect;
    }

    public void setType(MatchType type) {
        this.type = type;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public void setCollect(List<MatchEntry> collect) {
        this.collect = collect;
    }
}
