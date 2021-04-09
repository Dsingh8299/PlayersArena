package xyz.toors.toors;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import xyz.toors.toors.Notification.MyResponse;
import xyz.toors.toors.Notification.Sender;

public interface APIService {
    @Headers(
            {

                    "Content-Type:application/json",
                    "Authorization:key=AAAAXS1ihQc:APA91bHgn7ViOVMuA7hvKSKpopb5PUL_i-lBNufiSvjCa6nMnD3bJ0thk9BQvCDIuEmgZ-pAI64ReY5m-xHhdotYs1CUpXyHu5e_zjFQKcGhHc0zNEtexxH_DRxvfc2h84AoQmN3pPFt"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
