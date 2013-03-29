package net.minecraft.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.canarymod.api.CanaryNetServerHandler;
import net.canarymod.api.entity.living.humanoid.CanaryPlayer;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {

    private StringTranslate cl = new StringTranslate("en_US");
    public NetServerHandler a;
    public MinecraftServer b;
    public ItemInWorldManager c;
    public double d;
    public double e;
    public final List f = new LinkedList();
    public final List g = new LinkedList();
    private int cm = -99999999;
    private int cn = -99999999;
    private boolean co = true;
    private int cp = -99999999;
    private int cq = 60;
    private int cr = 0;
    private int cs = 0;
    private boolean ct = true;
    private int cu = 0;
    public boolean h;
    public int i;
    public boolean j = false;
    //CanarMod
    private CanaryPlayer playerEntity;
    public EntityPlayerMP(MinecraftServer minecraftserver, World world, String s0, ItemInWorldManager iteminworldmanager) {
        super(world);
        iteminworldmanager.b = this;
        this.c = iteminworldmanager;
        this.cr = minecraftserver.ad().o();
        ChunkCoordinates chunkcoordinates = world.I();
        int i0 = chunkcoordinates.a;
        int i1 = chunkcoordinates.c;
        int i2 = chunkcoordinates.b;

        if (!world.t.f && world.L().r() != EnumGameType.d) {
            int i3 = Math.max(5, minecraftserver.ak() - 6);

            i0 += this.ab.nextInt(i3 * 2) - i3;
            i1 += this.ab.nextInt(i3 * 2) - i3;
            i2 = world.i(i0, i1);
        }

        this.b = minecraftserver;
        this.Y = 0.0F;
        this.bS = s0;
        this.N = 0.0F;
        this.b((double) i0 + 0.5D, (double) i2, (double) i1 + 0.5D, 0.0F, 0.0F);

        while (!world.a((Entity) this, this.E).isEmpty()) {
            this.b(this.u, this.v + 1.0D, this.w);
        }
        this.playerEntity = new CanaryPlayer(this);
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.b("playerGameType")) {
            this.c.a(EnumGameType.a(nbttagcompound.e("playerGameType")));
        }
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("playerGameType", this.c.b().a());
    }

    @Override
    public void a(int i0) {
        super.a(i0);
        this.cp = -1;
    }

    public void d_() {
        this.bM.a((ICrafting) this);
    }

    @Override
    protected void e_() {
        this.N = 0.0F;
    }

    @Override
    public float e() {
        return 1.62F;
    }

    @Override
    public void l_() {
        this.c.a();
        --this.cq;
        this.bM.b();

        while (!this.g.isEmpty()) {
            int i0 = Math.min(this.g.size(), 127);
            int[] aint = new int[i0];
            Iterator iterator = this.g.iterator();
            int i1 = 0;

            while (iterator.hasNext() && i1 < i0) {
                aint[i1++] = ((Integer) iterator.next()).intValue();
                iterator.remove();
            }

            this.a.b(new Packet29DestroyEntity(aint));
        }

        if (!this.f.isEmpty()) {
            ArrayList arraylist = new ArrayList();
            Iterator iterator1 = this.f.iterator();
            ArrayList arraylist1 = new ArrayList();

            while (iterator1.hasNext() && arraylist.size() < 5) {
                ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) iterator1.next();

                iterator1.remove();
                if (chunkcoordintpair != null && this.q.f(chunkcoordintpair.a << 4, 0, chunkcoordintpair.b << 4)) {
                    arraylist.add(this.q.e(chunkcoordintpair.a, chunkcoordintpair.b));
                    arraylist1.addAll(((WorldServer) this.q).c(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, chunkcoordintpair.a * 16 + 16, 256, chunkcoordintpair.b * 16 + 16));
                }
            }

            if (!arraylist.isEmpty()) {
                this.a.b(new Packet56MapChunks(arraylist));
                Iterator iterator2 = arraylist1.iterator();

                while (iterator2.hasNext()) {
                    TileEntity tileentity = (TileEntity) iterator2.next();

                    this.b(tileentity);
                }

                iterator2 = arraylist.iterator();

                while (iterator2.hasNext()) {
                    Chunk chunk = (Chunk) iterator2.next();

                    this.o().p().a(this, chunk);
                }
            }
        }
    }

    @Override
    public void b(int i0) {
        super.b(i0);
        Collection collection = this.cp().a(ScoreObjectiveCriteria.f);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreobjective = (ScoreObjective) iterator.next();

            this.cp().a(this.am(), scoreobjective).a(Arrays.asList(new EntityPlayer[] { this}));
        }
    }

    public void g() {
        try {
            super.l_();

            for (int i0 = 0; i0 < this.bK.j_(); ++i0) {
                ItemStack itemstack = this.bK.a(i0);

                if (itemstack != null && Item.f[itemstack.c].f() && this.a.e() <= 5) {
                    Packet packet = ((ItemMapBase) Item.f[itemstack.c]).c(itemstack, this.q, this);

                    if (packet != null) {
                        this.a.b(packet);
                    }
                }
            }

            if (this.aX() != this.cm || this.cn != this.bN.a() || this.bN.e() == 0.0F != this.co) {
                this.a.b(new Packet8UpdateHealth(this.aX(), this.bN.a(), this.bN.e()));
                this.cm = this.aX();
                this.cn = this.bN.a();
                this.co = this.bN.e() == 0.0F;
            }

            if (this.cg != this.cp) {
                this.cp = this.cg;
                this.a.b(new Packet43Experience(this.ch, this.cg, this.cf));
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.a(throwable, "Ticking player");
            CrashReportCategory crashreportcategory = crashreport.a("Player being ticked");

            this.a(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public void a(DamageSource damagesource) {
        this.b.ad().k(this.bt.b());
        if (!this.q.M().b("keepInventory")) {
            this.bK.m();
        }

        Collection collection = this.q.V().a(ScoreObjectiveCriteria.c);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreObjective scoreobjective = (ScoreObjective) iterator.next();
            Score score = this.cp().a(this.am(), scoreobjective);

            score.a();
        }

        EntityLiving entityliving = this.bN();

        if (entityliving != null) {
            entityliving.c(this, this.aM);
        }
    }

    @Override
    public boolean a(DamageSource damagesource, int i0) {
        if (this.aq()) {
            return false;
        } else {
            boolean flag0 = this.b.T() && this.b.X() && "fall".equals(damagesource.o);

            if (!flag0 && this.cq > 0 && damagesource != DamageSource.i) {
                return false;
            } else {
                if (damagesource instanceof EntityDamageSource) {
                    Entity entity = damagesource.i();

                    if (entity instanceof EntityPlayer && !this.a((EntityPlayer) entity)) {
                        return false;
                    }

                    if (entity instanceof EntityArrow) {
                        EntityArrow entityarrow = (EntityArrow) entity;

                        if (entityarrow.c instanceof EntityPlayer && !this.a((EntityPlayer) entityarrow.c)) {
                            return false;
                        }
                    }
                }

                return super.a(damagesource, i0);
            }
        }
    }

    @Override
    public boolean a(EntityPlayer entityplayer) {
        return !this.b.X() ? false : super.a(entityplayer);
    }

    @Override
    public void c(int i0) {
        if (this.ar == 1 && i0 == 1) {
            this.a((StatBase) AchievementList.C);
            this.q.e((Entity) this);
            this.j = true;
            this.a.b(new Packet70GameEvent(4, 0));
        } else {
            if (this.ar == 1 && i0 == 0) {
                this.a((StatBase) AchievementList.B);
                ChunkCoordinates chunkcoordinates = this.b.a(i0).l();

                if (chunkcoordinates != null) {
                    this.a.a((double) chunkcoordinates.a, (double) chunkcoordinates.b, (double) chunkcoordinates.c, 0.0F, 0.0F, getCanaryWorld().getType().getId(), getCanaryWorld().getName());
                }

                i0 = 1;
            } else {
                this.a((StatBase) AchievementList.x);
            }

            this.b.ad().a(this, i0);
            this.cp = -1;
            this.cm = -1;
            this.cn = -1;
        }
    }

    private void b(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.m();

            if (packet != null) {
                this.a.b(packet);
            }
        }
    }

    @Override
    public void a(Entity entity, int i0) {
        super.a(entity, i0);
        this.bM.b();
    }

    @Override
    public EnumStatus a(int i0, int i1, int i2) {
        EnumStatus enumstatus = super.a(i0, i1, i2);

        if (enumstatus == EnumStatus.a) {
            Packet17Sleep packet17sleep = new Packet17Sleep(this, 0, i0, i1, i2);

            this.o().p().a((Entity) this, (Packet) packet17sleep);
            this.a.a(this.u, this.v, this.w, this.A, this.B, getCanaryWorld().getType().getId(), getCanaryWorld().getName());
            this.a.b(packet17sleep);
        }

        return enumstatus;
    }

    @Override
    public void a(boolean flag0, boolean flag1, boolean flag2) {
        if (this.bz()) {
            this.o().p().b(this, new Packet18Animation(this, 3));
        }

        super.a(flag0, flag1, flag2);
        if (this.a != null) {
            this.a.a(this.u, this.v, this.w, this.A, this.B, getCanaryWorld().getType().getId(), getCanaryWorld().getName());
        }
    }

    @Override
    public void a(Entity entity) {
        super.a(entity);
        this.a.b(new Packet39AttachEntity(this, this.o));
        this.a.a(this.u, this.v, this.w, this.A, this.B, getCanaryWorld().getType().getId(), getCanaryWorld().getName());
    }

    @Override
    protected void a(double d0, boolean flag0) {}

    public void b(double d0, boolean flag0) {
        super.a(d0, flag0);
    }

    private void cr() {
        this.cu = this.cu % 100 + 1;
    }

    @Override
    public void b(int i0, int i1, int i2) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 1, "Crafting", 9, true));
        this.bM = new ContainerWorkbench(this.bK, this.q, i0, i1, i2);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(int i0, int i1, int i2, String s0) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 4, s0 == null ? "" : s0, 9, s0 != null));
        this.bM = new ContainerEnchantment(this.bK, this.q, i0, i1, i2);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void c(int i0, int i1, int i2) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 8, "Repairing", 9, true));
        this.bM = new ContainerRepair(this.bK, this.q, i0, i1, i2, this);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(IInventory iinventory) {
        if (this.bM != this.bL) {
            this.h();
        }

        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 0, iinventory.b(), iinventory.j_(), iinventory.c()));
        this.bM = new ContainerChest(this.bK, iinventory);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(TileEntityHopper tileentityhopper) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 9, tileentityhopper.b(), tileentityhopper.j_(), tileentityhopper.c()));
        this.bM = new ContainerHopper(this.bK, tileentityhopper);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(EntityMinecartHopper entityminecarthopper) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 9, entityminecarthopper.b(), entityminecarthopper.j_(), entityminecarthopper.c()));
        this.bM = new ContainerHopper(this.bK, entityminecarthopper);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(TileEntityFurnace tileentityfurnace) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 2, tileentityfurnace.b(), tileentityfurnace.j_(), tileentityfurnace.c()));
        this.bM = new ContainerFurnace(this.bK, tileentityfurnace);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(TileEntityDispenser tileentitydispenser) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, tileentitydispenser instanceof TileEntityDropper ? 10 : 3, tileentitydispenser.b(), tileentitydispenser.j_(), tileentitydispenser.c()));
        this.bM = new ContainerDispenser(this.bK, tileentitydispenser);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(TileEntityBrewingStand tileentitybrewingstand) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 5, tileentitybrewingstand.b(), tileentitybrewingstand.j_(), tileentitybrewingstand.c()));
        this.bM = new ContainerBrewingStand(this.bK, tileentitybrewingstand);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(TileEntityBeacon tileentitybeacon) {
        this.cr();
        this.a.b(new Packet100OpenWindow(this.cu, 7, tileentitybeacon.b(), tileentitybeacon.j_(), tileentitybeacon.c()));
        this.bM = new ContainerBeacon(this.bK, tileentitybeacon);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
    }

    @Override
    public void a(IMerchant imerchant, String s0) {
        this.cr();
        this.bM = new ContainerMerchant(this.bK, imerchant, this.q);
        this.bM.d = this.cu;
        this.bM.a((ICrafting) this);
        InventoryMerchant inventorymerchant = ((ContainerMerchant) this.bM).e();

        this.a.b(new Packet100OpenWindow(this.cu, 6, s0 == null ? "" : s0, inventorymerchant.j_(), s0 != null));
        MerchantRecipeList merchantrecipelist = imerchant.b(this);

        if (merchantrecipelist != null) {
            try {
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);

                dataoutputstream.writeInt(this.cu);
                merchantrecipelist.a(dataoutputstream);
                this.a.b(new Packet250CustomPayload("MC|TrList", bytearrayoutputstream.toByteArray()));
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
    }

    @Override
    public void a(Container container, int i0, ItemStack itemstack) {
        if (!(container.a(i0) instanceof SlotCrafting)) {
            if (!this.h) {
                this.a.b(new Packet103SetSlot(container.d, i0, itemstack));
            }
        }
    }

    public void a(Container container) {
        this.a(container, container.a());
    }

    @Override
    public void a(Container container, List list) {
        this.a.b(new Packet104WindowItems(container.d, list));
        this.a.b(new Packet103SetSlot(-1, -1, this.bK.o()));
    }

    @Override
    public void a(Container container, int i0, int i1) {
        this.a.b(new Packet105UpdateProgressbar(container.d, i0, i1));
    }

    @Override
    public void h() {
        this.a.b(new Packet101CloseWindow(this.bM.d));
        this.j();
    }

    public void i() {
        if (!this.h) {
            this.a.b(new Packet103SetSlot(-1, -1, this.bK.o()));
        }
    }

    public void j() {
        this.bM.b((EntityPlayer) this);
        this.bM = this.bL;
    }

    @Override
    public void a(StatBase statbase, int i0) {
        if (statbase != null) {
            if (!statbase.f) {
                while (i0 > 100) {
                    this.a.b(new Packet200Statistic(statbase.e, 100));
                    i0 -= 100;
                }

                this.a.b(new Packet200Statistic(statbase.e, i0));
            }
        }
    }

    public void k() {
        if (this.n != null) {
            this.n.a((Entity) this);
        }

        if (this.ca) {
            this.a(true, false, false);
        }
    }

    public void l() {
        this.cm = -99999999;
    }

    @Override
    public void b(String s0) {
        StringTranslate stringtranslate = StringTranslate.a();
        String s1 = stringtranslate.a(s0);

        this.a.b(new Packet3Chat(s1));
    }

    @Override
    protected void m() {
        this.a.b(new Packet38EntityStatus(this.k, (byte) 9));
        super.m();
    }

    @Override
    public void a(ItemStack itemstack, int i0) {
        super.a(itemstack, i0);
        if (itemstack != null && itemstack.b() != null && itemstack.b().b_(itemstack) == EnumAction.b) {
            this.o().p().b(this, new Packet18Animation(this, 5));
        }
    }

    @Override
    public void a(EntityPlayer entityplayer, boolean flag0) {
        super.a(entityplayer, flag0);
        this.cp = -1;
        this.cm = -1;
        this.cn = -1;
        this.g.addAll(((EntityPlayerMP) entityplayer).g);
    }

    @Override
    protected void a(PotionEffect potioneffect) {
        super.a(potioneffect);
        this.a.b(new Packet41EntityEffect(this.k, potioneffect));
    }

    @Override
    protected void b(PotionEffect potioneffect) {
        super.b(potioneffect);
        this.a.b(new Packet41EntityEffect(this.k, potioneffect));
    }

    @Override
    protected void c(PotionEffect potioneffect) {
        super.c(potioneffect);
        this.a.b(new Packet42RemoveEntityEffect(this.k, potioneffect));
    }

    @Override
    public void a(double d0, double d1, double d2) {
        this.a.a(d0, d1, d2, this.A, this.B, getCanaryWorld().getType().getId(), getCanaryWorld().getName());
    }

    @Override
    public void b(Entity entity) {
        this.o().p().b(this, new Packet18Animation(entity, 6));
    }

    @Override
    public void c(Entity entity) {
        this.o().p().b(this, new Packet18Animation(entity, 7));
    }

    @Override
    public void n() {
        if (this.a != null) {
            this.a.b(new Packet202PlayerAbilities(this.ce));
        }
    }

    public WorldServer o() {
        return (WorldServer) this.q;
    }

    @Override
    public void a(EnumGameType enumgametype) {
        this.c.a(enumgametype);
        this.a.b(new Packet70GameEvent(3, enumgametype.a()));
    }

    @Override
    public void a(String s0) {
        this.a.b(new Packet3Chat(s0));
    }

    @Override
    public boolean a(int i0, String s0) {
        return "seed".equals(s0) && !this.b.T() ? true : (!"tell".equals(s0) && !"help".equals(s0) && !"me".equals(s0) ? this.b.ad().e(this.bS) : true);
    }

    public String p() {
        String s0 = this.a.a.c().toString();

        s0 = s0.substring(s0.indexOf("/") + 1);
        s0 = s0.substring(0, s0.indexOf(":"));
        return s0;
    }

    public void a(Packet204ClientInfo packet204clientinfo) {
        if (this.cl.b().containsKey(packet204clientinfo.d())) {
            this.cl.a(packet204clientinfo.d(), false);
        }

        int i0 = 256 >> packet204clientinfo.f();

                if (i0 > 3 && i0 < 15) {
                    this.cr = i0;
                }

                this.cs = packet204clientinfo.g();
                this.ct = packet204clientinfo.h();
                if (this.b.I() && this.b.H().equals(this.bS)) {
                    this.b.c(packet204clientinfo.i());
                }

                this.b(1, !packet204clientinfo.j());
    }

    @Override
    public StringTranslate r() {
        return this.cl;
    }

    public int t() {
        return this.cs;
    }

    public void a(String s0, int i0) {
        String s1 = s0 + "\u0000" + i0;

        this.a.b(new Packet250CustomPayload("MC|TPack", s1.getBytes()));
    }

    @Override
    public ChunkCoordinates b() {
        return new ChunkCoordinates(MathHelper.c(this.u), MathHelper.c(this.v + 0.5D), MathHelper.c(this.w));
    }

    /**
     * Get the CanaryEntity as CanaryPlayer
     * @return
     */
    public CanaryPlayer getPlayer() {
        return this.playerEntity;
    }

    public CanaryNetServerHandler getServerHandler() {
        return a.getCanaryServerHandler();
    }
}