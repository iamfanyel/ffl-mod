package com.alaharranhonor.vfl.client.registry;

import com.mojang.blaze3d.platform.InputConstants.Type;
import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeybindSetup {
   public static final KeyMapping TOGGLE_HELMET;

   public static void init(Consumer<KeyMapping> registrar) {
      registrar.accept(TOGGLE_HELMET);
   }

   static {
      TOGGLE_HELMET = new KeyMapping("key.vfl.toggle_helmet", KeyConflictContext.IN_GAME, Type.MOUSE, 2, "key.vfl.category");
   }
}
