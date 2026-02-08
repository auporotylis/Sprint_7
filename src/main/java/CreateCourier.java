import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class CreateCourier {
    private String login;
    private String password;
    private String firstName;
}