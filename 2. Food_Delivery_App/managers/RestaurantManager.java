package managers;

import java.util.ArrayList;
import java.util.List;

import models.Cart;
import models.Restaurant;

public class RestaurantManager {
    private List<Restaurant> restaurantlist=new ArrayList<>();
    private static RestaurantManager instance = null;


    private RestaurantManager(){

    }

    public static RestaurantManager getInstance(){
        if(instance==null){
            instance=new RestaurantManager();
        }
        return instance;
    }

    public void addRestaurant(Restaurant rest){
        restaurantlist.add(rest);
    }

    public List<Restaurant> searchRestaurant(String loc){
        List<Restaurant> result=new ArrayList<>();
        for(Restaurant rest:restaurantlist){
            if(rest.getAddress().toLowerCase().equals(loc.toLowerCase())){
                result.add(rest);
            }
        }
        return result;
    }
}
