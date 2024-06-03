package in.imast.impact.Connection;

import retrofit2.Response;

/**
 * Created by ahmad on 13-Apr-17.
 */

public interface APIResultLitener<T> {
    public void onAPIResult(Response<T> response, String errorMessage);
}
