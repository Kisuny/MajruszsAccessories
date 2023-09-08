package com.majruszsaccessories.boosters.components;

import com.majruszsaccessories.accessories.AccessoryHolder;
import com.majruszsaccessories.boosters.BoosterItem;
import com.majruszsaccessories.tooltip.TooltipHelper;
import com.mlib.attributes.AttributeHandler;
import com.mlib.config.ConfigGroup;
import com.mlib.config.IntegerConfig;
import com.mlib.contexts.OnPlayerTick;
import com.mlib.contexts.base.Condition;
import com.mlib.math.Range;
import com.mlib.text.TextHelper;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;

import java.util.function.Supplier;

public class LuckBonus extends BoosterComponent {
	final AttributeHandler attribute;
	final IntegerConfig luckBonus;

	public static ISupplier create( int luck ) {
		return ( item, group )->new LuckBonus( item, group, luck );
	}

	protected LuckBonus( Supplier< BoosterItem > item, ConfigGroup group, int luck ) {
		super( item );

		this.attribute = new AttributeHandler(
			TextHelper.createUUIDFromBytes( group.getName() ).toString(),
			String.format( "%sLuckBonus", group.getName() ),
			Attributes.LUCK,
			AttributeModifier.Operation.ADDITION
		);
		this.luckBonus = new IntegerConfig( luck, new Range<>( 1, 10 ) );

		OnPlayerTick.listen( this::updateLuck )
			.addCondition( Condition.< OnPlayerTick.Data > cooldown( 4, Dist.DEDICATED_SERVER ).configurable( false ) )
			.addConfig( this.luckBonus.name( "luck" ).comment( "Extra luck bonus." ) )
			.insertTo( group );

		this.addTooltip( "majruszsaccessories.boosters.luck_bonus", TooltipHelper.asItem( item ), TooltipHelper.asFixedValue( this.luckBonus ) );
	}

	private void updateLuck( OnPlayerTick.Data data ) {
		AccessoryHolder holder = AccessoryHolder.find( data.player, this.item.get() );
		int luck = holder.isValid() ? holder.apply( this.luckBonus ) : 0;

		attribute.setValue( luck ).apply( data.player );
	}
}
