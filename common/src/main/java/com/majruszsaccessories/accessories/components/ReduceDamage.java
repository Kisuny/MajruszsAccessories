package com.majruszsaccessories.accessories.components;

import com.majruszsaccessories.common.BonusComponent;
import com.majruszsaccessories.common.BonusHandler;
import com.majruszsaccessories.config.RangedFloat;
import com.majruszsaccessories.contexts.base.CustomConditions;
import com.majruszsaccessories.items.AccessoryItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.contexts.OnEntityPreDamaged;
import com.mlib.data.Serializable;
import com.mlib.math.AnyPos;
import com.mlib.math.Range;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public class ReduceDamage extends BonusComponent< AccessoryItem > {
	RangedFloat reduction = new RangedFloat().id( "reduction" ).maxRange( Range.of( 0.0f, 1.0f ) );

	public static ISupplier< AccessoryItem > create( float reduction ) {
		return handler->new ReduceDamage( handler, reduction );
	}

	protected ReduceDamage( BonusHandler< AccessoryItem > handler, float reduction ) {
		super( handler );

		this.reduction.set( reduction, Range.of( 0.0f, 1.0f ) );

		OnEntityPreDamaged.listen( this::reduceDamageDealt )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.attacker ) );

		OnEntityPreDamaged.listen( this::reduceDamageReceived )
			.addCondition( CustomConditions.hasAccessory( this::getItem, data->data.target ) );

		this.addTooltip( "majruszsaccessories.bonuses.reduce_damage", TooltipHelper.asPercent( this.reduction ) );

		Serializable config = handler.getConfig();
		config.defineCustom( "damage_reduction", this.reduction::define );
	}

	private void reduceDamageDealt( OnEntityPreDamaged data ) {
		data.damage *= 1.0f - CustomConditions.getLastHolder().apply( this.reduction );
		this.spawnEffects( data.target );
	}

	private void reduceDamageReceived( OnEntityPreDamaged data ) {
		data.damage *= 1.0f - CustomConditions.getLastHolder().apply( this.reduction );
		this.spawnEffects( data.target );
	}

	private void spawnEffects( Entity entity ) {
		float width = entity.getBbWidth();
		float height = entity.getBbHeight();

		CustomConditions.getLastHolder()
			.getParticleEmitter()
			.count( 2 )
			.offset( ()->AnyPos.from( width, height, width ).mul( 0.5 ).vec3() )
			.emit( ( ServerLevel )entity.level(), AnyPos.from( entity.position() ).add( 0.0, height * 0.5, 0.0 ).vec3() );
	}
}
