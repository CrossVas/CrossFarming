package dev.crossvas.farming.blockentities.base;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public abstract class BaseBlockEntity extends BlockEntity implements MenuProvider {

    public int farmRange = 7;
    public int farmArea = 4;

    protected int x;
    protected int y;
    protected int z;

    protected int tick;
    protected int seconds;
    protected int seedCounter;
    protected int soilCounter;
    protected int waterCounter;

    protected boolean initialized = false;
    protected boolean platformBuilt;
    protected boolean platformCleared;

    protected ContainerData data;

    protected BaseBlockEntity mainFarm = null;

    protected final LazyOptional<IEnergyStorage> LAZY_ENERGY_HANDLER = LazyOptional.of(() -> this.ENERGY_STORAGE);

    protected final CustomEnergyStorage ENERGY_STORAGE = new CustomEnergyStorage() {
        @Override
        public void onEnergyChanged() {
            setChanged();
        }
    };

    protected int ENERGY_USAGE;
    protected int ENERGY_STORED;

    public final ItemStackHandler ITEM_HANDLER = getItemHandler();
    protected final LazyOptional<IItemHandler> LAZY_ITEM_HANDLER = LazyOptional.of(() -> ITEM_HANDLER);

    protected Map<Direction, LazyOptional<ItemStackWrappedHandler>> sidedCaps;

    protected BlockPos[] aroundPoses = {getBlockPos().north(), getBlockPos().south(), getBlockPos().east(), getBlockPos().west()};

    public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return index == 0 ? BaseBlockEntity.this.getEnergyStorage().getEnergyStored() : 0;
            }

            @Override
            public void set(int index, int value) {
                BaseBlockEntity.this.ENERGY_STORED = value;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
    }

    public void init() {
        this.ENERGY_STORED = this.getEnergyStorage().getEnergyStored();
        initialized = true;
        this.x = getBlockPos().getX();
        this.y = getBlockPos().getY();
        this.z = getBlockPos().getZ();
        tick = 0;
        seconds = 0;
        soilCounter = 0;
        platformBuilt = false;
        platformCleared = false;
    }
    protected abstract boolean hasSidedCaps();

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY) {
            return LAZY_ENERGY_HANDLER.cast();
        } else if (hasSidedCaps()) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                if (side == null) {
                    return LAZY_ITEM_HANDLER.cast();
                }
                if(sidedCaps.containsKey(side)) {
                    return sidedCaps.get(side).cast();
                }
            }
        }
        return super.getCapability(cap, side);
    }

    public void onTick() {
        if (!initialized) {
            init();
        }
    }

    @Override
    public void setRemoved() {
        if (hasSidedCaps()) {
            for (Direction side : Direction.values()) {
                if (!sidedCaps.isEmpty()) {
                    if (sidedCaps.get(side) != null) {
                        sidedCaps.get(side).invalidate();
                    }
                }
            }
        }
        this.LAZY_ENERGY_HANDLER.invalidate();
        super.setRemoved();
    }

    @Override
    public void load(CompoundTag pTag) {
        if (pTag.contains("InventorySlot")) {
            this.ITEM_HANDLER.deserializeNBT(pTag.getCompound("InventorySlot"));
        }
        if (pTag.contains("Energy")) {
            this.ENERGY_STORAGE.deserializeNBT(pTag.get("Energy"));
        }
        if (pTag.contains("platformCleared")) {
            platformCleared = pTag.getBoolean("platformCleared");
        }
        if (pTag.contains("platformBuilt")) {
            platformBuilt = pTag.getBoolean("platformBuilt");
        }
        super.load(pTag);
    }

    public void saveAdditional(CompoundTag tag) {
        if (this.hasSidedCaps()) { // sided caps are used ONLY for ITEM_HANDLER
            tag.put("InventorySlot", this.ITEM_HANDLER.serializeNBT());
        }
        tag.putInt("Energy", this.ENERGY_STORAGE.getEnergyStored());

        tag.putBoolean("platformCleared", platformCleared);
        tag.putBoolean("platformBuilt", platformBuilt);

        CompoundTag infoTag = new CompoundTag();
        tag.put("Info", infoTag);
        super.saveAdditional(tag);
    }

    public abstract ItemStackHandler getItemHandler();

    public void drops() {
        if (hasSidedCaps()) {
            SimpleContainer inventory = new SimpleContainer(ITEM_HANDLER.getSlots());
            for (int i = 0; i < ITEM_HANDLER.getSlots(); i++) {
                inventory.setItem(i, ITEM_HANDLER.getStackInSlot(i));
            }
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }
    }

    // Utils

    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    public void extractEnergy() {
        this.ENERGY_STORAGE.consumeEnergy(this.ENERGY_USAGE);
    }

    public void extractEnergy(int amount) {
        this.ENERGY_STORAGE.consumeEnergy(amount);
    }

    public boolean hasEnergyToWork() {
        return this.ENERGY_STORAGE.getEnergyStored() >= this.ENERGY_USAGE;
    }

    public List<BlockPos> getWorkingSpace(BlockPos mainPos, int range) {
        List<BlockPos> returnList = new ArrayList<>();
        for (BlockPos pos : BlockPos.betweenClosed(mainPos.getX() - range, mainPos.getY(), mainPos.getZ() - range, mainPos.getX() + range, mainPos.getY(), mainPos.getZ() + range)) {
            if (hasLevel()) {
                if (!(Objects.requireNonNull(getLevel()).getBlockEntity(pos) instanceof BaseBlockEntity)) {
                    returnList.add(pos.immutable());
                }
            }
        }
        return returnList;
    }

    public List<BlockPos> getWaterBlockPos(BlockPos mainPos, int range) {
        List<BlockPos> waterPoses = new ArrayList<>();
        int pX = mainPos.getX();
        int pY = mainPos.getY();
        int pZ = mainPos.getZ();
        BlockPos topMostLeftPos = new BlockPos(pX - range + 1, pY, pZ - range + 1).immutable();
        BlockPos topMostRightPos = topMostLeftPos.east(12).immutable();
        BlockPos bottomMostRightPos = topMostRightPos.south(12).immutable();
        BlockPos bottomMostLeftPos = bottomMostRightPos.west(12).immutable();
        waterPoses.add(topMostLeftPos);
        waterPoses.add(topMostLeftPos.east(6));
        waterPoses.add(topMostRightPos);
        waterPoses.add(topMostRightPos.south(6));
        waterPoses.add(bottomMostRightPos);
        waterPoses.add(bottomMostRightPos.west(6));
        waterPoses.add(bottomMostLeftPos);
        waterPoses.add(bottomMostLeftPos.north(6));
        return waterPoses;
    }

    public static boolean spawnItemStack(ItemStack stack, Level world, BlockPos pos) {
        ItemEntity item = new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.2, (double)pos.getZ() + 0.5, stack);
        item.lerpMotion(0.0, -1.0, 0.0);
        item.setPickUpDelay(40);
        item.setItem(stack);
        return world.addFreshEntity(item);
    }

    public void initFarmSidedCaps() {
        this.sidedCaps = Map.of(
                Direction.DOWN, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, true, false)),
                Direction.UP, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, false, false)),
                Direction.NORTH, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, false, true)),
                Direction.SOUTH, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, false, true)),
                Direction.EAST, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, false, true)),
                Direction.WEST, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, false, true)));
    }

    public void initCombineSidedCaps() {
        this.sidedCaps = new HashMap<>();
        for (Direction side : Direction.values()) {
            sidedCaps.put(side, LazyOptional.of(() -> new ItemStackWrappedHandler(ITEM_HANDLER, true, false)));
        }
    }

    public boolean hasWaterSides(BlockPos pos, Block block) {
        return level.getBlockState(pos.east()).is(block) && level.getBlockState(pos.west()).is(block) &&
                level.getBlockState(pos.north()).is(block) && level.getBlockState(pos.south()).is(block);
    }

    public List<BlockPos> findPositions(BlockState state, BlockPos location, Level world) {
        List<BlockPos> found = new ArrayList<>();
        Set<BlockPos> checked = new ObjectOpenHashSet<>();
        found.add(location.immutable());
        Block startBlock = state.getBlock();
        for (int i = 0; i < found.size(); i++) {
            BlockPos blockPos = found.get(i);
            checked.add(blockPos);
            for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-2, -2, -2), blockPos.offset(2, 2, 2))) {
                // We can check contains as mutable
                if (!checked.contains(pos)) {
                    if (!world.getBlockState(pos).isAir()) {
                        if (startBlock == world.getBlockState(pos).getBlock()) {
                            // Make sure to add it as immutable
                            found.add(pos.immutable());
                        }
                    }
                }
            }
        }
        return found;
    }
}
