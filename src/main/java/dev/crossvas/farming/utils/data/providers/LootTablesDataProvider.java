package dev.crossvas.farming.utils.data.providers;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import dev.crossvas.farming.CrossFarmingData;
import dev.crossvas.farming.blocks.BaseBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTablesDataProvider extends LootTableProvider {

    private static final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> TABLES =
            ImmutableList.of(Pair.of(CrossFarmingBlockLoot::new, LootContextParamSets.BLOCK));

    public LootTablesDataProvider(DataGenerator pGenerator) {
        super(pGenerator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return TABLES;
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        map.forEach((location, loot) -> LootTables.validate(validationContext, location, loot));
    }

    public static class CrossFarmingBlockLoot extends BlockLoot {

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return CrossFarmingData.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
        }

        @Override
        protected void addTables() {
            CrossFarmingData.BLOCKS.getEntries().stream()
                    .filter(regObject -> regObject.get() instanceof BaseBlock)
                    .map(RegistryObject::get).toList().forEach(this::dropSelf);
            LootPool.Builder peatLootPool = LootPool.lootPool().add(LootItem.lootTableItem(CrossFarmingData.PEAT_ITEM.get()));
            LootPool.Builder dirtLootPool = LootPool.lootPool().add(LootItem.lootTableItem(Items.DIRT).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))));
            LootTable.Builder lootTable = LootTable.lootTable().withPool(peatLootPool).withPool(dirtLootPool);
            this.add(CrossFarmingData.PEAT_BLOCK.get(), lootTable);
            this.dropSelf(CrossFarmingData.PEAT_BOG_BLOCK.get());
        }
    }
}
