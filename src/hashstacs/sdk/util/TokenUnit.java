package hashstacs.sdk.util;

import java.math.BigDecimal;

import lombok.Getter;

/**
 * This represents a currency and its amount as a Unit
 * Should extend from Joda Money or Java Money libraries with more flexibility in handling currencies
 * TODO  Additional validation checks will be added
 * @author Hashstacs Solutions Engineering
 *
 */
@Getter
public class TokenUnit {

	private String _currency;
	private BigDecimal _amount;
	
	/**
	 * this is a store for a unit of tokens issued on a blockchain
	 * the smallest amount possible is 0 and the smallest increment is 1
	 * only int is allowed 
	 * @param currency
	 * @param amount
	 */
	public TokenUnit(String currency, BigDecimal amount) {
		_currency=currency;
		_amount=amount;
	}
	
	/**
	 * returns true if 1 token unit has the same currency as another
	 * if true, allow for arithmetic operations to take place
	 * @param tu1
	 * @param tu2
	 * @return
	 */
	public Boolean compareTokenUnitCurrencies(TokenUnit tu1, TokenUnit tu2) {
		if(tu1.get_currency().compareTo(tu2.get_currency())==0) {
			return true;
		}
		return false;
	}

}
