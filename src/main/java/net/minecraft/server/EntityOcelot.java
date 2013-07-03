package net.minecraft.server;


import net.canarymod.api.entity.living.animal.CanaryOcelot;
import net.canarymod.hook.entity.EntityTameHook;


public class EntityOcelot extends EntityTameable {

    private EntityAITempt bq;

    public EntityOcelot(World world) {
        super(world);
        this.a(0.6F, 0.8F);
        this.k().a(true);
        this.c.a(1, new EntityAISwimming(this));
        this.c.a(2, this.bp);
        this.c.a(3, this.bq = new EntityAITempt(this, 0.6D, Item.aW.cv, true));
        this.c.a(4, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.8D, 1.33D));
        this.c.a(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        this.c.a(6, new EntityAIOcelotSit(this, 1.33D));
        this.c.a(7, new EntityAILeapAtTarget(this, 0.3F));
        this.c.a(8, new EntityAIOcelotAttack(this));
        this.c.a(9, new EntityAIMate(this, 0.8D));
        this.c.a(10, new EntityAIWander(this, 0.8D));
        this.c.a(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.d.a(1, new EntityAITargetNonTamed(this, EntityChicken.class, 750, false));
        this.entity = new CanaryOcelot(this); // CanaryMod: Wrap Entity
    }

    protected void a() {
        super.a();
        this.ah.a(18, Byte.valueOf((byte) 0));
    }

    public void bg() {
        if (this.i().a()) {
            double d0 = this.i().b();

            if (d0 == 0.6D) {
                this.b(true);
                this.c(false);
            } else if (d0 == 1.33D) {
                this.b(false);
                this.c(true);
            } else {
                this.b(false);
                this.c(false);
            }
        } else {
            this.b(false);
            this.c(false);
        }
    }

    protected boolean t() {
        return !this.bP() && this.ac > 2400;
    }

    public boolean bb() {
        return true;
    }

    protected void ax() {
        super.ax();
        this.a(SharedMonsterAttributes.a).a(10.0D);
        this.a(SharedMonsterAttributes.d).a(0.30000001192092896D);
    }

    public int aW() {
        return maxHealth == 0 ? 10 : maxHealth; // CanaryMod: custom Max Health
    }

    protected void b(float f0) {}

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("CatType", this.bW());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.p(nbttagcompound.e("CatType"));
    }

    protected String r() {
        return this.bP() ? (this.bU() ? "mob.cat.purr" : (this.ab.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
    }

    protected String aK() {
        return "mob.cat.hitt";
    }

    protected String aL() {
        return "mob.cat.hitt";
    }

    protected float aW() {
        return 0.4F;
    }

    protected int s() {
        return Item.aH.cv;
    }

    public boolean m(Entity entity) {
        return entity.a(DamageSource.a((EntityLivingBase) this), 3.0F);
    }

    public boolean a(DamageSource damagesource, float f0) {
        if (this.ap()) {
            return false;
        } else {
            this.bp.a(false);
            return super.a(damagesource, f0);
        }
    }

    protected void b(boolean flag0, int i0) {}

    public boolean a(EntityPlayer entityplayer) {
        ItemStack itemstack = entityplayer.bn.h();

        if (this.bP()) {
            if (entityplayer.c_().equalsIgnoreCase(this.h_()) && !this.q.I && !this.c(itemstack)) {
                this.bp.a(!this.bQ());
            }
        } else if (this.bq.f() && itemstack != null && itemstack.d == Item.aW.cv && entityplayer.e(this) < 9.0D) {
            if (!entityplayer.bG.d) {
                --itemstack.b;
            }

            if (itemstack.b <= 0) {
                entityplayer.bn.a(entityplayer.bn.c, (ItemStack) null);
            }

            if (!this.q.I) {
                // CanaryMod: EntityTame
                EntityTameHook hook = new EntityTameHook((net.canarymod.api.entity.living.animal.EntityAnimal) this.getCanaryEntity(), ((EntityPlayerMP) entityplayer).getPlayer(), this.ab.nextInt(3) == 0);

                if (hook.isTamed() && !hook.isCanceled()) {
                    //
                    this.k(true);
                    this.p(1 + this.q.s.nextInt(3));
                    this.b(entityplayer.c_());
                    this.j(true);
                    this.bp.a(true);
                    this.q.a((Entity) this, (byte) 7);
                } else {
                    this.j(false);
                    this.q.a((Entity) this, (byte) 6);
                }
            }

            return true;
        }

        return super.a(entityplayer);
    }

    public EntityOcelot b(EntityAgeable entityageable) {
        EntityOcelot entityocelot = new EntityOcelot(this.q);

        if (this.bP()) {
            entityocelot.b(this.h_());
            entityocelot.k(true);
            entityocelot.p(this.bW());
        }

        return entityocelot;
    }

    public boolean c(ItemStack itemstack) {
        return itemstack != null && itemstack.d == Item.aW.cv;
    }

    public boolean a(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.bP()) {
            return false;
        } else if (!(entityanimal instanceof EntityOcelot)) {
            return false;
        } else {
            EntityOcelot entityocelot = (EntityOcelot) entityanimal;

            return !entityocelot.bP() ? false : this.bU() && entityocelot.bU();
        }
    }

    public int bW() {
        return this.ah.a(18);
    }

    public void p(int i0) {
        this.ah.b(18, Byte.valueOf((byte) i0));
    }

    public boolean bo() {
        if (this.q.s.nextInt(3) == 0) {
            return false;
        } else {
            if (this.q.b(this.E) && this.q.a((Entity) this, this.E).isEmpty() && !this.q.d(this.E)) {
                int i0 = MathHelper.c(this.u);
                int i1 = MathHelper.c(this.E.b);
                int i2 = MathHelper.c(this.w);

                if (i1 < 63) {
                    return false;
                }

                int i3 = this.q.a(i0, i1 - 1, i2);

                if (i3 == Block.z.cF || i3 == Block.P.cF) {
                    return true;
                }
            }

            return false;
        }
    }

    public String al() {
        return this.bx() ? this.bw() : (this.bP() ? "entity.Cat.name" : super.al());
    }

    public EntityLivingData a(EntityLivingData entitylivingdata) {
        entitylivingdata = super.a(entitylivingdata);
        if (this.q.s.nextInt(7) == 0) {
            for (int i0 = 0; i0 < 2; ++i0) {
                EntityOcelot entityocelot = new EntityOcelot(this.q);

                entityocelot.b(this.u, this.v, this.w, this.A, 0.0F);
                entityocelot.c(-24000);
                this.q.d((Entity) entityocelot);
            }
        }

        return entitylivingdata;
    }

    public EntityAgeable a(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
