/*
 * Copyright Developing-Rene(c) 2022. Do not Change this resource without permissions
 */

package me.femrene;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.femrene.commands.Manager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static JDA jda = null;
    public static JDABuilder builder = null;

    public static void main(String[] args) throws IOException {
        BotConfig.CreateConfig();
        builder = JDABuilder.createDefault(BotConfig.getString("token"));
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(new Manager());
        builder.setStatus(OnlineStatus.ONLINE);
        AudioTrack audioTrack = Manager.at;
        String a;
        if (audioTrack != null){
            a = BotConfig.getString("activity").replace("%song%",audioTrack.getInfo().title+" "+audioTrack.getInfo().author);
        } else {
            a = BotConfig.getString("activity").replace("%song%","nothing");
        }
        builder.setActivity(Activity.playing(a));
        jda = builder.build();
        setSlashCommands();
        System.out.println("[BOT] Online");
        stop();
    }

    private static void setSlashCommands() {
        jda.upsertCommand("play","Add a song to the queue").addOption(OptionType.STRING,"song","Name or URL of the Song",true).queue();
        jda.upsertCommand("volume","Changes the Volume").addOption(OptionType.STRING,"volume","Value of the Volume for the Bot",true).queue();
        jda.upsertCommand("stop","Lets disconnect the Bot").queue();
        jda.upsertCommand("next","Skips to the next Song").queue();
        jda.upsertCommand("pause","Stops the Playback").queue();
        jda.upsertCommand("resume","Resumes the Playback").queue();
    }

    private static void stop() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (reader.readLine().equalsIgnoreCase("stop")) {
                builder.setStatus(OnlineStatus.OFFLINE);
                jda.shutdown();
                System.out.println("[BOT] Offline");
                reader.close();
                System.exit(-1);
            }
        }
    }
}
