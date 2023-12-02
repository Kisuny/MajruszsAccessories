package com.majruszsaccessories.accessories.components;

import com.majruszlibrary.emitter.ParticleEmitter;
import com.majruszlibrary.events.OnFishingExtraItemsGet;
import com.majruszlibrary.item.LootHelper;
import com.majruszlibrary.level.LevelHelper;
import com.majruszlibrary.math.AnyPos;
import com.majruszlibrary.math.Range;
import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.config.RangedInteger;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class FishingExtraItems extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );
	RangedInteger count = new RangedInteger().id( "count" ).maxRange( Range.of( 2, 10 ) );

	public static ISupplier< AccessoryItem > create( float chance, int count ) {
		return handler->new FishingExtraItems( handler, chance, count );
	}

	protected FishingExtraItems( BonusHandler< AccessoryItem > handler, float chance, int count ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );
		this.count.set( count, Range.of( 2, 100 ) );

		OnFishingExtraItemsGet.listen( this::addExtraFishes )
			.addCondition( CustomConditions.chance( this::getItem, data->data.player, holder->holder.apply( this.chance ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.extra_fishing_items", TooltipHelper.asPercent( this.chance ), TooltipHelper.asValue( this.count ) );

		handler.getConfig()
			.define( "extra_fishing_item", subconfig->{
				this.chance.define( subconfig );
				this.count.define( subconfig );
			} );
	}

	private void addExtraFishes( OnFishingExtraItemsGet data ) {
		int count = CustomConditions.getLastHolder().apply( this.count ) - 1;
		for( int idx = 0; idx < count; ++idx ) {
			data.extraItems.addAll( LootHelper.getLootTable( BuiltInLootTables.FISHING_FISH ).getRandomItems( LootHelper.toGiftParams( data.player ) ) );
		}
		this.spawnEffects( data );
	}

	private void spawnEffects( OnFishingExtraItemsGet data ) {
		BlockPos position = LevelHelper.getPositionOverFluid( data.getLevel(), data.hook.blockPosition() );

		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 4 )
			.offset( ParticleEmitter.offset( 0.125f ) )
			.position( AnyPos.from( data.hook.getX(), position.getY() + 0.25, data.hook.getZ() ).vec3() )
			.emit( data.getServerLevel() );
	}
}
