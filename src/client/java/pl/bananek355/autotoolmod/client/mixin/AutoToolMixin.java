package pl.bananek355.autotoolmod.client.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pl.bananek355.autotoolmod.Autotoolmod;

@Mixin(ClientPlayerEntity.class)
public class AutoToolMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Sprawdzamy czy mod jest ON, czy mamy świat i czy gracz trzyma przycisk ataku
        if (!Autotoolmod.enabled || client.world == null || !client.options.attackKey.isPressed()) return;

        if (client.crosshairTarget instanceof BlockHitResult hit && hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos();
            BlockState state = client.world.getBlockState(pos);

            if (state.isAir()) return;

            ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
            PlayerInventory inv = player.getInventory();

            int bestSlot = -1;
            float bestSpeed = 1.0f;

            // Szukamy w hotbarze
            for (int i = 0; i < 9; i++) {
                ItemStack stack = inv.getStack(i);
                float speed = stack.getMiningSpeedMultiplier(state);

                if (speed > bestSpeed) {
                    bestSpeed = speed;
                    bestSlot = i;
                }
            }

            if (bestSlot != -1 && bestSlot != inv.selectedSlot) {
                inv.selectedSlot = bestSlot;
            }
        }
    }
}