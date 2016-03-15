package creativex.com.myapplication.DataModel;

/**
 * Created by karthik on 4/1/16.
 */
public class PlaceInfoDataModel {

    String mPlaceName;
    String mPlaceDesc;
    int mImageSource;


   public PlaceInfoDataModel(String placeName, String placeDesc, int imageSource) {
        mPlaceName = placeName;
        mPlaceDesc = placeDesc;
        mImageSource = imageSource;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String placeName) {
        this.mPlaceName = placeName;
    }


    public int getImageSource() {
        return mImageSource;
    }

    public void setImageSource(int imageSource) {
        this.mImageSource = imageSource;
    }

    public String getPlaceDesc() {
        return mPlaceDesc;
    }

    public void setPlaceDesc(String placeDesc) {
        this.mPlaceDesc = placeDesc;
    }


}
