package pl.bananek355.autotoolmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import pl.bananek355.autotoolmod.Autotoolmod;

public class AutotoolmodClient implements ClientModInitializer {
    private static KeyBinding toggleKey;

    @Override
    public void onInitializeClient() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.autotoolmod.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.autotoolmod"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            while (toggleKey.wasPressed()) {
                Autotoolmod.enabled = !Autotoolmod.enabled;

                Text message = Autotoolmod.enabled ?
                        Text.literal("§aAutoTool turned ON") :
                        Text.literal("§cAutoTool turned OFF");

                client.player.sendMessage(message, true);
            }
        });
    }
}