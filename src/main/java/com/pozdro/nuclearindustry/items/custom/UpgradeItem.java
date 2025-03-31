package com.pozdro.nuclearindustry.items.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UpgradeItem extends Item {

    public UpgradeItem(Properties pProperties) {
        super(pProperties);
    }

    enum FilterModes{
        skipHydrogen,
        skipOxygen,
        skipHydrogenAndOxygen
    }

    protected FilterModes filterModes =FilterModes.skipHydrogen;
    private int sel=0;

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if(pLevel.isClientSide()){
            sel++;
            if(sel > FilterModes.values().length-1){
                sel=0;
            }
            filterModes = FilterModes.values()[sel];
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Setting filter mode to: "+
                    filterModes.toString().toUpperCase()));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("MODE: "+filterModes.toString().toUpperCase()).withStyle(ChatFormatting.AQUA));

        if(Screen.hasShiftDown()){
            pTooltipComponents.add(Component.literal("THIS IS A WIP ITEM"));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

    }
}
