package com.example.catculate.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PriceHelper {

  public PriceHelper() {

  }

  public String formatPrice(long price) {
    NumberFormat formatter = new DecimalFormat("#,###.##");
    return formatter.format(price);
  }

  public String unformatPrice(String priceStr) {
    priceStr = priceStr.replace(".","");
    priceStr = priceStr.replace(",","");
    return priceStr;
  }

}
