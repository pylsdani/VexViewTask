package ink.ptms.cronus.builder.editor.module.tag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 坏黑
 * @Since 2019-03-12 13:34
 */
public class TagParser {

    private static Pattern pattern = Pattern.compile("(?<!/)/(?!/)(?<tag>\\S)(:(?<attribute>\\S+))?[ ]?");

    public static TagResult parser(String command) {
        TagResult result = new TagResult(command);
        Matcher matcher = pattern.matcher(command);
        while (matcher.find()) {
            if (matcher.group("attribute") != null) {
                result.getAttributes().put(matcher.group("tag"), matcher.group("attribute"));
            } else {
                result.getTags().add(matcher.group("tag"));
            }
            result.setResult(matcher.replaceFirst(""));
            matcher.reset(result.getResult());
        }
        result.setResult(result.getResult().replace("//", "/"));
        return result;
    }
}
