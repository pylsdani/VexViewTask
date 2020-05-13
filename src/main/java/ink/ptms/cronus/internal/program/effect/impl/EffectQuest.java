package ink.ptms.cronus.internal.program.effect.impl;

import ink.ptms.cronus.CronusAPI;
import ink.ptms.cronus.internal.program.QuestProgram;
import ink.ptms.cronus.uranus.annotations.Auto;
import ink.ptms.cronus.uranus.function.FunctionParser;
import ink.ptms.cronus.uranus.program.Program;
import ink.ptms.cronus.uranus.program.effect.Effect;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;

import java.util.regex.Matcher;

/**
 * @Author 坏黑
 * @Since 2019-05-11 15:24
 */
@Auto
public class EffectQuest extends Effect {

    @TInject
    private static TLogger logger;
    private String action;
    private String value;

    @Override
    public String pattern() {
        return "quest\\.(?!var|val|log)(?<action>\\S+) (?<value>.+)";
    }

    @Override
    public String getExample() {
        return "quest.[action] [content]";
    }

    @Override
    public void match(Matcher matcher) {
        action = matcher.group("action").toLowerCase();
        value = matcher.group("value");
    }

    @Override
    public void eval(Program program) {
        if (program instanceof QuestProgram) {
            switch (action) {
                case "accept":
                    CronusAPI.acceptQuest(((QuestProgram) program).getPlayer(), FunctionParser.parseAll(program, value));
                    break;
                case "quit":
                case "failure":
                    CronusAPI.failureQuest(((QuestProgram) program).getPlayer(), FunctionParser.parseAll(program, value));
                    break;
                default:
                    logger.warn("Invalid Action: " + action + " " + value);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return "EffectQuest{" +
                "action='" + action + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
