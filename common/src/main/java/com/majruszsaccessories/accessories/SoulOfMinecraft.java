package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.*;
import com.majruszsaccessories.common.AccessoryHandler;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.mlib.annotation.AutoInstance;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.data.Serializable;
import com.mlib.level.BlockHelper;
import com.mlib.math.Range;
import net.minecraft.world.level.material.Fluids;

@AutoInstance
public class SoulOfMinecraft extends AccessoryHandler {
	public SoulOfMinecraft() {
		super( MajruszsAccessories.SOUL_OF_MINECRAFT );

		this.add( MoreChestLoot.create( 1.8f ) )
			.add( BrushingExtraItem.create( 0.24f ) )
			.add( SwimmingSpeedBonus.create( 0.3f ) )
			.add( FishingLuckBonus.create( 4 ) )
			.add( FishingLureBonus.create( 0.3f ) )
			.add( FishingExtraItems.create( 0.3f, 4 ) )
			.add( TradingDiscount.create( 0.18f ) )
			.add( SleepingBonuses.create( 2, 420 ) )
			.add( StrongerPotions.create( 0.4f, 1 ) )
			.add( MiningExtraItem.create( 0.05f ) )
			.add( MiningSpeedBonus.create( 0.15f ) )
			.add( MiningDurabilityBonus.create( 0.15f ) )
			.add( TamingStrongerAnimals.create( 0.3f ) )
			.add( BreedingTwins.create( 0.36f ) )
			.add( HarvestingDoubleCrops.create( 0.36f ) );
	}
}