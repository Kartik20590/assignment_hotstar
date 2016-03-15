package creativex.com.myapplication.DataModel;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by karthik on 18/1/16.
 */
public class PlacesList implements Serializable {

    @Key
    public String status;

    @Key
    public List<Place> results;

}
