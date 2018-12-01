import android.annotation.SuppressLint;

import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.lernr.rxjavaexample.Response.LocationCord;
import com.lernr.rxjavaexample.Response.UserLocationResponse;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

  private final double mMinimumDistance = 50.0;
  private final double mHateToWaitLat = 28.413824;
  private final double mHateToWaitLong = 77.042282;
  private List<UserLocationResponse> mInvitedUserList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    filterUsers();

  }


  //for demo purpose
  private List<UserLocationResponse> getUserLocation() {
    List<UserLocationResponse> mUserLocationResponses = new ArrayList<>();
    mUserLocationResponses.add(new UserLocationResponse
        ("1739d5c7-1203-48db-848e-e9507de5ec97", "Mr. pritam ghosh",
            new LocationCord(29.754982399999996, -95.3991168)));
    mUserLocationResponses.add(new UserLocationResponse
        ("0d48c3c7-2c04-4a84-bac2-0f59974a0691", "Mr. shesh narain misra",
            new LocationCord(28.6303544, 77.2198032)));

    mUserLocationResponses.add(new UserLocationResponse
        ("18a1148a-9118-437d-bcbc-96ff8a40993b", "Rahul",
            new LocationCord(28.3637358, 76.9321878)));
    mUserLocationResponses.add(new UserLocationResponse
        ("78d2f400-2111-47a1-9828-ddef958cae8e", "Mr. mahendra",
            new LocationCord(28.6427874, 77.3293913)));
    return mUserLocationResponses;

  }



  @SuppressLint("CheckResult")
  private void filterUsers() {
    io.reactivex.Observable.just(getUserLocation()) //pass server response
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .flatMap(
            new Function<List<UserLocationResponse>, io.reactivex.Observable<UserLocationResponse>>() {
              @Override
              public io.reactivex.Observable<UserLocationResponse> apply(
                  List<UserLocationResponse> userLocationResponses) {
                return io.reactivex.Observable.fromIterable(userLocationResponses);
              }
            })
        .filter(new Predicate<UserLocationResponse>() {
          @Override
          public boolean test(UserLocationResponse userLocationResponse) {
            ///filter results and discard all users who have distance more than 50 kms
            return getCalculatedDistance(mHateToWaitLat, mHateToWaitLong,
                userLocationResponse.getLocationCord().getLat(),
                userLocationResponse.getLocationCord().getLng()) < mMinimumDistance;
          }
        }).toSortedList().subscribeWith(new DisposableSingleObserver<List<UserLocationResponse>>() {
      @Override
      public void onSuccess(List<UserLocationResponse> userLocationResponses) {

        setInvitedUserList(userLocationResponses);


      }

      @Override
      public void onError(Throwable e) {

        e.printStackTrace();
      }
    });

  }


  private void showAllInvitedUsers() {
    for (UserLocationResponse mUserLocationResponse : getInvitedUserList()) {
      Log.d("TAG",
          "BINGO" + mUserLocationResponse.getUserId() + " " + mUserLocationResponse.getUserName());
    }
  }


  public static double getCalculatedDistance(double lat1, double lon1, double lat2, double lon2) {
    float[] results = new float[1];
    Location.distanceBetween(lat1, lon1, lat2, lon2, results);
    float distanceInMeters = results[0];

    return metersToKm((double) distanceInMeters);


  }

  public List<UserLocationResponse> getInvitedUserList() {
    return mInvitedUserList;
  }

  public void setInvitedUserList(
      List<UserLocationResponse> invitedUserList) {
    mInvitedUserList = invitedUserList;
  }

  public static double milesToKm(double miles) {
    return 1.609339952468872d * miles;
  }

  public static double kmToMiles(double km) {
    return 0.6213709712028503d * km;
  }

  public static double metersToKm(double meters) {
    return meters / 1000.0d;
  }

  public static double metersToMiles(double meters) {
    return 6.21371204033494E-4d * meters;
  }

  public static long milesToMeters(double miles) {
    return (long) (miles * 1609.3399658203125d);
  }

  public static long kmToMeters(double km) {
    return (long) (km * 1000.0d);
  }
}
