/* 
 * Copyright (c) CovertJaguar, 2014 http://railcraft.info
 * 
 * This code is the property of CovertJaguar
 * and may only be used with explicit written
 * permission unless otherwise specified on the
 * license page at http://railcraft.info/wiki/info:license.
 */
package mods.railcraft.common.items;

import mods.railcraft.common.gui.tooltips.ToolTip;
import mods.railcraft.common.plugins.forge.CreativePlugin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ItemRailcraft extends Item implements IRailcraftItem {
    private float smeltingExperience = -1;
    private int rarity = 0;

    public ItemRailcraft() {
        setCreativeTab(CreativePlugin.RAILCRAFT_TAB);
    }

    public ItemRailcraft setRarity(int rarity) {
        this.rarity = rarity;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack) {
        return EnumRarity.values()[rarity];
    }

    public int getHeatValue(ItemStack stack) {
        return 0;
    }

    public ItemRailcraft setSmeltingExperience(float smeltingExperience) {
        this.smeltingExperience = smeltingExperience;
        return this;
    }

    @Override
    public float getSmeltingExperience(ItemStack item) {
        return smeltingExperience;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean adv) {
        super.addInformation(stack, player, info, adv);
        ToolTip tip = ToolTip.buildToolTip(stack.getUnlocalizedName() + ".tip");
        if (tip != null)
            info.addAll(tip.convertToStrings());
    }

    @Override
    public Object getRecipeObject(IItemMetaEnum meta) {
        assertMeta(meta);
        String oreTag = getOreTag(meta);
        if (oreTag != null)
            return oreTag;
        if (meta != null && getHasSubtypes())
            return new ItemStack(this, 1, meta.ordinal());
        return this;
    }

    public String getOreTag(IItemMetaEnum meta) {
        return null;
    }

    protected void assertMeta(IItemMetaEnum meta) {
        if (meta != null && meta.getItemClass() != getClass())
            throw new RuntimeException("Incorrect Item Meta object used.");
    }

    @Override
    public void defineRecipes() {
    }

    @Override
    public void definePostRecipes() {
    }

    @Override
    public void initItem() {
    }
}
