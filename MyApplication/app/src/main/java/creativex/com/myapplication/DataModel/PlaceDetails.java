package creativex.com.myapplication.DataModel;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by karthik on 18/1/16.
 */
public class PlaceDetails implements Serializable {

    @Key
    public String status;

    @Key
    public Place result;

    @Override
    public String toString() {
        if (result != null) {
            return result.toString();
        }
        return super.toString();
    }
}
