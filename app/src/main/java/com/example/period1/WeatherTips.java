package com.example.period1;

import java.util.ArrayList;
import java.util.Random;

public class WeatherTips {
    private final ArrayList<String> coldTips;
    private final ArrayList<String> warmTips;
    private final ArrayList<String> hotTips;

    public WeatherTips() {
        coldTips = new ArrayList<>();
        coldTips.add("Maintain body heat by layering clothing to prevent hypothermia risk.");
        coldTips.add("Protect extremities with gloves and thick socks to prevent frostbite.");
        coldTips.add("Consume warm beverages to maintain internal body temperature and prevent dehydration.");
        coldTips.add("Ensure proper insulation against cold air to reduce heat loss from the body.");
        coldTips.add("Use a humidifier to prevent dry skin and respiratory irritation caused by indoor heating.");
        coldTips.add("Maintain adequate Vitamin D levels through supplementation or diet to support immune function.");
        coldTips.add("Engage in regular physical activity to promote circulation and warmth.");
        coldTips.add("After intercourse, urinate to help prevent urinary tract infections, which can be more common in colder weather.");
        coldTips.add("Use water-based lubricants during sexual activity in cold weather to avoid irritation and dryness.");

        warmTips = new ArrayList<>();
        warmTips.add("Hydrate frequently to compensate for increased fluid loss through perspiration.");
        warmTips.add("Apply sunscreen with SPF 30 or higher to protect against harmful UV radiation.");
        warmTips.add("Consume water-rich foods like cucumber and watermelon to stay hydrated.");
        warmTips.add("Seek shade during peak sunlight hours to reduce heat exposure and risk of sunburn.");
        warmTips.add("Wear loose, breathable clothing made from natural fibers to enhance heat dissipation.");
        warmTips.add("Use aloe vera gel to soothe sunburned skin and promote healing.");
        warmTips.add("Avoid excessive alcohol and caffeine consumption, as they can dehydrate the body.");
        warmTips.add("Stay hydrated to support vaginal lubrication and overall sexual comfort, especially in warmer temperatures.");
        warmTips.add("After sex, urinate to flush out bacteria and reduce the risk of urinary tract infections in hot weather.");

        hotTips = new ArrayList<>();
        hotTips.add("Consider showering before and after sexual activity in hot weather to stay fresh and reduce the risk of bacterial growth.");
        hotTips.add("Keep your genital area dry and clean to prevent yeast infections, which can be more prevalent in humid conditions.");
        hotTips.add("Stay indoors during extreme heat to avoid heat-related illnesses like heatstroke.");
        hotTips.add("Drink cool fluids like water or electrolyte-rich beverages to prevent dehydration.");
        hotTips.add("Wear light-colored, loose-fitting clothing to reflect sunlight and promote ventilation.");
        hotTips.add("Take cool showers or baths to lower body temperature and relieve heat stress.");
        hotTips.add("Apply cold compresses to pulse points like wrists and neck to cool down quickly.");
        hotTips.add("Limit outdoor activities to early morning or late evening when temperatures are cooler.");
        hotTips.add("Use fans or air conditioning to circulate air and maintain a comfortable indoor temperature.");

    }

    public String getRandomTip(double temperatureCelsius) {
        if (temperatureCelsius < 15) {
            return getRandomTipFromList(coldTips);
        } else if (temperatureCelsius >= 15 && temperatureCelsius < 25) {
            return getRandomTipFromList(warmTips);
        } else {
            return getRandomTipFromList(hotTips);
        }
    }

    private String getRandomTipFromList(ArrayList<String> tipsList) {
        Random rand = new Random();
        int index = rand.nextInt(tipsList.size());
        return tipsList.get(index);
    }
}