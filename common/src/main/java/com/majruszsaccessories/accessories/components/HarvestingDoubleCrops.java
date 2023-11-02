package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnLootGenerated;
import com.mlib.contexts.base.Condition;
import com.mlib.contexts.base.Context;
import com.mlib.data.Serializable;
import com.mlib.level.BlockHelper;
import com.mlib.math.Range;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.function.Consumer;

public class HarvestingDoubleCrops extends BonusComponent< AccessoryItem > {
	RangedFloat chance = new RangedFloat().id( "chance" ).maxRange( Range.CHANCE );

	public static ISupplier< AccessoryItem > create( float chance ) {
		return handler->new HarvestingDoubleCrops( handler, chance );
	}

	protected HarvestingDoubleCrops( BonusHandler< AccessoryItem > handler, float chance ) {
		super( handler );

		this.chance.set( chance, Range.CHANCE );

		OnCropHarvested.listen( this::doubleLoot )
			.addCondition( CustomConditions.chance( this::getItem, data->( LivingEntity )data.entity, holder->holder.apply( this.chance ) ) );

		this.addTooltip( "majruszsaccessories.bonuses.double_crops", TooltipHelper.asPercent( this.chance ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "double_crops", this.chance::define );
	}

	private void doubleLoot( OnLootGenerated data ) {
		data.generatedLoot.addAll( new ArrayList<>( data.generatedLoot ) );
		this.spawnEffects( data );
	}

	private void spawnEffects( OnLootGenerated data ) {
		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 5 )
			.position( data.origin )
			.emit( data.getServerLevel() );
	}

	public static class OnCropHarvested {
		public static Context< OnLootGenerated > listen( Consumer< OnLootGenerated > consumer ) {
			return OnLootGenerated.listen( consumer )
				.addCondition( Condition.isLogicalServer() )
				.addCondition( data->data.origin != null )
				.addCondition( data->data.blockState != null && BlockHelper.isCropAtMaxAge( data.blockState ) )
				.addCondition( data->data.entity instanceof LivingEntity );
		}
	}
}