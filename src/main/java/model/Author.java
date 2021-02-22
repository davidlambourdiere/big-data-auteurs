package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @BsonId
    String id;
    String firstname;
    String lastname;
    public Document asDocument(){
        return new Document()
                .append("_id", new ObjectId())
                .append("firstname", this.getFirstname())
                .append("lastname", this.getLastname());
    }

    public String getName() {
        return String.join(" ", this.lastname.toUpperCase(), this.firstname);
    }
}
