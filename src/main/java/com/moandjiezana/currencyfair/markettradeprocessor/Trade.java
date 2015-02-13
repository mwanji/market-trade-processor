package com.moandjiezana.currencyfair.markettradeprocessor;

import java.math.BigDecimal;

public class Trade {

  public String userId;
  public String currencyFrom;
  public String currencyTo;
  public BigDecimal amountSell;
  public BigDecimal amountBuy;
  public BigDecimal rate;
  public String timePlaced;
  public String originatingCountry;
}
