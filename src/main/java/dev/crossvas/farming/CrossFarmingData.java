package dev.crossvas.farming;

import dev.crossvas.farming.blockentities.*;
import dev.crossvas.farming.blocks.BaseCombineBlock;
import dev.crossvas.farming.blocks.BaseFarmBlock;
import dev.crossvas.farming.blocks.BlockPeatBog;
import dev.crossvas.farming.gui.menus.*;
import dev.crossvas.farming.gui.screens.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CrossFarmingData {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CrossFarming.ID);

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrossFarming.ID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CrossFarming.ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrossFarming.ID);

    // Items

    public static final RegistryObject<Item> PEAT_ITEM = registerItem("peat", () -> new Item(new Item.Properties().tab(CrossFarming.TAB)));

    // Blocks

    public static final RegistryObject<Block> PEAT_BOG_BLOCK = registerBlock("peat_bog_block", BlockPeatBog::new);
    public static final RegistryObject<Block> PEAT_BLOCK = registerBlock("peat_block", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<Block> CROP_FARM_BLOCK = registerBlock("crop_farm", () -> new BaseFarmBlock("crop_farm", true));
    public static final RegistryObject<Block> CROP_COMBINE_BLOCK = registerBlock("crop_combine", () -> new BaseCombineBlock("crop_combine", true));
    public static RegistryObject<Block> RESIN_FARM_BLOCK;
    public static RegistryObject<Block> RESIN_COMBINE_BLOCK;
    public static final RegistryObject<Block> TREE_FARM_BLOCK = registerBlock("tree_farm", () -> new BaseFarmBlock("tree_farm", true));
    public static final RegistryObject<Block> TREE_COMBINE_BLOCK = registerBlock("tree_combine", () -> new BaseCombineBlock("tree_combine", true));
    public static final RegistryObject<Block> GOURD_COMBINE_BLOCK = registerBlock("gourd_combine", () -> new BaseCombineBlock("gourd_combine", true));
    public static final RegistryObject<Block> SUCCULENT_COMBINE_BLOCK = registerBlock("succulent_combine", () -> new BaseCombineBlock("succulent_combine", true));
    public static final RegistryObject<Block> PEAT_BOG_FARM_BLOCK = registerBlock("peat_bog_farm", () -> new BaseFarmBlock("peat_bog_farm", true));
    public static final RegistryObject<Block> PEAT_BOG_COMBINE_BLOCK = registerBlock("peat_bog_combine", () -> new BaseCombineBlock("peat_bog_combine", true));
    public static final RegistryObject<Block> COCOA_COMBINE_BLOCK = registerBlock("cocoa_combine", () -> new BaseCombineBlock("cocoa_combine", true));
    public static final RegistryObject<Block> INFERNAL_FARM_BLOCK = registerBlock("infernal_farm", () -> new BaseFarmBlock("infernal_farm", true));
    public static final RegistryObject<Block> INFERNAL_COMBINE_BLOCK = registerBlock("infernal_combine", () -> new BaseCombineBlock("infernal_combine", true));

    // Block Entities

    public static final RegistryObject<BlockEntityType<CropFarmBlockEntity>> CROP_FARM_TILE = BLOCK_ENTITIES.register("crop_farm",
            () -> BlockEntityType.Builder.of(CropFarmBlockEntity::new, CROP_FARM_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<CropCombineBlockEntity>> CROP_COMBINE_TILE = BLOCK_ENTITIES.register("crop_combine",
            () -> BlockEntityType.Builder.of(CropCombineBlockEntity::new, CROP_COMBINE_BLOCK.get()).build(null));
    public static RegistryObject<BlockEntityType<ResinFarmBlockEntity>> RESIN_FARM_TILE;
    public static RegistryObject<BlockEntityType<ResinCombineBlockEntity>> RESIN_COMBINE_TILE;
    public static final RegistryObject<BlockEntityType<TreeFarmBlockEntity>> TREE_FARM_TILE = BLOCK_ENTITIES.register("tree_farm",
            () -> BlockEntityType.Builder.of(TreeFarmBlockEntity::new, TREE_FARM_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<TreeCombineBlockEntity>> TREE_COMBINE_TILE = BLOCK_ENTITIES.register("tree_combine",
            () -> BlockEntityType.Builder.of(TreeCombineBlockEntity::new, TREE_COMBINE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<GourdCombineBlockEntity>> GOURD_COMBINE_TILE = BLOCK_ENTITIES.register("gourd_combine",
            () -> BlockEntityType.Builder.of(GourdCombineBlockEntity::new, GOURD_COMBINE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<SucculentCombineBlockEntity>> SUCCULENT_COMBINE_TILE = BLOCK_ENTITIES.register("succulent_combine",
            () -> BlockEntityType.Builder.of(SucculentCombineBlockEntity::new, SUCCULENT_COMBINE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<PeatBogFarmBlockEntity>> PEAT_BOG_FARM_TILE = BLOCK_ENTITIES.register("peat_bog_farm",
            () -> BlockEntityType.Builder.of(PeatBogFarmBlockEntity::new, PEAT_BOG_FARM_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<PeatBogCombineBlockEntity>> PEAT_BOG_COMBINE_TILE = BLOCK_ENTITIES.register("peat_bog_combine",
            () -> BlockEntityType.Builder.of(PeatBogCombineBlockEntity::new, PEAT_BOG_COMBINE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<CocoaCombineBlockEntity>> COCOA_COMBINE_TILE = BLOCK_ENTITIES.register("cocoa_combine",
            () -> BlockEntityType.Builder.of(CocoaCombineBlockEntity::new, COCOA_COMBINE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfernalFarmBlockEntity>> INFERNAL_FARM_TILE = BLOCK_ENTITIES.register("infernal_farm",
            () -> BlockEntityType.Builder.of(InfernalFarmBlockEntity::new, INFERNAL_FARM_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<InfernalCombineBlockEntity>> INFERNAL_COMBINE_TILE = BLOCK_ENTITIES.register("infernal_combine",
            () -> BlockEntityType.Builder.of(InfernalCombineBlockEntity::new, INFERNAL_COMBINE_BLOCK.get()).build(null));

    // Menus

    public static final RegistryObject<MenuType<CropFarmMenu>> CROP_FARM_MENU = registerMenuType(CropFarmMenu::new, "crop_farm_menu");
    public static final RegistryObject<MenuType<CropCombineMenu>> CROP_COMBINE_MENU = registerMenuType(CropCombineMenu::new, "crop_combine_menu");
    public static final RegistryObject<MenuType<ResinCombineMenu>> RESIN_COMBINE_MENU = registerMenuType(ResinCombineMenu::new, "resin_combine_menu");
    public static final RegistryObject<MenuType<TreeFarmMenu>> TREE_FARM_MENU = registerMenuType(TreeFarmMenu::new, "tree_farm_menu");
    public static final RegistryObject<MenuType<TreeCombineMenu>> TREE_COMBINE_MENU = registerMenuType(TreeCombineMenu::new, "tree_combine_menu");
    public static final RegistryObject<MenuType<GourdCombineMenu>> GOURD_COMBINE_MENU = registerMenuType(GourdCombineMenu::new, "gourd_combine_menu");
    public static final RegistryObject<MenuType<SucculentCombineMenu>> SUCCULENT_COMBINE_MENU = registerMenuType(SucculentCombineMenu::new, "succulent_combine_menu");
    public static final RegistryObject<MenuType<PeatBogFarmMenu>> PEAT_BOG_FARM_MENU = registerMenuType(PeatBogFarmMenu::new, "peat_bog_farm_menu");
    public static final RegistryObject<MenuType<PeatBogCombineMenu>> PEAT_BOG_COMBINE_MENU = registerMenuType(PeatBogCombineMenu::new, "peat_bog_combine_menu");
    public static final RegistryObject<MenuType<CocoaCombineMenu>> COCOA_COMBINE_MENU = registerMenuType(CocoaCombineMenu::new, "cocoa_combine_menu");
    public static final RegistryObject<MenuType<InfernalFarmMenu>> INFERNAL_FARM_MENU = registerMenuType(InfernalFarmMenu::new, "infernal_farm_menu");
    public static final RegistryObject<MenuType<InfernalCombineMenu>> INFERNAL_COMBINE_MENU = registerMenuType(InfernalCombineMenu::new, "infernal_combine_menu");

    public static void initIC2Compat() {
        RESIN_FARM_BLOCK = registerBlock("resin_farm", () -> new BaseFarmBlock("resin_farm", false));
        RESIN_COMBINE_BLOCK = registerBlock("resin_combine", () -> new BaseCombineBlock("resin_combine", true));
        RESIN_FARM_TILE = BLOCK_ENTITIES.register("resin_farm",
                () -> BlockEntityType.Builder.of(ResinFarmBlockEntity::new, RESIN_FARM_BLOCK.get()).build(null));
        RESIN_COMBINE_TILE = BLOCK_ENTITIES.register("resin_combine",
                () -> BlockEntityType.Builder.of(ResinCombineBlockEntity::new, RESIN_COMBINE_BLOCK.get()).build(null));
    }

    public static void initData(IEventBus bus) {
        if (ModList.get().isLoaded("ic2")) {
            initIC2Compat();
        }
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);
        MENUS.register(bus);
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(CrossFarming.TAB)));
    }

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    @Mod.EventBusSubscriber(modid = CrossFarming.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientRegistries {

        @SubscribeEvent
        public static void onClientSide(FMLClientSetupEvent e) {
            MenuScreens.register(CrossFarmingData.CROP_FARM_MENU.get(), CropFarmScreen::new);
            MenuScreens.register(CrossFarmingData.CROP_COMBINE_MENU.get(), CropCombineScreen::new);
            MenuScreens.register(CrossFarmingData.RESIN_COMBINE_MENU.get(), ResinCombineScreen::new);
            MenuScreens.register(CrossFarmingData.TREE_FARM_MENU.get(), TreeFarmScreen::new);
            MenuScreens.register(CrossFarmingData.TREE_COMBINE_MENU.get(), TreeCombineScreen::new);
            MenuScreens.register(CrossFarmingData.GOURD_COMBINE_MENU.get(), GourdCombineScreen::new);
            MenuScreens.register(CrossFarmingData.SUCCULENT_COMBINE_MENU.get(), SucculentCombineScreen::new);
            MenuScreens.register(CrossFarmingData.PEAT_BOG_FARM_MENU.get(), PeatBogFarmScreen::new);
            MenuScreens.register(CrossFarmingData.PEAT_BOG_COMBINE_MENU.get(), PeatBogCombineScreen::new);
            MenuScreens.register(CrossFarmingData.COCOA_COMBINE_MENU.get(), CocoaCombineScreen::new);
            MenuScreens.register(CrossFarmingData.INFERNAL_FARM_MENU.get(), InfernalFarmScreen::new);
            MenuScreens.register(CrossFarmingData.INFERNAL_COMBINE_MENU.get(), InfernalCombineScreen::new);
        }
    }

    public static class CustomTags {

        public static final TagKey<Item> FARM_SEEDS = createItemTag("crops/farm/seeds");
        public static final TagKey<Item> FARM_CROPS = createItemTag("crops/farm/crops");
        public static final TagKey<Block> FARM_CROPS_BLOCK = createBlockTag("crops/farm/crops");
        public static final TagKey<Item> FARM_SOIL = createItemTag("crops/farm/soil");
        public static final TagKey<Block> FARM_SOIL_BLOCK = createBlockTag("crops/farm/soil");

        public static final TagKey<Item> RESIN_CROPS = createItemTag("resin/farm/crops");

        public static final TagKey<Item> TREE_PLANTABLE = createItemTag("tree/farm/plantable");

        public static final TagKey<Item> TREE_FARM_SOIL = createItemTag("tree/farm/soil");
        public static final TagKey<Block> TREE_FARM_SOIL_BLOCK = createBlockTag("tree/farm/soil");
        public static final TagKey<Item> TREE_FARM_CROPS = createItemTag("tree/combine/crops");

        public static final TagKey<Item> GOURD_COMBINE_CROPS = createItemTag("gourd/combine/crops");
        public static final TagKey<Block> GOURD_COMBINE_CROPS_BLOCK = createBlockTag("gourd/combine/crops");
        public static final TagKey<Item> SUCCULENT_COMBINE_CROPS = createItemTag("succulent/combine/crops");
        public static final TagKey<Block> SUCCULENT_COMBINE_CROPS_BLOCK = createBlockTag("succulent/combine/crops");

        public static final TagKey<Item> NETHER_FARM_CROPS = createItemTag("nether/combine/crops");
        public static final TagKey<Block> NETHER_FARM_CROPS_BLOCK = createBlockTag("nether/combine/crops");
        public static final TagKey<Block> NETHER_FARM_SOIL_BLOCK = createBlockTag("nether/combine/soil");

        public static final TagKey<Item> PEAT_FARM_SOIL = createItemTag("peat/farm/soil");
        public static final TagKey<Item> PEAT_FARM_WASTE = createItemTag("peat/farm/waste");
        public static final TagKey<Block> PEAT_FARM_SOIL_BLOCK = createBlockTag("peat/farm/soil");

        public static final TagKey<Item> PEAT_COMBINE_CROPS = createItemTag("peat/combine/crops");
        public static final TagKey<Block> PEAT_COMBINE_HARVESTABLE = createBlockTag("peat/combine/harvestable");
        public static final TagKey<Item> COCOA_COMBINE_HARVESTABLE = createItemTag("cocoa/combine/harvestable");
        public static final TagKey<Block> INFERNAL_COMBINE_HARVESTABLE = createBlockTag("infernal/combine/harvestable");

        public static final TagKey<Item> INFERNAL_FARM_SOIL = createItemTag("infernal/farm/soil");
        public static final TagKey<Block> INFERNAL_FARM_SOIL_BLOCK = createBlockTag("infernal/farm/soil");
        public static final TagKey<Item> INFERNAL_FARM_SEEDS = createItemTag("infernal/farm/seeds");

        public static TagKey<Item> createItemTag(String path) {
            return ItemTags.create(new ResourceLocation(CrossFarming.ID, path));
        }

        public static TagKey<Block> createBlockTag(String path) {
            return BlockTags.create(new ResourceLocation(CrossFarming.ID, path));
        }
    }
}
