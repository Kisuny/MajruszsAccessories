package com.majruszsaccessories.accessories;

import com.majruszsaccessories.MajruszsAccessories;
import com.majruszsaccessories.accessories.components.SleepingBonuses;
import com.majruszsaccessories.accessories.components.StrongerPotions;
import com.majruszsaccessories.accessories.components.TradeOffer;
import com.majruszsaccessories.accessories.components.TradingDiscount;
import com.majruszsaccessories.common.AccessoryHandler;
import com.mlib.annotation.AutoInstance;

@AutoInstance
public class HouseholdRune extends AccessoryHandler {
	public HouseholdRune() {
		super( MajruszsAccessories.HOUSEHOLD_RUNE );

		this.add( TradingDiscount.create( 0.15f ) )
			.add( SleepingBonuses.create( 1, 360 ) )
			.add( StrongerPotions.create( 0.5f, 1 ) )
			.add( TradeOffer.create( 17 ) );
	}
}