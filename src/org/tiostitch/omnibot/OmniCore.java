package org.tiostitch.omnibot;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.tiostitch.omnibot.commands.CommandManager;
import org.tiostitch.omnibot.configuration.Configuration;
import org.tiostitch.omnibot.configuration.ItemManager;
import org.tiostitch.omnibot.database.Database;

@Getter @Setter
public class OmniCore {

    public static OmniCore instance;

    private JDA BOT;

    private Configuration config;
    private Database database;
    private ItemManager item;
    private CommandManager command;

    private OmniCore(String[] args) {
        instance = this;

        boolean export = args.length > 1 && args[1].equals("load");

        //--# Loading bot
        setBOT(JDABuilder.createDefault(args[0]).build());

        //--# Loading configurations
        setConfig(new Configuration(this));
        getConfig().init(export);

        //--# Loading database
        setDatabase(new Database(this));
        getDatabase().init();

        //--# Loading items
        setItem(new ItemManager(this));
        getItem().init();

        //--# Loading commands
        setCommand(new CommandManager(this));
        getCommand().init();

    }


    public static void main(String[] args) {
        new OmniCore(args);
    }
}
