package APIpractice.pojos;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPojo {

    // Expose annotation is used to indicate which fields must be included
    // when de/serializing
    // Jackson -> more annotations

    @Expose(serialize = false, deserialize = true)
    private int id;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String gender;
    @Expose
    private String status;

    public UserPojo(String name, String email, String gender, String status) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserPojo{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", gender='").append(gender).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
