package net.orandja.strawberry.mods.moretools.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.orandja.chocoflavor.utils.NBTUtils;
import net.orandja.chocoflavor.utils.StackUtils;
import net.orandja.chocoflavor.utils.TextUtils;
import net.orandja.chocoflavor.utils.Utils;
import net.orandja.strawberry.mods.core.intf.StrawberryItem;
import net.orandja.strawberry.mods.moretools.CustomToolMaterial;

public class StrawberrySwordItem extends SwordItem implements StrawberryItem {

    private final int customDataModel;
    private final SwordItem replacementItem;

    public StrawberrySwordItem(CustomToolMaterial material, Item replacementItem, int customDataModel) {
        this(material, ((SwordItem)replacementItem), customDataModel, new Item.Settings());
    }

    public StrawberrySwordItem(ToolMaterial material, SwordItem replacementItem, int customDataModel, Settings settings) {
        this(material, replacementItem, customDataModel, getBaseAttackDamage(replacementItem), getAttackSpeed(replacementItem), settings);
    }

    public StrawberrySwordItem(ToolMaterial material, SwordItem replacementItem, int customDataModel, int damage, float attackSpeed, Settings settings) {
        super(material, damage, attackSpeed, settings);
        this.customDataModel = customDataModel;
        this.replacementItem = replacementItem;
    }

    @Override
    public ItemStack transform(ItemStack sourceStack) {
        return Utils.apply(transform(sourceStack, this.replacementItem, this.customDataModel), it -> {
            it.setDamage(StackUtils.convertDurability(sourceStack, this.replacementItem));
            it.addHideFlag(ItemStack.TooltipSection.MODIFIERS);
            TextUtils.addDurability(it.getOrCreateNbt(), sourceStack.getDamage(), sourceStack.getMaxDamage());
        });
    }

    @Override
    public void register() {
        String path = Registries.ITEM.getId(this).getPath();
        register(this.replacementItem, this.customDataModel, path, "tools/" + path);
    }

    public static int getBaseAttackDamage(SwordItem pickaxeItem) {
        return (int) (pickaxeItem.getAttackDamage() - pickaxeItem.getMaterial().getAttackDamage());
    }

    public static float getAttackSpeed(SwordItem pickaxeItem) {
        return (float) ((ImmutableList<EntityAttributeModifier>)pickaxeItem.getAttributeModifiers(EquipmentSlot.MAINHAND).get(EntityAttributes.GENERIC_ATTACK_SPEED)).get(0).getValue();
    }
}