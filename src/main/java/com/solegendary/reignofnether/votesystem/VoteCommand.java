package com.solegendary.reignofnether.votesystem;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class VoteCommand {

    /*
    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
                Commands.literal("vote")
                        .executes(context -> {
                            Supplier<ServerPlayer> player = () -> context.getSource().getPlayer();
                            List<MapData> maps = loadMaps(context.getSource());
                            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(player), new ClientboundOpenVotenScreenPacket(maps));
                            return 1;
                        })
        );
    }
     */
    private static List<MapData> loadMaps(CommandSourceStack source) {
        MinecraftServer minecraftServerInstance = source.getServer();
        ResourceManager resourceManager = minecraftServerInstance.getResourceManager();
        List<MapData> maps = MapDataLoader.loadMaps(resourceManager);
        return maps;
    }


}

