package net.tjkraft.cesmptweaks.event.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.tjkraft.cesmptweaks.CreateEconomySMPTweaks;
import net.tjkraft.cesmptweaks.config.CESMPTweaksClientConfig;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.season.SeasonTime;

@Mod.EventBusSubscriber(modid = CreateEconomySMPTweaks.MOD_ID, value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onRenderHUD(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.level == null) return;

        long time = mc.level.getDayTime();
        long ticksInDay = time % 24000;
        long adjustedTicks = (ticksInDay + 24000 + 6000) % 24000;
        long hours = (adjustedTicks * 24) / 24000;
        long minutes = ((adjustedTicks * 24 * 60) / 24000) % 60;

        String timeString = String.format("%02d:%02d", hours, minutes);
        Font font = mc.font;
        GuiGraphics guiGraphics = event.getGuiGraphics();

        int x = 10;
        int y = 10;

        if (CESMPTweaksClientConfig.TIME_OVERLAY.get()) {
            guiGraphics.renderItem(new ItemStack(Items.CLOCK), x, y);
            guiGraphics.drawString(font, timeString, x + 20, y + 4, 0xFFFFFF);
        }

        //Serene Seasons
        if (ModList.get().isLoaded("sereneseasons")) {
            int seasonsCycle = SeasonHelper.getSeasonState(mc.level).getSeasonCycleTicks();
            SeasonTime timeSeasons = new SeasonTime(seasonsCycle);

            if (CESMPTweaksClientConfig.TIME_OVERLAY.get()) {
                y = y + 20;
            }

            if (CESMPTweaksClientConfig.SEASONS_OVERLAY.get()) {
                String seasonName = "";

                switch (timeSeasons.getSeason()) {
                    case SPRING -> {
                        guiGraphics.blit(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/spring.png"), x, y, 0, 0, 16, 16, 16, 16);
                        seasonName = "Spring";
                    }
                    case SUMMER -> {
                        guiGraphics.blit(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/summer.png"), x, y, 0, 0, 16, 16, 16, 16);
                        seasonName = "Summer";
                    }
                    case AUTUMN -> {
                        guiGraphics.blit(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/autumn.png"), x, y, 0, 0, 16, 16, 16, 16);
                        seasonName = "Autumn";
                    }
                    case WINTER -> {
                        guiGraphics.blit(new ResourceLocation(CreateEconomySMPTweaks.MOD_ID, "textures/gui/winter.png"), x, y, 0, 0, 16, 16, 16, 16);
                        seasonName = "Winter";
                    }
                    default -> {
                    }
                }
                guiGraphics.drawString(mc.font, seasonName, x + 20, y + 4, 0xffffff);
            }
        }
    }
}
