package com.earthmap.satellite.map.location.map.nearBy.helpers

import com.earthmap.satellite.map.location.map.R
import com.earthmap.satellite.map.location.map.nearBy.models.NearByModel


class NearByHelper {
    companion object{
        fun fillNearByCategoryList():ArrayList<NearByModel> {
            val list=ArrayList<NearByModel>()
            list.add(NearByModel("Restaurant", R.drawable.nearby_restaurant_icon,"13065"))
            list.add(NearByModel("Hospital", R.drawable.nearby_hospital_uicon,"15014"))
            list.add(NearByModel("School", R.drawable.nearby_school_icon,"12009"))
            list.add(NearByModel("Hotels", R.drawable.nearby_hotels_icon,"19014"))
            list.add(NearByModel("Groceries", R.drawable.nearby_grocery_icon,"17069"))
            list.add(NearByModel("Petrol", R.drawable.nearby_petrol_icon,"19007"))
            list.add(NearByModel("Parks", R.drawable.nearby_park_icon,"10001"))
            list.add(NearByModel("Airport", R.drawable.nearby_airport_icon,"19031"))
            list.add(NearByModel("Bakery", R.drawable.nearby_bakery_icon,"13002"))
            list.add(NearByModel("Bank", R.drawable.nearby_bank_icon,"11045"))
            list.add(NearByModel("Saloons", R.drawable.nearby_saloon_icon,"11064"))
            list.add(NearByModel("Bus Stand", R.drawable.nearby_bus_stand,"19042"))
            list.add(NearByModel("Cafe", R.drawable.nearby_cafe_icon,"13034"))
            list.add(NearByModel("Churches", R.drawable.nearby_churches_icon,"12101"))
            list.add(NearByModel("Fire Station", R.drawable.nearby_fire_station,"12071"))
            list.add(NearByModel("Gym", R.drawable.nearby_gym_icon,"18021"))
            list.add(NearByModel("Library", R.drawable.nearby_libarey_icon,"12080"))
            list.add(NearByModel("Mosque", R.drawable.nearby_nabawi_mosque,"12106"))
            list.add(NearByModel("Pet Shop", R.drawable.nearby_pets_shop,"17110"))
            list.add(NearByModel("Pharmacy", R.drawable.nearby_pharmacy_icon,"17095"))
            list.add(NearByModel("Police Station", R.drawable.nearby_police_station,"12072"))
            list.add(NearByModel("Zoo", R.drawable.nearby_zoo_icon,"10056"))
            list.add(NearByModel("Office", R.drawable.nearby_office_building,"11130"))
            return list
        }
    }
}