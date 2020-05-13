package ink.ptms.cronus.command.impl;

import com.google.common.collect.Lists;
import ink.ptms.cronus.Cronus;
import ink.ptms.cronus.builder.element.BuilderQuest;
import ink.ptms.cronus.command.CronusCommand;
import io.izzel.taboolib.module.command.base.*;
import io.izzel.taboolib.util.Files;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author 坏黑
 * @Since 2019-06-18 19:43
 */
@BaseCommand(name = "CronusBuilder", aliases = {"cBuilder", "cb"}, permission = "*")
public class CommandBuilder extends CronusCommand {

    @SubCommand
    BaseSubCommand create = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "创建任务构造器";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("名称")
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            File file = new File(Cronus.getCronusLoader().getFolder(), "builder/" + args[0] + ".yml");
            if (file.exists()) {
                error(sender, "任务 §7" + args[0] + " §c已存在.");
                return;
            }
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.createSection(args[0]);
            try {
                yaml.save(file);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            normal(sender, "任务 §f" + args[0] + " §7创建成功.");
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };

    @SubCommand
    BaseSubCommand open = new BaseSubCommand() {

        @Override
        public String getDescription() {
            return "打开任务构造器";
        }

        @Override
        public Argument[] getArguments() {
            return new Argument[] {
                    new Argument("名称", () -> {
                        File file = Files.folder(new File(Cronus.getCronusLoader().getFolder(), "builder"));
                        if (file.listFiles() == null) {
                            return Lists.newArrayList();
                        }
                        return Arrays.stream(file.listFiles()).map(s -> {
                            try {
                                return s.getName().substring(0, s.getName().indexOf("."));
                            } catch (Throwable ignored) {
                                return null;
                            }
                        }).filter(Objects::nonNull).collect(Collectors.toList());
                    })
            };
        }

        @Override
        public void onCommand(CommandSender sender, Command command, String s, String[] args) {
            File file = new File(Cronus.getCronusLoader().getFolder(), "builder/" + args[0] + ".yml");
            if (!file.exists()) {
                error(sender, "任务 §7" + args[0] + " §c无效.");
                return;
            }
            new BuilderQuest(file).open((Player) sender);
        }

        @Override
        public CommandType getType() {
            return CommandType.PLAYER;
        }
    };
}
