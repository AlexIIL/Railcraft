/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.util.crafting;

import mods.railcraft.common.carts.EnumCart;
import mods.railcraft.common.carts.ICartType;
import mods.railcraft.common.items.firestone.ItemFirestoneCracked;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CraftingHandler {

    @SubscribeEvent
    public void onCrafting(ItemCraftedEvent event) {
        EntityPlayer player = event.player;
        ItemStack result = event.crafting;
        IInventory craftMatrix = event.craftMatrix;
        int count = 0;
        ItemStack cartItem = null;
        for (int i = 0; i < craftMatrix.getSizeInventory(); i++) {
            ItemStack stack = craftMatrix.getStackInSlot(i);
            if (stack != null) {
                count++;
                if (stack.getItem() == ItemFirestoneCracked.item)
                    craftMatrix.setInventorySlotContents(i, null);
                ICartType cartType = EnumCart.getCartType(stack);
                if (cartType != null && cartType != EnumCart.BASIC)
                    cartItem = stack;
            }
        }
        if (cartItem != null) {
//            CartFilterRecipe.FilterType type = CartFilterRecipe.FilterType.fromCartType(EnumCart.getCartType(cartItem));
//            if (type != null && EnumCart.getCartType(result) == EnumCart.getCartType(cartItem)) {
//                ItemStack filterItem = EntityCartFiltered.getFilterFromCartItem(result);
//                if (filterItem != null)
//                    for (IInvSlot slot : InventoryIterator.getIterable(craftMatrix).notNull()) {
//                        ItemStack stack = slot.getStackInSlot();
//                        if (InvTools.isItemEqual(stack, filterItem)) {
//                            if (!player.inventory.addItemStackToInventory(stack))
//                                player.dropPlayerItemWithRandomChoice(stack, false);
//                            slot.setStackInSlot(null);
//                        }
//                    }
//                return;
//            }

//            if (count == 1 && EnumCart.getCartType(result) == EnumCart.BASIC) {
//                ItemStack contents = EnumCart.getCartType(cartItem).getContents();
//                if (contents != null)
//                    if (!player.inventory.addItemStackToInventory(contents))
//                        player.dropPlayerItemWithRandomChoice(contents, false);
//            }
        }
    }

}
