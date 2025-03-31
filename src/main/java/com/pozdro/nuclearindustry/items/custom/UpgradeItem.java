package com.pozdro.nuclearindustry.items.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UpgradeItem extends Item {
    public UpgradeItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide()){
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("hhhhhhh"));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
