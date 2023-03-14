import retrofit2.Call;

public interface ApiListener {
    void error(Call<?> call);
}
