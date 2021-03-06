package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace extends Container {

    private final IInventory a;
    private int f;
    private int g;
    private int h;
    private int i;

    public ContainerFurnace(InventoryPlayer inventoryplayer, IInventory iinventory) {
        this.a = iinventory;
        this.a(new Slot(iinventory, 0, 56, 17));
        this.a((Slot)(new SlotFurnaceFuel(iinventory, 1, 56, 53)));
        this.a((Slot)(new SlotFurnaceOutput(inventoryplayer.d, iinventory, 2, 116, 35)));

        int i0;

        for (i0 = 0; i0 < 3; ++i0) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(inventoryplayer, i1 + i0 * 9 + 9, 8 + i1 * 18, 84 + i0 * 18));
            }
        }

        for (i0 = 0; i0 < 9; ++i0) {
            this.a(new Slot(inventoryplayer, i0, 8 + i0 * 18, 142));
        }

        this.inventory = ((TileEntityFurnace)a).getCanaryFurnace(); // CanaryMod: Set inventory instance
    }

    public void a(ICrafting icrafting) {
        super.a(icrafting);
        icrafting.a(this, this.a);
    }

    public void b() {
        super.b();

        for (int i0 = 0; i0 < this.e.size(); ++i0) {
            ICrafting icrafting = (ICrafting)this.e.get(i0);

            if (this.f != this.a.a_(2)) {
                icrafting.a(this, 2, this.a.a_(2));
            }

            if (this.h != this.a.a_(0)) {
                icrafting.a(this, 0, this.a.a_(0));
            }

            if (this.i != this.a.a_(1)) {
                icrafting.a(this, 1, this.a.a_(1));
            }

            if (this.g != this.a.a_(3)) {
                icrafting.a(this, 3, this.a.a_(3));
            }
        }

        this.f = this.a.a_(2);
        this.h = this.a.a_(0);
        this.i = this.a.a_(1);
        this.g = this.a.a_(3);
    }

    public boolean a(EntityPlayer entityplayer) {
        return this.a.a(entityplayer);
    }

    public ItemStack b(EntityPlayer entityplayer, int i0) {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.c.get(i0);

        if (slot != null && slot.e()) {
            ItemStack itemstack1 = slot.d();

            itemstack = itemstack1.k();
            if (i0 == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            }
            else if (i0 != 1 && i0 != 0) {
                if (FurnaceRecipes.a().a(itemstack1) != null) {
                    if (!this.a(itemstack1, 0, 1, false)) {
                        return null;
                    }
                }
                else if (TileEntityFurnace.c(itemstack1)) {
                    if (!this.a(itemstack1, 1, 2, false)) {
                        return null;
                    }
                }
                else if (i0 >= 3 && i0 < 30) {
                    if (!this.a(itemstack1, 30, 39, false)) {
                        return null;
                    }
                }
                else if (i0 >= 30 && i0 < 39 && !this.a(itemstack1, 3, 30, false)) {
                    return null;
                }
            }
            else if (!this.a(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.b == 0) {
                slot.d((ItemStack)null);
            }
            else {
                slot.f();
            }

            if (itemstack1.b == itemstack.b) {
                return null;
            }

            slot.a(entityplayer, itemstack1);
        }

        return itemstack;
    }
}
