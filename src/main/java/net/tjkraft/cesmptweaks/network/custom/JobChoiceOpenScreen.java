package net.tjkraft.cesmptweaks.network.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tjkraft.cesmptweaks.gui.custom.jobChoice.JobChoiceScreen;

@OnlyIn(Dist.CLIENT)
public class JobChoiceOpenScreen {

    public static void open() {
        Minecraft.getInstance().setScreen(new JobChoiceScreen(Component.empty()));
    }
}
