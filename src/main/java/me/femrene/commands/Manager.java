/*
 * Copyright Developing-Rene(c) 2022. Do not Change this resource without permissions
 */

package de.DevelopingRene.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.DevelopingRene.BotConfig;
import de.DevelopingRene.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Manager extends ListenerAdapter {

    public static AudioTrack at = null;

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        super.onSlashCommandInteraction(event);
        event.deferReply().queue();
        switch (event.getName()) {
            case "play":
                String[] args = Objects.requireNonNull(event.getOption("song")).toString().split(" ");
                if (!event.getMember().getVoiceState().inAudioChannel()) {
                    //event.getChannel().sendMessage("You need to be in a VoiceChannel").queue();
                    event.getHook().sendMessage("You need to be in a Voice channel").setEphemeral(true).queue();
                    return;
                }

                if (event.getMember().getVoiceState().inAudioChannel()) {
                    final AudioManager audioManager = event.getGuild().getAudioManager();
                    final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                    audioManager.openAudioConnection(memberChannel);
                }

                StringBuilder a = new StringBuilder();
                for (String arg : args) {
                    a.append(arg);
                }
                //System.out.println(a.toString());
                String b = a.toString();
                String c = String.join(" ", b);
                String d = c.replace("Option[STRING](song=","");
                String e = d.replace(")","");
                if (!isURL(e)) {
                    e = "ytsearch:"+e+" audio";
                }
                PlayerManager.getINSTANCE().loadAndPlay(event.getChannel().asTextChannel(), e);
                AudioTrack at = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack();
                event.getHook().sendMessage("Song added tu queue").setEphemeral(true).queue();
                break;
            case "stop":
                if (event.getGuild().getAudioManager().isConnected()) {
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.stopTrack();
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue.clear();
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.destroy();
                    event.getGuild().getAudioManager().closeAudioConnection();
                    event.getHook().sendMessage("Queue cleared and the Bot Disconnected").setEphemeral(true).queue();
                }
                break;
            case "next":
                if (event.getGuild().getAudioManager().isConnected()) {
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.nextTrack();
                    AudioTrack ata = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack();
                    event.getHook().sendMessage("Now playing **"+ata.getInfo().title+"** by **"+ata.getInfo().author+"**").setEphemeral(true).queue();
                }
                break;
            case "pause":
                if (event.getGuild().getAudioManager().isConnected()) {
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.setPaused(true);
                    event.getHook().sendMessage("Playback paused").setEphemeral(true).queue();
                }
                break;
            case "volume":
                if (event.getGuild().getAudioManager().isConnected()) {
                    String y = event.getOption("volume").toString().replace("Option[STRING](volume=","").replace(")","");
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.setVolume(Integer.parseInt(y));
                    event.getHook().sendMessage("Volume set to "+y+"%").setEphemeral(true).queue();
                }
                break;
            case "resume":
                if (event.getGuild().getAudioManager().isConnected() && PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.isPaused()) {
                    PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.setPaused(false);
                    event.getHook().sendMessage("Playback resumed").setEphemeral(true).queue();
                }
                break;
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentStripped().split(" ");
        if (event.getMessage().getContentStripped().startsWith(BotConfig.getString("prefix") + "play")) {
            if (!event.getMember().getVoiceState().inAudioChannel()) {
                event.getChannel().sendMessage("You need to be in a VoiceChannel").queue();
                return;
            }

            if (event.getMember().getVoiceState().inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                audioManager.openAudioConnection(memberChannel);
            }

            StringBuilder a = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                a.append(args[i]);
            }
            String b = a.toString();
            String link = String.join(" ", b);
            if (!isURL(b)) {
                link = "ytsearch:"+b+" audio";
            }
            PlayerManager.getINSTANCE().loadAndPlay(event.getChannel().asTextChannel(), link);
            AudioTrack at = PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.getPlayingTrack();
        } else if (event.getMessage().getContentStripped().startsWith(BotConfig.getString("prefix") + "stop")) {
            if (event.getGuild().getAudioManager().isConnected()) {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.stopTrack();
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.queue.clear();
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.destroy();
                event.getGuild().getAudioManager().closeAudioConnection();
            }
        } else if (event.getMessage().getContentStripped().startsWith(BotConfig.getString("prefix") + "next")) {
            if (event.getGuild().getAudioManager().isConnected()) {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).scheduler.nextTrack();
            }
        } else if (event.getMessage().getContentStripped().startsWith(BotConfig.getString("prefix") + "pause")) {
            if (event.getGuild().getAudioManager().isConnected()) {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.setPaused(true);
            }
        } else if (event.getMessage().getContentStripped().startsWith(BotConfig.getString("prefix") + "vol")) {
            if (event.getGuild().getAudioManager().isConnected()) {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.setVolume(Integer.valueOf(args[1]));
            }
        } else if (event.getMessage().getContentStripped().startsWith(BotConfig.getString("prefix") + "resume")) {
            if (event.getGuild().getAudioManager().isConnected() && PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.isPaused()) {
                PlayerManager.getINSTANCE().getMusicManager(event.getGuild()).audioPlayer.setPaused(false);
            }
        }
    }

    public boolean isURL(String url) {
        try {
            (new java.net.URL(url)).openStream().close();
            return true;
        } catch (Exception ex) { }
        return false;
    }

}
