package botMain;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Main {
    private static JDA bot;
    public static boolean bracketInProgress = false;
    public static Guild guild;
    public static Brackets brackets;

    public static boolean testingMode = true;

    public static final String voiceChannelId =      "866899403821678632";
    public static final String botChannelId =        "866899299090169856";
    public static final String botChannelImageId =   "866899483598389268";

//    public static final String voiceChannelId =      "845373357096566822";
//    public static final String botChannelId =        "847491106442706994";
//    public static final String botChannelResultId =  "863540090281787412";
//    public static final String botChannelScoringId = "863540054769401906";
//    public static final String botChannelImageId =   "864661994690314280";

    public static final VoiceChannel[] vcs = new VoiceChannel[7];


    public static void main(String[] args) throws LoginException, InterruptedException {
        bot = JDABuilder.createDefault(Secret.TutorialBotToken,
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_EMOJIS
                )
                .setActivity(Activity.playing("testToggled"))
                .addEventListeners(new Listener())
//                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
//                .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
//                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build().awaitReady();
        guild = bot.getGuilds().get(0);
//        guild.loadMembers();
//        Task<List<Member>> load = guild.loadMembers();
//        System.out.println(load.get());

        for (int i = 0; i < 7; i++) {
            if (i < 6) vcs[i] = bot.getVoiceChannelsByName("Game " + (i + 1), true).get(0);
            else vcs[i] = bot.getVoiceChannelsByName("Final Game", true).get(0);
        }

        brackets = new Brackets(bot, bot.getVoiceChannelById(voiceChannelId));

    }

    public static JDA getBot() {
        return bot;
    }
}
